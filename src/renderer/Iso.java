package renderer;

import java.awt.Point;

/**
 * Created by Eliot on 15/09/2015.
 */
public final class Iso {
    private Iso(){}


    public static void isoTo2d(Point iso){

    }

    public static Point twoDToIso(Point twoDee){
        Point tempPoint = new Point(0,0);
        tempPoint.x = (twoDee.x - twoDee.y) + 400; // Half the screen width, to center 0,0.
        tempPoint.y = ((twoDee.x + twoDee.y) / 2) + 100; // Half the screen height
        return tempPoint;
    }

    static int[][] rotateCW(int[][] mat) {
        final int M = mat.length;
        final int N = mat[0].length;
        int[][] ret = new int[N][M];
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[c][M-1-r] = mat[r][c];
            }
        }
        return ret;
    }

}
