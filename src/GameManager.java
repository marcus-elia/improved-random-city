import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameManager
{
    // ==========================================
    //
    //             Pregame Things
    //
    // ==========================================
    private StartButton startButton;
    private boolean gameHasStarted;
    private LowMedHighButton lakeFrequencyButtons;
    private double lakeFrequency;
    private LowMedHighButton maxRoadsButtons;
    private int maxNumRoads;


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
    private RoadMap rm;
    private ArrayList<GameObject> gameObjects;

    public GameManager(int inputWidth, int inputHeight)
    {
        width = inputWidth;
        height = inputHeight;
        isScrolling = false;
        scrollSpeed = 5;
        diagonalScrollSpeed = scrollSpeed / Math.sqrt(2);

        startButton = new StartButton(this, new Point(width/2.0, 5.0*height/6),
                3.0*width/4, height/8.0, Color.BLUE, "Start", 24, null);
        gameHasStarted = false;
        lakeFrequencyButtons = new LowMedHighButton(this, new Point(width/2.0, height/3.0),
                100, 30, 40, Color.BLUE, "Lake Frequency");
        lakeFrequency = 0.1;
        maxRoadsButtons = new LowMedHighButton(this, new Point(width/2.0, height/6.0),
                100, 30, 40, Color.RED, "Maximum City Size");
        maxNumRoads = 500;


    }

    public void tick()
    {
        if(gameHasStarted)
        {
            if(isScrolling)
            {
                this.scroll();
            }
            else
            {
                rm.tick();
            }
        }
    }

    public void render(Graphics2D g2d)
    {
        if(gameHasStarted)
        {
            rm.render(g2d);
        }
        else
        {
            startButton.render(g2d);
            lakeFrequencyButtons.render(g2d);
            maxRoadsButtons.render(g2d);
        }
    }


    // ==========================================
    //
    //          Initialization Functions
    //
    // ==========================================
    public void startGame()
    {
        gameObjects = new ArrayList<GameObject>();
        rm = new RoadMap(this, new Point(0,0), lakeFrequency, maxNumRoads);
        gameObjects.add(rm);

        gameHasStarted = true;
    }

    public void setLakeFrequency(ButtonOutput bo)
    {
        if(bo == ButtonOutput.Low)
        {
            lakeFrequency = 0.002;
        }
        else if(bo == ButtonOutput.Medium)
        {
            lakeFrequency = 0.1;
        }
        else
        {
            lakeFrequency = 0.5;
        }
    }

    public void setMaxNumRoads(ButtonOutput bo)
    {
        if(bo == ButtonOutput.Low)
        {
            maxNumRoads = 50;
        }
        else if(bo == ButtonOutput.Medium)
        {
            maxNumRoads = 500;
        }
        else
        {
            maxNumRoads = 10000;
        }
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
    public RoadMap getRM()
    {
        return this.rm;
    }
    public ArrayList<GameObject> getGameObjects()
    {
        return gameObjects;
    }
    public boolean getGameHasStarted()
    {
        return gameHasStarted;
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
    public void addGameObject(GameObject obj)
    {
        gameObjects.add(obj);
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

    // Print debug stats about whatever object is closest to the click.
    public void reactToClick(int mx, int my)
    {
        if(gameHasStarted)
        {
            rm.setNewClick(mx, my);
        }
        else
        {
            startButton.reactToMouseClick(mx, my);

            if(lakeFrequencyButtons.reactToMouseClick(mx, my) != null)
            {
                this.setLakeFrequency(lakeFrequencyButtons.reactToMouseClick(mx, my));
            }
            if(maxRoadsButtons.reactToMouseClick(mx, my) != null)
            {
                this.setMaxNumRoads(maxRoadsButtons.reactToMouseClick(mx, my));
            }
        }
    }

    public void reactToMotion(int mx, int my)
    {
        if(!gameHasStarted)
        {
            startButton.reactToMouseMovement(mx, my);
            lakeFrequencyButtons.reactToMouseMovement(mx, my);
            maxRoadsButtons.reactToMouseMovement(mx, my);
        }
    }




}
