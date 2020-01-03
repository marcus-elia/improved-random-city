import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.LinkedList;

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
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics2D g2d) {

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
}
