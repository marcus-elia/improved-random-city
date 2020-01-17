import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class RoadMap extends GameObject
{
    private Point newClick;

    // ==========================================
    //
    //            Lists of Objects
    //
    // ==========================================
    private ArrayList<Intersection> intersections;
    private ArrayList<Road> roads;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Lake> lakes;
    private IntersectionGraph intGraph;

    // ==========================================
    //
    //             City Parameters
    //
    // ==========================================

    // for giving each road an ID
    private int numRoads;

    // this keeps track of when to build a new road or lake
    private int ticksBetweenBuilds;
    private int ticksSinceLastBuild;

    // how many times to try
    private int numAttempts;

    private double roadWidth;

    // How many pixels long each road is, on average
    private int averageRoadLength;

    // Between 0 and 1. If this is closer to 1, more roads will be on
    // horizontal or vertical lines
    private double perpendicularity;

    // How close another intersection has to be for us to lock onto it during road building
    private double intersectionLockOnDistance;

    // The minimum distance we force any two intersections to be away from each other
    private double minIntersectionDistance;

    // This determines how many roads an intersection will have based on the
    // intersection's coordinates
    private PerlinNoise roadDensityMap;

    // The average size of the lakes' longer radius (lake is ellipse)
    private int averageLakeSize;

    // Controls the density of Vehicles
    private double carsPerRoad;

    // ==========================================
    //
    //             City Properties
    //
    // ==========================================

    // The center of the city (which can move with scrolling)
    private Point cityCenter;

    // The farthest distance from the center that has been built
    private int currentRadius;

    public RoadMap(GameManager inputManager, Point inputCenter)
    {
        // Initialize Lists
        super(inputManager, inputCenter);
        newClick = null;

        intersections = new ArrayList<Intersection>();
        roads = new ArrayList<Road>();
        vehicles = new ArrayList<Vehicle>();
        lakes = new ArrayList<Lake>();
        intGraph = new IntersectionGraph();

        // Initialize parameters
        numRoads = 0;

        ticksBetweenBuilds = 50;
        ticksSinceLastBuild = 0;

        numAttempts = 5;

        roadWidth = 10;
        averageRoadLength = 70;

        perpendicularity = .70;

        roadDensityMap = new PerlinNoise(64, 64, 0.2);

        intersectionLockOnDistance = 18;

        minIntersectionDistance = 40;

        averageLakeSize = 140;

        carsPerRoad = 0.18;

        this.makeFirstIntersection();
    }

    public void tick()
    {
        ticksSinceLastBuild++;

        // If it's time to try to build a new road
        if(ticksSinceLastBuild == ticksBetweenBuilds)
        {
            if(Math.random() > 0.025)
            {
                this.buildNewRoad();
            }
            else
            {
                this.buildNewLake();
            }
            ticksSinceLastBuild = 0;
        }

        this.checkForCollisions();

        // if we don't have too many vehicles, make a new one
        if(vehicles.size() < (roads.size()-3)*this.carsPerRoad)
        {
            this.createNewVehicle();
        }

        for(Lake l : lakes)
        {
            l.tick();
        }
        for(Road r : roads)
        {
            r.tick();
        }
        for(Intersection intsec : intersections)
        {
            intsec.tick();
        }
        ArrayList<Vehicle> toRemove = new ArrayList<Vehicle>();
        for(Vehicle v : vehicles)
        {
            v.tick();
            if(v.getNeedsToBeRemoved())
            {
                toRemove.add(v);
            }
        }
        for(Vehicle gone : toRemove)
        {
            gone.removeSelf();
        }

        if(newClick != null)
        {
            this.respondToClick(newClick);
            newClick = null;
        }
    }

    public void render(Graphics2D g2d)
    {

        for(Lake l : lakes)
        {
            l.render(g2d);
        }
        for(Road r : roads)
        {
            r.render(g2d);
        }
        for(Intersection intsec : intersections)
        {
            intsec.render(g2d);
        }
        for(Vehicle v : vehicles)
        {
            v.render(g2d);
        }
    }

    // ==========================================
    //
    //          Initialization Functions
    //
    // ==========================================
    public void makeFirstIntersection()
    {
        cityCenter = new Point(manager.getWidth()/2, manager.getHeight()/2);
        currentRadius = 0;
        this.addIntersection(new Intersection(manager, cityCenter, this, 8, averageRoadLength));
    }


    // ==========================================
    //
    //               Getters
    //
    // ==========================================
    public ArrayList<Intersection> getIntersections()
    {
        return intersections;
    }
    public ArrayList<Road> getRoads()
    {
        return roads;
    }
    public ArrayList<Vehicle> getVehicles()
    {
        return vehicles;
    }
    public int getNumRoads()
    {
        return numRoads;
    }
    public Point getCityCenter()
    {
        return cityCenter;
    }
    public int getCurrentRadius()
    {
        return currentRadius;
    }
    public double getRoadWidth()
    {
        return roadWidth;
    }

    // ==========================================
    //
    //                Setters
    //
    // ==========================================
    public void addIntersection(Intersection intsec)
    {
        intersections.add(intsec);
        manager.addGameObject(intsec);

        // Every time we add in an intersection, check if it has extended the radius
        if(intsec.getCenter().distanceToPoint(cityCenter) > currentRadius)
        {
            currentRadius = (int)intsec.getCenter().distanceToPoint(cityCenter);
        }

        intGraph.addNode(intsec);
    }
    public void addLake(Lake l)
    {
        lakes.add(l);
        manager.addGameObject(l);
    }
    public void addRoad(Road r)
    {
        roads.add(r);
        manager.addGameObject(r);
        // The relevant intersections need to add the road
        r.getStartInt().addRoad(r, true);
        r.getEndInt().addRoad(r, false);
        intGraph.addEdge(r.getStartInt(), r.getEndInt());
    }
    public void addVehicle(Vehicle v)
    {
        vehicles.add(v);
        manager.addGameObject(v);
    }

    public void setNewClick(int mx, int my)
    {
        newClick = new Point(mx, my);
    }


    // ==========================================
    //
    //              City Generation
    //
    // ==========================================
    // Helper function for road building. Returns a random angle between 0 and 2PI, but
    // rounds to the nearest Pi/2 or Pi/4, depending on the perpendicularity
    public double getRandomAngle()
    {
        if(Math.random() < this.perpendicularity)
        {
            return Math.round(Math.random()*4)*Math.PI/2;
        }
        else
        {
            return Math.round(Math.random()*4)*Math.PI/2 + Math.PI/4;
        }
    }

    // If this Road Map already has an intersection with the given coordinates,
    // this returns the intersection. Otherwise it returns null.
    public Intersection hasIntersection(Point p)
    {
        for(Intersection intsec : intersections)
        {
            if(intsec.sameCoordinates(p))
            {
                return intsec;
            }
        }
        return null;
    }

    // Given a Point p, this returns the max number of roads an intersection at that
    // point should have.
    public int maxNumRoadsByCoords(Point p)
    {
        double[][] map = roadDensityMap.getPerlinNoise();
        int i = (((int)p.x % 2048 + 2048) % 2048) * map.length / 2048;
        int j = (((int)p.y % 2048 + 2048) % 2048) * map[0].length / 2048;

        double noiseValue =  roadDensityMap.getPerlinNoise()[i][j];
        return (int)(noiseValue * 6) + 1;
    }
    // What should the average road length be for roads leaving this intersection?
    public int roadLengthByCoords(Point p)
    {
        double[][] map = roadDensityMap.getPerlinNoise();
        int i = (((int)p.x % 2048 + 2048) % 2048) * map.length / 2048;
        int j = (((int)p.y % 2048 + 2048) % 2048) * map[0].length / 2048;

        double noiseValue =  1 - roadDensityMap.getPerlinNoise()[i][j];
        return (int)(this.averageRoadLength * noiseValue) + 2*averageRoadLength / 3;
    }


    // when we are adding a new road, do we need to make new intersections? If so, we do.
    // In either case, both intersections are returned in an array
    public Intersection[] addIntersectionsIfNeeded(Point p1, Point p2)
    {
        Intersection startInt = this.hasIntersection(p1);
        if(startInt == null)
        {
            startInt = new Intersection(manager, p1, this,
                    maxNumRoadsByCoords(p1), roadLengthByCoords(p1));
            this.addIntersection(startInt);
        }
        Intersection endInt = this.hasIntersection(p2);
        if(endInt == null)
        {
            endInt = new Intersection(manager, p2, this,
                    maxNumRoadsByCoords(p2), roadLengthByCoords(p2));
            this.addIntersection(endInt);
        }
        return new Intersection[]{startInt, endInt};
    }

    // Returns true if the proposed road hits or gets too close to an existing road
    // (Helper function for building roads)
    public boolean conflictsWithExistingRoad(Point p1, Point p2, Intersection connectTo)
    {
        for(Road curRoad : roads)
        {
            // If we intersect another road, definitely bad
            if(curRoad.hitsRoad(p1, p2))
            {
                return true;
            }
            // If the other road passes too close to p2
            if(curRoad.directedDistance(p2) < minIntersectionDistance)
            {
                // also make sure the road we are too close to isn't just coming out
                // from our target intersection we are connecting to.
                if(connectTo == null)
                {
                    return true;
                }
                // If the road that is too close to p2 actually connects to the existing
                // intersection at p2, it wouldn't be a problem
                if(!connectTo.getRoads().contains(curRoad))
                {
                    return true;
                }
            }
        }
        return false; // nothing bad happened
    }

    // Returns true if the proposed road gets too close to an existing intersection
    // (Helper function for building roads)
    public boolean conflictsWithExistingIntersection(Point p1, Point p2, Intersection connectTo)
    {
        for(Intersection intsec2 : intersections)
        {
            // if we are close to an intersection that isn't this one
            if(intsec2.directedDistance(p1, p2) < this.minIntersectionDistance
                    && !intsec2.sameCoordinates(p1))
            {
                if(connectTo == null)
                {
                    return true;
                }
                // and the intersection isn't the one we are locking to
                if(!connectTo.equals(intsec2))
                {
                    return true;
                }
            }
        }
        return false; // nothing bad happened
    }

    // Returns true if the proposed road intersects an existing lake
    // (Helper function for building roads)
    public boolean conflictsWithExistingLake(Point p1, Point p2)
    {
        for(Lake l : lakes)
        {
            if(Lake.isTooCloseToLake(l.getFocus1(), l.getFocus2(), l.getXRadius(), p1, roadWidth) ||
                    Lake.isTooCloseToLake(l.getFocus1(), l.getFocus2(), l.getXRadius(), p2, roadWidth))
            {
                return true;
            }
        }
        return false; // nothing bad happened
    }

    // Return true if the shape representing a proposed lake intersects
    // an existing lake.
    public boolean conflictsWithExistingLake(Point proposedCenter, double proposedXRadius)
    {
        for(Lake l : lakes)
        {
            if(Lake.potentiallyOverlapping(l.getCenter(), l.getXRadius(), proposedCenter, proposedXRadius))
            {
                return true;
            }
        }
        return false;  // nothing bad happened
    }

    // This function tries several times to build a road from each intersections.
    // It stops once one new road is built
    public void buildNewRoad()
    {
        Point targetPoint;
        double roadLength;
        double roadAngle;
        Intersection connectTo;
        boolean roadApproved;

        for(Intersection intsec : intersections)
        {
            if(intsec.canHaveNewRoad())
            {
                for(int i = 0; i < numAttempts; i++)
                {
                    connectTo = null;      // The intersection we are connecting the road to on the other side,
                                           // if any
                    roadApproved = true;   // We need to keep track of whether the potential road is accepted.

                    // get random nearby coordinates to see if we can add a road there
                    roadLength = (Math.random()*.4 + .8) * intsec.getAverageRoadLength();
                    roadAngle = this.getRandomAngle();
                    targetPoint = intsec.getCenter().getPointFromHere(roadLength, roadAngle);

                    // Iterate through every intersection.
                    // If the new point is very close to an existing intersection, lock onto it.
                    // Unless we already have a road to that intersection from the original one, or
                    // the new intersection has too many roads. In that case, reject this road.
                    for(Intersection intsec2 : intersections)
                    {
                        if(intsec2.getCenter().distanceToPoint(targetPoint) < intersectionLockOnDistance)
                        {
                            // if there is not already a road from this intersection to that one
                            // and it can take more roads, proceed
                            if(!intsec.getNeighbors().contains(intsec2) && intsec2.canHaveNewRoad())
                            {
                                connectTo = intsec2;
                                targetPoint = intsec2.getCenter();
                                break;
                            }
                            // If there is already a road there or it is full, we don't approve.
                            // This road would be too close to an intersection, but not able to
                            // lock onto it.
                            else
                            {
                                roadApproved = false;
                                break; // no need to check any other intersections.
                            }
                        }
                    }

                    // Make sure we don't intersect or come to close to an existing road
                    if(conflictsWithExistingRoad(intsec.getCenter(), targetPoint, connectTo))
                    {
                        roadApproved = false;
                    }
                    // Make sure we don't come to close to an existing intersection
                    if(conflictsWithExistingIntersection(intsec.getCenter(), targetPoint, connectTo))
                    {
                        roadApproved = false;
                    }
                    // Make sure we don't hit an existing lake
                    if(conflictsWithExistingLake(intsec.getCenter(), targetPoint))
                    {
                        roadApproved = false;
                    }

                    // if nothing went wrong, build the road
                    if(roadApproved)
                    {
                        // Put the points in order
                        Point p1, p2;
                        if(intsec.getCenter().x < targetPoint.x ||
                                (intsec.getCenter().x == targetPoint.x && intsec.getCenter().y > targetPoint.y))
                        {
                            p1 = intsec.getCenter();
                            p2 = targetPoint;
                        }
                        else
                        {
                            p1 = targetPoint;
                            p2 = intsec.getCenter();
                        }
                        Intersection[] roadInts = this.addIntersectionsIfNeeded(p1, p2);
                        Point centerOfSegment = new Point((intsec.getCenter().x + targetPoint.x)/2,
                                (intsec.getCenter().y + targetPoint.y)/2);
                        this.addRoad(new Road(manager, centerOfSegment,this, ++numRoads,
                                roadInts[0], roadInts[1], roadWidth));
                        return;
                    }
                }
            }
        }
    }

    // Tries several times to build a lake on the edge of town.
    public void buildNewLake()
    {
        Point targetPoint;
        double distanceFromCenter, angleFromCenter;
        double xRadius, yRadius, angle;

        xRadius = (.6*Math.random() + .6) * averageLakeSize;
        yRadius = (.3*Math.random() + .4) * xRadius; // y-radius < x-radius by definition
        angle = Math.random() * 2 * Math.PI;

        for(int i = 0; i < numAttempts; i++)
        {
            distanceFromCenter = currentRadius + xRadius + 10;
            angleFromCenter = Math.random()*2*Math.PI;

            targetPoint = new Point(cityCenter.x + distanceFromCenter*Math.cos(angleFromCenter),
                    cityCenter.y + distanceFromCenter*Math.sin(angleFromCenter));

            if(!conflictsWithExistingLake(targetPoint, xRadius))
            {
                this.addLake(new Lake(manager, targetPoint, xRadius, yRadius, angle));
                return;
            }
        }
    }

    // Returns a random road from the list. Used for putting new vehicles in the map.
    public Road getRandomRoad()
    {
        return roads.get((int)(Math.random()*roads.size()));
    }

    // This creates a new Vehicle on a random Road, and gives the Vehicle random characteristics.
    public void createNewVehicle()
    {
        Road r = this.getRandomRoad();
        Point spawnPoint = Point.midPoint(Point.midPoint(r.getFS(), r.getFE()), r.getFS());
        double randomPersonality = Math.random();
        Color color;
        double aggression;
        if(randomPersonality < 0.2) // yellow
        {
            color = new Color(255, 210, 31);
            aggression = 0;
        }
        else if(randomPersonality < 0.4)  // green
        {
            color = new Color(10, 150, 20);
            aggression = 0.25;
        }
        else if(randomPersonality < 0.6)  // purple
        {
            color = new Color(124, 20, 127);
            aggression = 0.5;
        }
        else if(randomPersonality < 0.8)  // blue
        {
            color = new Color(10, 62, 166);
            aggression = 0.75;
        }
        else  // red
        {
            color = new Color(150, 10, 3);
            aggression = 1;
        }
        Vehicle v = new Vehicle(manager, spawnPoint, 16, 8, color, r, aggression);
        this.addVehicle(v);
    }

    // ==========================================
    //
    //             Game Management
    //
    // ==========================================
    public void checkForCollisions()
    {
        for(int i = 0; i < vehicles.size() - 1; i++)
        {
            for(int j = i + 1; j < vehicles.size(); j++)
            {
                if(vehicles.get(i).getCenter().distanceToPoint(vehicles.get(j).getCenter()) < 25 &&
                vehicles.get(i).isColliding(vehicles.get(j)))
                {
                    vehicles.get(i).setIsFading(true);
                    vehicles.get(j).setIsFading(true);
                }
            }
        }
    }

    // ==========================================
    //
    //           Intersection Graph
    //
    // ==========================================
    public void makeAllCarsTargetIntersection(Intersection intsec)
    {
        for(Vehicle v : vehicles)
        {
            if(v.getIsOnRoad())
            {
                v.setPathToIntersection(intGraph.getShortestPath(v.getNextInt(), intsec));
            }
            else
            {
                v.setPathToIntersection(intGraph.getShortestPath(v.getNextNextInt(), intsec));
            }

        }
    }

    public void respondToClick(Point p)
    {
        for(Intersection intsec : intersections)
        {
            if(intsec.getIntersectionFill() != null && intsec.getIntersectionFill().contains(p.x, p.y))
            {
                this.makeAllCarsTargetIntersection(intsec);
                break;
            }
        }
    }


    // ==========================================
    //
    //                 Debug
    //
    // ==========================================
    // Iterates through all pairs of roads to see if any intersections happen.
    public void checkAllRoadsAndPrint()
    {
        boolean problem = false;
        for(int i = 0; i < roads.size() - 1; i++)
        {
            Road r1 = roads.get(i);
            for(int j = i + 1; j < roads.size(); j++)
            {
                Road r2 = roads.get(j);
                if(r1.hitsRoad(r2.getStartPoint(), r2.getEndPoint()))
                {
                    if(!r1.getStartInt().equals(r2.getStartInt()) &&
                    !r1.getEndInt().equals(r2.getStartInt()) &&
                    !r1.getStartInt().equals(r2.getEndInt()) &&
                    !r1.getEndInt().equals(r2.getEndInt()))
                    {
                        System.out.println("\n=======Problem==========");
                        roads.get(i).printThings();
                        roads.get(j).printThings();
                        problem = true;
                    }
                }
            }
        }
        if(!problem)
        {
            System.out.println("No problems");
        }
    }
}
