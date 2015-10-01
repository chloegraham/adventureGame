package gameWorld;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import movable.Boulder;
import movable.Item;
import movable.Key;
import userinterface.Action.Actions;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.PressurePad;
import tiles.Spikes;
import tiles.Tile;
import tiles.Unmoveable;
import tiles.Wall;

public class GameLogic {

	private Tile[][] tiles;
	private Tile[][] unmoveableTiles;
	private Tile[][] staticTiles;
	private Player player;
	private Level level;
	private Set<Boulder> boulders = new HashSet<Boulder>();
	
	public GameLogic() {
		this.level = Level.parseLevel("board.txt");
		this.tiles = level.getLevel();
		this.player = level.getPlayer();
		boulders.add(new Boulder(new Point(2,4), "i'm mr boul", "not so special"));
		//makeLayer3();
		//makeLayer1();
		//makeLayer2();
		//System.out.println(toString(tiles));
	}
	
	private void makeLayer3() {
		
//		this.moveableTiles = new Tile[this.tiles.length][this.tiles[0].length];
//		for (int i = 0; i < tiles.length; i++) {
//			for (int j = 0; j < tiles[0].length; j++) {
//				Tile temp = this.tiles[i][j];
//				if(temp instanceof Moveable){
//					moveableTiles[i][j] = temp;
//				}
//				else{
//					moveableTiles[i][j] = null;
//				}
//			}
//		}	
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
		
		//needs to handle userID
		Point current = this.player.getLocation();
		if(Actions.NORTH.ordinal() == ordinal){
			this.player.setDirection(Direction.NORTH);	
			move(player, new Point(current.x, current.y-1));
		}
		else if (Actions.EAST.ordinal() == ordinal){
			this.player.setDirection(Direction.EAST);
			move(player, new Point(current.x+1, current.y));
		}
		else if (Actions.SOUTH.ordinal() == ordinal){
			this.player.setDirection(Direction.SOUTH);
			move(player, new Point(current.x, current.y+1));
		}
		else if (Actions.WEST.ordinal() == ordinal){
			this.player.setDirection(Direction.WEST);
			move(player, new Point(current.x-1, current.y));
	   }
		else if (Actions.INTERACT.ordinal() == ordinal){ 
			interact(player, current);
			}
	}
	
	private boolean move(Player player, Point newLoc) {
		
		//check for out of bounds
		if (newLoc.y < 0 || newLoc.y > tiles.length-1 || newLoc.x < 0 || newLoc.x > tiles[0].length-1){
			return false;
		}	
		Tile tile = tiles[newLoc.y][newLoc.x];
		for(Boulder b: this.boulders){
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
		}
		else if (tile instanceof Spikes){
		//player can't walk onto spikes if active
		  if(((Spikes)tile).isActive()){
			  return false;
		  } 
		}
		return player.setLocation(newLoc);		
	}	
	
	private boolean interact(Player p, Point now) {
		
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
		if (interactWith.y < 0 || interactWith.y > tiles.length-1 || interactWith.x < 0 || interactWith.x > tiles[0].length-1){
			System.out.println("Stay inside bounds pls");
			return false;
		}	
		Tile tile = tiles[interactWith.y][interactWith.x];
		if (tile instanceof Chest){
			Key key = ((Chest)tile).getKey();
			((Chest)tile).setCharacter();
			if(key != null){
				p.addToInventory(key);
			}
		} 
		else if (tile instanceof Door){
			((Door) tile).openDoor(p.getKey());
		}
		
		if (p.containsBoulder()){
			if(tile instanceof EmptyTile || tile instanceof PressurePad){
				System.out.println("Trying to drop a boulder on an empty tile or pressure pad..");
				for(Item i: player.getInventory()){
					if(i instanceof Boulder){
						if(tiles[interactWith.y][interactWith.x] instanceof EmptyTile || tiles[interactWith.y][interactWith.x] instanceof PressurePad){
							for (Boulder j : this.boulders){
								if (j.getLocation().equals(interactWith)){
									System.out.println("Boulders don't do incest");
									return false;
								}
							}
							((Boulder) i).setLocation(interactWith);
							boulders.add((Boulder) i);
							player.removeBoulder();
							System.out.println("dropped boulder");
							return true;
						}
					}
				}
				System.out.println("shame you aint got no boulder bitch");
			}
		}
		
		if (p.containsBoulder()){
			return false;
		} else {
			//pick up boulder if one is in front of player
			for(Boulder b: this.boulders){
				if(b.getLocation().equals(interactWith)){
					System.out.println("picking up boulder, id = " + b.getId());
					player.addToInventory(b);
					//now remove the boulder from the list so that it can't be redrawn/picked up again
					boulders.remove(b);
					return true;
				}
			}
		}	
		player.testInventory();
		return false;
	}
	
	@SuppressWarnings("unused")
	private void violence(Player player2) {
		System.out.println("HHA U DIED");		
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
	
	private char[][] convertToChar(Tile[][] tileArray){
		
		char[][] newArray = new char[tileArray.length][tileArray[0].length];
		for (int i = 0; i < tileArray.length; i++) {
			for (int j = 0; j < tileArray[0].length; j++) {
				newArray[i][j] = tileArray[i][j].toString().charAt(0);
			}
		}	
		//overwrite char in array with boulder
		for(Boulder b: this.boulders){
			Point boulderPoint = b.getLocation();
			newArray[boulderPoint.y][boulderPoint.x] = b.toString().charAt(0);
		}
		Point myPoint = this.player.getLocation();
		//overwrite char in array with player
		newArray[myPoint.y][myPoint.x] = this.player.toString().charAt(0);
		return newArray;
	}
	
	public char[][] getGameWorld(){
		char[][] newArray = new char[tiles.length][tiles[0].length];
		newArray = convertToChar(this.tiles);
		return newArray;
	}	
}
