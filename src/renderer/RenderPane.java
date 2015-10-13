package renderer;

import javax.swing.*;

import gameWorld.Direction;

import java.awt.*;
import java.math.*;


/**
 * Created by Eliot on 15/09/2015.
 * 
 * This is the main render pane. It draws the game state based off 3 2d char arrays.
 * 
 * Each char layer must be
 * 	- The same width and height
 * 	- Complete (no nulls, no rows without full width)
 * 
 * The first layer is the level layer, which includes the floor tiles, and walls. They never change position, and are drawn first
 * The second layer is objects, chests, doors, spikes, pressure plates. They change state (open - closed), but don't change position. Drawn second. 
 * The final layer is moveables, which are the players, and the boulders. They both change state and change position. 
 * 
 * The renderer draws each layer one by one, layering them up. This is so we can have complex tile states (player holding a
 * boulder on an active pressure pad).
 */



public class RenderPane extends JPanel {
	private static final long serialVersionUID = 1L;
	public final int tilesize = 128;
    private TilePainter tilePainter = new TilePainter(tilesize);
    private char[][] level;
    private char[][] objects;
    private char[][] moveables;
    private Point camOffset = new Point(0,0);
    private float xLerpOffset = 0;
    private float yLerpOffset = 0;

    
    private Direction viewDir = Direction.NORTH;

    
    /**
     * Paints the render object. All 3 char layers must be set before painting. 
     * Sends the graphics context to paintFromCharLayers where most the magic happens
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(this.level != null && this.objects != null && this.moveables != null ){
            	paintFromCharLayers(g2);        	
        }else{
        	System.out.println("Set the layers before painting");
        }
    }
    
    
    public Dimension getPreferredSize() {
        return new Dimension(800,600);
    }
   
    
    
    
    /**
     * Sets the char layers which the renderer draws from. 
     * @param level
     * @param objects
     * @param moveables
     */
    public void setLayers(char[][] level, char[][] objects, char[][] moveables){
    	this.level = level;
    	this.objects = objects;
    	this.moveables = moveables;
    }
    
    
    public void setCamOffset(int x, int y){
    	this.camOffset.x = x;
    	this.camOffset.y = y;
    }

