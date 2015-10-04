package gameWorld;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.management.RuntimeErrorException;

import movable.Boulder;
import movable.Item;
import movable.Key;
import movable.Moveable;
import movable.Player;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.PressurePad;
import tiles.Spikes;
import tiles.Tile;
import tiles.Unmoveable;
import tiles.Wall;

public class Level {
	
	private int width;
	private int height;	
	private Player player;
	private Tile[][] tiles;
	private Set<Boulder> boulders;
	
	public Level(int height, int width, Scanner sc) {
		
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];
		this.boulders = new HashSet<Boulder>();
		setupTiles(sc);
		setupMovables();
		joinObjects();
		//System.out.println(tiles.toString());
	}

	private void joinObjects() {
		List<PressurePad> pads = new ArrayList<PressurePad>();
		List<Door> doors = new ArrayList<Door>();
		// Link pressure pads to their doors
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				Tile tile = tiles[i][j];
				if (tile instanceof PressurePad) {
					pads.add((PressurePad) tile);
				} else if(tile instanceof Door){
					doors.add((Door)tile);
				}
			}
		}
		for(PressurePad p: pads){
			if(doors.size() > 0){
				p.setDoor(doors.remove(doors.size()-1));
			}
		}
	}

	@SuppressWarnings("resource")
	public static Level parseLevel(String filename) {
		
		Scanner sc;
		//InputStream input = Board.class.getClassLoader().getResourceAsStream(filename);
		try {
			sc = new Scanner(new File(filename));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Dun dun dunnn, could not parse: File not found!!");
		}
		//sc.useDelimiter(";");		
		int width;
		int height;
		try{
			height = sc.nextInt();
			width = sc.nextInt();
			System.out.println(width + " - width ; " + height + " height" );
		} catch (RuntimeException e1){
			throw new RuntimeErrorException(
					null, "Couldn't parse dimensions of board");
		}
		Level level = new Level(width, height, sc);
		sc.close();	
		return level;
	}
	
	private void setupTiles(Scanner sc) {
		
		for (int i = 0; i < this.width; i++){
			for(int j = 0; j < this.height; j++){
				if(sc.hasNext()){
					String temp = sc.next();
					if(temp.equals("e")){
						tiles[i][j] = new EmptyTile();
					}
					else if(temp.equals("w")){
						tiles[i][j] = new Wall();
					}
					else if(temp.equals("d")){
						tiles[i][j] = new Door();
					}
					else if(temp.equals("c")){
						tiles[i][j] = new Chest();
					}
					else if(temp.equals("z")){
						tiles[i][j] = new PressurePad(); 
					}
					else if(temp.equals("s")){
						tiles[i][j] = new Spikes();
					}
				}
			}
		}
	}
	
	private void setupMovables() {
		this.player = new Player(new Point (2, 2));
		this.boulders.add(new Boulder(new Point(2,4), "i'm mr boul", "not so special"));
		//this.boulders.add(new Boulder(new Point(0,3), "i'm mr boul", "i'm a very special lady"));
	}
	
	public String toStringss() {
		String rtn = "";
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				try {
					rtn += tiles[i][j] + " ";
				} catch (NullPointerException e) {
				}
			}
			rtn += "\n";
		}
		return rtn;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public Tile[][] getTiles(){
		return this.tiles;
	}
	
	public Set<Boulder> getBoulders(){
		return this.boulders;
	}
	
	public char[][] getCharArray() {
		char[][] newArray = new char[tiles.length][tiles[0].length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				newArray[i][j] = tiles[i][j].toString().charAt(0);
			}
		}		
		//overwrite char in array with boulder
		for(Boulder b: this.boulders){
			Point boulderPoint = b.getLocation();
			newArray[boulderPoint.y][boulderPoint.x] = b.toString().charAt(0);
		}
		Point loc = this.player.getLocation();
		newArray[loc.y][loc.x] = this.player.toString().charAt(0);
		return newArray;
	}

	public char[][] getStaticLevel() {
		char[][] staticTiles = new char[this.tiles.length][this.tiles[0].length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				Tile temp = this.tiles[i][j];
				if(temp instanceof EmptyTile || temp instanceof Wall){
					staticTiles[i][j] = temp.toString().charAt(0);
				}
				else{
					staticTiles[i][j] = 'e';
				}
			}
		}
		return staticTiles;
	}

	public char[][] getStateLevel() {
		
		char[][] unmoveableTiles = new char[this.tiles.length][this.tiles[0].length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				Tile temp = this.tiles[i][j];
				if(temp instanceof Unmoveable){
					unmoveableTiles[i][j] = temp.toString().charAt(0);
				}
				else{
					unmoveableTiles[i][j] = 'n';
				}
			}
		}	
		return unmoveableTiles;
	}

	public char[][] getMoveableLevel() {
		
		char[][] moveableTiles = new char[this.tiles.length][this.tiles[0].length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				moveableTiles[i][j] = 'n';
			}
		}	
		for (Boulder b : boulders) {
			Point p = b.getLocation();
			moveableTiles[p.y][p.x] = b.toString().charAt(0);
		}
		Point p = player.getLocation();
		moveableTiles[p.y][p.x] = player.toString().charAt(0);
		return moveableTiles;
	}
}