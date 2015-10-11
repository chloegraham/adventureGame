package tiles;

public class Spikes extends Unmoveable implements Tile {

	private boolean isActivated;
	
	public Spikes(boolean activated) {
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
			return "S";
		return "s";
	}
}
