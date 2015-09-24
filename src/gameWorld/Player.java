package gameWorld;

import java.awt.Point;

public class Player {
	private Point myLocation;
	
	public Player(Point loc){
		this.myLocation = loc;
	}

	public Point getMyLocation() {
		return this.myLocation;
	}
	
	public void setMyLocation(Point myLocation) {
		this.myLocation = myLocation;
	}
	
	@Override
	public String toString() {
		return "p";
	}
}