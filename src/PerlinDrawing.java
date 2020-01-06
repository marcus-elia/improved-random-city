// This is copied from https://docs.oracle.com/javase/tutorial/uiswing/painting/step2.html

import javax.swing.*;
import java.awt.*;

class PerlinDrawing extends JPanel
{
    private double[][] pn;

    public PerlinDrawing(double[][] inputPN)
    {
        pn = inputPN;
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(512,512);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for(int i = 0; i < pn.length; i++)
        {
            for(int j = 0; j < pn[0].length; j++)
            {
                g.setColor(new Color((float)pn[i][j], 0, 0, 1));
                g.fillRect(i * 512 / pn.length, j * 512 / pn[0].length, 512 / pn.length, 512 / pn[0].length);
            }
        }
    }
}