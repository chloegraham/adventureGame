package gameWorld;

import java.awt.Point;

import movable.Boulder;
import movable.Player;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.LevelDoor;
import tiles.Passable;
import tiles.PressurePad;
import tiles.PutDownOnable;
import tiles.Tile;
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
		
		// Check for out of bounds
		if (newLoc.y < 0 || newLoc.y > level.getTiles().length-1 || newLoc.x < 0 || newLoc.x > level.getTiles()[0].length-1)
			return false;
		
		
		
		Tile tile = level.getTiles()[newLoc.y][newLoc.x];
		Point currentTile =  player.getLocation();
		
		
		
		// Check Boulders because Players can't move on to Boulders
		for(Boulder b: level.getBoulders())
			if(b.getLocation().equals(newLoc))
				return false;
			
		
		
		// If Tile is not type Passable = can't move on to.
		// If Tile is type Passable but state is !isPassable() = can't move on to.
		if (!(tile instanceof Passable) || (tile instanceof Passable && !((Passable)tile).isPassable()))
			return false;
		
		
		
		if(level.getTiles()[currentTile.y][currentTile.x] instanceof PressurePad){
			PressurePad pad = (PressurePad) level.getTiles()[currentTile.y][currentTile.x];
			pad.activate();
			Point doorPoint = level.getDoorFromPad(player.getLocation());
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
		
		
		
		// If Tile is a LevelDoor
		// The Passable check above ensure LevelDoor is also Passable
		if (tile instanceof LevelDoor) {
			moveNextLevel();
			System.out.println("--------- MOVED TO THE NEXT LEVEL");
			return true;
			
			/*
			 *  I could instead write a method to change the New Location for tidier code
			 */
		}

		
		
		
		// If Tile is a Door
		// The Passable check above ensure LevelDoor is also Passable
		if (tile instanceof Door) {
			moveNextDoor(player);
			System.out.println("--------- TRANSFER TO ANOTHER DOOR");
			return true;
			
			/*
			 *  I could instead write a method to change the New Location for tidier code
			 */
		}



		if (tile instanceof PressurePad){
			PressurePad pad = (PressurePad) tile;
			pad.activate();
			Point door = level.getDoorFromPad(newLoc);
			System.out.println("door point = " + door);
			Door doorTile = (Door)level.getTiles()[door.y][door.x];	
			doorTile.unlock();
		}
		
		
		
		// If made it to here return true because move is valid.
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
		
		
		
		if (interactWith.y < 0 || interactWith.y > level.getTiles().length-1 || interactWith.x < 0 || interactWith.x > level.getTiles()[0].length-1)
			return "temp - you are trying to interact with a tile outside the bounds of the game.";	
		
		
		
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
			
			// facingBoulder() true = There is Boulder in front of player (which is now removed from the list of boulders in the level)
			boolean facingBoulder = level.removeBoulder(new Boulder(interactWith));
			// If Boulder there pick it up
			// If no Boulder this section of code is irrelevant
			if(facingBoulder) {
				player.addBoulder();		// true - "You picked up a Boulder"	
				/*
				 *  IF pressure pad then call activate() on PressurePad in level so level can manage openeing closing doors
				 */
			}
		}
		else if (player.hasBoulder()) {
			// If there's a boulder in front of you, you can't drop your current boulder or pick another
			boolean facingBoulder = level.containsBoulder(new Boulder(interactWith));

			// Check if there's a player present on the tile you're trying to drop the boulder onto. If Player there you can't drop.
			boolean facingPlayer = level.playerAt(interactWith);
				
			if (tile instanceof PutDownOnable) {
				level.addBoulder(new Boulder(interactWith));
				player.dropBoulder();
				
				if (tile instanceof PressurePad) 
					((PressurePad)tile).activate();
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
		
//		if(tile instanceof Furniture && !(tile instanceof PressurePad)){	//doesn't include pressure pad
//			return Msgs.inspectUnmovable((Furniture) tile);
//		}
//		//if code reaches here, might be trying to inspect an empty tile (empty tile doesn't extend unmoveable) boulders and players (moveables) can be on empty tiles
//		for(Boulder b: level.getBoulders()){
//			if(b.getLocation().equals(interactWith)){
//				if(tile instanceof PressurePad){
//					return Msgs.inspectMoveable(b, true);
//				}
//				return Msgs.inspectMoveable(b, false);
//			}
//		}
//		//check both players in list, if one is present on the tile you're inspecting then return a string that says so
//		for(Player p: this.players){
//			if(p.getLocation().equals(interactWith)){
//				if(tile instanceof PressurePad){
//					return Msgs.inspectMoveable(p, true);
//				}
//				return Msgs.inspectMoveable(p, false);
//			}
//		}
//		
//		//if code reaches here, pressure pad can't contain a moveable object and so return message stating it's a pressure pad
//		if(tile instanceof PressurePad){
//			return Msgs.inspectPressurePad();
//		}
		return "BEN TURNED OFF INSPECT TEMPORARILY";
	}

	
	/*
	 *  // TODO need to add some sort of message for changing level
	 */
	private boolean moveNextDoor(Player p) {
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
	
	private boolean movePrevDoor(Player p) {
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
				l.activateSpikes();
			}
		}
		
		
	}
	
	
	
	private void moveNextLevel() {
		// TODO Auto-generated method stub
		
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
