package tiles;


public class Chest extends Unmoveable implements Tile {
	
	private boolean hasKey;
	private boolean isOpen;
	
	public Chest(boolean open) {
		if (open) {
			hasKey = false;
			isOpen = true;
		} else {
			hasKey = true;
			isOpen = false;
		}
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public void open() {
		isOpen = true;
	}
	
	public boolean hasKey() {
		return hasKey;
	}
	
	public boolean takeKey() {
		if (!hasKey)
			return false;
		hasKey = false;
		return true;
	}
	
	@Override
	public String toString() {
		if (isOpen)
			return "C";
		return "c";
	}




	
}
