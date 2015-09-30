package movable;

import java.awt.Point;

public abstract class Moveable {
	
	private Point location;
	protected String character;
	
	public Moveable(Point location, String character) {
		this.location = location;
		this.character = character;
	}
	
	public Point getLocation() {
		return this.location;
	}
	
	public boolean setLocation(Point myLocation) {
		this.location = myLocation;
		return true;
	}
	
	@Override
	public String toString() {
		return character;
	}
	

}
