package server.tiles;

/**
 * Represents a tile.
 * 
 * Our tiles utilize the Strategy pattern. There are multiple different behaviours our
 * tiles could or could not use, for example
 * 
 * Passable - players can work over it
 * Impassable - players can't walk over it
 * PutDownAble - items can be placed on it. 
 * and some more
 * 
 * We have multiple inferfaces for all these conditions, and our different tile types
 * inherit the function they desire. This is better than inheriting, as we can't extend from 
 * multiple classes.
 */
public interface Tile {
	
	/**
	 * Every tile must return a string representing it's type.
	 * This is used to build the char arrays, which render the game.
	 * @return
	 */
	String toString();
}
