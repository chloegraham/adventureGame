package tiles;

public class PressurePad extends Unmoveable implements Tile {
	
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
	public String toString() {
		if (isActivated)
			return "Z";
		return "z";
	}
}
