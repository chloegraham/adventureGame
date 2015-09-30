package gameWorld;

import java.awt.Point;
import java.io.File;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import movable.Moveable;
import tiles.Chest;
import tiles.Door;
import tiles.EmptyTile;
import tiles.PressurePad;
import movable.PlayerTile;
import tiles.Tile;
import tiles.Wall;

public class Level {
	private int width;
	private int height;
	
	private Tile[][] staticTiles;
	private Moveable[][] items; //includes players and items (boulders, keys)
	private Player player;
	private static Tile[][] tiles;
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[height][width];
		parseLevel("board.txt");
		//setupWalls();
		//setupDoors();
		//setupChests();
		//setupPlayer();
	}
	
	public void parseLevel(String filename) {
		
		Scanner sc;
		//InputStream input = Board.class.getClassLoader().getResourceAsStream(filename);
		try {
			sc = new Scanner(new File(filename));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Dun dun dunnn, could not parse: File not found!!");
		}
		//sc.useDelimiter(";");		
		int width;
		int height;
		try{
			width = sc.nextInt();
			height = sc.nextInt();
			System.out.println(width + " - width ; " + height + " height" );
		} catch (RuntimeException e1){
			throw new RuntimeErrorException(null, "Couldn't parse dimensions of board");
		}
		setupTiles(sc);
		sc.close();	
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	private void setupPlayer() {
		Point p = new Point(2, 2);
		this.player = new Player(p);
	}
	
	private void setupDoors() {
		tiles[0][3] = new Door();
	}
	
	private void setupChests() {
		tiles[3][3] = new Chest();
	}

	private void setupEmptyTiles() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[y][x] = new EmptyTile();
			}
		}
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
					else if(temp.equals("p")){
						this.player = new Player(new Point(j, i));
						tiles[i][j] = new PlayerTile(this.player);
					}
					else if(temp.equals("b")){
						tiles[i][j] = new PressurePad();
					}
				}
			}
		}
	}
	
	public static String toStringss() {
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
	
	@SuppressWarnings("static-access")
	public Tile[][] getLevel() {
		/*char[][] array = new char[HEIGHT][WIDTH];
		for (int x = 0; x < WIDTH; x++)
			for (int y = 0; y < HEIGHT; y++) 
				array[y][x] = tiles[y][x].toString().charAt(0);

		Point loc = this.player.getMyLocation();
		array[loc.y][loc.x] = this.player.toString().charAt(0);
		return array;*/
		return this.tiles;
	}

}