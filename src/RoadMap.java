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

        this.makeFirstIntersection();
    }

    public void tick()
    {

    }

    public void render(Graphics2D g2d)
    {

    }

    // ==========================================
    //
    //          Initialization Functions
    //
    // ==========================================
    public void makeFirstIntersection()
    {
        Point centerOfMap = new Point(manager.getWidth()/2, manager.getHeight()/2);
        this.addIntersection(new Intersection(manager, centerOfMap, this));
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
}
