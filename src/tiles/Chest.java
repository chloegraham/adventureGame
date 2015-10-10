package tiles;


public class Chest extends Unmoveable implements Tile {
	
	private boolean hasKey;
	
	public Chest() {
		hasKey = true;
	}
	
	
	
	
	@Override
	public String toString() {
		return "c";
	}
}
