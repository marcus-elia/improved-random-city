// This class functions as a struct
public class Point
{
    public double x;
    public double y;

    public Point(double inputX, double inputY)
    {
        x = inputX;
        y = inputY;
    }

    // ==========================================
    //
    //             Geometry Functions
    //
    // ==========================================

    // The Euclidean distance
    public double distanceFormula(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
    }
    // Wrapper function
    public double distanceToPoint(Point p)
    {
        return distanceFormula(x, y, p.x, p.y);
    }

    // This returns the angle of the ray from this point to the argument
    // Point p. The angle is returned in radians between 0 and 2pi.
    public double angleToOtherPoint(Point p)
    {
        double theta = Math.atan2(p.y - y, p.x - x);
        if(theta < 0)
        {
            return theta + 2*Math.PI;
        }
        return theta;
    }

    // Returns the point whose coordinates are p rotated around this by theta radians
    public Point rotateAroundThis(double px, double py, double theta)
    {
        // Translate the point to the origin
        double shiftedX = px - this.x;
        double shiftedY = py - this.y;

        // Rotate around the origin
        double newX = shiftedX*Math.cos(theta) - shiftedY*Math.sin(theta);
        double newY = shiftedX*Math.sin(theta) + shiftedY*Math.cos(theta);

        // Translate it back
        return new Point(newX + this.x, newY + this.y);
    }
    // Wrapper function
    public Point rotateAroundThis(Point p, double theta)
    {
        return this.rotateAroundThis(p.x, p.y, theta);
    }


    public Point getPointFromHere(double distance, double angle)
    {
        return new Point(x + distance*Math.cos(angle), y + distance*Math.sin(angle));
    }
}
