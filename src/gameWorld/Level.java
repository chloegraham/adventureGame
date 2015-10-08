package gameWorld;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import movable.Boulder;
import movable.Player;
import serverclient.LevelState;
import testconvert.Messages;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.PressurePad;
import tiles.Spikes;
import tiles.Tile;
import tiles.Unmoveable;
import tiles.Wall;

public class Level {
	private int levelID;
	private int width;
	private int height;	
	
	private Tile[][] tiles;
	private Set<Boulder> boulders;
	private Player[] players;
	
	public Level(String encodedLevel) {
		// Split String up in to x3 Strings which will be converted to char[][]
		String[] layers = encodedLevel.split("@");
			
		// Split up the 2d-char[][] in to 1d-char[] (they are still Strings at the moment)
		String[] subLayers1 = layers[0].split("%");
		String[] subLayers2 = layers[1].split("%");
		String[] subLayers3 = layers[2].split("%");
		levelID = Integer.parseInt(layers[3]);
			
		char[][] level;
		char[][] objects;
		char[][] movables;
		
		// Now build the actual 2d-char[][] from the broken down Strings
		level = new char[subLayers1.length][];
		for (int x = 0; x < level.length; x++)
			level[x] = subLayers1[x].toCharArray();
		
		objects = new char[subLayers2.length][];
		for (int x = 0; x < objects.length; x++)
			objects[x] = subLayers2[x].toCharArray();
		
		movables = new char[subLayers3.length][];
		for (int x = 0; x < movables.length; x++)
			movables[x] = subLayers3[x].toCharArray();
		
		setupTiles(level);
		
		buildLevel(level);
		buildObjects(objects);
		buildMovables(movables);
	}
	
	
	/*
	 *  Getter LevelID
	 */
	public int getLevelID() {
		return levelID;
	}
	
	
	/*
	 *  Getters for GameLogic
	 */
	public Tile[][] getTiles() { return tiles; }
	public Set<Boulder> getBoulders(){ return boulders;}
	
	
	/*
	 *  Getter for GameWorld
	 */
	public String getEncodedLevel() {
		char[][] level = getLevel();
		char[][] objects = getObjects();
		char[][] movables= getMovables();
		
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < level.length; x++) {
			sb.append(level[x]);
			sb.append('%');
		}
		sb.append('@');
		
		for (int x = 0; x < objects.length; x++) {
			sb.append(objects[x]);
			sb.append('%');
		}
		sb.append('@');
		
		for (int x = 0; x < movables.length; x++) {
			sb.append(movables[x]);
			sb.append('%');
		}
		sb.append('@');
		sb.append(levelID);
		sb.append('@');
		return sb.toString() +
			   Messages.DELIM_LEVEL +
			   Messages.DELIM_SPLIT;
	}
	
	
	/*
	 *  Getter to Remove Players from a Level
	 */
	public void removePlayers() {
		players = null;
	}
	
	
	/*
	 *  Method to Transfer Players between Levels
	 */
	public void addPlayers(Player[] players) {
		this.players = players;
	}
	
	
	/*
	 *  Initialization methods in Level Constructor
	 */
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
					// 'n' is fine because it is a substitute for null
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
				if (temp.equals("d")){
					tiles[y][x] = new Door();
				}
				else if (temp.equals("c")) {
					tiles[y][x] = new Chest();
				}
				else if (temp.equals("z")){
					tiles[y][x] = new PressurePad();
				}
				else if (temp.equals("s")) {
					tiles[y][x] = new Spikes();
				}
				else if (temp.equals("n")) {
					// 'n' is fine because it is a substitute for null
				}
				else
					throw new RuntimeException();
			}
		}
	}
	
	private void buildMovables(char[][] movables) {
		boulders = new HashSet<>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				String temp = Character.toString((movables[y][x]));
				if(temp.equals("b"))
					boulders.add(new Boulder(new Point(x, y)));
			}
		}
	}
	
	
	
	/*
	 *  Helper methods for 'getLevelState()'
	 */
	private char[][] getLevel() {
		char[][] array = new char[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Tile temp = tiles[i][j];
				if(temp instanceof EmptyTile || temp instanceof Wall){
					array[i][j] = temp.toString().charAt(0);
				}
				else{
					array[i][j] = 'e';
				}
			}
		}
		return array;
	}

	private char[][] getObjects() {
		char[][] array = new char[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Tile temp = tiles[i][j];
				if(temp instanceof Unmoveable){
					array[i][j] = temp.toString().charAt(0);
				}
				else{
					array[i][j] = 'n';
				}
			}
		}	
		return array;
	}

	private char[][] getMovables() {
		char[][] array = new char[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				array[i][j] = 'n';
			}
		}	
		for (Boulder b : boulders) {
			Point p = b.getLocation();
			array[p.y][p.x] = b.toString().charAt(0);
		}
		
		if (players != null) {
			for (int i = 0; i != players.length; i++) {
				if (players[i] != null) {
					Point p = players[i].getLocation();
					array[p.y][p.x] = players[0].toString().charAt(0);
				}
			}
		}
		
		return array;
	}


	
}