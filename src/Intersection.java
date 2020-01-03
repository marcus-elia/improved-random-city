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
}
