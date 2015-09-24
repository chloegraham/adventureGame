package gameWorld;

import java.awt.Point;

import tiles.EmptyTile;
import tiles.Tile;

public class GameLogic {
	private Tile[][] tiles;
	
	public GameLogic(Tile[][] tiles) {
		this.tiles = tiles;
	}
	
	public boolean moveLeft(Player p){
		Point now = p.getMyLocation();
		Point next = new Point(now.x-1, now.y);
		Tile tile = tiles[next.y][next.x];
		
		if(tile instanceof EmptyTile){
			p.setMyLocation(next);
			return true;
		}
		return false;
	}
	
	public boolean moveRight(Player p){
		Point now = p.getMyLocation();
		Point next = new Point(now.x+1, now.y);
		Tile tile = tiles[next.y][next.x];
		
		if(tile instanceof EmptyTile){
			p.setMyLocation(next);
			return true;
		}
		return false;
	}
	
	public boolean moveUp(Player p){
		Point now = p.getMyLocation();
		Point next = new Point(now.x, now.y-1);
		Tile tile = tiles[next.y][next.x];
		
		if(tile instanceof EmptyTile){
			p.setMyLocation(next);
			return true;
		}
		return false;
	}
	
	public boolean moveDown(Player p){
		Point now = p.getMyLocation();
		Point next = new Point(now.x, now.y+1);
		Tile tile = tiles[next.y][next.x];
		
		if(tile instanceof EmptyTile){
			p.setMyLocation(next);
			return true;
		}
		return false;
	}
}
