import java.awt.*;
import java.util.ArrayList;

public class GameManager
{
    // The overall size of the window
    private int width;
    private int height;

    // Keep track of whether the user is holding down a key
    private boolean isScrolling;
    private double scrollSpeed;
    private double diagonalScrollSpeed;
    private ScrollDirection scrollDirection;



    // ==========================================
    //
    //             Lists of Objects
    //
    // ==========================================
    private ArrayList<GameObject> gameObjects;

    public GameManager(int inputWidth, int inputHeight)
    {
        width = inputWidth;
        height = inputHeight;
        isScrolling = false;
        scrollSpeed = 5;
        diagonalScrollSpeed = scrollSpeed / Math.sqrt(2);

        gameObjects = new ArrayList<GameObject>();
    }

    public void tick()
    {
         if(isScrolling)
         {
             this.scroll();
         }
    }

    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.CYAN);
        g2d.fillOval(width/2, height/2, 40, 30);
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
    public double getScrollSpeed()
    {
        return scrollSpeed;
    }
    public ScrollDirection getScrollDirection()
    {
        return scrollDirection;
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
    public void setScrollSpeed(double input)
    {
        scrollSpeed = input;
    }
    public void setScrollDirection(ScrollDirection input)
    {
        scrollDirection = input;
    }



    // ==========================================
    //
    //            User Input Functions
    //
    // ==========================================

    // This function actually moves all of the GameObjects when scrolling. Note that their
    // coordinates are shifted in the opposite direction of the scrolling, so it looks like
    // the camera is moving properly.
    public void scroll()
    {
        // The 4 cardinal directions
        if(scrollDirection == ScrollDirection.Up)
        {
            this.moveDown(scrollSpeed);
        }
        else if(scrollDirection == ScrollDirection.Left)
        {
            this.moveRight(scrollSpeed);
        }
        else if(scrollDirection == ScrollDirection.Down)
        {
            this.moveUp(scrollSpeed);
        }
        else if(scrollDirection == ScrollDirection.Right)
        {
            this.moveLeft(scrollSpeed);
        }
        // The diagonals
        else if(scrollDirection == ScrollDirection.UpLeft)
        {
            this.moveRight(diagonalScrollSpeed);
            this.moveDown(diagonalScrollSpeed);
        }
        else if(scrollDirection == ScrollDirection.DownLeft)
        {
            this.moveRight(diagonalScrollSpeed);
            this.moveUp(diagonalScrollSpeed);
        }
        else if(scrollDirection == ScrollDirection.DownRight)
        {
            this.moveUp(diagonalScrollSpeed);
            this.moveLeft(diagonalScrollSpeed);
        }
        else if(scrollDirection == ScrollDirection.UpRight)
        {
            this.moveDown(diagonalScrollSpeed);
            this.moveLeft(diagonalScrollSpeed);
        }
    }

    // Helper functions for movement
    public void moveUp(double amount)
    {
        for(GameObject obj : gameObjects)
        {
            obj.moveUp(amount);
        }
    }
    public void moveDown(double amount)
    {
        for(GameObject obj : gameObjects)
        {
            obj.moveDown(amount);
        }
    }
    public void moveLeft(double amount)
    {
        for(GameObject obj : gameObjects)
        {
            obj.moveLeft(amount);
        }
    }
    public void moveRight(double amount)
    {
        for(GameObject obj : gameObjects)
        {
            obj.moveRight(amount);
        }
    }




}
