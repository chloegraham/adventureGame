package gameWorld;

import java.awt.Point;

import userinterface.Action.Actions;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.Tile;

public class GameLogic {
	private Tile[][] tiles;
	Player player;
	Level level;
	
	public GameLogic() {
		this.level = new Level(this);
		this.player = new Player(new Point(2, 2));
		this.tiles = level.getTiles();
	}

	public void handleAction(int ordinal, int userID){
		
		Point current = player.getMyLocation();
		if(Actions.NORTH.ordinal() == ordinal && player.getMyLocation().getY() > 0){ move(player, new Point(current.x, current.y-1)); }
		else if (Actions.EAST.ordinal() == ordinal && player.getMyLocation().getX() < tiles.length){ move(player, new Point(current.x+1, current.y)); }
		else if (Actions.SOUTH.ordinal() == ordinal && player.getMyLocation().getY() < tiles[0].length-1){ move(player, new Point(current.x, current.y+1)); }
		else if (Actions.WEST.ordinal() == ordinal && player.getMyLocation().getX() > 0 ){ move(player, new Point(current.x-1, current.y)); }
		else if (Actions.INTERACT.ordinal() == ordinal){ interact(player); }
	}
	
	private boolean move(Player player, Point newLoc) {
		
		Tile tile = tiles[newLoc.y][newLoc.x];
		if(tile instanceof EmptyTile){
			player.setMyLocation(newLoc);
			return true;
		}
		return false;
	}
	
	private boolean interact(Player p) {
		Point now = p.getMyLocation();
		String direction = p.getDirection();
		Point interactWith;
		switch(direction){
		case "North":
			interactWith = new Point(now.x, now.y+1);
			break;
		case "South":
			interactWith = new Point(now.x, now.y-1);
			break;
		case "East":
			interactWith = new Point(now.x+1, now.y);
			break;
		default:
			interactWith = new Point(now.x-1, now.y);
			break;
		}
		Tile tile = tiles[interactWith.y][interactWith.x];
		if (tile instanceof Chest){
			openChest((Chest)tile, p);
			return true;
		} 
		if (tile instanceof Door){
			if (openDoor((Door) tile, p) == true){
				tiles[interactWith.y][interactWith.x] = new EmptyTile();
				return true;
			}
		} 
		return false;
	}
	
	private boolean openDoor(Door tile, Player p) {
		if (p.getInventory().size() > 0){
			tile.openDoor();
			return true;
		}
		return false;
	}

	private void openChest(Chest tile, Player p) {
		if (tile.getState() == true){
			tile.emptyChest();
			p.addToInventory(tile.getKey());
			p.testInventory();
		}		
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
}
