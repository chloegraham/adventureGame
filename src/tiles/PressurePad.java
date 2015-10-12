package tiles;

public class PressurePad implements Tile, Passable, Furniture {
	
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
}
