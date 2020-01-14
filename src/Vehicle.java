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
    private double vx, vy;       // x and y components of the current velocity
    private Point target;

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
        isGoingForward = true;
        isOnRoad = true;
        this.updateNextIntersection();
        aggression = inputAggression;
        this.updateTarget();

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




    // ==========================================
    //
    //          Movement and Behavior
    //
    // ==========================================
    // The nextInt is set to be either the start or end int of the current road,
    // depending on which way the Vehicle is going
    public void updateNextIntersection()
    {
        if(this.isGoingForward)
        {
            this.nextInt = this.curRoad.getEndInt();
        }
        else
        {
            this.nextInt = this.curRoad.getStartInt();
        }
    }

    // Return true if we are going down a dead end road
    public boolean checkStuck()
    {
        return nextInt.getRoads().size() == 1;
    }

    // If the Vehicle is on a dead end road, target halfway along the Road
    public void updateTargetStuck()
    {
        if(isGoingForward)
        {
            target = Point.midPoint(curRoad.getFS(), curRoad.getFE());
        }
        else
        {
            target = Point.midPoint(curRoad.getBS(), curRoad.getBE());
        }
    }


    public void updateTarget()
    {
        if(this.checkStuck())
        {
            this.updateTargetStuck();
        }
        else
        {
            // If we are on a Road, target the end of the Road
            if(this.isOnRoad)
            {
                if(this.isGoingForward)
                {
                    target = curRoad.getFE();
                }
                else
                {
                    target = curRoad.getBE();
                }
            }
            // If we are in an Intersection, target the start of the next Road
            else
            {
                if(nextRoad.getStartInt().equals(nextInt))
                {
                    target = nextRoad.getFS();
                }
                else target = nextRoad.getBS();
            }
        }
    }
}
