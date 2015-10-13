package gameWorld;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import movable.Boulder;
import movable.Player;
import tiles.Chest;
import tiles.DoorNormal;
import tiles.DrawFirst;
import tiles.EmptyTile;
import tiles.Furniture;
import tiles.DoorLevel;
import tiles.PressurePad;
import tiles.Spikes;
import tiles.Tile;
import tiles.Wall;
import convertors.Msgs;

public class Room {
	private int roomID;
	private int width;
	private int height;	
	
	private Tile[][] tiles;
	private Set<Boulder> boulders;
	private Set<Spikes> spikes;
	private Set<Player> players;
	
	public Room(String encodedRoom, int roomID) {
		this.roomID = roomID;
		encodedRoom = encodedRoom.replace(Msgs.DELIM_ROOM, "");
		
		// Split String up in to x3 Strings which will be converted to char[][]
		String[] layers = encodedRoom.split("@");
			
		// Split up the 2d-char[][] in to 1d-char[] (they are still Strings at the moment)
		String[] wallEmpty = layers[0].split("%");
		String[] furniture = layers[1].split("%");
		String[] bouldersPlayers = layers[2].split("%");
		char[][] wallEmptyCH;
		char[][] furnitureCH;
		char[][] bouldersPlayersCH;	
		
		// Now build the actual 2d-char[][] from the broken down Strings
		wallEmptyCH = new char[wallEmpty.length][];
		for (int x = 0; x < wallEmptyCH.length; x++)
			wallEmptyCH[x] = wallEmpty[x].toCharArray();
		
		furnitureCH = new char[furniture.length][];
		for (int x = 0; x < furnitureCH.length; x++)
			furnitureCH[x] = furniture[x].toCharArray();
		
		bouldersPlayersCH = new char[bouldersPlayers.length][];
		for (int x = 0; x < bouldersPlayersCH.length; x++)
			bouldersPlayersCH[x] = bouldersPlayers[x].toCharArray();
		
		setupTiles(wallEmptyCH);
		
		buildWallEmpties(wallEmptyCH);
		buildFurniture(furnitureCH);
		buildBouldersPlayers(bouldersPlayersCH);
		
		
		
		System.out.println("	Constructor-" + toStringConstructor());
	}
	
	
	


	/*
	 *  Getter LevelID
	 */
	public int getRoomID() {
		return roomID;
	}
	
	
	/*
	 *  Getters for GameLogic
	 */
	public Tile[][] getTiles() { return tiles; }
	
	
	
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
	public boolean removePlayer(Player player) {
		boolean success = players.remove(player);
		System.out.println(toString() + "  removed a player");
		return success;
	}
	
	/*
	 *  Method to Transfer Players between Levels
	 */
	public boolean addPlayer(Player player) {
		boolean success = players.add(player);
		if (!success)
			throw new IllegalArgumentException("Should never be able to add two of the same player to one Level.");
		System.out.println(toString() + "  added a player");
		return success;
	}
	
	public boolean containsPlayer() {
		return !players.isEmpty();
	}
	
	public boolean playerAt(Point location) {
		for (Player p : players)
			if (p.getLocation().equals(location))
				return true;
		return false;
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
	public String getEncodedRoom() {
		char[][] level = getWallsEmpties();
		char[][] objects = getFurniture();
		char[][] movables= getBouldersPlayers();
		
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
		sb.append(roomID);
		sb.append('@');
		return sb.toString() +
			   Msgs.DELIM_ROOM +
			   Msgs.DELIM_SPLIT;
	}
	
	
	
	/*
	 *  Helper methods for 'getEncodedLevel()'
	 */
	private char[][] getWallsEmpties() {
		char[][] array = new char[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Tile temp = tiles[i][j];
				if(temp instanceof DrawFirst){
					array[i][j] = temp.toString().charAt(0);
				}
				else{
					array[i][j] = 'e';
				}
			}
		}
		return array;
	}

	private char[][] getFurniture() {
		char[][] array = new char[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Tile temp = tiles[i][j];
				
				if(temp instanceof Furniture) {
					array[i][j] = temp.toString().charAt(0);
				}
				else{
					array[i][j] = 'n';
				}
			}
		}	
		return array;
	}

	private char[][] getBouldersPlayers() {
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
	private void setupTiles(char[][] wallsEmpties) {
		width = wallsEmpties[0].length;
		height = wallsEmpties.length;
		tiles = new Tile[height][width];
	}
	
	private void buildWallEmpties(char[][] wallsEmpties) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				String temp = Character.toString((wallsEmpties[y][x]));
				if(temp.equals("e")){
					tiles[y][x] = new EmptyTile();
				}
				else if(temp.equals("w")) {
					tiles[y][x] = new Wall();
				}
				else {
					throw new RuntimeException();
				}
			}
		}
	}
	
	private void buildFurniture(char[][] furniture) {
		spikes = new HashSet<>();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				String temp = Character.toString((furniture[y][x]));
				
				if (temp.equals("d")) {
					System.out.println(x + ", " + y);
					tiles[y][x] = new DoorNormal("d");
				}
				else if (temp.equals("D")) {
					tiles[y][x] = new DoorNormal("D");
				}
				else if (temp.equals("l")) {
					tiles[y][x] = new DoorLevel("l");
				}
				else if (temp.equals("L")) {
					tiles[y][x] = new DoorLevel("L");
				}
				
				
				else if (temp.equals("c")) {
					tiles[y][x] = new Chest(false);
				}
				else if (temp.equals("C")) {
					tiles[y][x] = new Chest(true);
				}
				
				
				else if (temp.equals("z")) {
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
	}
	
	
	private void buildBouldersPlayers(char[][] bouldersPlayers) {
		players = new HashSet<>();
		boulders = new HashSet<>();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				String temp = Character.toString((bouldersPlayers[y][x]));
				if(temp.equals("b"))
					boulders.add(new Boulder(new Point(x, y)));
			}
		}
	}

	public String toStringConstructor() {
		String str = toString();
		
		str += "\n";
		
		for (int y = 0; y < height; y++) {
			str += "\n";
			for (int x = 0; x < width; x++) {
				str += tiles[y][x].toString();
			}
		}
		
		return str;
	}
	
	@Override
	public String toString() {
		String str = "   ROOM:   roomID:  " + roomID + "    #players:  " + players.size() + ".   IDs of those Players:  "; 
		
		for (Player p : players)
			str += p.getUserID() + ",  ";
		
		str += "   	#boulders:   " + boulders.size();
		str += "	#spikes:   " + spikes.size();
		
		str += "\n";
		
		for (Player p : players)
			str += p.toStringConstructor();
		
		return str;
	}
}