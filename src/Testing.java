public class Testing
{
    public static void main(String[] args)
    {
        double tolerance = 0.01;
        testPoint(tolerance);
        testLake(tolerance);
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
}
