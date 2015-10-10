package renderer;

import javax.swing.*;

import gameWorld.Direction;

import java.awt.*;
import java.util.Arrays;

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
    private Point rotatedCameraLocation = new Point(0,0);
    private Direction viewDir = Direction.NORTH;


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
    
    /**
     * True for clockwise, false for counter-clockwise
     * @param direction
     */
    public void rotateViewClockwise(boolean dir){
    	if(dir == false){
    		viewDir = Direction.rotateClockwise(viewDir);

    	}else{
    		viewDir = Direction.rotateCounterClockwise(viewDir);
    	}
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

        if(this.level != null && this.objects != null && this.moveables != null ){
            	paintFromCharLayers(g2);        	
        }else{
        	System.out.println("Set the layers before painting");
            return;
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
        
        char[][] rotatedLevel = null;
        char[][] rotatedObjects = null;
        char[][] rotatedMoveables = null;
        
        if(this.viewDir == Direction.NORTH){
        	rotatedLevel = this.level;
        	rotatedObjects = this.objects;
        	rotatedMoveables = this.moveables;
        }
        else if(this.viewDir == Direction.EAST){
        	rotatedLevel = Iso.rotateCW(this.level);
        	rotatedObjects = Iso.rotateCW(this.objects);
        	rotatedMoveables = Iso.rotateCW(this.moveables);
        }
        else if(this.viewDir == Direction.WEST){
        	rotatedLevel = Iso.rotateCCW(this.level);
        	rotatedObjects = Iso.rotateCCW(this.objects);
        	rotatedMoveables = Iso.rotateCCW(this.moveables);
        }
        else if(this.viewDir == Direction.SOUTH){
        	rotatedLevel = Iso.rotate180(this.level);
        	rotatedObjects = Iso.rotate180(this.objects);
        	rotatedMoveables = Iso.rotate180(this.moveables);
        }
        
        int numberOfRows = rotatedLevel.length;
        int numberOfColums = rotatedLevel[0].length;
        
        
        
        /* The camera position must be found before using Iso.TwoDee */
        
        int camX = 0;
		int camY = 0;
		
		 for (int i = 0; i < numberOfRows; i++) {
	            for (int j = 0; j < numberOfColums; j++) {
	            	if(rotatedMoveables[i][j] == 'i' || 
	            			rotatedMoveables[i][j] == 'j' || 
	            			rotatedMoveables[i][j] == 'k' || 
	            			rotatedMoveables[i][j] == 'l' ||
	            			rotatedMoveables[i][j] == 'I' || 
	            			rotatedMoveables[i][j] == 'J' || 
	            			rotatedMoveables[i][j] == 'K' || 
	            			rotatedMoveables[i][j] == 'L'){
		            		
		            		camX = j;
		            		camY = i;
		            }
	            }
		 }
		 
		 // Rotated camera location
		 setCameraLocation(camX, camY);
        
      
        g2.setStroke(new BasicStroke(1));
       // System.out.println(Arrays.deepToString(rotatedLevel));


        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColums; j++) {
                int x = j * tilesize / 2;
                int y = i * tilesize / 2;
                Point tile = new Point(x, y);
                Point isoTile = Iso.twoDToIso(tile);
                
                parseAndDrawTile(rotatedLevel[i][j], isoTile, g2);
                parseAndDrawTile(rotatedObjects[i][j], isoTile, g2);
                parseAndDrawTile(rotatedMoveables[i][j], isoTile, g2);
             
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
