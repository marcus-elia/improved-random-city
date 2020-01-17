import java.awt.*;
import java.awt.geom.Point2D;

// This class creates a horizontal bar of three buttons
public class LowMedHighButton
{
    private GameManager manager;
    private double xWidth;
    private double yWidth;
    private Point center;
    private double distanceBetweenCenters;
    private Color color;
    private Button low;
    private Button medium;
    private Button high;
    private String label;

    public LowMedHighButton(GameManager inputManager, Point inputCenter, double inputXWidth, double inputYWidth,
                            double inputDistanceBetweenCenters, Color inputColor, String inputLabel)
    {
        manager = inputManager;
        xWidth = inputXWidth;
        yWidth = inputYWidth;
        center = inputCenter;
        distanceBetweenCenters = inputDistanceBetweenCenters;
        color = inputColor;
        label = inputLabel;
        this.makeButtons();
    }

    public void makeButtons()
    {
        low = new Button(manager, new Point(center.x - xWidth - distanceBetweenCenters, center.y), xWidth, yWidth,
            color, "Low", 32, ButtonOutput.Low);
        medium = new Button(manager, new Point(center.x, center.y), xWidth, yWidth,
                color, "Medium", 32, ButtonOutput.Medium);
        high = new Button(manager, new Point(center.x + xWidth + distanceBetweenCenters, center.y), xWidth, yWidth,
                color, "High", 32, ButtonOutput.High);
    }

    public void render(Graphics2D g2d)
    {
        low.render(g2d);
        medium.render(g2d);
        high.render(g2d);
        this.drawWord(g2d);
    }

    // Draw the label above the medium button
    public void drawWord(Graphics2D g2d)
    {
        g2d.setFont(new Font("Tahoma", Font.BOLD, 36));
        g2d.setColor(Color.WHITE);
        int pixelLength = g2d.getFontMetrics().stringWidth(label); // the number of pixels the string is long
        g2d.drawString(label, (int)center.x - pixelLength/2, (int)center.y  - (int)yWidth/2 - 20);
    }


    // ==========================================
    //
    //           Reacting to the Mouse
    //
    // ==========================================
    public void reactToMouseMovement(int mx, int my)
    {
        low.reactToMouseMovement(mx, my);
        medium.reactToMouseMovement(mx, my);
        high.reactToMouseMovement(mx, my);
    }

    public ButtonOutput reactToMouseClick(int mx, int my)
    {
        if(low.reactToMouseClick(mx, my) != null)
        {
            return low.reactToMouseClick(mx, my);
        }
        if(medium.reactToMouseClick(mx, my) != null)
        {
            return medium.reactToMouseClick(mx, my);
        }
        if(high.reactToMouseClick(mx, my) != null)
        {
            return high.reactToMouseClick(mx, my);
        }
        return null;
    }


}
