import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Button
{
    protected boolean isHighlighted;
    protected Point center;
    protected double xWidth;
    protected double yWidth;
    protected Shape rectangle;
    protected ButtonOutput output;
    protected GameManager manager;
    protected Color normalColor, highlightedColor;
    protected String word;
    protected int fontSize;

    public Button(GameManager inputManager, Point inputCenter,  double inputXWidth, double inputYWidth,
                  Color inputNormalColor, String inputWord, int inputFontSize, ButtonOutput inputOutput)
    {
        manager = inputManager;
        center = inputCenter;
        xWidth = inputXWidth;
        yWidth = inputYWidth;
        rectangle = new Rectangle2D.Double(center.x - xWidth/2, center.y - yWidth/2, xWidth, yWidth);
        isHighlighted = false;
        normalColor = inputNormalColor;
        highlightedColor = new Color(normalColor.getRed(), normalColor.getGreen(), normalColor.getBlue(), 128);
        word = inputWord;
        fontSize = inputFontSize;
        output = inputOutput;
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

    // Draw the word in the middle of the rectangle
    public void drawWord(Graphics2D g2d)
    {
        g2d.setFont(new Font("Tahoma", Font.BOLD, fontSize));
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

    public ButtonOutput reactToMouseClick(int mx, int my)
    {
        if(rectangle.contains(new Point2D.Double(mx, my)))
        {
            return output;
        }
        return null;
    }
}
