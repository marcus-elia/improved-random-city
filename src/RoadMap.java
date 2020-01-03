import java.awt.*;
import java.util.ArrayList;

public class RoadMap extends GameObject
{
    // ==========================================
    //
    //            Lists of Objects
    //
    // ==========================================
    private ArrayList<Intersection> intersections;
    private ArrayList<Road> roads;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Lake> lakes;

    // ==========================================
    //
    //             City Parameters
    //
    // ==========================================

    // this keeps track of when to build a new road or lake
    private int ticksBetweenBuilds;
    private int ticksSinceLastBuild;

    // how many times to try
    private int numAttempts;

    // How many pixels long each road is, on average
    private int averageRoadLength;

    // Between 0 and 1. If this is closer to 1, more roads will be on
    // horizontal or vertical lines
    private double perpendicularity;

    // How close another intersection has to be for us to lock onto it during road building
    private double intersectionLockOnDistance;

    // The minimum distance we force any two intersections to be away from each other
    private double minIntersectionDistance;

    public RoadMap(GameManager inputManager, Point inputCenter)
    {
        // Initialize Lists
        super(inputManager, inputCenter);
        intersections = new ArrayList<Intersection>();
        roads = new ArrayList<Road>();
        vehicles = new ArrayList<Vehicle>();
        lakes = new ArrayList<Lake>();

        // Initialize parameters
        ticksBetweenBuilds = 500;
        ticksSinceLastBuild = 0;

        numAttempts = 5;

        averageRoadLength = 40;

        perpendicularity = 0.5;

        intersectionLockOnDistance = 10;

        minIntersectionDistance = 30;

        this.makeFirstIntersection();
        this.addLake(new Lake(manager, new Point(50, 50), 40, 30, Math.PI/4));
    }

    public void tick()
    {
        ticksSinceLastBuild++;

        // If it's time to try to build a new road
        if(ticksSinceLastBuild == ticksBetweenBuilds)
        {
            if(Math.random() > 0.01)
            {
                this.buildNewRoad();
            }
            else
            {
                this.buildNewLake();
            }
            ticksSinceLastBuild = 0;
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
        for(Vehicle v : vehicles)
        {
            v.tick();
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
        Point centerOfMap = new Point(manager.getWidth()/2, manager.getHeight()/2);
        this.addIntersection(new Intersection(manager, centerOfMap, this, 4));
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

    // ==========================================
    //
    //               Setters
    //
    // ==========================================
    public void addIntersection(Intersection intsec)
    {
        intersections.add(intsec);
        manager.addGameObject(intsec);
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
    }
    public void addVehicle(Vehicle v)
    {
        vehicles.add(v);
        manager.addGameObject(v);
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


    // when we are adding a new road, do we need to make new intersections? If so, we do.
    // In either case, both intersections are returned in an array
    public Intersection[] addIntersectionsIfNeeded(Point p1, Point p2)
    {
        Intersection startInt = this.hasIntersection(p1);
        if(startInt == null)
        {
            startInt = new Intersection(manager, p1, this, 5);
            this.intersections.add(startInt);
        }
        Intersection endInt = this.hasIntersection(p2);
        if(endInt == null)
        {
            endInt = new Intersection(manager, p2, this, 5);
            this.intersections.add(endInt);
        }
        return new Intersection[]{startInt, endInt};
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
                    roadLength = (Math.random()*.4 + .8) * this.averageRoadLength;
                    roadAngle = this.getRandomAngle();
                    targetPoint = intsec.getCenter().getPointFromHere(roadLength, roadAngle);

                    // Iterate through every intersection.
                    // If the new point is very close to an existing intersection, lock onto it.
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

                    // if the proposed road would cross an existing road or end too close to one,
                    // cancel
                    for(Road curRoad : roads)
                    {
                        if(curRoad.hitsRoad(intsec.getCenter(), targetPoint) ||
                                curRoad.directedDistance(targetPoint) < minIntersectionDistance)
                        {
                            // also make sure the road we are too close to isn't just coming out
                            // from our target intersection we are connecting to.
                            if(connectTo == null)
                            {
                                roadApproved = false;
                                break;
                            }
                            if(!connectTo.getRoads().contains(curRoad))
                            {
                                roadApproved = false;
                                break;
                            }
                        }
                    }

                    // if the road passes too close to an existing intersection, cancel
                    for(Intersection intsec2 : intersections)
                    {
                        // if we are close to an intersection that isn't this one
                        if(intsec2.directedDistance(intsec.getCenter(), targetPoint) < this.minIntersectionDistance
                                && !intsec.equals(intsec2))
                        {
                            if(connectTo == null)
                            {
                                roadApproved = false;
                                break;
                            }
                            // and the intersection isn't the one we are locking to
                            if(!connectTo.equals(intsec2))
                            {
                                roadApproved = false;
                                break;
                            }
                        }
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
                        this.addRoad(new Road(manager, null,this,0,
                                roadInts[0], roadInts[1]));
                        return;
                    }
                }
            }
        }
    }
}
