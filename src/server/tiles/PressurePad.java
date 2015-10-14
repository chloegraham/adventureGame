package server.tiles;

/**
 * Pressure pads! They can either be up, or down. They're designed to be
 * activated by either a boulder, or the player standing on them.
 * @author Eliot
 *
 */
public class PressurePad implements Tile, Passable, Furniture, PutDownOnable {	
	private boolean isActivated;
	
	public PressurePad(boolean activated) {
		isActivated = activated;
	}

	public void activate(){		
		isActivated = !isActivated;
	}
	
	public boolean isActivated(){
		return isActivated;
	}
	
	@Override
	public boolean isPassable() {
		return true;
	}
	
	@Override
	public String toString() {
		if (isActivated)
			return "Z";
		return "z";
	}

	@Override
	public boolean isPutDownOnable() {
		return true;
	}
}
