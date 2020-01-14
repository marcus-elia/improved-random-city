import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Vehicle extends GameObject
{
    // ==========================================
    //
    //            Shape and Drawing
    //
    // ==========================================
    private double xWidth;
    private double yWidth;
    private AffineTransform rotateTransform;
    private Shape hitbox;
    private Color color;

    // ==========================================
    //
    //           Movement and Position
    //
    // ==========================================
    private double angle;
    private double curSpeed;
    private double maxSpeed;
    private double acceleration;

    // ==========================================
    //
    //           AI and Game Management
    //
    // ==========================================
    private double aggression;
    private Road curRoad, nextRoad;
    private Intersection nextInt;
    private boolean isGoingForward; // relative to curRoad, are we going forward?
    private boolean isOnRoad;       // are we on a road, versus in an intersection?
    private boolean isStuck;        // are we on a dead end road?
    private int stuckTime;
    private boolean isStopped;      // are we stopped before entering an intersection?
    private int stoppedTime;


    public Vehicle(GameManager inputManager, Point inputCenter, double inputXWidth, double inputYWidth, Color inputColor,
                   Road inputCurRoad, double inputAggression)
    {
        super(inputManager, inputCenter);
        xWidth = inputXWidth;
        yWidth = inputYWidth;
        rotateTransform = new AffineTransform();
        this.makeHitbox();
        color = inputColor;

        curRoad = inputCurRoad;
        aggression = inputAggression;


    }

    @Override
    public void tick()
    {

    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(color);
        g2d.fillOval((int)center.x, (int)center.y, 5, 5);
    }

    // ==========================================
    //
    //         Initialization Functions
    //
    // ==========================================
    public void makeHitbox()
    {
        hitbox = new Rectangle2D.Double(center.x - xWidth/2, center.y - yWidth/2, xWidth, yWidth);
        rotateTransform.rotate(angle);
        hitbox = rotateTransform.createTransformedShape(hitbox);
    }
}
