package server.tiles;


public class DoorNormal implements Tile, Passable, PutDownOnable, Furniture, Door {	
	private boolean locked;

	public DoorNormal(String symbol) {
		if (symbol.equals("d")) {
			locked = true;
		}
		else if (symbol.equals("D")) {
			locked = false;
		}
		else {
			throw new IllegalArgumentException("Door Constructor: Passed an invalid character(  " + symbol + " ). Should be one of the following: 'd', 'D' .");
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
	public boolean isPutDownOnable() {
		return !isLocked();
	}
	
	@Override
	public String toString() {
		if (locked)
			return "d";
		return "D";
	}
}