package gameWorld;

import java.awt.Point;

import movable.Boulder;
import movable.Player;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.PressurePad;
import tiles.Tile;
import tiles.Unmoveable;
import tiles.Wall;
import userinterface.Action.Actions;
import convertors.Msgs;

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
		return message;
	}
	
	private boolean move(Player player, Level level, Point newLoc) {
		
		//check for out of bounds
		if (newLoc.y < 0 || newLoc.y > level.getTiles().length-1 || newLoc.x < 0 || newLoc.x > level.getTiles()[0].length-1){
			return false;
		}	
		
		Tile tile = level.getTiles()[newLoc.y][newLoc.x];
		Point currentTile =  player.getLocation();
		
		//Can't move into a boulder
		for(Boulder b: level.getBoulders())
			if(b.getLocation().equals(newLoc))
				return false;
		//Can't move into the following
		if (tile instanceof Chest || tile instanceof Wall || (tile instanceof Door && ((Door)tile).isLocked())) {
			return false;
		} 
		
		if(level.getTiles()[currentTile.y][currentTile.x] instanceof PressurePad){
			deactivateDoor(tile, level, player.getLocation());
		}
		//TODO: if door is closed behind a player and they come back to that level they can be seen within the door. change this
		if (tile instanceof Door) {
			Door door = (Door) tile;
			if (!door.isLocked() && door.isLevelChanger()) {
				if (door.isNextLevel()) {
					if (moveNextLevel(player))
						System.out.println("--------- MOVED TO THE NEXT LEVEL");
					else
						System.out.println("--------- HOPEFULLY YOU CAN'T MOVE TO THE NEXT LEVEL BECAUSE THERE ISN'T ONE");
				} else {
					if (movePrevLevel(player))
						System.out.println("--------- MOVED TO THE PREV LEVEL");
					else
						System.out.println("--------- HOPEFULLY YOU CAN'T MOVE TO THE PREV LEVEL BECAUSE THERE ISN'T ONE");
				}
				return true;
			}
		}
		
		if (tile instanceof PressurePad){
			activateDoor(tile, level, newLoc);
		}	
		return player.setLocation(newLoc);		
	}	
	//this method is called when either a player or a boulder is placed on a pressure pad
	private void activateDoor(Tile tile, Level level, Point newLoc) {
		PressurePad pad = (PressurePad) tile;
		pad.activate();
		Point door = level.getDoorFromPad(newLoc);
		Door doorTile = (Door)level.getTiles()[door.y][door.x];	
		doorTile.unlock();
	}
	//this method is called when either a player or a boulder is removed from a pressure pad, killing a player if present on door position
	private void deactivateDoor(Tile tile, Level level, Point currentLoc) {
		PressurePad pad = (PressurePad) level.getTiles()[currentLoc.y][currentLoc.x];
		pad.activate();
		Point doorPoint = level.getDoorFromPad(currentLoc);
		Door doorTile = (Door)level.getTiles()[doorPoint.y][doorPoint.x];	
		doorTile.lock();
		//if there's a player in the doorway when getting off the activated pressure pad then kill them
		for(Player p: this.players){
			if(p.getLocation().equals(doorPoint)){
				//TODO: kill player
				p.murder();
				System.out.println("you dead");
			}
		}
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
				if(tile instanceof PressurePad) deactivateDoor(tile, level, interactWith);
				player.addBoulder();		// true - "You picked up a Boulder"
			} else {
				isBoulderRelavent = false;	// false - [do nothing]
			}
		}
		else if (player.hasBoulder()) {
			//if there's a boulder in front of you, you can't drop your current boulder or pick it up
			boolean facingBoulder = level.containsBoulder(new Boulder(interactWith));
			for(Player p: this.players){
				//ensure that there isn't a player on the same level at the position you're trying to drop the boulder onto
				if(p.getLocation().equals(interactWith) && p.getLevelID() == level.getLevelID()){
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
				emptyORpressure = true;						// "You dropped a Boulder on an Empty Tile"
			}
			else if (tile instanceof PressurePad) {
				level.addBoulder(new Boulder(interactWith));
				player.dropBoulder();
				activateDoor(tile, level, interactWith);
				emptyORpressure = true;						// "You dropped a Boulder on a Pressure Pad."
			}
		}
			
		if (hadBoulder) {
			return Msgs.boulderPutDownMsg(infrontBoulder, emptyORpressure);
		} else {
			if (isBoulderRelavent)
				return Msgs.boulderPickUpMsg();
		
		}
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
	private boolean moveNextLevel(Player p) {
		// what level is the player on?
		int currentLvl = p.getLevelID();
		
		// what index is the current lvl?
		int indexCurrentLvl = 8989;
		for (int i = 0; i != levels.length; i++) {
			if (levels[i].getLevelID() == currentLvl) {
				if (indexCurrentLvl == 8989)
					indexCurrentLvl = i;
				else
					throw new IllegalArgumentException("Found a bug. There shouldn't be two levels with the same levelID.");
			}
		}
		
		// is there another level after indexCurrentLvl?
		if (indexCurrentLvl >= levels.length-1)
			return false;
			
		int indexNextLvl = indexCurrentLvl+1;
		Level lvl = levels[indexNextLvl];
		int nextLvlID = lvl.getLevelID();
		p.setLevelID(nextLvlID, lvl.getPrev());
		levels[indexCurrentLvl].removePlayer(p);
		levels[indexNextLvl].addPlayer(p);
		
		return true;
	}
	
	private boolean movePrevLevel(Player p) {
		// what level is the player on?
		int currentLvl = p.getLevelID();
		
		// what index is the current lvl?
		int indexCurrentLvl = 8989;
		for (int i = 0; i != levels.length; i++) {
			if (levels[i].getLevelID() == currentLvl) {
				if (indexCurrentLvl == 8989) {
					indexCurrentLvl = i;
				} else {
					throw new IllegalArgumentException("Found a bug. There shouldn't be two levels with the same levelID.");
				}
			}
		}
		
		// is there another level after indexCurrentLvl?
		if (indexCurrentLvl <= 0)
			return false;
			
		int indexPrevLvl = indexCurrentLvl-1;
		Level lvl = levels[indexPrevLvl];
		int prevLvlID = lvl.getLevelID();
		p.setLevelID(prevLvlID, lvl.getNext());
		levels[indexCurrentLvl].removePlayer(p);
		levels[indexPrevLvl].addPlayer(p);
		
		return true;
	}
	
	public void activateSpikes() {
		for (Level l : levels) {
			if (l.containsPlayer()) {
				l.activateSpikes(); //don't bother activate them if nobody is in a level to be affected by them
			}
		}
		
		
	}
	
	public String bouldersKeysLocation(int userID) {
		int index = 8989;
		for (int i = 0; i != players.length; i++) {
			if (players[i].getUserID() == userID) {
				if (index == 8989) {
					index = i;
				} else {
					throw new IllegalArgumentException("bouldersKeysLocation(): There were two players with the same ID. This is a bug, investigate.");
				}
			}
		}
		
		boolean hasBoulder = players[index].hasBoulder();
		int keyNumber = players[index].getNumberOfKeys();
		int playerX = players[index].getLocation().x;
		int playerY = players[index].getLocation().y;
		String bouldersKeysLocation = hasBoulder + Msgs.DELIM_DETAILS + keyNumber + Msgs.DELIM_DETAILS +
									  playerX + Msgs.DELIM_DETAILS + playerY + Msgs.DELIM_DETAILS +
									  Msgs.DELIM_SPLIT;
		return bouldersKeysLocation;
	}
}