    /**
     * Rotates the this.viewDir variable. 
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
    
    


    
    /**
     * Paints from all 3 game layers, to the supplied graphics context. 
     * Most of the work happens here. 
     * @param g2
     */
    private void paintFromCharLayers(Graphics2D g2){
        
    	
    	//First, we rotate the all the char arrays, to the right view dirrection.
        char[][] rotatedLevel = null;
        char[][] rotatedObjects = null;
        char[][] rotatedMoveables = null;
        
        if(this.viewDir == Direction.NORTH){
        	rotatedLevel = this.level;
        	rotatedObjects = this.objects;
        	rotatedMoveables = this.moveables;
        }
        else if(this.viewDir == Direction.EAST){
        	rotatedLevel = IsoHelper.rotateCW(this.level);
        	rotatedObjects = IsoHelper.rotateCW(this.objects);
        	rotatedMoveables = IsoHelper.rotateCW(this.moveables);
        	
            int width = rotatedLevel[0].length;

        	
        	int x = width - camOffset.x;
        	int y = camOffset.x;
        	
        	setCamOffset(x, y);
        }
        else if(this.viewDir == Direction.WEST){
        	rotatedLevel = IsoHelper.rotateCCW(this.level);
        	rotatedObjects = IsoHelper.rotateCCW(this.objects);
        	rotatedMoveables = IsoHelper.rotateCCW(this.moveables);
        }
        else if(this.viewDir == Direction.SOUTH){
        	rotatedLevel = IsoHelper.rotate180(this.level);
        	rotatedObjects = IsoHelper.rotate180(this.objects);
        	rotatedMoveables = IsoHelper.rotate180(this.moveables);
        }
        else
        	throw new RuntimeException("Invalid direction.");
        
        int numberOfRows = rotatedLevel.length;
        int numberOfColums = rotatedLevel[0].length;
        
		 
		//Loop through each tile position
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColums; j++) {
            	
                Point tile = new Point(0, 0);
                tile.x = j * tilesize / 2;
                tile.y = i * tilesize / 2;
                
                
       
                //Take the Cartesian point and convert to isometric
                Point isoTile = IsoHelper.twoDToIsoWithLerpOffset(tile, xLerpOffset, yLerpOffset);
                
                //Check each layer and draw the right tile at that point.
                parseAndDrawTile(rotatedLevel[i][j], isoTile, g2);
                parseAndDrawTile(rotatedObjects[i][j], isoTile, g2);
                parseAndDrawTile(rotatedMoveables[i][j], isoTile, g2);
             
            }
        }
    }
    
    
    public void update(){
    	float xGoal = camOffset.x;
    	float yGoal = camOffset.y;
    	
    	float nearestX = (float)Math.round(xLerpOffset * 10f) / 10f;
    	float nearestY = (float)Math.round(yLerpOffset * 10f) / 10f;
    	    	
    	
    	float xDifference = Math.abs(xGoal - xLerpOffset);
    	float yDifference = Math.abs(yGoal - yLerpOffset);

    	
    	if(nearestX != xGoal){
    		if(xGoal > xLerpOffset){
        		xLerpOffset = xLerpOffset + (xDifference / 4);
        	}else{
        		xLerpOffset = xLerpOffset - (xDifference / 4);
        	}
    	}
    	
    	if(nearestY != yGoal){
    		if(yGoal > yLerpOffset){
    			yLerpOffset = yLerpOffset + (yDifference / 3);
        	}else{
        		yLerpOffset = yLerpOffset - (yDifference / 3);
        	}
    	}
   	
    }
    
    
    
    /**
     * Figures out what a char represents, then asks the tilePainter to draw
     * that tile at the supplied location
     * @param tile
     * @param isoTile
     * @param g2
     */
    private void parseAndDrawTile(char tile, Point isoTile, Graphics2D g2 ){
    	switch(tile){
	    	case 'e' : tilePainter.drawFloorTile(g2, isoTile.x, isoTile.y); break;
		    case 'w' : tilePainter.drawCubeTile(g2, isoTile.x, isoTile.y); break;
		    case 'c' : tilePainter.drawChest(g2, isoTile.x, isoTile.y); break;
		    case 'C' : tilePainter.drawOpenedChest(g2, isoTile.x, isoTile.y); break;
		    case 'd' : tilePainter.drawDoor(g2, isoTile.x, isoTile.y); break;
		    case 'D' : tilePainter.drawOpenDoor(g2, isoTile.x, isoTile.y); break;
		    case 'M' : tilePainter.drawGoalTile(g2, isoTile.x, isoTile.y); break;
		    case 'x' : tilePainter.drawDoor(g2, isoTile.x, isoTile.y); break;
		    case 'X' : tilePainter.drawOpenDoor(g2, isoTile.x, isoTile.y); break;
		    case 'z' : tilePainter.drawPressurePad(g2, isoTile.x, isoTile.y); break;
		    case 'Z' : tilePainter.drawPressurePadActive(g2, isoTile.x, isoTile.y); break;
		    case 'b' : tilePainter.drawBoulder(g2,  isoTile.x, isoTile.y); break;
		    case 'S' : tilePainter.drawSpikesUp(g2,  isoTile.x, isoTile.y); break;
	        case 's' : tilePainter.drawSpikesDown(g2,  isoTile.x, isoTile.y); break;
	        case 'i' : tilePainter.drawCharachterNorth(g2, isoTile.x, isoTile.y); break;
	        case 'j' : tilePainter.drawCharachterWest(g2, isoTile.x, isoTile.y); break;
	        case 'l' : tilePainter.drawCharachterEast(g2, isoTile.x, isoTile.y); break;
	        case 'k' : tilePainter.drawCharachterSouth(g2, isoTile.x, isoTile.y); break;
	        case 'I' : tilePainter.drawCharachterNorthWithBoulder(g2, isoTile.x, isoTile.y); break;
	        case 'J' : tilePainter.drawCharachterWestWithBoulder(g2, isoTile.x, isoTile.y); break;       		
	        case 'L' : tilePainter.drawCharachterEastWithBoulder(g2, isoTile.x, isoTile.y); break;
	        case 'K' : tilePainter.drawCharachterSouthWithBoulder(g2, isoTile.x, isoTile.y); break;
	        //TODO: handle player being 0 as dead? we can chat about that
    	}
    }

}
