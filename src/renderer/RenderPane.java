package renderer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Eliot on 15/09/2015.
 */

public class RenderPane extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final int tilesize = 128;
    private TileDrawingMachine tilePainter = new TileDrawingMachine(tilesize);
    private char[][] level;
    private Point CameraLocation = new Point(0,0);


    public RenderPane() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return new Dimension(800,600);
    }

    public void setLevel(char[][] level){
        this.level = level;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(level == null){
            System.out.println("Set the level before painting");
        }else{
            paintLayer(g2, 0, level);
        }
    }



    private void paintLayer(Graphics2D g2, int depth, char[][] layer){
    	
    	
//    	char[][] levelTester =    {{'w','d','w','w'},
//                 {'w','e','p','e'},
//                 {'o','e','e','e'},
//                 {'w','l','c','e'}};
  
  		// Uncomment for tile testing
  		//layer = levelTester;

        int numberOfRows = layer.length;
        int numberOfColums = layer[0].length;

        g2.setStroke(new BasicStroke(1));

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColums; j++) {
                int x = j * tilesize / 2;
                int y = i * tilesize / 2;
                Point tile = new Point(x, y);
                Point isoTile = Iso.twoDToIso(tile);
                isoTile.y = isoTile.y  - ((tilesize / 2) * depth);

                switch(layer[i][j]){
                    case 'e': tilePainter.drawFloorTile(g2, isoTile.x, isoTile.y);
                        break;

                    case 'w': tilePainter.drawCubeTile(g2, isoTile.x, isoTile.y);
                        break;

                    case 'p': tilePainter.drawCharachter(g2, isoTile.x, isoTile.y);
                        break;
                        
                    case 'c': tilePainter.drawChest(g2, isoTile.x, isoTile.y);
                    	break;
                    	
                    case 'l': tilePainter.drawOpenedChest(g2, isoTile.x, isoTile.y);
                		break;
                		
                    case 'd': tilePainter.drawDoor(g2, isoTile.x, isoTile.y);
                    	break;
                    	
                    case 'o': tilePainter.drawFloorTile(g2, isoTile.x, isoTile.y);
                }
            }
        }
    }

	public void setCameraLocation(int x, int y) {
		this.CameraLocation.x = x;
		this.CameraLocation.y = y;
		
		Iso.cameraOffset.x = x;
		Iso.cameraOffset.y = y;
	}

}
