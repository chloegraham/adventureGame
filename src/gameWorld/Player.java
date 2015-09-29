package gameWorld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.api.addressing.OneWayFeature;

import movable.Item;
import movable.Key;

public class Player {
	private Point myLocation;
	private String direction = "North"; //String representing which direction player is facing
	private List<Item> inventory = new ArrayList<Item>();
	private String character = "p";
	private boolean onPressurePad = false;
	private boolean onSpikes = false;
	
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

	public Key getKey(){
		for(Item i: this.inventory){
			if(i instanceof Key){
				return (Key) i;
			}
		}
		return null;
	}
	
	public void addToInventory(Item item){
		inventory.add(item);
	}
	
	public void testInventory(){
		for (Item s : inventory){
			System.out.println(s.toString());
		}
	}
	
	public void togglePressurePad(){
		if(onPressurePad){
			this.onPressurePad = false;
			this.character = "p";
		} else {
			this.onPressurePad = true;
			this.character = "Z";
		}
	}
	
	public boolean onPressurePad(){
		return onPressurePad;
	}

	public void toggleOnSpikes() {
		if(onSpikes){
			this.onSpikes = false;
			this.character = "p";
		} else	{
			this.onSpikes = true;
			this.character = "x";
		}
	}
	
	public boolean onSpikes(){
		return onSpikes;
	}
	
	@Override
	public String toString() {
		return character;
	}
	
}