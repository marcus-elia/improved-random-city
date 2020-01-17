import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class Intersection extends GameObject
{
    // Roads are stored in order by the angle they leave this intersection at, from
    // least to greatest, with 0 being directly to the right, and going clockwise.
    private LinkedList<Road> roads;

    private RoadMap rm;

    // Used to fill in the intersection
    //private ArrayList<Double> intersectionFillXPoints;
    //private ArrayList<Double> intersectionFillYPoints;
    private ArrayList<Point> intersectionFillPoints;
    private Path2D intersectionFill;

    // a list of the intersections directly connected to this by a road
    private ArrayList<Intersection> neighbors;

    private int maxNumRoads;

    // The average road length of roads created from this intersection.
    // Should be chosen based on the Perlin noise.
    private int averageRoadLength;

    private Color temporaryColor; // For now

    public Intersection(GameManager inputManager, Point inputCenter, RoadMap inputRM,
                        int inputMaxNumRoads, int inputAverageRoadLength)
    {
        super(inputManager, inputCenter);
        rm = inputRM;
        maxNumRoads = inputMaxNumRoads;
        averageRoadLength = inputAverageRoadLength;

        // Initializing lists
        roads = new LinkedList<Road>();
        neighbors = new ArrayList<Intersection>();
        this.intersectionFillPoints = new ArrayList<Point>();

        temporaryColor = new Color(0f, maxNumRoads/8.0f, 0, 1);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.GRAY);

        // If there is 1 road (or none), just draw a circle for the dead end
        if(roads.size() < 2)
        {
            g2d.fill(new Ellipse2D.Double(center.x - rm.getRoadWidth(), center.y - rm.getRoadWidth(),
                    2*rm.getRoadWidth(), 2*rm.getRoadWidth()));
        }
        // But if we have multiple roads, do the intersection fill.
        else
        {
            g2d.fill(this.intersectionFill);
        }

        // Draw a continuation of the center stripe if there are only two roads
        if(roads.size() == 2)
        {
            g2d.setColor(Color.YELLOW);
            Path2D line = new Path2D.Double();
            line.moveTo(roads.getFirst().getCenter().x, roads.getFirst().getCenter().y);
            line.lineTo(center.x, center.y);
            line.lineTo(roads.getLast().getCenter().x, roads.getLast().getCenter().y);
            g2d.draw(line);
        }
    }

    // ==========================================
    //
    //                 Getters
    //
    // ==========================================
    public LinkedList<Road> getRoads()
    {
        return roads;
    }
    public ArrayList<Intersection> getNeighbors()
    {
        return neighbors;
    }
    public int getMaxNumRoads()
    {
        return maxNumRoads;
    }
    public boolean canHaveNewRoad()
    {
        return roads.size() < maxNumRoads;
    }
    public int getAverageRoadLength()
    {
        return averageRoadLength;
    }
    public Shape getIntersectionFill()
    {
        return intersectionFill;
    }

    // ==========================================
    //
    //                Setters
    //
    // ==========================================

    // Add a Road which starts or ends at this Intersection
    // If boolean startsHere == true, then the road starts at this intersection
    // Otherwise, it ends here.
    // Also calls functions to update the intersection fill.
    public void addRoad(Road r, boolean startsHere)
    {
        // Add the other side of the road to the neighbors here
        if(startsHere)
        {
            this.neighbors.add(this.rm.hasIntersection(r.getEndPoint()));
        }
        else
        {
            this.neighbors.add(this.rm.hasIntersection(r.getStartPoint()));
        }


        // Put the road in proper spot in the linked list
        double angleToAdd = r.getAngleFromIntersection(this);
        if(this.roads.isEmpty() || angleToAdd < roads.getFirst().getAngleFromIntersection(this))
        {
            roads.addFirst(r);
        }
        else
        {
            double curAngle;
            Road curRoad;
            ListIterator<Road> iter = (ListIterator)roads.iterator();

            while(iter.hasNext())
            {
                curRoad = iter.next();
                curAngle = curRoad.getAngleFromIntersection(this);
                if(angleToAdd < curAngle)
                {
                    iter.previous();
                    iter.add(r);
                    this.updateIntersectionFillPoints();
                    this.updateIntersectionFill();
                    return;
                }
            }
            roads.addLast(r);
        }
        this.updateIntersectionFillPoints();
        this.updateIntersectionFill();
    }


    // ==========================================
    //
    //            Vehicle Navigation
    //
    // ==========================================

    // Returns a random road that is not the given Road, unless it is the only one
    public Road getRandomRoadExcept(Road r)
    {
        if(roads.size() == 1)
        {
            return roads.get(0);
        }
        int rand = (int) Math.floor(Math.random() * roads.size());
        Road curRoad = roads.get(rand);
        while(curRoad.equals(r))
        {
            rand = (int) Math.floor(Math.random() * roads.size());
            curRoad = roads.get(rand);
        }
        return curRoad;
    }




    // ==========================================
    //
    //               Road Building
    //
    // ==========================================

    // Returns the shortest distance from this Intersection to a segment
    public double directedDistance(Point p1, Point p2)
    {
        double distPA = center.distanceToPoint(p1);
        double distPB = center.distanceToPoint(p2);
        double distAB = p1.distanceToPoint(p2);

        double sqdistPA = distPA * distPA;
        double sqdistPB = distPB * distPB;
        double sqdistAB = distAB * distAB;

        if(sqdistPA > sqdistPB + sqdistAB || sqdistPB > sqdistPA + sqdistAB)
        {
            return Math.min(distPA, distPB);
        }

        double A = p2.y - p1.y;
        double B = p1.x - p2.x;
        double C = p1.y*p2.x - p2.y*p1.x;

        return Math.abs(A*center.x + B*center.y + C) / Math.sqrt(A*A + B*B);
    }

    // Returns true if this intersection is the same as the given coordinates
    public boolean sameCoordinates(Point p)
    {
        double tolerance = .4;
        return center.distanceToPoint(p) < tolerance;
    }

    // Calculate the intersection point between the lines determined by these
    // 4 points, with p1 and p2 determining a line, and p3 and p3 determining a line.
    // Must assume they are not both vertical
    public Point intersectionPoint(Point p1, Point p2, Point p3, Point p4)
    {
        // If both are vertical, print an error and return null
        if(Math.abs(p1.x - p2.x) < 0.001 && Math.abs(p3.x - p4.x) < 0.001)
        {
            System.out.println("Cannot intersect two vertical lines.");
            return null;
        }

        // Otherwise, proceed
        double x, y, slope1, slope2, b1, b2;
        // If the first segment is vertical
        if(Math.abs(p1.x - p2.x) < 0.001)
        {
            slope2 = (p4.y - p3.y) / (p4.x - p3.x);
            b2 = p3.y - slope2 * p3.x;
            x = p1.x;
            y = slope2*x + b2;
            return new Point(x, y);
        }
        // If the second segment is vertical
        else if(Math.abs(p3.x - p4.x) < 0.001)
        {
            slope1 = (p2.y - p1.y) / (p2.x - p1.x);
            b1 = p1.y - slope1 * p1.x;
            x = p3.x;
            y = slope1*x + b1;
            return new Point(x, y);
        }
        else
        {
            // Find both slopes
            slope1 = (p2.y - p1.y) / (p2.x - p1.x);
            slope2 = (p4.y - p3.y) / (p4.x - p3.x);
            // If parallel
            if(Math.abs(slope1 - slope2) < 0.001)
            {
                System.out.println("Cannot intersect parallel lines.");
                return null;
            }
            b1 = p1.y - slope1 * p1.x;
            b2 = p3.y - slope2 * p3.x;
            x = (b2 - b1) / (slope1 - slope2);
            y = slope1*x + b1;
            return new Point(x, y);
        }
    }

    // Given that two roads intersect at this Intersection, this finds the point where
    // their lines actually hit.
    // The assumption is that if you start at prevRoad and move clockwise around the
    // intersection, you will reach curRoad in less than 180 degrees. If that were not
    // the case, this function would give the wrong point.
    public Point roadsIntersection(Road prevRoad, Road curRoad)
    {
        // check if curRoad and prevRoad start or end at this intersection
        boolean r1Start = prevRoad.getStartInt().equals(this);
        boolean r2Start = curRoad.getStartInt().equals(this);
        if(r1Start && r2Start)
        {
            return this.intersectionPoint(prevRoad.getStartRight(), prevRoad.getEndRight(),
                    curRoad.getStartLeft(), curRoad.getEndLeft());
        }
        else if(r1Start)
        {
            return this.intersectionPoint(prevRoad.getStartRight(), prevRoad.getEndRight(),
                    curRoad.getStartRight(), curRoad.getEndRight());
        }
        else if(r2Start)
        {
            return this.intersectionPoint(prevRoad.getStartLeft(), prevRoad.getEndLeft(),
                    curRoad.getStartLeft(), curRoad.getEndLeft());
        }
        else
        {
            return this.intersectionPoint(prevRoad.getStartLeft(), prevRoad.getEndLeft(),
                    curRoad.getStartRight(), curRoad.getEndRight());
        }

    }

    // This is a helper function for updateIntersectionFillPoints. Given two consecutive roads
    // going from this intersection (must be that prevRoad is just before curRoad, going clockwise),
    // this adds points to the intersectionFillPoints list as needed (either 1 or 2).
    // If updateDrivingPoints is true, then it calls updateFBSEPoints() on prevRoad.
    public void addPointsToIntersectionFill(Road prevRoad, Road curRoad, boolean updateDrivingPoints)
    {
        // Get the angle between the two roads. Note that if curRoad is in quadrant I and prevRoad is
        // in quadrant IV, the difference will be less than negative Pi.
        double angleDifference = curRoad.getAngleFromIntersection(this)
                - prevRoad.getAngleFromIntersection(this);

        // If the two roads are coming straight at each other, add the two endPoints from the roads
        if(Math.abs(angleDifference - Math.PI) < 0.01 || Math.abs(angleDifference + Math.PI) < 0.01)
        {
            // Add the correct corner from the previous road
            if(prevRoad.getStartInt().equals(this))
            {
                intersectionFillPoints.add(prevRoad.getStartRight().getPointFromHere(
                        rm.getRoadWidth()/2, prevRoad.getAngle()));
                intersectionFillPoints.add(prevRoad.getStartRight().getPointFromHere(
                        -rm.getRoadWidth()/2, prevRoad.getAngle()));
            }
            else
            {
                intersectionFillPoints.add(prevRoad.getEndLeft().getPointFromHere(
                        -rm.getRoadWidth()/2, prevRoad.getAngle()));
                intersectionFillPoints.add(prevRoad.getEndLeft().getPointFromHere(
                        rm.getRoadWidth()/2, prevRoad.getAngle()));
            }

            if(updateDrivingPoints)
            {
                updateFBSEPoints(prevRoad, false);
            }
        }
        // If the two roads form an angle that is less than 180,
        // just add their intersection point
        else if((angleDifference > 0 && angleDifference < Math.PI) ||
                (angleDifference > -2*Math.PI && angleDifference < -Math.PI))
        {
            this.intersectionFillPoints.add(this.roadsIntersection(prevRoad, curRoad));
            if(updateDrivingPoints)
            {
                updateFBSEPoints(prevRoad, true);
            }
        }
        // Otherwise, add both of the roads' endpoints to make sure the polygon is convex
        // (this is a spot where a wedge would be missing if we just drew the roads and
        // no intersection fill).
        else
        {
            // Add the correct corner from the previous road
            if(prevRoad.getStartInt().equals(this))
            {
                intersectionFillPoints.add(prevRoad.getStartRight());
            }
            else
            {
                intersectionFillPoints.add(prevRoad.getEndLeft());
            }
            // Add the correct corner from the current road
            if(curRoad.getStartInt().equals(this))
            {
                intersectionFillPoints.add(curRoad.getStartLeft());
            }
            else
            {
                intersectionFillPoints.add(curRoad.getEndRight());
            }

            if(updateDrivingPoints)
            {
                updateFBSEPoints(prevRoad, false);
            }
        }
    }

    // This sets the driving target points for two consecutive roads in the intersection.
    // It essentially finds the intersection between the edge of the intersection fill and
    // the line the vehicles drive on in the Road.
    public void updateFBSEPoints(Road prevRoad, boolean lessThan180)
    {
        int arrSize = intersectionFillPoints.size();
        if(lessThan180)
        {
            Point p1 = intersectionPoint(prevRoad.getFS(), prevRoad.getFE(),
                    intersectionFillPoints.get(arrSize-2), intersectionFillPoints.get(arrSize-1));
            Point p2 = intersectionPoint(prevRoad.getBS(), prevRoad.getBE(),
                    intersectionFillPoints.get(arrSize-2), intersectionFillPoints.get(arrSize-1));
            if(prevRoad.getStartInt().equals(this))
            {
                prevRoad.setFS(p1);
                prevRoad.setBE(p2);
            }
            else
            {
                prevRoad.setBS(p2);
                prevRoad.setFE(p1);
            }
        }
        else
        {
            Point p1 = intersectionPoint(prevRoad.getFS(), prevRoad.getFE(),
                    intersectionFillPoints.get(arrSize-2), intersectionFillPoints.get(arrSize-3));
            Point p2 = intersectionPoint(prevRoad.getBS(), prevRoad.getBE(),
                    intersectionFillPoints.get(arrSize-2), intersectionFillPoints.get(arrSize-3));
            if(prevRoad.getStartInt().equals(this))
            {
                prevRoad.setFS(p1);
                prevRoad.setBE(p2);
            }
            else
            {
                prevRoad.setBS(p2);
                prevRoad.setFE(p1);
            }
        }
    }

    // This function sets the lists of intersection fill points. This should be called each time a
    // new road is added to the intersection, so the polygon filled in by the intersection will
    // fill in any new gaps that are created.
    public void updateIntersectionFillPoints()
    {
        if(roads.size() > 1)
        {
            // Prepare to iterate through the list.
            ListIterator<Road> iter = (ListIterator) roads.iterator();

            // Reset the list of points, since it will likely be disjoint from the previous one
            this.intersectionFillPoints = new ArrayList<Point>();

            // The corners of the rectangles for drawing the roads. We need to store this for two
            // roads at a time as we iterate through.
            Road prevRoad, curRoad;


            // Before we can iterate through, we have to handle the gap between the last and the first road
            // as a separate case.
            prevRoad = roads.getLast();
            curRoad = roads.getFirst();

            this.addPointsToIntersectionFill(prevRoad, curRoad, false);

            // Now iterate through all the roads
            iter.next();
            while(iter.hasNext())
            {
                prevRoad = curRoad;
                curRoad = iter.next();
                this.addPointsToIntersectionFill(prevRoad, curRoad, true);
            }

            // Finally, update the driving points of the last road.
            // It's an exception, so we handle it manually
            prevRoad = roads.getLast();
            int arrSize = intersectionFillPoints.size();
            Point p1 = intersectionPoint(prevRoad.getFS(), prevRoad.getFE(),
                    intersectionFillPoints.get(0), intersectionFillPoints.get(arrSize-1));
            Point p2 = intersectionPoint(prevRoad.getBS(), prevRoad.getBE(),
                    intersectionFillPoints.get(0), intersectionFillPoints.get(arrSize-1));

            if(prevRoad.getStartInt().equals(this))
            {
                prevRoad.setFS(p1);
                prevRoad.setBE(p2);
            }
            else
            {
                prevRoad.setBS(p2);
                prevRoad.setFE(p1);
            }
        }
    }

    // Whenever we add a new road, we need to update the Path2D that
    // fills the center of the intersection
    public void updateIntersectionFill()
    {
        if(roads.size() < 2) // If this was the first road, return. There is not fill yet.
        {
            return;
        }
        this.intersectionFill = new Path2D.Double();
        this.intersectionFill.moveTo(intersectionFillPoints.get(0).x,
                intersectionFillPoints.get(0).y);

        for(int i = 1; i < intersectionFillPoints.size(); i++)
        {
            intersectionFill.lineTo(intersectionFillPoints.get(i).x,
                    intersectionFillPoints.get(i).y);
        }
        intersectionFill.closePath();
    }






    // ==========================================
    //
    //           Scrolling Functions
    //
    // ==========================================
    public void moveUp(double amount)
    {
        center.y -= amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(0, -amount);
        intersectionFill = (Path2D) tx.createTransformedShape(intersectionFill);
    }
    public void moveDown(double amount)
    {
        center.y += amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(0, amount);
        intersectionFill = (Path2D) tx.createTransformedShape(intersectionFill);
    }
    public void moveLeft(double amount)
    {
        center.x -= amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(-amount, 0);
        intersectionFill = (Path2D) tx.createTransformedShape(intersectionFill);
    }
    public void moveRight(double amount)
    {
        center.x += amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(amount, 0);
        intersectionFill = (Path2D) tx.createTransformedShape(intersectionFill);
    }


    // ==========================================
    //
    //                  Debug
    //
    // ==========================================
    public void printThings()
    {
        System.out.println("Intersection at " + center.x + ", " + center.y);
        System.out.println("\tNum roads: " + roads.size());
    }
}
