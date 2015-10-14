package server.tiles;

/**
 * An empty tile is just a standard floor tile.
 * Players can walk on it, boulders can be placed on it.
 */
public class EmptyTile implements Tile, Passable, PutDownOnable, DrawFirst {
	@Override
	public boolean isPassable() {
		return true;
	}
	
	@Override
	public boolean isPutDownOnable() {
		return true;
	}
	
	@Override
	public String toString() {
		return "e";
	}
}
