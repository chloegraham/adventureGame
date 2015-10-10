package tiles;


public class Chest extends Unmoveable implements Tile {
	
	private boolean hasKey;
	private boolean isOpen;
	
	public Chest() {
		hasKey = true;
		isOpen = false;
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public boolean open() {
		if (isOpen)
			return false;
		isOpen = true;
		return isOpen;
	}
	
	public boolean hasKey() {
		return hasKey;
	}
	
	public boolean takeKey() {
		if (hasKey)
			return false;
		hasKey = false;
		return true;
	}
	
	@Override
	public String toString() {
		return "c";
	}




	
}
