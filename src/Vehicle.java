import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
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
        curSpeed = 0.5;
        this.initializeTarget();
        this.updateAngleAndSpeed();
    }

    @Override
    public void tick()
    {
        // First, check if we are at the target
        if(center.distanceToPoint(target) < 0.001)
        {
            // Handle the case of being halfway down a dead end Road
            if(isStuck)
            {
                stuckTime++;
                // If a new Road has been built for us to drive on to, the Vehicle
                // is now unstuck
                if(!this.checkStuck())
                {
                    isStuck = false;
                    stuckTime = 0;
                    updateTargetOnRoad(); // Now target the end of the Road
                }
            }
            // Next, handle the case of being stopped at an Intersection
            else if(isStopped)
            {
                stoppedTime++;
                // If we have waited long enough, start going
                if(stoppedTime == 50)
                {
                    isStopped = false;
                    stoppedTime = 0;
                    isOnRoad = false;
                    this.updateTargetThroughIntersection();
                    this.updateAngleAndSpeed();
                }
            }
            // If we have just arrived at an Intersection
            else if(isOnRoad)
            {
                isStopped = true;
            }
            // The last case is that we are at the start of the new Road
            else
            {
                isOnRoad = true;
                this.enterNewRoad();
                this.updateAngleAndSpeed();
            }
        }

        // Otherwise, drive.
        else
        {
            // If we are one frame away from the target or less, move to it.
            if(center.distanceToPoint(target) < curSpeed)
            {
                this.moveX(target.x - center.x);
                this.moveY(target.y - center.y);
            }
            // Otherwise, keep going toward it
            else
            {
                this.moveX(vx);
                this.moveY(vy);
            }
        }
    }

    @Override
    public void render(Graphics2D g2d)
    {
        if(isStuck)
        {
            g2d.setColor(Color.white);
        }
        else if(isOnRoad)
        {
            g2d.setColor(Color.green);
        }
        else
        {

            g2d.setColor(color);
        }
        g2d.fill(hitbox);

        g2d.setColor(Color.blue);
        Shape dot = new Ellipse2D.Double(curRoad.getCenter().x-4, curRoad.getCenter().y-4, 8, 8);
        g2d.fill(dot);
        g2d.setColor(Color.green);
        dot = new Ellipse2D.Double(nextInt.getCenter().x-4, nextInt.getCenter().y-4, 8, 8);
        g2d.fill(dot);
    }

    // ==========================================
    //
    //         Initialization Functions
    //
    // ==========================================
    public void makeHitbox()
    {
        hitbox = new Rectangle2D.Double(center.x - xWidth/2, center.y - yWidth/2, xWidth, yWidth);
        rotateTransform.rotate(angle, center.x, center.y);
        hitbox = rotateTransform.createTransformedShape(hitbox);
    }


    public void initializeTarget()
    {
        if(this.checkStuck())
        {
            isStuck = true;
            this.updateTargetStuck();
        }
        else
        {
            this.updateNextIntersection();
            this.updateTargetOnRoad();
        }
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
        isStuck = true;
        if(isGoingForward)
        {
            target = Point.midPoint(curRoad.getFS(), curRoad.getFE());
        }
        else
        {
            target = Point.midPoint(curRoad.getBS(), curRoad.getBE());
        }
    }

    // If we are on a Road that is not a dead end, update the target and the
    // nextInt and nextRoad
    public void updateTargetOnRoad()
    {
        if(this.isGoingForward)
        {
            target = curRoad.getFE();
        }
        else
        {
            target = curRoad.getBE();
        }
        nextRoad = nextInt.getRandomRoadExcept(curRoad);
    }

    // When the Vehicle leaves an Intersection and starts driving on a new Road, this sets
    // the target and curRoad and nextInt (and nextRoad if applicable) based on whether
    // the new Road is a dead end or not.
    public void enterNewRoad()
    {
        curRoad = nextRoad;
        this.isGoingForward = curRoad.getStartInt().equals(nextInt);
        this.updateNextIntersection();
        if(this.checkStuck())
        {
            isStuck = true;
            this.updateTargetStuck();
        }
        else
        {
            this.updateTargetOnRoad();
        }
    }

    // When the Vehicle has just arrived at the beginning of an Intersection,
    // target the start of the next Road through the Intersection.
    public void updateTargetThroughIntersection()
    {
         if(nextRoad.getStartInt().equals(nextInt))
         {
             target = nextRoad.getFS();
         }
         else
         {
             target = nextRoad.getBS();
         }
    }

    public void updateVelocityComponents()
    {
        vx = curSpeed*Math.cos(angle);
        vy = curSpeed*Math.sin(angle);
    }

    // Whenever the target Point is changed, we have to update the angle and speed fields
    // so that the Vehicle moves toward the target
    public void updateAngleAndSpeed()
    {
        double newAngle = center.angleToOtherPoint(target);
        rotateTransform = new AffineTransform();
        rotateTransform.rotate(newAngle - angle, center.x, center.y);
        hitbox = rotateTransform.createTransformedShape(hitbox);
        angle = newAngle;
        this.updateVelocityComponents();
    }

    // Move the Vehicle in the x-axis, but keep the target in place
    public void moveX(double amount)
    {
        center.x += amount;
        AffineTransform move = new AffineTransform();
        move.translate(amount, 0);
        hitbox = move.createTransformedShape(hitbox);
    }
    // Move the Vehicle in the y-axis, but keep the target in place
    public void moveY(double amount)
    {
        center.y += amount;
        AffineTransform move = new AffineTransform();
        move.translate(0, amount);
        hitbox = move.createTransformedShape(hitbox);
    }


    // ==========================================
    //
    //            Scrolling Functions
    //
    // ==========================================
    // Everything, including the target, moves when scrolling
    public void moveUp(double amount)
    {
        center.y -= amount;
        //target.y -= amount;
        AffineTransform move = new AffineTransform();
        move.translate(0, -amount);
        hitbox = move.createTransformedShape(hitbox);
    }
    public void moveDown(double amount)
    {
        center.y += amount;
        //target.y += amount;
        AffineTransform move = new AffineTransform();
        move.translate(0, amount);
        hitbox = move.createTransformedShape(hitbox);
    }
    public void moveLeft(double amount)
    {
        center.x -= amount;
        //target.x -= amount;
        AffineTransform move = new AffineTransform();
        move.translate(-amount, 0);
        hitbox = move.createTransformedShape(hitbox);
    }
    public void moveRight(double amount)
    {
        center.x += amount;
        //target.x += amount;
        AffineTransform move = new AffineTransform();
        move.translate(amount, 0);
        hitbox = move.createTransformedShape(hitbox);
    }
}
