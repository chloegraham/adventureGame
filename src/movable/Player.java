package movable;

import gameWorld.Direction;

import java.awt.Point;

import convertors.Msgs;

public class Player extends Moveable{
	private int userID;
	private int stageID;
	private int roomID;
	private Direction direction; 	//String representing which direction player is facing
	private int keys;
	private boolean hasBoulder;
	
	
	
	/*
	 *  For opening a Saved/Stored Game
	 */
	public Player(int userID, int stageID, int roomID, Point location, Direction dir, int keyAmount, boolean boulder) {
		super(location);
		this.userID = userID;
		this.stageID = stageID;
		this.roomID = roomID;
		this.direction = dir;
		this.keys = keyAmount;
		this.hasBoulder = boulder;
	}

	
	
	/*
	 *  
	 */
	public Player(String encodedPlayer) {
		super(new Point(1,1));
		String[] parts = encodedPlayer.split("%");
		
		userID = Integer.parseInt(parts[0]);
		stageID = Integer.parseInt(parts[1]);
		roomID = Integer.parseInt(parts[2]);
		
		int x = Integer.parseInt(parts[6]);
		int y = Integer.parseInt(parts[7]);
		Point point = new Point(x, y);
		direction = Direction.getMsg( Integer.parseInt(parts[5]) );
		
		keys = Integer.parseInt(parts[3]);
		hasBoulder = Integer.parseInt(parts[4]) == 1;
		
		setLocation(point);
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
		str += keys + "%";
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