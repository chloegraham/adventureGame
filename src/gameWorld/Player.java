package gameWorld;

import java.awt.Point;

public class Player {
	private Point myLocation;
	private String direction; //String representing which direction player is facing
	
	public Player(Point loc){
		this.myLocation = loc;
	}

	public Point getMyLocation() {
		return this.myLocation;
	}
	
	public void setMyLocation(Point myLocation) {
		int oldX = this.myLocation.x;
		int oldY = this.myLocation.y;
		int newX = myLocation.x;
		int newY = myLocation.y;
		if (oldX == newX && newY > oldY){
			setDirection("North");
		} else if (oldX == newX && newY < oldY){
			setDirection("South");
		} else if (oldX > newX && newY == oldY){
			setDirection("West");
		} else {
			setDirection("East");
		}
		this.myLocation = myLocation;
	}
	
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "p";
	}
}