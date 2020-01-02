import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class Lake extends GameObject
{
    private double xRadius;
    private double yRadius;
    private double angle;
    private Ellipse2D.Double ellipse;

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
        ellipse = (Ellipse2D.Double) tx.createTransformedShape(ellipse);
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
    public Ellipse2D getEllipse()
    {
        return ellipse;
    }
}
