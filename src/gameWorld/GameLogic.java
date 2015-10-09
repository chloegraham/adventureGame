package gameWorld;

import java.awt.Point;

import movable.Boulder;
import movable.Item;
import movable.Key;
import movable.Player;
import testconvert.ConvertAction;
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
	
	
	public String handleAction(int ordinal, int userID){
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
		
		if(Actions.NORTH.ordinal() == ordinal){
			player.setDirection(Direction.NORTH);
			newLocation = new Point(current.x, current.y-1);
		}
		else if (Actions.EAST.ordinal() == ordinal){
			player.setDirection(Direction.EAST);
			newLocation = new Point(current.x+1, current.y);
		}
		else if (Actions.SOUTH.ordinal() == ordinal){
			player.setDirection(Direction.SOUTH);
			newLocation = new Point(current.x, current.y+1);
		}
		else if (Actions.WEST.ordinal() == ordinal){
			player.setDirection(Direction.WEST);
			newLocation = new Point(current.x-1, current.y);
	   }
		else if (Actions.INTERACT.ordinal() == ordinal){ 
			message = interact(player, level, current);
		}
		
		// Only for Move not Interact
		if (ordinal >= 6 && ordinal <= 9) {
			boolean success = move(player, level, newLocation);
			message = ConvertAction.moveMsg(player.getDirection(), success);
		}
		
		return message;
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
					moveNextLevel();
				} else {
					movePrevLevel();
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
							((Door)doors[i][j]).openWithPad();
						} else {
							((Door)doors[i][j]).closeWithPad();
						}
					}
				}
			}
		}
		else if (tile instanceof Spikes){
		//player can't walk onto spikes if active
		  /*if(((Spikes)tile).isActive()){
			  return false;
		  } */
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
			System.out.println("Stay inside bounds pls");
			return "Stay inside bounds";
		}	
		
		Tile tile = level.getTiles()[interactWith.y][interactWith.x];
		
		if (tile instanceof Chest){
			Key key = ((Chest)tile).openChest();
			if(key != null){
				player.addToInventory(key);
				return ConvertAction.openChestMsg(false, true, player.numberOfKeys());
			}
			return ConvertAction.openChestMsg(false, false, player.numberOfKeys());
		} 
		else if (tile instanceof Door){
			((Door) tile).openDoor(player.getKey());
			return ConvertAction.openDoorMsg(false, true, player.numberOfKeys());
		}
		
		if (player.containsBoulder()){
			if(tile instanceof EmptyTile || tile instanceof PressurePad){
				System.out.println("Trying to drop a boulder on an empty tile or pressure pad..");
				for(Item i: player.getInventory()){
					if(i instanceof Boulder){
						if(level.getTiles()[interactWith.y][interactWith.x] instanceof EmptyTile || level.getTiles()[interactWith.y][interactWith.x] instanceof PressurePad){
							for (Boulder j : level.getBoulders()){
								if (j.getLocation().equals(interactWith)){
									System.out.println("Boulders don't do incest");
									return ConvertAction.boulderMsg(false, player.containsBoulder());
								}
							}
							if(tile instanceof PressurePad){
								((PressurePad) tile).activate();
							}
							((Boulder) i).setLocation(interactWith);
							level.getBoulders().add((Boulder) i);
							player.removeBoulder();
							System.out.println("dropped boulder");
							return ConvertAction.boulderMsg(false, player.containsBoulder());
						}
					}
				}
				System.out.println("shame you aint got no boulder bitch");
			}
		}
		
		if (player.containsBoulder()){
			return "boulder";
		} else {
			//pick up boulder if one is in front of player
			for(Boulder b: level.getBoulders()){
				if(b.getLocation().equals(interactWith)){
					System.out.println("picking up boulder, id = " + b.getId());
					player.addToInventory(b);
					//now remove the boulder from the list so that it can't be redrawn/picked up again
					level.getBoulders().remove(b);
					return ConvertAction.boulderMsg(true, player.containsBoulder());
				}
			}
		}	
		return ConvertAction.inspectMsg();
	}
	
	private void moveNextLevel() {
		for (int i = 0; i != players.length; i++) {
			players[i].nextLevel();
		}
		for (int i = 0; i != levels.length; i++) {
			if (levels[i].getLevelID() == players[0].getLevelID())
				levels[i].addPlayers(players);
			else
				levels[i].removePlayers();
		}
	}
	
	private void movePrevLevel() {
		for (int i = 0; i != players.length; i++) {
			players[i].prevLevel();
		}
		for (int i = 0; i != levels.length; i++) {
			if (levels[i].getLevelID() == players[0].getLevelID())
				levels[i].addPlayers(players);
			else
				levels[i].removePlayers();
		}
	}
}
