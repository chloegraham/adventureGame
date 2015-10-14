package server.tiles;

/**
 * If something is passable, 
 * the player can walk over it
 *
 * However, a passable tile can be set 
 * to be not passable temporarily, such 
 * as doors which can be closed or open
 */
public interface Passable {
	boolean isPassable();
}
