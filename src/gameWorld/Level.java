package gameWorld;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import convertors.Msgs;
import movable.Boulder;
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
	private int levelID;
	private int width;
	private int height;	
	
	private Point prev;
	private Point next;
	
	private Tile[][] tiles;
	private Set<Boulder> boulders;
	private Set<Spikes> spikes;
	private Set<Player> players;
	private Map<Point, Point> padsToDoors;
	
	public Level(String encodedLevel) {
		
		this.padsToDoors = new HashMap<Point, Point>();
		// Split String up in to x3 Strings which will be converted to char[][]
		String[] layers = encodedLevel.split("@");
			
		// Split up the 2d-char[][] in to 1d-char[] (they are still Strings at the moment)
		String[] subLayers1 = layers[0].split("%");
		String[] subLayers2 = layers[1].split("%");
		String[] subLayers3 = layers[2].split("%");
		levelID = Integer.parseInt(layers[3]);
		String[] padToDoor = layers[4].split("%");
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
		connectPadsToDoors(padToDoor);
		System.out.println(toString());
	}
	
	
	private void connectPadsToDoors(String[] points) {
			
			if(points.length == 1)return;
			if(points.length %4 != 0) throw new IllegalArgumentException("A level has the wrong number of coordinates connecting pads to doors");
			//for(int i = 0; i < points.length; i = i + 3){
			int i = 0;
			Point pressurePad = new Point(Integer.parseInt(points[i+1]), Integer.parseInt(points[i]));
			Point door = new Point(Integer.parseInt(points[i+3]), Integer.parseInt(points[i+2]));
			System.out.println("pressure pad: x = " + pressurePad.getX() + " y = " + pressurePad.getY());
			System.out.println("door: x = " + door.getX() + " y = " + door.getY());
			this.padsToDoors.put(pressurePad, door);
			//}	
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
	
	
	
	/*
	 *  Getters for Prev & Next level locations.
	 */
	public Point getPrev() {
		return prev;
	}
	public Point getNext() {
		return next;
	}
	
	
	
	/*
	 *  Boulder methods
	 */
	public Set<Boulder> getBoulders(){ return boulders;}
	
	public void addBoulder(Boulder boulder) {
		boolean success = boulders.add(boulder);
		if (!success)
			throw new IllegalArgumentException("Invalid to add a Boulder that is already in Boulder set. Investigate how this was possible.");
		System.out.println(toString());
	}
	
	public boolean removeBoulder(Boulder boulder) {
		boolean success = boulders.remove(boulder);
		System.out.println(toString());
		return success;
	}
	
	public boolean containsBoulder(Boulder boulder) {
		return boulders.contains(boulder);
	}
	
	
	

	/*
	 *  Getter to Remove Players from a Level
	 */
	public void removePlayer(Player player) {
		boolean success = players.remove(player);
		System.out.println(toString() + "  removed a player");
	}
	
	/*
	 *  Method to Transfer Players between Levels
	 */
	public void addPlayer(Player player) {
		boolean success = players.add(player);
		if (!success)
			throw new IllegalArgumentException("Should never be able to add two of the same player to one Level.");
		System.out.println(toString() + "  added a player");
	}
	
	public boolean containsPlayer() {
		return !players.isEmpty();
	}
	
	
	/*
	 *  Activate all the Spikes on this Level
	 */
	public void activateSpikes() {
		for (Spikes s : spikes)
			s.activate();
		
		for (Player p : players)
			for (Spikes s : spikes)
				if (p.getLocation().equals(s.getLocation()))
					if (s.isActivated())
						p.getUserID();
	}
	
	
	
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
			   Msgs.DELIM_LEVEL +
			   Msgs.DELIM_SPLIT;
	}
	
	
	
	/*
	 *  Helper methods for 'getEncodedLevel()'
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
				if(temp instanceof Unmoveable && !(temp instanceof Wall) || temp instanceof PressurePad) {
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
		
		for (Player player : players) {
			Point p = player.getLocation();
			array[p.y][p.x] = player.toString().charAt(0);
		}
		
		return array;
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
				else {
					throw new RuntimeException();
				}
			}
		}
	}
	
	private void buildObjects(char[][] objects) {
		spikes = new HashSet<>();
		System.out.println(objects[0].length);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				String temp = Character.toString((objects[y][x]));
				/*if(temp.equals("z")){
					temp += Character.toString((objects[y][++x]));
					temp += Character.toString((objects[y][++x]));
					System.out.println(temp);
					System.out.println("x length = " + x);
				}*/
				if (temp.equals("d")) {
					tiles[y][x] = new Door("d");
				}
				else if (temp.equals("D")) {
					tiles[y][x] = new Door("D");
				}
				else if (temp.equals("m")) {
					tiles[y][x] = new Door("m");
					if (prev == null)
						prev = new Point(x, y);
					else
						throw new IllegalArgumentException("Level Constructor: Tried to make another 'Prev'. There should only be one 'Prev' location.");
				}
				else if (temp.equals("M")) {
					tiles[y][x] = new Door("M");
					if (prev == null)
						prev = new Point(x, y);
					else
						throw new IllegalArgumentException("Level Constructor: Tried to make another 'Prev'. There should only be one 'Prev' location.");
				}
				else if (temp.equals("x")) {
					tiles[y][x] = new Door("x");
					if (next == null)
						next = new Point(x, y);
					else
						throw new IllegalArgumentException("Level Constructor: Tried to make another 'Next'. There should only be one 'Next' location.");
				}
				else if (temp.equals("X")) {
					tiles[y][x] = new Door("X");
					if (next == null)
						next = new Point(x, y);
					else
						throw new IllegalArgumentException("Level Constructor: Tried to make another 'Next'. There should only be one 'Next' location.");
				}
				
				else if (temp.equals("c")) {
					tiles[y][x] = new Chest(false);
				}
				else if (temp.equals("C")) {
					tiles[y][x] = new Chest(true);
				}
				
				else if (temp.charAt(0) =='z') {
					tiles[y][x] = new PressurePad(false);
				}
				else if (temp.equals("Z")) {
					tiles[y][x] = new PressurePad(true);
				}
				
				else if (temp.equals("s")) {
					Spikes spike = new Spikes(false, new Point(x, y));
					tiles[y][x] = spike;
					spikes.add(spike);
				}
				else if (temp.equals("S")) {
					Spikes spike = new Spikes(true, new Point(x, y));
					tiles[y][x] = spike;
					spikes.add(spike);
				}
				
				else if (temp.equals("n")) {
					// 'n' is fine because it is a substitute for null
				}
				else {
					throw new RuntimeException();
				}
			}
		}
		if (prev == null || next == null)
			throw new IllegalArgumentException("Level Constructor: There should always be a 'Prev' and 'Next' on every Level. This Level one is null.");
	}
	
	private void buildMovables(char[][] movables) {
		players = new HashSet<>();
		boulders = new HashSet<>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				String temp = Character.toString((movables[y][x]));
				if(temp.equals("b"))
					boulders.add(new Boulder(new Point(x, y)));
			}
		}
	}
	
	public Map<Point,Point> getMapOfPads(){
		return this.padsToDoors;
	}
	
	public String toString() {
		return "   Level( levelID- " + levelID + "):   #players:  " + players.size() + "    #boulders:  " + boulders.size();
	}


	public Point getDoorFromPad(Point newLoc) {
		
		//TODO: throw illagal argument if not present
		Point door = this.padsToDoors.get(newLoc);
		return door;
	}
}