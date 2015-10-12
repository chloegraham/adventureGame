package tiles;

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
