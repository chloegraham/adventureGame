package tiles;

import item.Key;

public class Chest implements Tile {
	
	private Key myKey = new Key("1234");
	private String character = "c";
	
	@Override
	public String toString() {
		return character;
	}

	public Key getKey() {
		this.character = "l";
		Key key = myKey;
		myKey = null;
		return key;
	}
	
}
