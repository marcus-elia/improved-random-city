import java.awt.*;
import java.util.ArrayList;

public class RoadMap extends GameObject
{
    private ArrayList<Intersection> intersections;
    private ArrayList<Road> roads;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Lake> lakes;

    public RoadMap(GameManager inputManager, Point inputCenter)
    {
        super(inputManager, inputCenter);
        intersections = new ArrayList<Intersection>();
        roads = new ArrayList<Road>();
        vehicles = new ArrayList<Vehicle>();
        lakes = new ArrayList<Lake>();

        this.makeFirstIntersection();
    }

    public void tick()
    {
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
