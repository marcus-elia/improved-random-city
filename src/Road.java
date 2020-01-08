import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
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

    // Standard properties of a line
    private Optional<Double> slope;  // Optional in case of undefined
    private Optional<Double> yInt;   // Extending the segment to the y-axis
    private double angle;            // The angle from the first point to the second point


    // ==========================================
    //
    //            For Drawing the Road
    //
    // ==========================================
    private double roadWidth;
    // the Points that determine where the road is drawn in 2D
    private Point startRight, endRight, startLeft, endLeft;

    private Path2D rectangle;
    private Shape centerStripe;

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
                Intersection inputStartInt, Intersection inputEndInt, double inputRoadWidth)
    {
        super(inputManager, inputCenter);
        rm = inputRM;
        roadWidth = inputRoadWidth;
        this.ID = inputID;
        this.orderIntersections(inputStartInt, inputEndInt);
        this.computeSlope();
        this.computeYInt();
        this.computeAngle();
        this.setDrawPoints();
        this.setRectangle();
        this.setCenterStripe();
    }



    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.GRAY);
        g2d.fill(rectangle);
        g2d.setColor(Color.YELLOW);
        g2d.draw(centerStripe);
    }

    // ==========================================
    //
    //          Initialization Functions
    //
    // ==========================================
    // Set startInt and endInt such that startInt is left or down from endInt
    public void orderIntersections(Intersection int1, Intersection int2)
    {
        if(int1.getCenter().x - int2.getCenter().x < -0.001 ||
                (Math.abs(int1.getCenter().x - int2.getCenter().x) < 0.001 && int1.getCenter().y > int2.getCenter().y))
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
        if(Math.abs(startInt.getCenter().x - endInt.getCenter().x) < 0.001) // x-coordinates are same means vertical
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


    public void setDrawPoints()
    {
        // vertical line case
        if(slope.isEmpty())
        {
            this.startRight = new Point(x1() + roadWidth, y1());
            this.endRight = new Point(x1() + roadWidth, y2());
            this.startLeft = new Point(x1() - roadWidth, y1());
            this.endLeft = new Point(x1() - roadWidth, y2());
        }
        // negative slope
        else if(slope.get() < 0)
        {
            double angleC = Math.PI/2 - Math.abs(Math.atan(slope.get())); // complement angle
            this.startRight = new Point(x1() + Math.cos(angleC)*roadWidth,y1() + Math.sin(angleC)*roadWidth);
            this.startLeft = new Point(x1() - Math.cos(angleC)*roadWidth, y1() - Math.sin(angleC)*roadWidth);
            this.endRight = new Point(x2() + Math.cos(angleC)*roadWidth, y2() + Math.sin(angleC)*roadWidth);
            this.endLeft = new Point(x2() - Math.cos(angleC)*roadWidth, y2() - Math.sin(angleC)*roadWidth);
        }
        // positive slope
        else
        {
            double angleC = Math.PI/2 - Math.abs(Math.atan(slope.get())); // complement angle
            this.startRight = new Point(x1() - Math.cos(angleC)*roadWidth, y1() + Math.sin(angleC)*roadWidth);
            this.startLeft = new Point(x1() + Math.cos(angleC)*roadWidth, y1() - Math.sin(angleC)*roadWidth);
            this.endRight = new Point(x2() - Math.cos(angleC)*roadWidth, y2() + Math.sin(angleC)*roadWidth);
            this.endLeft = new Point(x2() + Math.cos(angleC)*roadWidth, y2() - Math.sin(angleC)*roadWidth);
        }
    }

    public void setRectangle()
    {
        rectangle = new Path2D.Double();
        rectangle.moveTo(startRight.x, startRight.y);
        rectangle.lineTo(startLeft.x, startLeft.y);
        rectangle.lineTo(endLeft.x, endLeft.y);
        rectangle.lineTo(endRight.x, endRight.y);
        rectangle.lineTo(startRight.x, startRight.y);
    }
    public void setCenterStripe()
    {
        Point startMidPoint = Point.midPoint(startLeft, startRight);
        Point endMidPoint = Point.midPoint(endLeft, endRight);
        centerStripe = new Line2D.Double(startMidPoint.x, startMidPoint.y, endMidPoint.x, endMidPoint.y);
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
    public Point getStartPoint()
    {
        return startInt.getCenter();
    }
    public Point getEndPoint()
    {
        return endInt.getCenter();
    }
    public Optional<Double> getSlope()
    {
        return slope;
    }
    public Optional<Double> getYInt()
    {
        return yInt;
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

    // Return the actual coordinates where the road is drawn
    /*public double[] getDrawCoordinates()
    {
        return new double[]{startRightX, startRightY, endRightX,
                endRightY, startLeftX, startLeftY, endLeftX, endLeftY};
    }*/
    public Point getStartRight()
    {
        return startRight;
    }
    public Point getEndRight()
    {
        return endRight;
    }
    public Point getStartLeft()
    {
        return startLeft;
    }
    public Point getEndLeft()
    {
        return endLeft;
    }




    // ==========================================
    //
    //          Road Building Functions
    //
    // ==========================================

    // Return true if the road with slope and yInt, determined by p1 and p2,
    // intersects the road determined by p3 and p4
    // This does not handle the case of the two roads being on the same line. It does
    // not need to, since the program will know that the point is too close
    public static boolean hitsRoad(Optional<Double> slope, Optional<Double> yInt, Point p1, Point p2,
                            Point p3, Point p4)
    {
        double xOfIntersection;
        Point temp;

        // Reorder p3 and p4 if needed
        if(p3.x > p4.x || (Math.abs(p3.x - p4.x) < 0.001 && p3.y < p4.y))
        {
            temp = p3;
            p3 = p4;
            p4 = temp;
        }
        // If both are vertical
        if(Math.abs(p3.x - p4.x) < 0.001 && slope.isEmpty())
        {
            return false;
        }
        // If just the new segment is vertical
        else if(Math.abs(p3.x - p4.x) < 0.001)
        {
            // Check that this line a) starts left of the vertical line
            //                      b) ends right of the vertical line
            //                      c) crosses the vertical line higher than its bottom
            //                      d) crosses the vertical line lower than its top
            return p1.x < p3.x && p2.x > p3.x &&
                    slope.get()*p3.x + yInt.get() < p3.y && slope.get()*p3.x + yInt.get() > p4.y;
        }

        // If we haven't returned yet, then we know the new segment is not vertical. We can find its
        // slope and y-intercept
        double otherSlope = (p4.y - p3.y) / (double)(p4.x - p3.x);
        double otherYint = p3.y - otherSlope*p3.x;

        // if just this road is vertical
        if(slope.isEmpty())
        {
            // Same thing as we just did, just the opposite
            return p3.x < p1.x && p4.x > p1.x &&
                    otherSlope*p1.x + otherYint < p1.y &&
                    otherSlope*p1.x + otherYint > p2.y;
        }

        // If the two segments have the same slope, they do not intersect
        if(Math.abs(otherSlope - slope.get()) < 0.001)
        {
            return false;
        }

        // Otherwise, calculate the intersection point normally
        xOfIntersection = (otherYint - yInt.get()) / (slope.get() - otherSlope);
        return xOfIntersection > p1.x && xOfIntersection < p2.x
                && xOfIntersection >= p3.x  && xOfIntersection <= p4.x;
    }

    // Wrapper function. Returns true if the segment determined by p1 and p2 hits this road
    public boolean hitsRoad(Point p1, Point p2)
    {
        return Road.hitsRoad(slope, yInt, getStartPoint(), getEndPoint(), p1, p2);
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


    // Get the angle that this road makes when leaving the given intersection
    // Only works if the intersection is either the start or end of the Road
    public double getAngleFromIntersection(Intersection intsec)
    {
        if(this.startInt.equals(intsec))
        {
            return this.angle;
        }
        else if(this.endInt.equals(intsec))
        {
            if(this.angle >= Math.PI)
            {
                return this.angle - Math.PI;
            }
            else
            {
                return this.angle + Math.PI;
            }
        }
        System.out.println("The intersection is not attached to this road.");
        return 0;
    }

    // ==========================================
    //
    //           Scrolling Functions
    //
    // ==========================================
    public void moveUp(double amount)
    {
        center.y -= amount;
        if(yInt.isPresent())
        {
            yInt = Optional.of(yInt.get() - amount);
        }
        AffineTransform tx = new AffineTransform();
        tx.translate(0, -amount);
        rectangle = (Path2D) tx.createTransformedShape(rectangle);
        centerStripe = tx.createTransformedShape(centerStripe);
    }
    public void moveDown(double amount)
    {
        center.y += amount;
        if(yInt.isPresent())
        {
            yInt = Optional.of(yInt.get() + amount);
        }
        AffineTransform tx = new AffineTransform();
        tx.translate(0, amount);
        rectangle = (Path2D) tx.createTransformedShape(rectangle);
        centerStripe = tx.createTransformedShape(centerStripe);
    }
    public void moveLeft(double amount)
    {
        center.x -= amount;
        if(yInt.isPresent())
        {
            yInt = Optional.of(yInt.get() + amount * slope.get());
        }
        AffineTransform tx = new AffineTransform();
        tx.translate(-amount, 0);
        rectangle = (Path2D) tx.createTransformedShape(rectangle);
        centerStripe = tx.createTransformedShape(centerStripe);
    }
    public void moveRight(double amount)
    {
        center.x += amount;
        if(yInt.isPresent())
        {
            yInt = Optional.of(yInt.get() - amount * slope.get());
        }
        AffineTransform tx = new AffineTransform();
        tx.translate(amount, 0);
        rectangle = (Path2D) tx.createTransformedShape(rectangle);
        centerStripe = tx.createTransformedShape(centerStripe);
    }

    // ==========================================
    //
    //                  Debug
    //
    // ==========================================
    public void printThings()
    {
        System.out.println("Road centered at " + center.x + ", " + center.y);
        if(slope.isPresent())
        {
            System.out.println("\tSlope: " + slope.get());
        }
        System.out.println("\tStart: " + getStartPoint().x + ", " + getStartPoint().y);
        System.out.println("\tEnd: " + getEndPoint().x + ", " + getEndPoint().y);
    }
}
