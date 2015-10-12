package tiles;


public class LevelDoor implements Tile, Passable, PutDownOnable, LevelChanger, Furniture {	
	private boolean locked;

	public LevelDoor(String symbol) {
		if (symbol.equals("y")){
			locked = true;
		}
		else if (symbol.equals("Y")) {
			locked = false;
		}
		else {
			throw new IllegalArgumentException("Level Door Constructor: Passed an invalid character(  " + symbol + " ). Should be one of the following: 'y', 'Y' .");
		}
	}

	
	
	/*
	 *  Locking & Unlocking Doors changes whether Players can pass through them or not
	 */
	public boolean isLocked(){
		return locked;
	}
	
	public void unlock() {
		locked = false;
	}
	
	public void lock() {
		locked = true;
	}
	
	
	
	@Override
	public boolean isPassable() {
		return !isLocked();
	}
	
	@Override
	public boolean isPutDownOnable() {
		return !isLocked();
	}
	
	@Override
	public String toString() {
		if (locked = true)
			return "y";
		return "Y";
	}
}