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

    public Lake(GameManager inputManager, Point inputCenter, double inputXRadius, double inputYRadius, double inputAngle)
    {
        super(inputManager, inputCenter);
        xRadius = inputXRadius;
        yRadius = inputYRadius;
        angle = inputAngle;
        this.createEllipse();

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
        tx.rotate(this.angle);

        ellipse = new Ellipse2D.Double(center.x - xRadius, center.y - yRadius, xRadius*2, yRadius*2);
        ellipse = tx.createTransformedShape(ellipse);
    }

    // ==========================================
    //
    //               Getters
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

    // ==========================================
    //
    //           Scrolling Functions
    //
    // ==========================================
    public void moveUp(double amount)
    {
        center.y -= amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(0, -amount);
        ellipse = tx.createTransformedShape(ellipse);
    }
    public void moveDown(double amount)
    {
        center.y += amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(0, amount);
        ellipse = tx.createTransformedShape(ellipse);
    }
    public void moveLeft(double amount)
    {
        center.x -= amount;
        AffineTransform tx = new AffineTransform();
        tx.translate(-amount, 0);
        ellipse = tx.createTransformedShape(ellipse);
    }
    public void moveRight(double amount)
    {
        center.x += amount;
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
}
