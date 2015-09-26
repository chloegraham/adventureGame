package gameWorld;

import java.awt.Point;

import character.Player;
import userinterface.Action;
import userinterface.Action.Actions;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.Tile;

public class GameLogic {
	private Tile[][] tiles;
	Player player;
	Level level;
	
	public GameLogic(Tile[][] tiles, Player p, Level level) {
		this.tiles = tiles;
		this.player = p;
		this.level = level;
	}


	public void handleAction(int ordinal, int userID){
		
		Point current = player.getMyLocation();
		if(Actions.NORTH.ordinal() == ordinal && player.getMyLocation().getY() > 0){ move(player, new Point(current.x, current.y-1)); }
		else if (Actions.EAST.ordinal() == ordinal && player.getMyLocation().getX() < tiles.length){ move(player, new Point(current.x+1, current.y)); }
		else if (Actions.SOUTH.ordinal() == ordinal && player.getMyLocation().getY() < tiles[0].length-1){ move(player, new Point(current.x, current.y+1)); }
		else if (Actions.WEST.ordinal() == ordinal && player.getMyLocation().getX() > 0 ){ move(player, new Point(current.x-1, current.y)); }
		//else if (Actions.INTERACT.ordinal() == ordinal){ interact(player); }
	}
		
	private boolean move(Player player, Point newLoc) {
		
		Tile tile = tiles[newLoc.y][newLoc.x];
		if(tile instanceof EmptyTile){
			player.setMyLocation(newLoc);
			return true;
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
}
