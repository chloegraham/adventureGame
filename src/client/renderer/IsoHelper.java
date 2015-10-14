package client.renderer;

import java.awt.Point;

/**
 * Created by Eliot on 15/09/2015.
 * 300285842
 * 
 * A helper class to convert from Cartesian Coordinates to Isometric, and rotate arrays. 
 */
public final class IsoHelper {
    private IsoHelper(){}
    private static int tileSize = 128;
    
    
    /**
     * Converts a Cartesian Coordinate into an isometric point in screen space, offset by the camera position.
     * @param twoDee
     * @return
     */
    protected static Point twoDToIsoWithLerpOffset(Point twoDee, float xoffset, float yoffset){
        Point tempPoint = new Point(0,0);
        
        double camX = xoffset * (tileSize / 2);
        double camY = yoffset * (tileSize / 2);
        
        double x = twoDee.x;
        double y = twoDee.y;
        
        x -= camX;
        y -= camY;
        
        tempPoint.x = (int) ((x - y) + 400); // Half the screen width, to center 0,0.
        tempPoint.y = (int) (((x + y) / 2) + 300); // Half the screen height
        return tempPoint;
    }
    
    
    /**
     * Rotates a 2d level array 90 degrees clockwise. 
     * Will also rotate player tiles which have direction
     * 
     * Based off this stack overflow answer http://stackoverflow.com/questions/2799755/rotate-array-clockwise
     * @param mat
     * @return
     */
    protected static char[][] rotateCW(char[][] mat) {
        final int M = mat.length;
        final int N = mat[0].length;
        char[][] ret = new char[N][M];
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
            	   	
                ret[c][M-1-r] = mat[r][c];           
                
                //Rotating the player direction tiles as well
                if(mat[r][c] == 'I'){ ret[c][M-1-r] = 'L';}
            	if(mat[r][c] == 'L'){ ret[c][M-1-r] = 'K';}
            	if(mat[r][c] == 'K'){ ret[c][M-1-r] = 'J';}
            	if(mat[r][c] == 'J'){ ret[c][M-1-r] = 'I';}
            	
            	if(mat[r][c] == 'i'){ ret[c][M-1-r] = 'l';}
            	if(mat[r][c] == 'l'){ ret[c][M-1-r] = 'k';}
            	if(mat[r][c] == 'k'){ ret[c][M-1-r] = 'j';}
            	if(mat[r][c] == 'j'){ ret[c][M-1-r] = 'i';}
            }
        }
        return ret;
    }
    
    /**
     * Rotates a 2d level array 90 degrees counter-clockwise. 
     * @param mat
     * @return
     */
    protected static char[][] rotateCCW(char[][] mat) {
       return rotateCW(rotateCW(rotateCW(mat)));
    }
    
    /**
     * Rotates a 2d level array 180 degrees 
     * @param mat
     * @return
     */
    protected static char[][] rotate180(char[][] mat) {
        return rotateCW(rotateCW(mat));
     }

}
