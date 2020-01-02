import java.awt.*;
import java.util.ArrayList;

public class RoadMap extends GameObject
{
    private ArrayList<Intersection> intersections;
    private ArrayList<Road> roads;
    private ArrayList<Vehicle> vehicles;

    public RoadMap(GameManager inputManager, Point inputCenter)
    {
        super(inputManager, inputCenter);
        intersections = new ArrayList<Intersection>();
        roads = new ArrayList<Road>();
        vehicles = new ArrayList<Vehicle>();
    }

    public void tick()
    {

    }

    public void render(Graphics2D g2d)
    {

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
}
