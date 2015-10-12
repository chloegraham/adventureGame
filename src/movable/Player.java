package movable;

import gameWorld.Direction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import convertors.Msgs;

public class Player extends Moveable{

	private int userID;
	private int levelID;
	private Direction direction = Direction.NORTH; 	//String representing which direction player is facing
	private List<Item> inventory = new ArrayList<Item>();
	private boolean hasBoulder;
	
	
	
	/*
	 *  Basic constructor
	 */
	public Player(Point location) {
		super(location);
		hasBoulder = false;
	}
	
	
	
	/*
	 *  For opening a Saved/Stored Game
	 */
	public Player(int userID, int levelID, int keyAmount, boolean boulder, Direction dir, Point point) {
		super(point);
		this.userID = userID;
		this.levelID = levelID;
		this.direction = dir;
		createKeys(keyAmount);
		this.hasBoulder = boulder;
	}

	
	
	/*
	 *  
	 */
	public Player(String encodedPlayer) {
		super(new Point(1,1));
		String[] parts = encodedPlayer.split("%");
		
		userID = Integer.parseInt(parts[0]);
		levelID = Integer.parseInt(parts[1]);
		int keyAmount = Integer.parseInt(parts[2]);
		createKeys(keyAmount);
		hasBoulder = Integer.parseInt(parts[3]) == 1;
		direction = Direction.getMsg( Integer.parseInt(parts[4]) );
		
		int x = Integer.parseInt(parts[5]);
		int y = Integer.parseInt(parts[6]);
		Point point = new Point(x, y);
		setLocation(point);
	}
	
	
	
	/*
	 *  Get UserID & Get LevelID
	 */
	public int getUserID() { return this.userID; }
	public int getLevelID() { return levelID; }
	
	
	
	/*
	 *  Encoded a Player object in to a String for sending through Server of Saving Game
	 */
	public String getEncodedPlayer() {
		String str = "";
		
		str += userID + "%";
		str += levelID + "%";
		str += getNumberOfKeys() + "%";
		int boulder = hasBoulder() ? 1 : 0;
		str += boulder + "%";
		str += direction.ordinal() + "%";
		
		Point point = getLocation();
		int x = point.x;
		str += x + "%";
		int y = point.y;
		str += y + "%";
		
		str += Msgs.DELIM_PLAYER;
		str += Msgs.DELIM_SPLIT;
		return str;
	}
	
	
	/*
	 *  Direction
	 */
	public Direction getDirection() { return direction; }
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	
	
	/*
	 *  Key
	 */
	public boolean addKey() {
		return inventory.add(new Key());
	}
	
	public boolean hasKey() {
		for(Item i: inventory)
			if(i instanceof Key)
				return true;
		return false;
	}
	
	public boolean useKey(){
		if (!hasKey()) 
			return false;
		removeKey();
		return true;
	}
	
	private void removeKey() {
		for(Item i: inventory) {
			if(i instanceof Key) {
				inventory.remove(i);
				return;
			}
		}
		throw new IllegalArgumentException("Should be impossible to remove a key if Player has no keys.");
	}
	
	public void createKeys(int amount){
		for(int i = 0; i < amount; i++){
			inventory.add(new Key());
		}
	}
	
	public int getNumberOfKeys() {
		int amount = 0;
		for(Item i: inventory)
			if(i instanceof Key)
				amount++;	
		return amount;
	}

	
	
	/*
	 *  Boulder
	 */
	public boolean addBoulder() {
		if (hasBoulder)
			return false;
		hasBoulder = true;
		return hasBoulder;
	}
	
	public boolean dropBoulder(){
		if (!hasBoulder)
			return false;
		hasBoulder = false;
		return true;
	}
	
	public boolean hasBoulder(){
		return hasBoulder;
	}
	
	

	

	/*
	 *  Change Level
	 */
	public void setLevelID(int lvlID, Point loc) {
		levelID = lvlID;
		setLocation(loc);
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