package gameWorld;

import java.awt.Point;

import controller.Controller;
import tiles.EmptyTile;
import tiles.Tile;
import tiles.Wall;
import userinterface.Action.Actions;

public class Level {
	private static final int WIDTH = 9;
	private static final int HEIGHT = 9;
	
	private Controller controller;
	private Tile[][] tiles;
	private Player player;
	private GameLogic logic;
	
	public Level(Controller controller) {
		this.controller = controller;
		tiles = new Tile[HEIGHT][WIDTH];
		setupEmptyTiles();
		setupWalls();
		setupPlayer();
		setupGameLogic();
	}
	
	public Level() {
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
		logic = new GameLogic(tiles, player, this);
	}
	
	public void handleAction(int action){
		if (Actions.NORTH.ordinal() == action){ moveUp(); }
		else if (Actions.SOUTH.ordinal() == action){ moveDown(); }
		else if (Actions.EAST.ordinal() == action){ moveRight(); }
		else if (Actions.WEST.ordinal() == action){ moveLeft(); }
	}
	
	public void moveLeft() {
		if (logic.moveLeft(player))
			controller.updateUI(getLevelImg());
	}
	
	public void moveRight() {
		if (logic.moveRight(player))
			controller.updateUI(getLevelImg());
	}
	
	public void moveUp() {
		if (logic.moveUp(player))
			controller.updateUI(getLevelImg());
	}
	
	public void moveDown() {
		if (logic.moveDown(player))
			controller.updateUI(getLevelImg());
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
}
