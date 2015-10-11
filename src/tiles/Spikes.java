package tiles;

import java.awt.Point;

public class Spikes extends Unmoveable implements Tile {

	private boolean isActivated;
	private Point location;
	
	public Spikes(boolean activated, Point location) {
		isActivated = activated;
		this.location = location;
	}

	public void activate(){		
		isActivated = !isActivated;
	}
	
	public boolean isActivated(){
		return isActivated;
	}
	
	public Point getLocation() {
		return location;
	}
	
	@Override
	public String toString() {
		if (isActivated)
			return "S";
		return "s";
	}
}
