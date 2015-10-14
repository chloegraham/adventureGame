package server.tiles;

/**
 * If a tile is PutDownOnable, 
 * a boulder can be placed upon it.
 * 
 * eg, empty tiles, pressure pads.
 */
public interface PutDownOnable {
	boolean isPutDownOnable();
}
