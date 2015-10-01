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
    private TilePainter tilePainter = new TilePainter(tilesize);
    private char[][] level;
    private char[][] objects;
    private char[][] moveables;
    private Point CameraLocation = new Point(0,0);


    public RenderPane() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return new Dimension(800,600);
    }
    
    /**
     * Update the charset to draw
     * @param level
     */
    public void setLevel(char[][] level){
        this.level = level;
    }
    
    public void setLayers(char[][] level, char[][] objects, char[][] moveables){
    	this.level = level;
    	this.objects = objects;
    	this.moveables = moveables;
    }
    
    /**
     * An alternative set level, for drawing as 3 layers not one. 
     * @param level
     * @param objects
     * @param moveables
     */
    public void setCharLayers(char[][] level, char[][] objects, char[][] moveables){
        this.level = level;
        this.objects = objects;
        this.moveables = moveables;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(level == null){
            System.out.println("Set the level before painting");
            return;
        }
        
        if(level != null && this.objects == null){
            paintLayer(g2, 0, level);
            return;
        }
        
        if(this.level != null && this.objects != null && this.moveables != null ){
        	paintFromCharLayers(g2);
        }
    }


    /**
     * Paints a single layer
     * @param g2
     * @param depth
     * @param layer
     */
    private void paintLayer(Graphics2D g2, int depth, char[][] layer){

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
                
                parseAndDrawTile(level[i][j], isoTile, g2);
            }
        }
    }
    
    /**
     * Paints from all 3 game layers
     * @param g2
     */
    private void paintFromCharLayers(Graphics2D g2){

        int numberOfRows = level.length;
        int numberOfColums = level[0].length;
      
        g2.setStroke(new BasicStroke(1));

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColums; j++) {
                int x = j * tilesize / 2;
                int y = i * tilesize / 2;
                Point tile = new Point(x, y);
                Point isoTile = Iso.twoDToIso(tile);
                
                parseAndDrawTile(this.level[i][j], isoTile, g2);
                parseAndDrawTile(this.objects[i][j], isoTile, g2);
                parseAndDrawTile(this.moveables[i][j], isoTile, g2);
             
            }
        }
    }
    
    
    
    /**
     * Figures out what a char represents, then asks the tilePainter to draw
     * the right type of tile at the designated spot
     * @param tile
     * @param isoTile
     * @param g2
     */
    private void parseAndDrawTile(char tile, Point isoTile, Graphics2D g2 ){
    	switch(tile){
	    	case 'e': tilePainter.drawFloorTile(g2, isoTile.x, isoTile.y);
		        break;
		    case 'w': tilePainter.drawCubeTile(g2, isoTile.x, isoTile.y);
		        break;
		
		    case 'p': tilePainter.drawCharachter(g2, isoTile.x, isoTile.y);
		        break;
		        
		    case 'c': tilePainter.drawChest(g2, isoTile.x, isoTile.y);
		    	break;
		    	
		    case 'C': tilePainter.drawOpenedChest(g2, isoTile.x, isoTile.y);
				break;
				
		    case 'd': tilePainter.drawDoor(g2, isoTile.x, isoTile.y);
		    	break;
		    	
		    case 'D': tilePainter.drawOpenDoor(g2, isoTile.x, isoTile.y);
		    	break;
		    	
		    case 'z' : tilePainter.drawPressurePad(g2, isoTile.x, isoTile.y);
		    	break;
		    	
		    case 'Z' :tilePainter.drawPressurePadActive(g2, isoTile.x, isoTile.y);
		    	break;
		    	
		    case 'b' : tilePainter.drawBoulder(g2,  isoTile.x, isoTile.y);
		    	break;
		    	
		    case 'S' : tilePainter.drawSpikesUp(g2,  isoTile.x, isoTile.y);
	    		break;
	    		
	        case 's' : tilePainter.drawSpikesDown(g2,  isoTile.x, isoTile.y);
				break;
				
	        case 'i' : tilePainter.drawCharachterNorth(g2, isoTile.x, isoTile.y);
	        	break;
	        
	        case 'j' : tilePainter.drawCharachterWest(g2, isoTile.x, isoTile.y);
        		break;
        		
	        case 'l' : tilePainter.drawCharachterEast(g2, isoTile.x, isoTile.y);
        		break;
        		
	        case 'k' : tilePainter.drawCharachterSouth(g2, isoTile.x, isoTile.y);
        		break;
        	
	        case 'I' : tilePainter.drawCharachterNorthWithBoulder(g2, isoTile.x, isoTile.y);
        		break;
        	
	        case 'J' : tilePainter.drawCharachterWestWithBoulder(g2, isoTile.x, isoTile.y);
        		break;
        		
	        case 'L' : tilePainter.drawCharachterEastWithBoulder(g2, isoTile.x, isoTile.y);
        		break;
        		
	        case 'K' : tilePainter.drawCharachterSouthWithBoulder(g2, isoTile.x, isoTile.y);
	        	break;
	        
    	}
    }

    /**
     * Sets what tile for the camera to center upon (in tile coordinates) 
     * @param x
     * @param y
     */
	public void setCameraLocation(int x, int y) {
		this.CameraLocation.x = x;
		this.CameraLocation.y = y;
		
		Iso.cameraOffset.x = x;
		Iso.cameraOffset.y = y;
	}

}
