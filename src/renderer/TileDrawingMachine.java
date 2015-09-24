package renderer;

import java.awt.*;

/**
 * Created by Eliot on 21/09/15.
 */
public class TileDrawingMachine {
    private int tilesize;

    public TileDrawingMachine(int tileSize){
        tilesize = tileSize;
    }



    protected void drawFloorTile(Graphics2D g2, int x, int y){
        int xPoints[] = {x, x + (tilesize / 2), x, x - (tilesize / 2)};
        int yPoints[] = {y, y + (tilesize / 4), y + (tilesize / 2), y + (tilesize / 4)};

        g2.setPaint(new Color(255, 255, 255));
        g2.fillPolygon(xPoints, yPoints, 4);

        g2.setPaint(new Color(50, 50, 50));
        g2.drawPolygon(xPoints, yPoints, 4);

    }

    protected void drawCubeTile(Graphics2D g2, int x, int y){
        // top bit
        int xPoints[] = {x, x + (tilesize / 2), x, x - (tilesize / 2)};
        int yPoints[] = {y, y - (tilesize / 4), y - (tilesize / 2), y - (tilesize / 4)};

        int xrPoints[] = {x, x + (tilesize / 2), x + (tilesize / 2), x};
        int yrPoints[] = {y, y - (tilesize / 4), y + (tilesize / 4), y + (tilesize / 2)};

        int xlPoints[] = {x, x - (tilesize / 2), x - (tilesize / 2), x};
        int ylPoints[] = {y, y - (tilesize / 4), y + (tilesize / 4), y + (tilesize / 2)};

        g2.setPaint(new Color(255,151,25));
        g2.fillPolygon(xPoints, yPoints, 4);

        g2.setPaint(new Color(198, 105, 24));
        g2.fillPolygon(xrPoints, yrPoints, 4);

        g2.setPaint(new Color(137, 71, 22));
        g2.fillPolygon(xlPoints, ylPoints, 4);

    }

    protected void drawHalfCubeTile(Graphics2D g2, int x, int y){
        // top bit
        int xPoints[] = {x, x + (tilesize / 2), x, x - (tilesize / 2)};
        int yPoints[] = {y + (tilesize / 4), y, y - (tilesize / 4), y};

        int xrPoints[] = {x, x + (tilesize / 2), x + (tilesize / 2), x};
        int yrPoints[] = {y + (tilesize / 4), y, y + (tilesize / 4), y + (tilesize / 2)};

        int xlPoints[] = {x, x - (tilesize / 2), x - (tilesize / 2), x};
        int ylPoints[] = {y + (tilesize / 4), y, y + (tilesize / 4), y + (tilesize / 2)};

        g2.setPaint(new Color(255,151,25));
        g2.fillPolygon(xPoints, yPoints, 4);

        g2.setPaint(new Color(198, 105, 24));
        g2.fillPolygon(xrPoints, yrPoints, 4);

        g2.setPaint(new Color(137, 71, 22));
        g2.fillPolygon(xlPoints, ylPoints, 4);

    }

    protected void drawCharachter(Graphics2D g2, int x, int y){
        // First draw a floor tile
        drawFloorTile(g2, x, y);


        int xrPoints[] = {x, x + (tilesize / 4), x};
        int yrPoints[] = {y + ((tilesize / 2) - (tilesize / 8)), y + (tilesize / 4), y - (tilesize / 4)};

        int xlPoints[] = {x, x - (tilesize / 4), x};
        int ylPoints[] = {y + ((tilesize / 2) - (tilesize / 8)), y + (tilesize / 4), y - (tilesize / 4)};


        g2.setPaint(new Color(70, 142, 255));
        g2.fillPolygon(xrPoints, yrPoints, 3);

        g2.setPaint(new Color(63, 113, 213));
        g2.fillPolygon(xlPoints, ylPoints, 3);
    }
}
