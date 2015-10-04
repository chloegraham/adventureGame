package gameWorld;

import java.awt.Point;

import movable.Boulder;
import movable.Item;
import movable.Key;
import movable.Player;
import serverclient.GameState;
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

	private Level level;
	private Level222 level2;
	
	public GameLogic() {
		this.level = Level.parseLevel("level1.txt");
	}
	public GameLogic(Level222 level2){
		this.level2 = level2;
	}
	
	public String handleAction(int ordinal, int userID){
		
		String message = "";
		//needs to handle userID
		Point current = level.getPlayer().getLocation();
		if(Actions.NORTH.ordinal() == ordinal){
			level.getPlayer().setDirection(Direction.NORTH);	
			boolean success = move(level.getPlayer(), new Point(current.x, current.y-1));
			message = ConvertAction.moveMsg(level.getPlayer().getDirection(), success, 1);
		}
		else if (Actions.EAST.ordinal() == ordinal){
			level.getPlayer().setDirection(Direction.EAST);
			boolean success = move(level.getPlayer(), new Point(current.x+1, current.y));
			message = ConvertAction.moveMsg(level.getPlayer().getDirection(), success, 1);
		}
		else if (Actions.SOUTH.ordinal() == ordinal){
			level.getPlayer().setDirection(Direction.SOUTH);
			boolean success = move(level.getPlayer(), new Point(current.x, current.y+1));
		}
		else if (Actions.WEST.ordinal() == ordinal){
			level.getPlayer().setDirection(Direction.WEST);
			boolean success = move(level.getPlayer(), new Point(current.x-1, current.y));
			message = ConvertAction.moveMsg(level.getPlayer().getDirection(), success, 1);
	   }
		else if (Actions.INTERACT.ordinal() == ordinal){ 
		  message = interact(level.getPlayer(), current);
		}
		
		/*else if (Actions.INSPECT.ordinal() == ordinal){ 
			inspect(level.getPlayer(), current);
			}*/
		
		//key for messages, find what's in front of player and return description
		return message;
	}
	
	private boolean move(Player player, Point newLoc) {
		
		//check for out of bounds
		if (newLoc.y < 0 || newLoc.y > level.getTiles().length-1 || newLoc.x < 0 || newLoc.x > level.getTiles()[0].length-1){
			return false;
		}	
		Tile tile = level.getTiles()[newLoc.y][newLoc.x];
		for(Boulder b: level.getBoulders()){
			if(b.getLocation().equals(newLoc)){
				System.out.println("OOPS DON't HIT MR BOUL");
				return false;
			}
		}
		if (tile instanceof Chest || tile instanceof Wall || tile instanceof Door && ((Door)tile).isLocked()){
			return false;
		} //activate pressure pad if moved onto one
		else if (tile instanceof PressurePad){
			((PressurePad)tile).activate();
			if(((PressurePad)tile).getDoor()!=null){
				((PressurePad)tile).getDoor().openWithPad();
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
	
	private String interact(Player p, Point now) {
		
		Direction direction = p.getDirection();
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
			throw new RuntimeException();
		}
		if (interactWith.y < 0 || interactWith.y > level.getTiles().length-1 || interactWith.x < 0 || interactWith.x > level.getTiles()[0].length-1){
			System.out.println("Stay inside bounds pls");
			return "Stay inside bounds";
		}	
		Tile tile = level.getTiles()[interactWith.y][interactWith.x];
		if (tile instanceof Chest){
			Key key = ((Chest)tile).openChest();
			if(key != null){
				p.addToInventory(key);
				return ConvertAction.keyMsg();
			}
			return ConvertAction.openMsg(true, true); //did open, is a chest 
		} 
		else if (tile instanceof Door){
			((Door) tile).openDoor(p.getKey());//boolean success =
			return ConvertAction.openMsg(true, false); //did open(success), is a door
		}
		
		if (p.containsBoulder()){
			if(tile instanceof EmptyTile || tile instanceof PressurePad){
				System.out.println("Trying to drop a boulder on an empty tile or pressure pad..");
				for(Item i: level.getPlayer().getInventory()){
					if(i instanceof Boulder){
						if(level.getTiles()[interactWith.y][interactWith.x] instanceof EmptyTile || level.getTiles()[interactWith.y][interactWith.x] instanceof PressurePad){
							for (Boulder j : level.getBoulders()){
								if (j.getLocation().equals(interactWith)){
									System.out.println("Boulders don't do incest");
									return "boulder";
								}
							}
							if(tile instanceof PressurePad){
								((PressurePad) tile).activate();
							}
							((Boulder) i).setLocation(interactWith);
							level.getBoulders().add((Boulder) i);
							level.getPlayer().removeBoulder();
							System.out.println("dropped boulder");
							return "boulder";
						}
					}
				}
				System.out.println("shame you aint got no boulder bitch");
			}
		}
		
		if (p.containsBoulder()){
			return "boulder";
		} else {
			//pick up boulder if one is in front of player
			for(Boulder b: level.getBoulders()){
				if(b.getLocation().equals(interactWith)){
					System.out.println("picking up boulder, id = " + b.getId());
					level.getPlayer().addToInventory(b);
					//now remove the boulder from the list so that it can't be redrawn/picked up again
					level.getBoulders().remove(b);
					return "drop boulder";
				}
			}
		}	
		//level.getPlayer().testInventory();
		return "false interact";
	}
	
	public String getMessage(){
		return "MUMBLEWORLD";
	}
	
	@SuppressWarnings("unused")
	private void violence(Player player2) {
		System.out.println("HHA U DIED");		
	}
	
	public GameState getGameWorld2(){	
		//return new GameState(getNumberOfKeys(), getMessage(), level.getStaticLevel(), level.getStateLevel(), level.getMoveableLevel());
		return new GameState(level.getStaticLevel(), level.getStateLevel(), level.getMoveableLevel());
	}
	
	public char[][] getGameWorld(){
		return level.getCharArray();
	}	
}
