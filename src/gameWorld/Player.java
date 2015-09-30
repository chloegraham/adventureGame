package gameWorld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.api.addressing.OneWayFeature;

import movable.Boulder;
import movable.Item;
import movable.Key;
import movable.Moveable;

public class Player extends Moveable{

	private String direction = "North"; //String representing which direction player is facing
	private List<Item> inventory = new ArrayList<Item>();
	private boolean onPressurePad = false;
	private boolean onSpikes = false;
	
	public Player(Point location) {
		this(location, "p");
	}
	
	public Player(Point location, String c) {
		super(location, c);
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
	
	public void removeBoulder(){
		for(int i = 0; i < this.inventory.size()-1; i++){
			if(this.inventory.get(i) instanceof Boulder){
				this.inventory.remove(i);
			}
		}
	}
	
	public void addToInventory(Item item){
		inventory.add(item);
	}
	
	public List<Item> getInventory(){
		return inventory;
	}
	
	public void testInventory(){
		for (Item s : inventory){
			System.out.println(s.toString());
		}
	}
	
	public void togglePressurePad(){
		if(onPressurePad){
			this.onPressurePad = false;
		} else {
			this.onPressurePad = true;
		}
	}
	
	public boolean onPressurePad(){
		return onPressurePad;
	}

	public void toggleOnSpikes() {
		if(onSpikes){
			this.onSpikes = false;
		} else	{
			this.onSpikes = true;
		}
	}
	
	public boolean onSpikes(){
		return onSpikes;
	}
}