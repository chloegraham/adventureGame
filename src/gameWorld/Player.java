package gameWorld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import movable.Boulder;
import movable.Item;
import movable.Key;
import movable.Moveable;

public class Player extends Moveable{

	private Direction direction = Direction.NORTH; //String representing which direction player is facing
	private List<Item> inventory = new ArrayList<Item>();
	private boolean onPressurePad = false;
	private boolean hasBoulder = false;
	
	public Player(Point location) {
		this(location, "i");
	}
	
	public Player(Point location, String c) {
		super(location, c);
	}
	
	@Override
	public String toString() {
		switch (this.direction) {
		case NORTH:
			if(hasBoulder) return "I";
			else return "i";
		case SOUTH:
			if(hasBoulder) return "K";
			else return "k";
		case EAST:
			if(hasBoulder) return "L";
			else return "l";
		default:
			if(hasBoulder) return "J";
			else return "j";
		}
	}
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
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
		
		List<Item> listCopy = new ArrayList<Item>(inventory);
		for(Item i: this.inventory){
			if(i instanceof Boulder){
				listCopy.remove(i);
				this.inventory = listCopy;
			}
		}
		this.hasBoulder = false;
	}
	
	public boolean containsBoulder(){
		for(Item i: this.inventory){
			if(i instanceof Boulder){
				return true;
			}
		}
		return false;
	}
	
	public void addToInventory(Item item){
		inventory.add(item);
		if(item instanceof Boulder) this.hasBoulder = true;
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
}