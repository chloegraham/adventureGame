package movable;

import tiles.Tile;
import gameWorld.Player;

public class PlayerTile implements Moveable, Tile{

	private Player player;
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PlayerTile(Player p){
		this.player = p;
	}
	
	public String toString(){
		return "p";
	}
	
}
