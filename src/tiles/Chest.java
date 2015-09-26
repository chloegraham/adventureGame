package tiles;

import item.Key;

public class Chest implements Tile {
	
	private Key myKey = new Key("Key1122");
	private String character = "c";
	
	@Override
	public String toString() {
		return character;
	}

	public Key getKey() {
		this.character = "C";
		Key key = myKey;
		myKey = null;
		return key;
	}
	
}
