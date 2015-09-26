package gameWorld;

import item.Item;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Player {
	private Point myLocation;
	private String direction; //String representing which direction player is facing
	private List<Item> inventory = new ArrayList<Item>();
	
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

	public List<Item> getInventory(){
		return inventory;
	}
	
	public void addToInventory(Item item){
		inventory.add(item);
	}
	
	public void testInventory(){
		for (Item s : inventory){
			System.out.println(s.toString());
		}
	}
	
	@Override
	public String toString() {
		return "p";
	}
}