public abstract class GameObject
{
    private Point center;
    private GameManager manager;

    public GameObject(GameManager inputManager, Point inputCenter)
    {
        manager = inputManager;
        center = inputCenter;
    }

    // ==========================================
    //
    //                 Getters
    //
    // ==========================================
    public Point getCenter()
    {
        return center;
    }

    


    // ==========================================
    //
    //           Scrolling Functions
    //
    // ==========================================
    public void moveUp(double amount)
    {
        center.y -= amount;
    }
    public void moveDown(double amount)
    {
        center.y += amount;
    }
    public void moveLeft(double amount)
    {
        center.x -= amount;
    }
    public void moveRight(double amount)
    {
        center.x += amount;
    }
}
