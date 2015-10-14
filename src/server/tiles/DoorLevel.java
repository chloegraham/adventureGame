package server.tiles;


public class DoorLevel implements Tile, Passable, Furniture, Door {	

	public DoorLevel(String symbol) {
		if (!symbol.equals("y"))
			throw new IllegalArgumentException("Level Door Constructor: Passed an invalid character(  " + symbol + " ). Should be one of the following: 'y', 'Y' .");
	}

	
	
	/*
	 *  Locking & Unlocking Doors changes whether Players can pass through them or not
	 */
	@Override
	public boolean isLocked(){
		return false;
	}
	
	@Override
	public void unlock() {
	}
	
	@Override
	public void lock() {
	}
	
	
	
	@Override
	public boolean isPassable() {

		return true;
	}
	
	@Override
	public String toString() {
		return "y";
	}
}