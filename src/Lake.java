import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Lake extends GameObject
{
    private double xRadius;
    private double yRadius;
    private double angle;
    private Shape ellipse;

    // The focii of the ellipse
    private Point focus1;
    private Point focus2;

    public Lake(GameManager inputManager, Point inputCenter, double inputXRadius, double inputYRadius, double inputAngle)
    {
        super(inputManager, inputCenter);
        xRadius = inputXRadius;
        yRadius = inputYRadius;
        angle = inputAngle;
        this.createEllipse();
        this.setFocii();
    }


    @Override
    public void tick()
    {

    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.BLUE);
        g2d.fill(this.ellipse);
    }

    // ==========================================
    //
    //          Initialization Functions
    //
    // ==========================================
    public void createEllipse()
    {
        AffineTransform tx = new AffineTransform();
        tx.rotate(this.angle, center.x, center.y);

        ellipse = new Ellipse2D.Double(center.x - xRadius, center.y - yRadius, 2*xRadius, 2*yRadius);
        ellipse = tx.createTransformedShape(ellipse);
    }

    // Set the focus points using ellipse properties
    public void setFocii()
    {
        double c = Math.sqrt(xRadius*xRadius - yRadius*yRadius);
        focus1 = new Point(center.x + c*Math.cos(angle), center.y + c*Math.sin(angle));
        focus2 = new Point(center.x - c*Math.cos(angle), center.y - c*Math.sin(angle));
    }

    // ==========================================
    //
    //                Getters
    //
    // ==========================================
    public double getXRadius()
    {
        return xRadius;
    }
    public double getYRadius()
    {
        return yRadius;
    }
    public double getAngle()
    {
        return angle;
    }
    public Shape getEllipse()
    {
        return ellipse;
    }
    public Point getFocus1()
    {
        return focus1;
    }
    public Point getFocus2()
    {
        return focus2;
    }

    // ==========================================
    //
    //           Scrolling Functions
    //
    // ==========================================
    public void moveUp(double amount)
    {
        center.y -= amount;
        focus1.y -= amount;
        focus2.y -= amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(0, -amount);
        ellipse = tx.createTransformedShape(ellipse);
    }
    public void moveDown(double amount)
    {
        center.y += amount;
        focus1.y += amount;
        focus2.y += amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(0, amount);
        ellipse = tx.createTransformedShape(ellipse);
    }
    public void moveLeft(double amount)
    {
        center.x -= amount;
        focus1.x -= amount;
        focus2.x -= amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(-amount, 0);
        ellipse = tx.createTransformedShape(ellipse);
    }
    public void moveRight(double amount)
    {
        center.x += amount;
        focus1.x += amount;
        focus2.x += amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(amount, 0);
        ellipse = tx.createTransformedShape(ellipse);
    }



    // ==========================================
    //
    //         City Building Functions
    //
    // ==========================================
    public boolean containsPoint(Point p)
    {
        Point2D p2d = new Point2D.Double(p.x, p.y);
        return ellipse.contains(p2d);
    }

    // Returns true if the two lakes would be intersecting if at the right angle
    // (if their x-axes happened to align)
    public static boolean potentiallyOverlapping(Point c1, double xRad1, Point c2, double xRad2)
    {
        return c1.distanceToPoint(c2) < xRad1 + xRad2;
    }

    public static double focusDistanceSum(Point f1, Point f2, Point p)
    {
        return f1.distanceToPoint(p) + f2.distanceToPoint(p);
    }

    // Returns true if the given point p is too close to the ellipse to put an
    // intersection there.
    public static boolean isTooCloseToLake(Point f1, Point f2, double xRadius, Point p, double roadWidth)
    {
        return Lake.focusDistanceSum(f1, f2, p) < 2*xRadius + 2*roadWidth;
    }
}
