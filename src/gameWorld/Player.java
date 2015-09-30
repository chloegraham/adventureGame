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
	private boolean onSpikes = false;
	private boolean hasBoulder = false;
	
	public Player(Point location) {
		this(location, "p");
	}
	
	public Player(Point location, String c) {
		super(location, c);
	}
	
	@Override
	public String toString() {
		switch (this.direction) {
		case NORTH:
			if(hasBoulder) return "1";
			else return "2";
		case SOUTH:
			if(hasBoulder) return "3";
			else return "4";
		case EAST:
			if(hasBoulder) return "5";
			else return "6";
		default:
			if(hasBoulder) return "7";
			else return "8";
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