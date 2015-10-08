package movable;

import gameWorld.Direction;
import gameWorld.Level;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import testconvert.Messages;
import testenums.MsgDirection;

public class Player extends Moveable{

	private Direction direction = Direction.NORTH; //String representing which direction player is facing
	private List<Item> inventory = new ArrayList<Item>();
	private boolean onPressurePad = false;
	private boolean hasBoulder = false;
	private int userID;
	private int levelID;
	
	public Player(Point location) {
		super(location);
		
	}
	
	//open a saved game
	public Player(int userID, int levelID, int keyAmount, boolean boulder,
			Direction direction2, Point point) {
		super(point);
		this.userID = userID;
		this.levelID = levelID;
		createKeys(keyAmount);
		this.hasBoulder = boulder;
		this.direction = direction2;
	}

	public int getLevelID() { return levelID; }
	
	public Direction getDirection() {
		return direction;
	}
	
	public String getEncodedPlayer() {
		
		String playerStr = "";
		int userID = getUserID();
		playerStr += userID + "%";
		
		int levelID = getLevelID();
		playerStr += levelID + "%";
		
		int keyAmount = numberOfKeys();
		playerStr += keyAmount + "%";
		
		int boulder = containsBoulder() ? 1 : 0;
		playerStr += boulder + "%";
		
		Direction direction = getDirection();
		playerStr += direction.ordinal() + "%";
		
		Point point = getLocation();
		int x = point.x;
		playerStr += x + "%";
		
		int y = point.y;
		playerStr += y + "%@";
		
		playerStr += Messages.DELIM_PLAYER;
		playerStr += Messages.DELIM_SPLIT;
		return playerStr;
	}
	
	public int getUserID() {
		return this.userID;
	}

	public Key getKey(){
		for(Item i: this.inventory){
			if(i instanceof Key){
				return (Key) i;
			}
		}
		return null;
	}
	
	
	
	public void createKeys(int amount){
		for(int i = 0; i < amount; i++){
			addToInventory(new Key("1234", "I'm the key to your heart"));
		}
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
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

	public int numberOfKeys() {
		int amount = 0;
		for(Item i: this.inventory){
			if(i instanceof Key){ amount++;	}
		}
		return amount;
	}


	public void nextLevel() {
		levelID++;
		setLocation(new Point(1, 1));
	}
	
	public void prevLevel() {
		if(levelID > 1){ //levels will start on one
			levelID--; 
			setLocation(new Point(1, 1));
		}
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
}