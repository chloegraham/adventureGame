package movable;

import java.awt.Point;

public abstract class Moveable {
	
	private Point location;
	
	public Moveable(Point location) {
		this.location = location;
	}
	
	public Point getLocation() {
		return this.location;
	}
	
	public boolean setLocation(Point myLocation) {
		this.location = myLocation;
		return true;
	}
}
