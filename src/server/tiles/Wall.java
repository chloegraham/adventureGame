package server.tiles;

public class Wall implements Tile, DrawFirst, Furniture{
	@Override
	public String toString() {
		return "w";
	}
}
