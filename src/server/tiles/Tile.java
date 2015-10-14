package server.tiles;

/**
 * Represents a tile.
 * 
 * This interface doesn't ensure it
 */
public interface Tile {
	
	/**
	 * Every tile must return a string representing it's type.
	 * This is used to build the char arrays, which render the game.
	 * @return
	 */
	String toString();
}
