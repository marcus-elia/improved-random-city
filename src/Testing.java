public class Testing
{
    public static void main(String[] args)
    {
        double tolerance = 0.01;
        testPoint(tolerance);
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
        //testAngle(tolerance);
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
            System.out.println("All tests passed");
        }
    }
}
