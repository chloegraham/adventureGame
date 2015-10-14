package server.movable;

import java.awt.Point;

/**
 * Classes which implement Moveable, can move around the board, 
 * they are either players or boulders.
 * 
 * They keep track of their own location.
 */

public abstract class Moveable {
	
	private Point location;
	
	public Moveable(Point location) {
		this.location = location;
	}
	
	public Point getLocation() {
		return this.location;
	}
	
	public boolean setLocation(Point location) {
		this.location = location;
		return true;
	}
}
