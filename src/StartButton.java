import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class StartButton
{
    private boolean isHighlighted;
    private Point center;
    private Shape rectangle;
    private GameManager manager;
    private Color normalColor, highlightedColor;
    private String word;
    private int fontsize;

    public StartButton(GameManager inputManager, Point inputCenter, double xWidth,
                       double yWidth, Color inputColor)
    {
        manager = inputManager;
        center = inputCenter;
        rectangle = new Rectangle2D.Double(center.x - xWidth/2, center.y - yWidth/2, xWidth, yWidth);
        isHighlighted = false;
        normalColor = inputColor;
        highlightedColor = new Color(normalColor.getRed(), normalColor.getGreen(), normalColor.getBlue(), 128);
        word = "Start";
        fontsize = 32;
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
        this.drawWord(g2d);
    }

    public void drawWord(Graphics2D g2d)
    {
        // The word Percent
        g2d.setFont(new Font("Tahoma", Font.BOLD, fontsize));
        g2d.setColor(Color.BLACK);
        int pixelLength = g2d.getFontMetrics().stringWidth(word); // the number of pixels the string is long
        g2d.drawString(word, (int)center.x - pixelLength/2, (int)center.y + 5);
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
