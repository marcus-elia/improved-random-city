import java.awt.*;
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
    private ArrayList<Double> intersectionFillXPoints;
    private ArrayList<Double> intersectionFillYPoints;
    private Path2D intersectionFill;

    // a list of the intersections directly connected to this by a road
    private ArrayList<Intersection> neighbors;

    private int maxNumRoads;

    public Intersection(GameManager inputManager, Point inputCenter, RoadMap inputRM, int inputMaxNumRoads)
    {
        super(inputManager, inputCenter);
        rm = inputRM;
        maxNumRoads = inputMaxNumRoads;

        // Initializing lists
        roads = new LinkedList<Road>();
        neighbors = new ArrayList<Intersection>();
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.MAGENTA);
        g2d.fill(new Ellipse2D.Double(center.x - 4, center.y - 4, 8, 8));
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

    // ==========================================
    //
    //                Setters
    //
    // ==========================================

    // Add a Road which starts or ends at this Intersection
    // If boolean startsHere == true, then the road starts at this intersection
    // Otherwise, it ends here.
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


        // put the road in proper spot in the linked list
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
                    //this.updateIntersectionFillPoints();
                    //this.updateIntersectionFill();
                    return;
                }
            }
            roads.addLast(r);
        }
        //this.updateIntersectionFillPoints();
        //this.updateIntersectionFill();
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
}
