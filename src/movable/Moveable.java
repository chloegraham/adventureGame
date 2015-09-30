package movable;

import java.awt.Point;

public abstract class Moveable {
	
	private Point location;
	private String character;
	
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
	
	public void setCharacter(String character){
		this.character = character;
	}
	
	@Override
	public String toString() {
		return character;
	}
	

}
