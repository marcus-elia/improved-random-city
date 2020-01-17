import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class StartButton extends Button
{


    public StartButton(GameManager inputManager, Point inputCenter, double inputXWidth, double inputYWidth,
                       Color inputNormalColor, String inputWord, int inputFontSize, ButtonOutput inputOutput) {
        super(inputManager, inputCenter, inputXWidth, inputYWidth, inputNormalColor, inputWord, inputFontSize, inputOutput);
    }

   @Override
    public ButtonOutput reactToMouseClick(int mx, int my)
    {
        if(rectangle.contains(new Point2D.Double(mx, my)))
        {
            manager.startGame();
        }
        return null;
    }
}
