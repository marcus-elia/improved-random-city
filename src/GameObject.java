import java.awt.*;

public abstract class GameObject
{
    protected Point center;
    protected GameManager manager;

    public GameObject(GameManager inputManager, Point inputCenter)
    {
        manager = inputManager;
        center = inputCenter;
    }

    public abstract void tick();

    public abstract void render(Graphics2D g2d);

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

    // ==========================================
    //
    //                  Debug
    //
    // ==========================================
    public void printThings()
    {
        System.out.println("Object centered at " + center.x + ", " + center.y);
    }
}
