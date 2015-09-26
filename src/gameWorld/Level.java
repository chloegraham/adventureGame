package gameWorld;

import java.awt.Point;

import tiles.EmptyTile;
import tiles.Tile;
import tiles.Wall;

public class Level {
	private static final int WIDTH = 25;
	private static final int HEIGHT = 25;
	
	private Tile[][] tiles;
	private Player player;
	
	public Level() {
		tiles = new Tile[HEIGHT][WIDTH];
		setupEmptyTiles();
		setupWalls();
		setupPlayer();
	}
	
	private void setupPlayer() {
		Point p = new Point(2, 2);
		this.player = new Player(p);
	}
	
	public Player getPlayer(){
		return this.player;
	}

	private void setupEmptyTiles() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				tiles[y][x] = new EmptyTile();
			}
		}
	}
	
	private void setupWalls() {
		for (int x = 0; x < WIDTH; x++) {
			tiles[0][x] = new Wall();
			tiles[HEIGHT-1][x] = new Wall();
		}
		
		for (int y = 0; y < HEIGHT; y++) {
			tiles[y][0] = new Wall();
			tiles[y][WIDTH-1] = new Wall();
		}
	}
	
	public char[][] getLevel() {
		char[][] array = new char[HEIGHT][WIDTH];
		for (int x = 0; x < WIDTH; x++)
			for (int y = 0; y < HEIGHT; y++) 
				array[y][x] = tiles[y][x].toString().charAt(0);

		Point loc = this.player.getMyLocation();
		array[loc.y][loc.x] = this.player.toString().charAt(0);
		return array;
	}

	public Tile[][] getTiles() {
		return this.tiles;
	}
}
