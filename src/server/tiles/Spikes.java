package server.tiles;

import java.awt.Point;

/**
 * Deadly spikes. Spikes are either up, or down. 
 *
 */
public class Spikes implements Tile, Passable, Furniture {

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
	public boolean isPassable() {
		return !isActivated();
	}
	
	@Override
	public String toString() {
		if (isActivated)
			return "S";
		return "s";
	}
}
