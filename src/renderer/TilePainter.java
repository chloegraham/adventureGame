package renderer;

import java.awt.*;

/**
 * Created by Eliot on 21/09/15.
 */
public class TilePainter {
    private int tilesize;

    public TilePainter(int tileSize){
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
    
    protected void drawChest(Graphics2D g2, int x, int y){
    	// First draw a floor tile
        drawFloorTile(g2, x, y);
        int eigth = tilesize / 8;
        int forth = tilesize / 4;
        
     // top bit
        int xPoints[] = {x, x + forth, x, x - forth};
        int yPoints[] = {y + eigth, y, y - eigth, y};

        
        int xrPoints[] = {x, 			x + forth, 		x + forth, 	x};
        int yrPoints[] = {y + eigth, 	y, 				y + forth, 	y + (eigth * 3)};
        
        int xlPoints[] = {x, 			x - forth, 		x - forth, 	x};
        int ylPoints[] = {y + eigth, 	y, 				y + forth, 	y + (eigth * 3)};
        
        g2.setPaint(new Color(104, 76, 53));
        g2.fillPolygon(xlPoints, ylPoints, 4);
        
        g2.setPaint(new Color(135, 97, 69));
        g2.fillPolygon(xrPoints, yrPoints, 4);
        
        g2.setPaint(new Color(166, 124, 82));
        g2.fillPolygon(xPoints, yPoints, 4);   
    }
    
    protected void drawOpenedChest(Graphics2D g2, int x, int y){
    	// First draw a floor tile
        drawFloorTile(g2, x, y);
        int eigth = tilesize / 8;
        int forth = tilesize / 4;
        
     // top bit
        int xPoints[] = {x, x + forth, x, x - forth};
        int yPoints[] = {y + eigth, y, y - eigth, y};

        
        int xrPoints[] = {x, 			x + forth, 		x + forth, 	x};
        int yrPoints[] = {y + eigth, 	y, 				y + forth, 	y + (eigth * 3)};
        
        int xlPoints[] = {x, 			x - forth, 		x - forth, 	x};
        int ylPoints[] = {y + eigth, 	y, 				y + forth, 	y + (eigth * 3)};
        
        g2.setPaint(new Color(104, 76, 53));
        g2.fillPolygon(xlPoints, ylPoints, 4);
        
        g2.setPaint(new Color(135, 97, 69));
        g2.fillPolygon(xrPoints, yrPoints, 4);
        
        g2.setPaint(new Color(61, 45, 31));
        g2.fillPolygon(xPoints, yPoints, 4);   
    }
    
    protected void drawDoor(Graphics2D g2, int x, int y){
        // top bit
        int xPoints[] = {x, x + (tilesize / 2), x, x - (tilesize / 2)};
        int yPoints[] = {y, y - (tilesize / 4), y - (tilesize / 2), y - (tilesize / 4)};

        int xrPoints[] = {x, x + (tilesize / 2), x + (tilesize / 2), x};
        int yrPoints[] = {y, y - (tilesize / 4), y + (tilesize / 4), y + (tilesize / 2)};

        int xlPoints[] = {x, x - (tilesize / 2), x - (tilesize / 2), x};
        int ylPoints[] = {y, y - (tilesize / 4), y + (tilesize / 4), y + (tilesize / 2)};

        g2.setPaint(new Color(255, 106, 79));
        g2.fillPolygon(xPoints, yPoints, 4);

        g2.setPaint(new Color(185, 79, 27));
        g2.fillPolygon(xrPoints, yrPoints, 4);

        g2.setPaint(new Color(168, 65, 49));
        g2.fillPolygon(xlPoints, ylPoints, 4);
    }
    
    protected void drawOpenDoor(Graphics2D g2, int x, int y){
	    int xPoints[] = {x, x + (tilesize / 2), x, x - (tilesize / 2)};
	    int yPoints[] = {y, y + (tilesize / 4), y + (tilesize / 2), y + (tilesize / 4)};
	
	    g2.setPaint(new Color(255, 106, 79));
	    g2.fillPolygon(xPoints, yPoints, 4);
	
	    g2.setPaint(new Color(50, 50, 50));
	    g2.drawPolygon(xPoints, yPoints, 4);
    }
    
    protected void drawPressurePad(Graphics2D g2, int x, int y){
    	int sixthenth = tilesize / 16;	
    	
    	// First draw a floor tile
        drawFloorTile(g2, x, y);
	
		int xPoints[] = {x, x + (tilesize / 2), x, x - (tilesize / 2)};
	    int yPoints[] = {y - sixthenth, y + (tilesize / 4) - sixthenth, y + (tilesize / 2) - sixthenth, y + (tilesize / 4) - sixthenth};
	
	    g2.setPaint(new Color(100, 100, 100));
	    g2.fillPolygon(xPoints, yPoints, 4);
	
	    g2.setPaint(new Color(50, 50, 50));
	    g2.drawPolygon(xPoints, yPoints, 4);
    }
    
    protected void drawPressurePadActive(Graphics2D g2, int x, int y){    	
		int xPoints[] = {x, x + (tilesize / 2), x, x - (tilesize / 2)};
	    int yPoints[] = {y, y + (tilesize / 4), y + (tilesize / 2), y + (tilesize / 4)};
	
	    g2.setPaint(new Color(80, 80, 80));
	    g2.fillPolygon(xPoints, yPoints, 4);
	
	    g2.setPaint(new Color(50, 50, 50));
	    g2.drawPolygon(xPoints, yPoints, 4);
    }
    
    protected void drawBoulder(Graphics2D g2, int x, int y){   
    	int third = tilesize / 3;
    	
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    	    	RenderingHints.VALUE_ANTIALIAS_ON);
    	
	    g2.setPaint(new Color(50, 50, 60));
    	g2.fillOval(x - third, y - third, third * 2, third * 2);
    	
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    	    	RenderingHints.VALUE_ANTIALIAS_OFF);
    }
}
