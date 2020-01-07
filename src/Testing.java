import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class Testing
{
    public static void main(String[] args)
    {
        double tolerance = 0.01;
        testPoint(tolerance);
        testLake(tolerance);
        testPerlin();
        testRoad(tolerance);
    }


    // ==========================================
    //
    //                 Point
    //
    // ==========================================
    public static void testPoint(double tolerance)
    {
        System.out.println("\nTesting the Point Class");
        testDistance(tolerance);
        testAngle(tolerance);
        //testGetPoint(tolerance);
        //testRotateAround(tolerance);
    }

    public static void testDistance(double tolerance)
    {
        boolean passed = true;
        Point p1, p2;
        double exp, obs;
        System.out.println("\nTesting distance between points.");

        // Same point
        p1 = new Point(2.5, 2.5);
        p2 = p1;
        exp = 0;
        obs = p1.distanceToPoint(p2);
        if(Math.abs(exp - obs) > tolerance)
        {
            passed = false;
            System.out.println("FAILED test case of the same point.");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Different point
        p2 = new Point(10, 1.5);
        exp = 7.56637;
        obs = p1.distanceToPoint(p2);
        if(Math.abs(exp - obs) > tolerance)
        {
            passed = false;
            System.out.println("FAILED test case of different points.");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Negative coordinates
        p1 = new Point(2.5, -2.5);
        p2 = new Point(-10, 1.5);
        exp = 13.1244;
        obs = p1.distanceToPoint(p2);
        if(Math.abs(exp - obs) > tolerance)
        {
            passed = false;
            System.out.println("FAILED test case of points with negative coordinates.");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        if(passed)
        {
            System.out.println("All tests passed.");
        }
    }

    public static void testAngle(double tolerance) {
        boolean passed = true;
        Point p1, p2;
        double exp, obs;
        System.out.println("\nTesting angle between points.");

        // The same point
        p1 = new Point(2.5, 2.5);
        p2 = p1;
        exp = 0;
        obs = p1.angleToOtherPoint(p2);
        if (Math.abs(exp - obs) > tolerance) {
            passed = false;
            System.out.println("FAILED test case of same point.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Zero angle
        p2 = new Point(3.5, 2.5);
        exp = 0;
        obs = p1.angleToOtherPoint(p2);
        if (Math.abs(exp - obs) > tolerance) {
            passed = false;
            System.out.println("FAILED test case of angle = 0.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // First quadrant
        p2 = new Point(3.5, 3.5);
        exp = Math.PI/4;
        obs = p1.angleToOtherPoint(p2);
        if (Math.abs(exp - obs) > tolerance) {
            passed = false;
            System.out.println("FAILED test case of 1st quadrant angle.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // 90 degrees
        p2 = new Point(2.5, 3.5);
        exp = Math.PI/2;
        obs = p1.angleToOtherPoint(p2);
        if (Math.abs(exp - obs) > tolerance) {
            passed = false;
            System.out.println("FAILED test case of 90 degree angle.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Second quadrant
        p2 = new Point(1.5, 3.5);
        exp = 3*Math.PI/4;
        obs = p1.angleToOtherPoint(p2);
        if (Math.abs(exp - obs) > tolerance) {
            passed = false;
            System.out.println("FAILED test case of 2nd quadrant angle.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // 180 degrees
        p2 = new Point(1.5, 2.5);
        exp = Math.PI;
        obs = p1.angleToOtherPoint(p2);
        if (Math.abs(exp - obs) > tolerance) {
            passed = false;
            System.out.println("FAILED test case of 180 degree angle.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Third quadrant
        p2 = new Point(1.5, 1.5);
        exp = 5*Math.PI/4;
        obs = p1.angleToOtherPoint(p2);
        if (Math.abs(exp - obs) > tolerance) {
            passed = false;
            System.out.println("FAILED test case of 3rd quadrant angle.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // 270 degrees
        p2 = new Point(2.5, 1.5);
        exp = 3*Math.PI/2;
        obs = p1.angleToOtherPoint(p2);
        if (Math.abs(exp - obs) > tolerance) {
            passed = false;
            System.out.println("FAILED test case of 270 degree angle.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Fourth quadrant
        p2 = new Point(3.5, 1.5);
        exp = 7*Math.PI/4;
        obs = p1.angleToOtherPoint(p2);
        if (Math.abs(exp - obs) > tolerance) {
            passed = false;
            System.out.println("FAILED test case of 4th quadrant angle.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        if(passed)
        {
            System.out.println("All tests passed.");
        }
    }

    // ==========================================
    //
    //                  Lake
    //
    // ==========================================
    public static void testLake(double tolerance)
    {
        System.out.println("\nTesting the Lake Class.");
        testOverlapping(tolerance);
        testTooClose(tolerance);
    }

    public static void testOverlapping(double tolerance)
    {
        boolean passed = true;
        Point c1, c2;
        double xRad1, xRad2;
        boolean exp, obs;
        System.out.println("\nTesting overlapping of lakes.");

        // The same lake
        c1 = new Point(0, 0);
        xRad1 = 10;
        c2 = c1;
        xRad2 = xRad1;
        exp = true;
        obs = Lake.potentiallyOverlapping(c1, xRad1, c2, xRad2);
        if (exp != obs) {
            passed = false;
            System.out.println("FAILED test case of the same lake.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Lake contained in lake
        xRad2 = 5;
        exp = true;
        obs = Lake.potentiallyOverlapping(c1, xRad1, c2, xRad2);
        if (exp != obs) {
            passed = false;
            System.out.println("FAILED test case of lake contained in lake.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Partial overlap
        c2 = new Point(10, 0);
        exp = true;
        obs = Lake.potentiallyOverlapping(c1, xRad1, c2, xRad2);
        if (exp != obs) {
            passed = false;
            System.out.println("FAILED test case of partial overlap.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Partial overlap, different angle
        c2 = new Point(-8, -2);
        exp = true;
        obs = Lake.potentiallyOverlapping(c1, xRad1, c2, xRad2);
        if (exp != obs) {
            passed = false;
            System.out.println("FAILED test case of partial overlap, different angle.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Non-overlapping
        c2 = new Point(20, 0);
        exp = false;
        obs = Lake.potentiallyOverlapping(c1, xRad1, c2, xRad2);
        if (exp != obs) {
            passed = false;
            System.out.println("FAILED test case of non-overlapping.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        if(passed)
        {
            System.out.println("All tests passed.");
        }
    }

    public static void testTooClose(double tolerance)
    {
        System.out.println("\nTesting point too close to lake.");
        boolean passed = true;
        Point f1, f2, p;
        double xRadius, roadWidth;
        boolean exp, obs;

        // Point is the center
        f1 = new Point(3, 0);
        f2 = new Point(-3, 0);
        p = new Point(0,0);
        xRadius = 5;
        roadWidth = 1;
        exp = true;
        obs = Lake.isTooCloseToLake(f1, f2, xRadius, p, roadWidth);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of point is center");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Point is a focus
        p = f1;
        exp = true;
        obs = Lake.isTooCloseToLake(f1, f2, xRadius, p, roadWidth);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of point is a focus");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Point is inside ellipse
        p = new Point(1, 1);
        exp = true;
        obs = Lake.isTooCloseToLake(f1, f2, xRadius, p, roadWidth);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of point is inside ellipse");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Point is on ellipse
        p = new Point(0, 4);
        exp = true;
        obs = Lake.isTooCloseToLake(f1, f2, xRadius, p, roadWidth);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of point is on ellipse");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Point just outside ellipse
        p = new Point(5.5, 0);
        exp = true;
        obs = Lake.isTooCloseToLake(f1, f2, xRadius, p, roadWidth);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of point is just outside ellipse");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Point well outside ellipse
        p = new Point(6.5, 0);
        exp = false;
        obs = Lake.isTooCloseToLake(f1, f2, xRadius, p, roadWidth);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of point is well outside ellipse");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Point is inside ellipse rotated
        f1 = new Point(2.12132, 2.12132);
        f2 = new Point(-2.12132, -2.12132);
        p = new Point(1, 1);
        exp = true;
        obs = Lake.isTooCloseToLake(f1, f2, xRadius, p, roadWidth);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of point is inside ellipse rotated");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Point is on ellipse rotated
        p = new Point(3.5355339, 3.5355339);
        exp = true;
        obs = Lake.isTooCloseToLake(f1, f2, xRadius, p, roadWidth);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of point is on ellipse rotated");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Point is just outside ellipse rotated
        p = new Point(3.7, 3.7);
        exp = true;
        obs = Lake.isTooCloseToLake(f1, f2, xRadius, p, roadWidth);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of point is just outside ellipse rotated");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        // Point is well outside ellipse rotated
        p = new Point(5, 5);
        exp = false;
        obs = Lake.isTooCloseToLake(f1, f2, xRadius, p, roadWidth);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of point is just outside ellipse rotated");
            System.out.println("Expected " + exp +", observed " + obs);
        }

        if(passed)
        {
            System.out.println("All tests passed.");
        }
    }

    // ==========================================
    //
    //               Perlin Noise
    //
    // ==========================================
    public static void testPerlin()
    {
        System.out.println("\nTesting Perlin Noise. Inspect the output.");
        PerlinNoise pn = new PerlinNoise(128, 128, .2);
        double[][] output2d = pn.getPerlinNoise();

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                //System.out.println("Created GUI on EDT? "+
                //        SwingUtilities.isEventDispatchThread());
                JFrame f = new JFrame("Testing Perlin Noise Generation");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.add(new PerlinDrawing(output2d));
                f.pack();
                f.setVisible(true);
            }
        });
    }

    // ==========================================
    //
    //                  Road
    //
    // ==========================================
    public static void testRoad(double tolerance)
    {
        System.out.println("\nTesting the Road Class.");
        testHitsRoad(tolerance);
    }

    public static void testHitsRoad(double tolerance)
    {
        System.out.println("\nTesting roads hitting each other.");

        boolean passed = true;
        Optional<Double> slope;
        Optional<Double> yInt;
        Point p1, p2, p3, p4;
        boolean exp, obs;

        // Both vertical
        slope = Optional.empty();
        yInt = Optional.empty();
        p1 = new Point(1,0);
        p2 = new Point(1, 5);
        p3 = new Point(2, 1);
        p4 = new Point(2, 4);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of both vertical lines.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Both horizontal
        slope = Optional.of(0.0);
        yInt = Optional.of(1.0);
        p1 = new Point(-4,1);
        p2 = new Point(3, 1);
        p3 = new Point(2, 4);
        p4 = new Point(5, 4);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of both horiztonal lines.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Parallel diagonal
        slope = Optional.of(1.0);
        yInt = Optional.of(2.0);
        p1 = new Point(0,2);
        p2 = new Point(3, 5);
        p3 = new Point(2, 0);
        p4 = new Point(4, 2);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of parallel diagonal lines.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // p3 and p4 reversed
        p3 = new Point(4, 2);
        p4 = new Point(2, 0);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of p3 and p4 reversed.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Line1 vertical, but to the right
        slope = Optional.empty();
        yInt = Optional.empty();
        p1 = new Point(1,0);
        p2 = new Point(1, 5);
        p3 = new Point(1.5, 3);
        p4 = new Point(5.5, 3);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of line1 vertical, but to the right.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Line1 vertical, but to the left
        slope = Optional.empty();
        yInt = Optional.empty();
        p1 = new Point(1,0);
        p2 = new Point(1, 5);
        p3 = new Point(-5.5, 3);
        p4 = new Point(-1.5, 3);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of line1 vertical, but to the left.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Line1 vertical, but too high
        slope = Optional.empty();
        yInt = Optional.empty();
        p1 = new Point(1,0);
        p2 = new Point(1, 5);
        p3 = new Point(-1.5, -3);
        p4 = new Point(5.5, -3);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of line1 vertical, but too high.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Line1 vertical, but too low
        slope = Optional.empty();
        yInt = Optional.empty();
        p1 = new Point(1,0);
        p2 = new Point(1, 5);
        p3 = new Point(-1.5, 7);
        p4 = new Point(5.5, 7);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of line1 vertical, but too low.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Line1 vertical, intersecting
        slope = Optional.empty();
        yInt = Optional.empty();
        p1 = new Point(1,5);
        p2 = new Point(1, 0);
        p3 = new Point(-1.5, 3);
        p4 = new Point(5.5, 3);
        exp = true;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of line1 vertical, intersecting.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Line2 vertical, but to the right
        slope = Optional.of(0.0);
        yInt = Optional.of(2.0);
        p1 = new Point(1.5,2);
        p2 = new Point(3.5, 2);
        p3 = new Point(1, -1);
        p4 = new Point(1, 6);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of line2 vertical, but to the right.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Line2 vertical, but to the left
        slope = Optional.of(0.0);
        yInt = Optional.of(2.0);
        p1 = new Point(-3.5,2);
        p2 = new Point(-1.5, 2);
        p3 = new Point(1, -1);
        p4 = new Point(1, 6);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of line2 vertical, but to the left.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Line2 vertical, but too high
        slope = Optional.of(0.0);
        yInt = Optional.of(-4.0);
        p1 = new Point(-3.5,-4);
        p2 = new Point(3.5, -4);
        p3 = new Point(1, -1);
        p4 = new Point(1, 6);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of line2 vertical, but too high.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Line2 vertical, but too low
        slope = Optional.of(0.0);
        yInt = Optional.of(8.0);
        p1 = new Point(-3.5,8);
        p2 = new Point(3.5, 8);
        p3 = new Point(1, -1);
        p4 = new Point(1, 6);
        exp = false;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of line2 vertical, but too low.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        // Line2 vertical, intersecting
        slope = Optional.of(0.0);
        yInt = Optional.of(2.0);
        p1 = new Point(-1.5,2);
        p2 = new Point(3.5, 2);
        p3 = new Point(1, -1);
        p4 = new Point(1, 6);
        exp = true;
        obs = Road.hitsRoad(slope, yInt, p1, p2, p3, p4);
        if(exp != obs)
        {
            passed = false;
            System.out.println("FAILED test case of line2 vertical, intersecting.");
            System.out.println("Expected " + exp + ", observed " + obs);
        }

        if(passed)
        {
            System.out.println("All tests passed.");
        }
    }
}
