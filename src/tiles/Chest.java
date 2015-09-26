package tiles;

import item.Item;
import item.Key;

public class Chest implements Tile {
	
	private boolean state = true;
	private Key myKey = new Key("Key1122");
	
	public String toString() {
		return "c";
	}
	public boolean getState(){
		return state;
	}
	public void emptyChest(){
		this.state = false;
	}
	public Item getKey() {
		return myKey;
	}
}
