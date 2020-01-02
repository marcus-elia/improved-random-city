import java.awt.*;

public class GameManager
{
    // The overall size of the window
    private int width;
    private int height;

    // Keep track of whether the user is holding down a key
    private boolean isScrolling;

    public GameManager(int inputWidth, int inputHeight)
    {
        width = inputWidth;
        height = inputHeight;
        isScrolling = false;
    }

    public void tick()
    {

    }

    public void render(Graphics2D g2d)
    {

    }

    // ==========================================
    //
    //                 Getters
    //
    // ==========================================
    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }
    public boolean getIsScrolling()
    {
        return isScrolling;
    }

    // ==========================================
    //
    //                 Setters
    //
    // ==========================================
    public void setIsScrolling(boolean input)
    {
        isScrolling = input;
    }
}
