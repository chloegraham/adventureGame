package tiles;

import movable.Key;

public class Chest extends Unmoveable implements Tile {
	
	private Key myKey = new Key("1234", "I'm the key to your heart");
	private String character = "c";
	
	@Override
	public String toString() {
		return character;
	}
	
	public Key openChest(){
		this.character = "l";
		Key key = myKey;
		myKey = null;
		return key;
	}
	
}
