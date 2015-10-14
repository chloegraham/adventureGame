package server.tiles;


public class DoorLevel implements Tile, Passable, Furniture, Door {	
	private boolean locked;

	public DoorLevel(String symbol) {
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
	@Override
	public boolean isLocked(){
		return locked;
	}
	
	@Override
	public void unlock() {
		locked = false;
	}
	
	@Override
	public void lock() {
		locked = true;
	}
	
	
	
	@Override
	public boolean isPassable() {
		return !isLocked();
	}
	
	
	@Override
	public String toString() {
		if (locked = true)
			return "y";
		return "Y";
	}
}