package gameWorld;

import java.awt.Point;

import convertors.Msgs;
import movable.Boulder;
import movable.Player;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.PressurePad;
import tiles.Spikes;
import tiles.Tile;
import tiles.Unmoveable;
import tiles.Wall;
import userinterface.Action.Actions;

public class GameLogic {
	private Level[] levels;
	private Player[] players;
	
	public GameLogic(Level[] levels, Player[] players){
		this.levels = levels;
		this.players = players;
	}
	
	
	public String handleAction(int ordinal, int userID) {
		Player player = null;
		Level level = null;
		
		
		if (userID == 101)
			player = players[0];
		else if (userID == 202)
			player = players[1];
		else
			throw new IllegalArgumentException();
		
		
		for (Level l : levels)
			if (l.getLevelID() == player.getLevelID())
				level = l;
		
		
		if (level == null)
			throw new IllegalArgumentException();
		
		
		String message = "";
		Point current = player.getLocation();
		Point newLocation = null;
		
		if(Actions.NORTH.ordinal() == ordinal) {
			player.setDirection(Direction.NORTH);
			newLocation = new Point(current.x, current.y-1);
			boolean success = move(player, level, newLocation);
			message = Msgs.moveMsg(player.getDirection(), success);
			
		}
		else if (Actions.EAST.ordinal() == ordinal) {
			player.setDirection(Direction.EAST);
			newLocation = new Point(current.x+1, current.y);
			boolean success = move(player, level, newLocation);
			message = Msgs.moveMsg(player.getDirection(), success);
			
		}
		else if (Actions.SOUTH.ordinal() == ordinal) {
			player.setDirection(Direction.SOUTH);
			newLocation = new Point(current.x, current.y+1);
			boolean success = move(player, level, newLocation);
			message = Msgs.moveMsg(player.getDirection(), success);
			
		}
		else if (Actions.WEST.ordinal() == ordinal) {
			player.setDirection(Direction.WEST);
			newLocation = new Point(current.x-1, current.y);
			boolean success = move(player, level, newLocation);
			message = Msgs.moveMsg(player.getDirection(), success);
			
		}
		else if (Actions.INTERACT.ordinal() == ordinal) { 
			message = interact(player, level, current);
			
		}
		else if (Actions.INSPECT.ordinal() == ordinal) { 
			message = inspect(player, level, current);
			
			
		}
		else if (Actions.NEWGAME.ordinal() == ordinal) {
		}
		else if (Actions.LOAD.ordinal() == ordinal) {
		}
		else if (Actions.SAVE.ordinal() == ordinal) {
	    }
		else {
			throw new IllegalArgumentException("GameLogic:  received an unexpected ordinal. It might be 'Inspect' which we have't coded yet.");
		}
		
		
		boolean hasBoulder = player.hasBoulder();
		int keyNumber = player.getNumberOfKeys();
		int playerX = player.getLocation().x;
		int playerY = player.getLocation().y;
		String bouldersKeysLocation = hasBoulder + Msgs.DELIM_DETAILS + keyNumber + Msgs.DELIM_DETAILS +
									  playerX + Msgs.DELIM_DETAILS + playerY + Msgs.DELIM_DETAILS +
									  Msgs.DELIM_SPLIT;
		
		return bouldersKeysLocation + message;
	}
	
	private boolean move(Player player, Level level, Point newLoc) {
		
		//check for out of bounds
		if (newLoc.y < 0 || newLoc.y > level.getTiles().length-1 || newLoc.x < 0 || newLoc.x > level.getTiles()[0].length-1){
			return false;
		}	
		
		Tile tile = level.getTiles()[newLoc.y][newLoc.x];
	
		
		
		for(Boulder b: level.getBoulders())
			if(b.getLocation().equals(newLoc))
				return false;
			
		
		
		if (tile instanceof Chest || tile instanceof Wall || (tile instanceof Door && ((Door)tile).isLocked())) {
			return false;
		} 
		
		
		
		if (tile instanceof Door) {
			Door door = (Door) tile;
			if (!door.isLocked() && door.isLevelChanger()) {
				if (door.isNextLevel()) {
					moveNextLevel(player);
				} else {
					movePrevLevel(player);
				}
				return true;
			}
		}
		
		
		
		if (tile instanceof PressurePad){
			PressurePad pad = (PressurePad) tile;
			pad.activate();
			
		}
		else if (tile instanceof Spikes){
			Spikes spikes = (Spikes) tile;
			spikes.activate();
		}
		
		return player.setLocation(newLoc);		
	}	
	


