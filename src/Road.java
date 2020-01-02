import java.awt.*;
import java.util.Optional;

// A road is fundamentally two points with a segment connecting them.
// I am choosing to order the points such that the road starts at
// the more left point, and ends at the more right point. A vertical
// road starts down and goes up.
public class Road extends GameObject
{
    // ==========================================
    //
    //              Line Properties
    //
    // ==========================================
    private Intersection startInt;
    private Intersection endInt;
    private Point p1;
    private Point p2;

    // Standard properties of a line
    private Optional<Double> slope;  // Optional in case of undefined
    private Optional<Double> yInt;   // Extending the segment to the y-axis
    private double angle;            // The angle from the first point to the second point

    // ==========================================
    //
    //          Game Management Fields
    //
    // ==========================================
    private GameManager manager;
    private RoadMap rm;
    // The nth road created is number n;
    private int ID;

    public Road(GameManager inputManager, Point inputCenter, RoadMap inputRM, int inputID,
                Intersection inputStartInt, Intersection inputEndInt)
    {
        super(inputManager, inputCenter);
        rm = inputRM;
        this.ID = inputID;
        this.orderIntersections(inputStartInt, inputEndInt);
        this.computeSlope();
        this.computeYInt();
        this.computeAngle();
    }



    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics2D g2d) {

    }

    // ==========================================
    //
    //          Initialization Functions
    //
    // ==========================================
    // Set startInt and endInt such that startInt is left or down from endInt
    public void orderIntersections(Intersection int1, Intersection int2)
    {
        if(int1.getCenter().x < int2.getCenter().x ||
                (int1.getCenter().x == int2.getCenter().x && int1.getCenter().y > int2.getCenter().y))
        {
            startInt = int1;
            endInt = int2;
        }
        else
        {
            startInt = int2;
            endInt = int1;
        }
    }
    public void computeSlope()
    {
        if(startInt.getCenter().x == endInt.getCenter().x) // x-coordinates are same means vertical
        {
            slope = Optional.empty();
        }
        else
        {   // Otherwise, use the slope formula
            slope = Optional.of((endInt.getCenter().y - startInt.getCenter().y) /
                    (endInt.getCenter().x - startInt.getCenter().x));
        }
    }
    public void computeYInt()
    {
        if(slope.isPresent())
        {
            yInt = Optional.of(startInt.getCenter().y - slope.get()*startInt.getCenter().x);
        }
        else
        {
            yInt = Optional.empty();
        }
    }
    public void computeAngle()
    {
        angle = startInt.getCenter().angleToOtherPoint(endInt.getCenter());
    }
}
