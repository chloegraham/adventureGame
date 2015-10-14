package server.movable;

import java.awt.Point;

import sharedHelpers.Direction;
import sharedHelpers.Msgs;


/**
 *  The all important player.
 *  
 *  Players keep track of a lot of things, most noticeably their location, and their inventory.
 *  They can also die, and are facing a direction.
 *  
 *  Player location is stored as, what stage are they in, what room are they in, and then what tile are they on. 
 *  
 */
public class Player extends Moveable{
	private int userID;
	private int stageID;
	private int roomID;
	private Direction direction; 	//String representing which direction player is facing
	private int keys;
	private boolean hasBoulder;
	private boolean dead = false;
	private boolean won = false;
	
	/**
	 * Creates a player from an encodeded string. The string is 
	 * for creating players from saved gamestate in xml
	 * 
	 * @param encodedPlayer
	 */
	public Player(String encodedPlayer) {
		super(new Point(1,1));
		String[] parts = encodedPlayer.split("%");
		
		userID = Integer.parseInt(parts[0]);
		stageID = Integer.parseInt(parts[1]);
		roomID = Integer.parseInt(parts[2]);
		
		int x = Integer.parseInt(parts[3]);
		int y = Integer.parseInt(parts[4]);
		Point point = new Point(x, y);
		direction = Direction.getMsg( Integer.parseInt(parts[5]) );
		
		keys = Integer.parseInt(parts[6]);
		hasBoulder = Integer.parseInt(parts[7]) == 1;
		
		setLocation(point);
		
		System.out.println("\n  Constructor-" + toStringConstructor());
	}
	
	
	
	/*
	 *  Get UserID & Get LevelID
	 */
	public int getUserID() { return this.userID; }
	public int getStageID() { return stageID; }
	public int getRoomID() { return roomID; }
	
	
	
	/*
	 *  Encoded a Player object in to a String for sending through Server of Saving Game
	 */
	public String getEncodedPlayer() {
		String str = "";
		
		str += userID + "%";
		str += stageID + "%";
		str += roomID + "%";
		
		Point point = getLocation();
		int x = point.x;
		str += x + "%";
		int y = point.y;
		str += y + "%";
		
		str += direction.ordinal() + "%";
		str += keys + "%";
		int boulder = hasBoulder() ? 1 : 0;
		str += boulder + "%";
		
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
		keys = keys+1;
		return true;
	}
	
	public boolean hasKey() {
		return keys > 0;
	}
	
	public boolean useKey(){
		if (!hasKey()) 
			return false;
		removeKey();
		return true;
	}
	
	private void removeKey() {
		keys = keys-1;
	}
	
	public int getNumberOfKeys() {
		return keys;
	}

	
	
	/**
	 * Adds a boulder to the players inventory. 
	 * The player can only carry one boulder at a time.
	 * @return
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

	
	
	/**
	 * Kills the player
	 */
	public void murder() {
		dead = true;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void win(){
		won = true;
	}
	
	public boolean hasWon() {
		return won;
	}
	
	/**
	 * Sets the location of the player
	 * 
	 * @param stage		What stage to send the player to
	 * @param room		What room inside that stage
	 * @param location	The coordinate they should be on
	 * @return
	 */
	public boolean setLocation(int stage, int room, Point location) {
		stageID = stage;
		roomID = room;
		setLocation(location);
		return true;
	}
	
	public String toStringConstructor() {
		String str = "  PLAYER-";
		
		str += "  userID: ";
		str += userID;
		
		str += "  stageID: ";
		str += stageID;
		
		str += "  roomID: ";
		str += roomID;
		
		str += "  location: ";
		str += getLocation().x + ", ";
		str += getLocation().y;
		
		str += "  direction: ";
		str += direction.toString();
		
		str += "  #keys: ";
		str += keys;
		
		str += "  hasBoulder: ";
		str += hasBoulder();
		
		str += "  isDead: ";
		str += isDead();
		
		return str;
	}
	
	
	/**
	 * Gets the string representing the players current state.
	 * 
	 * Returns either 'i', 'j', 'k', 'l' depending on the direction. 
	 * Uppercase if the player is carrying a boulder
	 */
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
		case WEST:
			if(hasBoulder) return "J";
			else return "j";
		default:
			throw new IllegalArgumentException();
		}
	}
}