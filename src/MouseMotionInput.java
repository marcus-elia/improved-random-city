import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionInput implements MouseMotionListener
{
    private GameManager manager;

    public MouseMotionInput(GameManager inputManager)
    {
        this.manager = inputManager;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();
        this.manager.reactToMotion(x, y);
    }
}