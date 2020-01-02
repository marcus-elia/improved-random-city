import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter
{
    private GameManager manager;

    // Is the user holding down an arrow key?
    private boolean leftArrowKey;
    private boolean rightArrowKey;
    private boolean upArrowKey;
    private boolean downArrowKey;

    public KeyInput(GameManager inputManager)
    {
        manager = inputManager;

        leftArrowKey = false;
        rightArrowKey = false;
        upArrowKey = false;
        downArrowKey = false;
    }

    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_LEFT)
        {
            leftArrowKey = true;
            setManagerScrollDirection();
        }
        else if(key == KeyEvent.VK_RIGHT)
        {
            rightArrowKey = true;
            setManagerScrollDirection();
        }
        else if(key == KeyEvent.VK_UP)
        {
            upArrowKey = true;
            setManagerScrollDirection();
        }
        else if(key == KeyEvent.VK_DOWN)
        {
            downArrowKey = true;
            setManagerScrollDirection();
        }
    }

    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_LEFT)
        {
            leftArrowKey = false;
            setManagerScrollDirection();
        }
        else if(key == KeyEvent.VK_RIGHT)
        {
            rightArrowKey = false;
            setManagerScrollDirection();
        }
        else if(key == KeyEvent.VK_UP)
        {
            upArrowKey = false;
            setManagerScrollDirection();
        }
        else if(key == KeyEvent.VK_DOWN)
        {
            downArrowKey = false;
            setManagerScrollDirection();
        }
    }


    public void setManagerScrollDirection()
    {
        if(upArrowKey || downArrowKey || leftArrowKey || rightArrowKey)
        {
            manager.setIsScrolling(true);

            // When the up arrow key is held
            if(upArrowKey)
            {
                // If both L and R, either going up or not at all
                if(leftArrowKey && rightArrowKey)
                {
                    if(downArrowKey)
                    {
                        manager.setScrollDirection(ScrollDirection.None);
                    }
                    else
                    {
                        manager.setScrollDirection(ScrollDirection.Up);
                    }
                }
                // Just L, not R
                else if(leftArrowKey)
                {
                    if(downArrowKey)
                    {
                        manager.setScrollDirection(ScrollDirection.Left);
                    }
                    else
                    {
                        manager.setScrollDirection(ScrollDirection.UpLeft);
                    }
                }
                // Just R, not L
                else if(rightArrowKey)
                {
                    if(downArrowKey)
                    {
                        manager.setScrollDirection(ScrollDirection.Right);
                    }
                    else
                    {
                        manager.setScrollDirection(ScrollDirection.UpRight);
                    }
                }
                // Just up and down
                else if(downArrowKey)
                {
                    manager.setScrollDirection(ScrollDirection.None);
                }
                // Just up
                else
                {
                    manager.setScrollDirection(ScrollDirection.Up);
                }
            }
            // The down arrow key
            else if(downArrowKey)
            {
                if(leftArrowKey && rightArrowKey) // already handled the all four case
                {
                    manager.setScrollDirection(ScrollDirection.Down);
                }
                else if(leftArrowKey) // already handed up, left, down
                {
                    manager.setScrollDirection(ScrollDirection.DownLeft);
                }
                else if(rightArrowKey) // already handed up, right, down
                {
                    manager.setScrollDirection(ScrollDirection.DownRight);
                }
                else  // already handed both up and down
                {
                    manager.setScrollDirection(ScrollDirection.Down);
                }
            }
            // L
            else if(leftArrowKey)
            {
                manager.setScrollDirection(ScrollDirection.Left);
            }
            // R
            else
            {
                manager.setScrollDirection(ScrollDirection.Right);
            }
        }
        else
        {
            manager.setIsScrolling(false);
            manager.setScrollDirection(ScrollDirection.None);
        }
    }
}
