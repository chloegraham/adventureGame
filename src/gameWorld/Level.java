package gameWorld;

import java.awt.Point;

import renderer.Render;
import tiles.EmptyTile;
import tiles.Tile;
import tiles.Wall;

public class Level {
	private static final int WIDTH = 25;
	private static final int HEIGHT = 25;
	
	private Tile[][] tiles;
	private Player player;
	private Render ui;
	private GameLogic logic;
	
	public Level(Render ui) {
		this.ui = ui;
		tiles = new Tile[HEIGHT][WIDTH];
		setupEmptyTiles();
		setupWalls();
		setupPlayer();
		setupGameLogic();
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
	
	private void setupPlayer() {
		player = new Player(new Point(2, 2));
	}
	
	private void setupGameLogic() {
		logic = new GameLogic(tiles);
	}
	
	
	public void moveLeft() {
		if (logic.moveLeft(player))
			ui.redraw(getLevelImg());
	}
	
	public void moveRight() {
		if (logic.moveRight(player))
			ui.redraw(getLevelImg());
	}
	
	public void moveUp() {
		if (logic.moveUp(player))
			ui.redraw(getLevelImg());
	}
	
	public void moveDown() {
		if (logic.moveDown(player))
			ui.redraw(getLevelImg());
	}
	
	public char[][] getLevelImg() {
		char[][] array = new char[HEIGHT][WIDTH];
		for (int x = 0; x < WIDTH; x++)
			for (int y = 0; y < HEIGHT; y++) 
				array[y][x] = tiles[y][x].toString().charAt(0);

		Point loc = player.getMyLocation();
		array[loc.y][loc.x] = player.toString().charAt(0);
		
		return array;
	}

	public void horriblemethod() {
		ui.redraw(getLevelImg());
	}
}