	private String interact(Player player, Level level, Point now) {
		
		Direction direction = player.getDirection();
		Point interactWith;
		
		switch(direction){
			case NORTH:
				interactWith = new Point(now.x, now.y-1);
				break;
			case SOUTH:
				interactWith = new Point(now.x, now.y+1);
				break;
			case EAST:
				interactWith = new Point(now.x+1, now.y);
				break;
			case WEST:
				interactWith = new Point(now.x-1, now.y);
				break;
			default:
				throw new IllegalArgumentException();
		}
		
		if (interactWith.y < 0 || interactWith.y > level.getTiles().length-1 || interactWith.x < 0 || interactWith.x > level.getTiles()[0].length-1){
			return "temp - you are trying to interact with a tile outside the bounds of the game.";
		}	
		
		Tile tile = level.getTiles()[interactWith.y][interactWith.x];	
		
		/*
		 *  Chest interaction or inspection code & messages
		 */
		if (tile instanceof Chest){

			Chest chest = (Chest) tile;
			
			// OPEN
			// true - "Chest already open"
			// false - "You opened the Chest"
			boolean open = chest.isOpen();
			
			// Open it either way
			chest.open();
			
			// OPENED
			// true - no comment needed
			// false - chest is closed and you can't open it and you can't look inside
			boolean opened = chest.isOpen();
			
			// ISKEY
			// true - "there is a key inside & player has picked it up"
			// false - "there is no key inside this chest"	
			boolean isKey = chest.hasKey();
			
			// [chest should now be open unless we add more game logic/chest logic in the future]
			if (opened) {
				if (isKey) {
					player.addKey();
					chest.takeKey();
				}		
			}
			
			return Msgs.chestMsg(open, opened, isKey);
		} 
		
		
		
		/*
		 *  Door interaction code & message
		 */
		if (tile instanceof Door){
			
			Door door = (Door) tile;
			
			// ISLOCKED
			// true - "The Door is locked"
			// false - "The Door is unlocked. You may enter." RETURN
			boolean isLocked = door.isLocked();
			
			// HASKEY
			// true - [move on to try unlock the door]
			// false - "you don't have a key so you can't unlock the door. go find one."
			boolean hasKey = player.hasKey();
			
			if (isLocked) {
				if (hasKey) {
					door.unlock();
					player.useKey();
				}	
			}
			
			return Msgs.doorMsg(isLocked, hasKey);
		}
		
		
		
		/*
		 *  Boulder interaction code & message
		 */
		boolean hadBoulder = player.hasBoulder();
		boolean isBoulderRelavent = true;
		boolean infrontBoulder = false;
		boolean emptyORpressure = false;
		
		if (!player.hasBoulder()) {
			//facingBoulder will be true if there was a boulder in front of player (which is now removed from the list of boulders in the level)
			boolean facingBoulder = level.removeBoulder(new Boulder(interactWith));
			if(facingBoulder){
				player.addBoulder();		// true - "You picked up a Boulder"
			} else {
				isBoulderRelavent = false;	// false - [do nothing]
			}
		}
		else if (player.hasBoulder()) {
			//if there's a boulder in front of you, you can't drop your current boulder or pick it up
			boolean facingBoulder = level.containsBoulder(new Boulder(interactWith));
			for(Player p: this.players){
				if(p.getLocation().equals(interactWith)){
					return "You can't put a boulder on your friend lol";
				}
			}
				
			if(facingBoulder) {
				infrontBoulder = true; 						// "You already have a Boulder, so can't pick up another."
			}
			//check if there's a player present on the tile you're trying to drop the boulder onto
			else if (tile instanceof EmptyTile) {
				level.addBoulder(new Boulder(interactWith));
				player.dropBoulder();
				emptyORpressure = true;						// "You dropped a Boulder on an Empty Tile or Pressure Pad."
			}
			else if (tile instanceof PressurePad) {
				level.addBoulder(new Boulder(interactWith));
				player.dropBoulder();
				PressurePad pad = (PressurePad) tile;
				pad.activate();
				emptyORpressure = true;						// "You dropped a Boulder on an Empty Tile or Pressure Pad."
			}
		}
			
		if (hadBoulder) {
			return Msgs.boulderPutDownMsg(infrontBoulder, emptyORpressure);
		} else {
			if (isBoulderRelavent)
				return Msgs.boulderPickUpMsg();
		
		}
		/*
		 *  //TODO I think should only reach here is there is nothing to interact with.
		 */
		return "";
	}
	

	
	private String inspect(Player player, Level level, Point now) {
		
		Direction direction = player.getDirection();
		Point interactWith;
		
		switch(direction){
			case NORTH:
				interactWith = new Point(now.x, now.y-1);
				break;
			case SOUTH:
				interactWith = new Point(now.x, now.y+1);
				break;
			case EAST:
				interactWith = new Point(now.x+1, now.y);
				break;
			case WEST:
				interactWith = new Point(now.x-1, now.y);
				break;
			default:
				throw new IllegalArgumentException();
		}
		
		if (interactWith.y < 0 || interactWith.y > level.getTiles().length-1 || interactWith.x < 0 || interactWith.x > level.getTiles()[0].length-1){
			return "Why are you trying to inspect a tile outside the bounds of the game? Jesus.";
		}	
		
		Tile tile = level.getTiles()[interactWith.y][interactWith.x];
		
		if(tile instanceof Unmoveable){	//doesn't include pressure pad
			return Msgs.inspectUnmovable((Unmoveable) tile);
		}
		//if code reaches here, might be trying to inspect an empty tile (empty tile doesn't extend unmoveable) boulders and players (moveables) can be on empty tiles
		for(Boulder b: level.getBoulders()){
			if(b.getLocation().equals(interactWith)){
				if(tile instanceof PressurePad){
					return Msgs.inspectMoveable(b, true);
				}
				return Msgs.inspectMoveable(b, false);
			}
		}
		//check both players in list, if one is present on the tile you're inspecting then return a string that says so
		for(Player p: this.players){
			if(p.getLocation().equals(interactWith)){
				if(tile instanceof PressurePad){
					return Msgs.inspectMoveable(p, true);
				}
				return Msgs.inspectMoveable(p, false);
			}
		}
		
		//if code reaches here, pressure pad can't contain a moveable object and so return message stating it's a pressure pad
		if(tile instanceof PressurePad){
			return Msgs.inspectPressurePad();
		}
		return "You're trying to inspect not much";
	}

	
	/*
	 *  // TODO need to add some sort of message for changing level
	 */
	private void moveNextLevel(Player p) {
		int pastLevel = p.getLevelID();
		p.nextLevel();
		
		for (int i = 0; i != levels.length; i++) {
			if (levels[i].getLevelID() == p.getLevelID())
				levels[i].addPlayer(p);
			
			if (levels[i].getLevelID() == pastLevel)
				levels[i].removePlayer(p);
		}
	}
	
	private void movePrevLevel(Player p) {
		int pastLevel = p.getLevelID();
		p.prevLevel();
		
		for (int i = 0; i != levels.length; i++) {
			if (levels[i].getLevelID() == p.getLevelID())
				levels[i].addPlayer(p);
			
			if (levels[i].getLevelID() == pastLevel)
				levels[i].removePlayer(p);
		}
	}
}
