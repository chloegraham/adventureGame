package gameWorld;

import java.awt.Point;

import movable.Key;
import movable.Moveable;
import userinterface.Action.Actions;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.PressurePad;
import tiles.Spikes;
import movable.PlayerTile;
import tiles.Tile;
import tiles.Unmoveable;
import tiles.Wall;

public class GameLogic {

	private Tile[][] tiles;
	private Tile[][] moveableTiles;
	private Tile[][] unmoveableTiles;
	private Tile[][] staticTiles;
	private Player player;
	private Level level;
	private Tile[][] staticBoard = new Tile[5][5];
	
	public GameLogic() {
	//	this.level = Level.parseLevel("board.txt");
		this.level = new Level(5, 5);
		this.tiles = level.getLevel();
		for (int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				this.staticBoard[i][j] = this.tiles[i][j];
			}
		}
		//System.out.println(Level.toStringss());
		this.player = level.getPlayer();
		makeLayer3();
		makeLayer1();
		makeLayer2();
		//System.out.println(toString(staticTiles));
	}
	
	private void makeLayer3() {
		
		this.moveableTiles = new Tile[this.tiles.length][this.tiles[0].length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				Tile temp = this.tiles[i][j];
				if(temp instanceof Moveable){
					moveableTiles[i][j] = temp;
				}
				else{
					moveableTiles[i][j] = null;
				}
			}
		}	
	}
	
	private void makeLayer2() {
		
		this.unmoveableTiles = new Tile[this.tiles.length][this.tiles[0].length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				Tile temp = this.tiles[i][j];
				if(temp instanceof Unmoveable){
					unmoveableTiles[i][j] = temp;
				}
				else{
					unmoveableTiles[i][j] = null;
				}
			}
		}	
	}

	private void makeLayer1() {
		
		this.staticTiles = new Tile[this.tiles.length][this.tiles[0].length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				Tile temp = this.tiles[i][j];
				if(temp instanceof EmptyTile || temp instanceof Wall){
					staticTiles[i][j] = temp;
				}
				else{
					staticTiles[i][j] = new EmptyTile();
				}
			}
		}	
		
	}

	public void handleAction(int ordinal, int userID){
		String direction = this.player.getDirection();
		Point current = this.player.getMyLocation();
		if(Actions.NORTH.ordinal() == ordinal && player.getMyLocation().getY() > 0){
			this.player.setDirection("North");	
			move(player, new Point(current.x, current.y-1));
		}
		else if (Actions.EAST.ordinal() == ordinal && player.getMyLocation().getX() < tiles.length){
			if(player.getDirection().equals("East")){
				move(player, new Point(current.x+1, current.y));
			} else {
				this.player.setDirection("East");
			}
		}
		else if (Actions.SOUTH.ordinal() == ordinal && player.getMyLocation().getY() < tiles[0].length-1){
			if(player.getDirection().equals("South")){
				move(player, new Point(current.x, current.y+1));
			} else {
				this.player.setDirection("South");
			}
		}
		else if (Actions.WEST.ordinal() == ordinal && player.getMyLocation().getX() > 0 ){
			if(player.getDirection().equals("West")){
				move(player, new Point(current.x-1, current.y));
			} else {
				this.player.setDirection("West");
		
			}
	   }
		else if (Actions.INTERACT.ordinal() == ordinal){ 
			interact(player, current);
			}
		if(ordinal != 99) {System.out.println(player.getDirection());}
	}
	
	private boolean move(Player player, Point newLoc) {

		Tile tile = tiles[newLoc.y][newLoc.x];
		Tile lastTile = staticBoard[player.getMyLocation().y][player.getMyLocation().x];
		//if the last tile is a spike and is now active, kill the player lol
		if(lastTile instanceof Spikes){
			if(((Spikes) lastTile).isActive()){
				violence(player);
			}
		}
		if(tile instanceof EmptyTile || (tile instanceof Door && !((Door)tile).isLocked())) {
			if(lastTile instanceof PressurePad){
				tiles[player.getMyLocation().y][player.getMyLocation().x] = new PressurePad();
				player.togglePressurePad();
			}
			else if(lastTile instanceof Spikes){
				tiles[player.getMyLocation().y][player.getMyLocation().x] = new Spikes();
			}
			else{
				tiles[player.getMyLocation().y][player.getMyLocation().x] = new EmptyTile();	
			}
			player.setMyLocation(newLoc);
			tiles[newLoc.y][newLoc.x] = new PlayerTile(this.player);			
			return true;
		}
		else if(tile instanceof PressurePad){
			player.togglePressurePad();
			if(lastTile instanceof Spikes){
				tiles[player.getMyLocation().y][player.getMyLocation().x] = new Spikes();
			} else {
				tiles[player.getMyLocation().y][player.getMyLocation().x] = new EmptyTile();		
			}
			player.setMyLocation(newLoc);		
			tiles[newLoc.y][newLoc.x] = new PlayerTile(this.player);			
			return true;	
		}
		else if(tile instanceof Spikes){
			if(((Spikes) tiles[player.getMyLocation().y][player.getMyLocation().x]).isActive()){
				System.out.println("can't walk onto active spike");
				return false;
			}
			else{
				if(lastTile instanceof PressurePad){
					tiles[player.getMyLocation().y][player.getMyLocation().x] = new PressurePad();
				} else {
					tiles[player.getMyLocation().y][player.getMyLocation().x] = new EmptyTile();		
				}
				player.setMyLocation(newLoc);		
				tiles[newLoc.y][newLoc.x] = new PlayerTile(this.player);
				player.toggleOnSpikes();
				return true;
			}	
		}
		return false;
	}
	
	
	private void violence(Player player2) {
		
		System.out.println("HHA U DIED");
		//TODO: reset game to default
		
	}

	public String toString(Tile[][] tile) {
		String rtn = "";
		for (int i = 0; i < tile.length; i++) {
			for (int j = 0; j < tile[i].length; j++) {
				try {
					rtn += tile[i][j] + " ";
				} catch (NullPointerException e) {
				}
			}
			rtn += "\n";
		}
		return rtn;
	}
	
	private boolean interact(Player p, Point now) {
		
		String direction = p.getDirection();
		Point interactWith;
		switch(direction){
		case "North":
			interactWith = new Point(now.x, now.y-1);
			break;
		case "South":
			interactWith = new Point(now.x, now.y+1);
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
			Key key = ((Chest)tile).getKey();
			if(key != null){
				p.addToInventory(key);
			}
		} 
		if (tile instanceof Door){
			((Door) tile).openDoor(p.getKey());
		} 
		return false;
	}
	
	private char[][] convertToChar(Tile[][] tileArray){
		
		char[][] newArray = new char[tileArray.length][tileArray[0].length];
		for (int i = 0; i < tileArray.length; i++) {
			for (int j = 0; j < tileArray[0].length; j++) {
				newArray[i][j] = tileArray[i][j].toString().charAt(0);
			}
		}	
		return newArray;
	}
	
	public char[][] getGameWorld(){
		char[][] newArray = new char[5][5];
		newArray = convertToChar(this.tiles);
		return newArray;
	}
	
	
	
	
	
	
}
