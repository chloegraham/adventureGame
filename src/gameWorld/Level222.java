package gameWorld;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import movable.Boulder;
import movable.Player;
import serverclient.LevelState;
import testconvert.ConvertPlayer;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.Tile;
import tiles.Unmoveable;
import tiles.Wall;



public class Level222 {
	
	private int width;
	private int height;	
	private Player player;
	private Tile[][] tiles;
	private Set<Boulder> boulders;
	
	public Level222(LevelState game) {
		setupTiles(game.getLevel());
		
		buildLevel(game.getLevel());
		buildObjects(game.getObjects());
		boulders = new HashSet<>();
		buildMovables(game.getMovables());
		buildPlayers(game.getEncodedPlayers());
	}

	public Player getPlayers(){	return this.player; }
	public Tile[][] getTiles(){ return this.tiles; }
	public Set<Boulder> getBoulders(){ return this.boulders;}
	
	
	private void setupTiles(char[][] level) {
		width = level[0].length;
		height = level.length;
		tiles = new Tile[height][width];
	}
	
	private void buildLevel(char[][] level) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				String temp = Character.toString((level[y][x]));
				if(temp.equals("e")){
					tiles[y][x] = new EmptyTile();
				}
				else if(temp.equals("w")) {
					tiles[y][x] = new Wall();
				}
				else if(temp.equals("n")) {
					throw new RuntimeException("Found 'n' while building a level.");
				}
				else
					throw new RuntimeException();
			}
		}
	}
	
	private void buildObjects(char[][] objects) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				String temp = Character.toString((objects[y][x]));
				if(temp.equals("d")){
					tiles[y][x] = new Door();
				}
				else if(temp.equals("c")) {
					tiles[y][x] = new Chest();
				}
				else if(temp.equals("p")){
					tiles[y][x] = new Door();
				}
				else if(temp.equals("s")) {
					tiles[y][x] = new Chest();
				}
				else if(temp.equals("n")) {
					throw new RuntimeException("Found 'n' while building objects of a level.");
				}
				else
					throw new RuntimeException();
			}
		}
	}
	
	private void buildMovables(char[][] movables) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				String temp = Character.toString((movables[y][x]));
				if(temp.equals("b"))
					boulders.add(new Boulder(new Point(x, y), "b"));
				else
					System.out.println(temp);
			}
		}
	}
	
	private void buildPlayers(String players) {
		player = ConvertPlayer.toPlayer(players);
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