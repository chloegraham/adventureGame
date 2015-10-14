package server.gameWorld;

import java.awt.Point;
import java.util.List;

import server.helpers.Actions;
import server.helpers.Direction;
import server.helpers.Msgs;
import server.helpers.TileConnections;
import server.helpers.TileFullLocation;
import server.movable.Boulder;
import server.movable.Player;
import server.tiles.Chest;
import server.tiles.Door;
import server.tiles.DoorNormal;
import server.tiles.Furniture;
import server.tiles.Passable;
import server.tiles.PressurePad;
import server.tiles.PutDownOnable;
import server.tiles.Spikes;
import server.tiles.Tile;

public class GameLogic {
	private GameWorld gameWorld;
	private List<Stage> stages;
	private List<Player> players;
	
	public GameLogic(GameWorld gameWorld, List<Stage> stages, List<Player> players){
		this.gameWorld = gameWorld;
		this.stages = stages;
		this.players = players;
	}
	
	
	public String handleAction(int ordinal, int userID) {
		// absdjkfasj
		Player player = null;
		for (Player p : players){
			if (userID == p.getUserID()){
				player = p;
			}
		}
		
		assert(player != null);
		
		
		// ansjfknsadjkf
		Stage stage = null;
		for (Stage s : stages)
			if (s.getStageID() == player.getStageID())
				stage = s;
		
		assert(stage != null);
		
		
		// sajndfjaskd
		Room room = null;
		for (Room r : stage.getRooms())
			if (r.getRoomID() == player.getRoomID())
				room = r;
		
		assert(room != null);
		
		
		String message = "";
		Point current = player.getLocation();
		Point newLocation = null;
		
		if(Actions.NORTH.ordinal() == ordinal) {
			player.setDirection(Direction.NORTH);
			newLocation = new Point(current.x, current.y-1);
			boolean success = move(player, room, newLocation);
			message = Msgs.moveMsg(player.getDirection(), success);
			
		}
		else if (Actions.EAST.ordinal() == ordinal) {
			player.setDirection(Direction.EAST);
			newLocation = new Point(current.x+1, current.y);
			boolean success = move(player, room, newLocation);
			message = Msgs.moveMsg(player.getDirection(), success);
			
		}
		else if (Actions.SOUTH.ordinal() == ordinal) {
			player.setDirection(Direction.SOUTH);
			newLocation = new Point(current.x, current.y+1);
			boolean success = move(player, room, newLocation);
			message = Msgs.moveMsg(player.getDirection(), success);
			
		}
		else if (Actions.WEST.ordinal() == ordinal) {
			player.setDirection(Direction.WEST);
			newLocation = new Point(current.x-1, current.y);
			boolean success = move(player, room, newLocation);
			message = Msgs.moveMsg(player.getDirection(), success);
			
		}
		else if (Actions.INTERACT.ordinal() == ordinal) { 
			message = interact(player, room, current);
			
		}
		else if (Actions.INSPECT.ordinal() == ordinal) { 
			message = inspect(player, room, current);
			
			
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
		
		for (Player p : players){
			//CHECK IF EITHER PLAYERS ARE DEAD and return this to the server
			if(p.isDead()){
				return "You're dead";
			}
		}
		return message;
	}
	
	private boolean move(Player player, Room room, Point nextLoc) {
		
		// Check for out of bounds
		if (nextLoc.y < 0 || nextLoc.y > room.getTiles().length-1 || nextLoc.x < 0 || nextLoc.x > room.getTiles()[0].length-1)
			return false;
			
		Tile nextTile = room.getTiles()[nextLoc.y][nextLoc.x];
		Point currLoc =  player.getLocation();
		Tile currTile = room.getTiles()[currLoc.y][currLoc.x];
		
		// Check Boulders because Players can't move on to Boulders
		for(Boulder b: room.getBoulders())
			if(b.getLocation().equals(nextLoc))
				return false;	
		
		// If Tile is not type Passable = can't move on to.
		// If Tile is type Passable but state is !isPassable() = can't move on to.
		if (!(nextTile instanceof Passable) || (nextTile instanceof Passable && !((Passable)nextTile).isPassable()))
			return false;
		
		if(currTile instanceof PressurePad){
			((PressurePad)currTile).activate();
			
			TileFullLocation d = TileConnections.getConnectedTile(player.getStageID(), player.getRoomID(), currLoc);
			
			if (d != null) {
				Point doorPoint = d.getLocation();
				Door doorTile = (Door)room.getTiles()[doorPoint.y][doorPoint.x];
				
				doorTile.lock();
				
				//if there's a player in the doorway when getting off the activated pressure pad then kill them
				for(Player p : players)
					if(p.getLocation().equals(doorPoint))
						p.murder();
			}
		}
		
		
		
		if (nextTile instanceof PressurePad) {
			((PressurePad)nextTile).activate();
			
			TileFullLocation d = TileConnections.getConnectedTile(player.getStageID(), player.getRoomID(), nextLoc);
			
			if (d != null) {
				Point doorPoint = d.getLocation();
				Door doorTile = (Door)room.getTiles()[doorPoint.y][doorPoint.x];	
			
				doorTile.unlock();
			}
		}
		
		// If Tile is a Door
		// The Passable check above ensure LevelDoor is also Passable
		if (nextTile instanceof Door) {
			if ( ((Passable)nextTile).isPassable() ) {
				TileFullLocation d = TileConnections.getConnectedTile(player.getStageID(), player.getRoomID(), nextLoc);
				gameWorld.removePlayers();
				player.setLocation(d.getStage(), d.getRoom(), d.getLocation());
				gameWorld.addPlayersToRooms();
				
				// TODO I think this fucks up messages
				return true;
			}
		}
		
		
		
		// If made it to here return true because move is valid.
		return player.setLocation(nextLoc);		
	}
		
	
	private String interact(Player player, Room room, Point now) {
		
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
		
		
		
		if (interactWith.y < 0 || interactWith.y > room.getTiles().length-1 || interactWith.x < 0 || interactWith.x > room.getTiles()[0].length-1){
			if(player.hasBoulder()){
				return "DON'T CHUCK YOUR BABY OVERBOARD!";
			} else {
				return "You are trying to interact with a tile outside the bounds of the game.";
			}	
		}
			
		Tile interactTile = room.getTiles()[interactWith.y][interactWith.x];	
		
		
		/*
		 *  Chest interaction or inspection code & messages
		 */
		if (interactTile instanceof Chest){

			Chest chest = (Chest) interactTile;
			
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
		if (interactTile instanceof DoorNormal){
			
			DoorNormal doorNormal = (DoorNormal) interactTile;
			
			// ISLOCKED
			// true - "The Door is locked"
			// false - "The Door is unlocked. You may enter." RETURN
			boolean isLocked = doorNormal.isLocked();
			
			// HASKEY
			// true - [move on to try unlock the door]
			// false - "you don't have a key so you can't unlock the door. go find one."
			boolean hasKey = player.hasKey();
			
			if (isLocked) {
				if (hasKey) {
					doorNormal.unlock();
					player.useKey();
				}	
			}
			
			return Msgs.doorMsg(isLocked, hasKey);
		}
		
		
		
		/*
		 *  Boulder interaction code & message
		 */
		boolean hadBoulder = player.hasBoulder();
		boolean isBoulderRelavent = false;
		boolean infrontBoulder = false;
		boolean emptyORpressure = false;
		
		if (!player.hasBoulder()) {
			
			// facingBoulder() true = There is Boulder in front of player (which is now removed from the list of boulders in the level)
			boolean facingBoulder = room.removeBoulder(new Boulder(interactWith));
			// If Boulder there pick it up
			// If no Boulder this section of code is irrelevant
			if(facingBoulder) {
				isBoulderRelavent = true;
				player.addBoulder();		// true - "You picked up a Boulder"	
				if(interactTile instanceof PressurePad){
					
					((PressurePad)interactTile).activate();			
					TileFullLocation d = TileConnections.getConnectedTile(player.getStageID(), player.getRoomID(), interactWith);
					
					if (d != null) {
						Point doorPoint = d.getLocation();
						Door doorTile = (Door)room.getTiles()[doorPoint.y][doorPoint.x];
						
						doorTile.lock();
						
						//if there's a player in the doorway when getting off the activated pressure pad then kill them
						for(Player p : players){
							if(p.getLocation().equals(doorPoint)){
								p.murder();
							}
						}
					}
				}
			}
		}
		else if (player.hasBoulder()) {
			
			// If there's a boulder in front of you, you can't drop your current boulder or pick another
			if(room.containsBoulder(new Boulder(interactWith))){
				return "Don't pile yo eggs..";
			}
			// Check if there's a player present on the tile you're trying to drop the boulder onto. If Player there you can't drop.
			if(room.playerAt(interactWith)){
				return "Don't squish your friend silly CHICKIE";
			}

			if (interactTile instanceof PutDownOnable) {

				room.addBoulder(new Boulder(interactWith));
				player.dropBoulder();
				
				if (interactTile instanceof PressurePad) {
					
					((PressurePad)interactTile).activate();
					TileFullLocation d = TileConnections.getConnectedTile(player.getStageID(), player.getRoomID(), interactWith);
					if (d != null) {
						Point doorPoint = d.getLocation();
						Door doorTile = (Door)room.getTiles()[doorPoint.y][doorPoint.x];	
						doorTile.unlock();
					}
					
				}
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
	

	
	private String inspect(Player player, Room room, Point now) {
		
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
		
		if (interactWith.y < 0 || interactWith.y > room.getTiles().length-1 || interactWith.x < 0 || interactWith.x > room.getTiles()[0].length-1){
			return "Why are you trying to inspect a tile outside the bounds of the game? Jesus.";
		}	
		
		Tile tile = room.getTiles()[interactWith.y][interactWith.x];
		
		if(tile instanceof Furniture && !(tile instanceof PressurePad)){	//doesn't include pressure pad
			return Msgs.inspectUnmovable((Furniture) tile);
		}
		//if code reaches here, might be trying to inspect an empty tile (empty tile doesn't extend unmoveable) boulders and players (moveables) can be on empty tiles
		for(Boulder b: room.getBoulders()){
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
		return "BEN TURNED OFF INSPECT TEMPORARILY";
	}

	
	
	public String activateSpikes() {
		String temp = "";
		for (Stage s : stages){
			for (Room r : s.getRooms()){
				temp = r.activateSpikes();		
			}
		}
		return temp;
	}
	
	
	
	public String bouldersKeysLocation(int userID) {
		Player player = null;
		for (Player p : players)
			if (p.getUserID() == userID)
				player = p;
		
		assert(player != null);
		
		
		boolean hasBoulder = player.hasBoulder();
		int keyNumber = player.getNumberOfKeys();
		int playerX = player.getLocation().x;
		int playerY = player.getLocation().y;
		String bouldersKeysLocation = hasBoulder + Msgs.DELIM_DETAILS + keyNumber + Msgs.DELIM_DETAILS +
									  playerX + Msgs.DELIM_DETAILS + playerY + Msgs.DELIM_DETAILS +
									  Msgs.DELIM_SPLIT;
		return bouldersKeysLocation;
	}
}
