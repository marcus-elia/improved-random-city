import java.awt.*;
import java.awt.geom.Line2D;
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
    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.RED);
        Line2D line = new Line2D.Double(x1(), y1(), x2(), y2());
        g2d.draw(line);
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


    // ==========================================
    //
    //                 Getters
    //
    // ==========================================
    public Intersection getStartInt()
    {
        return startInt;
    }
    public Intersection getEndInt()
    {
        return endInt;
    }
    public double x1()
    {
        return startInt.getCenter().x;
    }
    public double y1()
    {
        return startInt.getCenter().y;
    }
    public double x2()
    {
        return endInt.getCenter().x;
    }
    public double y2()
    {
        return endInt.getCenter().y;
    }




    // ==========================================
    //
    //          Road Building Functions
    //
    // ==========================================
    // Return true if this road intersects the road determined by p1 and p2
    public boolean hitsRoad(Point p1, Point p2)
    {
        double xOfIntersection;
        Point temp;

        // Reorder p1 and p2 if needed
        if(p1.x > p2.x || (p1.x == p2.x && p1.y < p2.y))
        {
            temp = p1;
            p1 = p2;
            p2 = temp;
        }

        // If both are vertical
        if(p1.x == p2.x && slope.isEmpty())
        {
            return false;
        }
        // If just the new segment is vertical
        else if(p1.x == p2.x)
        {
            // Check that this line a) starts left of the vertical line
            //                      b) ends right of the vertical line
            //                      c) crosses the vertical line higher than its bottom
            //                      d) crosses the vertical line lower than its top
            return startInt.getCenter().x < p1.x && endInt.getCenter().x > p1.x &&
                    slope.get()*p1.x + yInt.get() < p1.y && slope.get()*p1.x + yInt.get() > p2.y;
        }

        // If we haven't returned yet, then we know the new segment is not vertical. We can find its
        // slope and y-intercept
        double otherSlope = (p2.y - p1.y) / (double)(p2.x - p1.x);
        double otherYint = p1.y - otherSlope*p1.x;

        // if just this road is vertical
        if(slope.isEmpty())
        {
            // Same thing as we just did, just the opposite
            return p1.x < startInt.getCenter().x && p2.x > startInt.getCenter().x &&
                otherSlope*startInt.getCenter().x + otherYint < startInt.getCenter().y &&
                otherSlope*startInt.getCenter().x + otherYint > endInt.getCenter().y;
        }

        // If the two segments have the same slope, they do not intersect
        if(Math.abs(otherSlope - slope.get()) < 0.001)
        {
            return false;
        }

        // Otherwise, calculate the intersection point normally
        xOfIntersection = (otherYint - yInt.get()) / (slope.get() - otherSlope);
        return xOfIntersection > startInt.getCenter().x && xOfIntersection < endInt.getCenter().x
                && xOfIntersection >= p1.x  && xOfIntersection <= p2.x;
    }

    // This returns the shortest distance between this segment and p. It is either the distance between
    // p and one of the endpoints, or the true perpendicular directed distance
    public double directedDistance(Point p)
    {
        double A, B, C;

        // if this road is vertical
        if(slope.isEmpty())
        {
            if(p.y > endInt.getCenter().y && p.y < startInt.getCenter().y)
            {
                return Math.abs(p.x - startInt.getCenter().x);
            }
            else
            {
                return Math.min(startInt.getCenter().distanceToPoint(p),
                        endInt.getCenter().distanceToPoint(p));
            }
        }
        // if the road is horizontal
        if(slope.get() == 0)
        {
            if(p.x > startInt.getCenter().x && p.x < endInt.getCenter().x)
            {
                return Math.abs(p.y - startInt.getCenter().y);
            }
            else
            {
                return Math.min(startInt.getCenter().distanceToPoint(p),
                        endInt.getCenter().distanceToPoint(p));
            }
        }

        else
        {
            A = slope.get();
            B = -1;
            C = yInt.get();
            double otherSlope = -1/A;
            double otherYint = p.y - otherSlope*p.x;
            double xOfIntersection = (otherYint - yInt.get()) / (slope.get() - otherSlope);
            if(xOfIntersection > startInt.getCenter().x && xOfIntersection < endInt.getCenter().x)
            {
                return Math.abs(A*p.x + B*p.y + C) / Math.sqrt(A*A + B*B);
            }
            else
            {
                return Math.min(startInt.getCenter().distanceToPoint(p),
                        endInt.getCenter().distanceToPoint(p));
            }
        }
    }
}
