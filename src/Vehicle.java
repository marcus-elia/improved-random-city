import java.awt.*;

public class Vehicle extends GameObject
{
    private double xWidth;
    private double yWidth;

    private Color color;

    public Vehicle(GameManager inputManager, Point inputCenter, double inputXWidth, double inputYWidth, Color inputColor)
    {
        super(inputManager, inputCenter);
        xWidth = inputXWidth;
        yWidth = inputYWidth;
        color = inputColor;
    }

    @Override
    public void tick()
    {

    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(color);
        g2d.fillOval((int)center.x, (int)center.y, 5, 5);
    }
}
