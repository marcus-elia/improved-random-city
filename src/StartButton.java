import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class StartButton
{
    private boolean isHighlighted;
    private Shape rectangle;
    private GameManager manager;
    private Color normalColor, highlightedColor;

    public StartButton(GameManager inputManager, Point center, double xWidth,
                       double yWidth, Color inputColor)
    {
        manager = inputManager;
        rectangle = new Rectangle2D.Double(center.x - xWidth/2, center.y - yWidth/2, xWidth, yWidth);
        isHighlighted = false;
        normalColor = inputColor;
        highlightedColor = new Color(normalColor.getRed(), normalColor.getGreen(), normalColor.getBlue(), 128);
    }

    public void render(Graphics2D g2d)
    {
        if(isHighlighted)
        {
            g2d.setColor(highlightedColor);
        }
        else
        {
            g2d.setColor(normalColor);
        }
        g2d.fill(rectangle);
    }

    // ==========================================
    //
    //                Setters
    //
    // ==========================================
    public void setIsHighlighted(boolean input)
    {
        isHighlighted = input;
    }


    // ==========================================
    //
    //           Reacting to the Mouse
    //
    // ==========================================
    public void reactToMouseMovement(int mx, int my)
    {
        if(rectangle.contains(new Point2D.Double(mx, my)))
        {
            isHighlighted = true;
        }
        else
        {
            isHighlighted = false;
        }
    }

    public void reactToMouseClick(int mx, int my)
    {
        if(rectangle.contains(new Point2D.Double(mx, my)))
        {
            manager.startGame();
        }
    }
}
