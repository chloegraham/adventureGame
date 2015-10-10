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
		}
		else if (Actions.EAST.ordinal() == ordinal) {
			player.setDirection(Direction.EAST);
			newLocation = new Point(current.x+1, current.y);
		}
		else if (Actions.SOUTH.ordinal() == ordinal) {
			player.setDirection(Direction.SOUTH);
			newLocation = new Point(current.x, current.y+1);
		}
		else if (Actions.WEST.ordinal() == ordinal) {
			player.setDirection(Direction.WEST);
			newLocation = new Point(current.x-1, current.y);
	   }
		else if (Actions.INTERACT.ordinal() == ordinal) { 
			message = interact(player, level, current);
		}
		else if (Actions.NEWGAME.ordinal() == ordinal) {
		}
		else if (Actions.LOAD.ordinal() == ordinal) {
	    }
		else {
			throw new IllegalArgumentException("GameLogic:  received an unexpected ordinal. It might be 'Inspect' which we have't coded yet.");
		}
		
		// Only for Move not Interact
		if (ordinal == Actions.NORTH.ordinal() || ordinal == Actions.SOUTH.ordinal() || ordinal == Actions.WEST.ordinal() || ordinal == Actions.EAST.ordinal()) {
			boolean success = move(player, level, newLocation);
			message = Msgs.moveMsg(player.getDirection(), success);
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
		
		
		
		if (tile instanceof PressurePad){	//activate pressure pad if moved onto one
			((PressurePad)tile).activate();
			//TODO: THIS IS TEMPORARY LOGIC
			Tile[][]doors = level.getTiles();
			for (int i = 0; i < doors.length; i++) {
				for (int j = 0; j < doors[0].length; j++) {
					if(doors[i][j] instanceof Door){
						if(!((PressurePad)tile).isActivated()){
							((Door)doors[i][j]).unlock();
						} else {
							((Door)doors[i][j]).lock();
						}
					}
				}
			}
		}
		else if (tile instanceof Spikes){
			((Spikes)tile).activate();
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
		 *  Chest interaction code & messages
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
		
		// BOULDER SITUATIONS
		
		if (!player.hasBoulder()) {
			boolean facingBoulder = level.removeBoulder(new Boulder(interactWith));
			if (!facingBoulder)
				return "";
			player.addBoulder();
			return "temp pickup a boulder";
		}
			
		if (player.hasBoulder()) {
			boolean facingBoulder = level.containsBoulder(new Boulder(interactWith));
			if (facingBoulder) {
				return "you already have a boulder and can't pick up another";
			}
			if (tile instanceof EmptyTile) {
				level.addBoulder(new Boulder(interactWith));
				player.dropBoulder();
				return "you dropped a boulder on an empty tile";
			}
			if (tile instanceof PressurePad) {
				level.addBoulder(new Boulder(interactWith));
				player.dropBoulder();
				PressurePad pad = (PressurePad) tile;
				pad.activate();
				return "you dropped a boulder on a pressurepad";
			}
		}
			
		// standing facing a boulder
		// HASBOULDER 
		// true - "you already have a Boulder, you can't pick up another" [do nothing]
		// false - "You picked up a boulder" [pick up boulder]
		
		
		// standing facing a wall
		// HASBOULDER
		// true -  "you can't put a boulder in a wall / you have no room to put boulder down" [do nothing]
		// false - [do nothing]
		
		
		// standing facing empty tile
		// HASBOULDER
		// true - "you put a boulder down" [put boulder down]
		// false - [do nothing]
		
		
		// can only put boulder down on:
		// empty, pressure pad
		
		return "temp - tried to interact but came up short - ERROR in game logic";
	}
	
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
