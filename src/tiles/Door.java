package tiles;

import movable.Key;
//either set tile as empty once open so can walk through it, otherwise door implememnts walk throughable
public class Door extends Unmoveable implements Tile {
	
	private boolean locked = true;
	private String character = "d";
	private Key key = new Key("1234", "I'm the key to your heart");

	public String toString() {
		return character;
	}
	
	public void openDoor(Key pKey){
		if(key.equals(pKey)){
			this.locked = false;
			this.character = "o";
		}
	}

	public boolean isLocked(){
		return locked;
	}

	public void openWithPad() {
		this.locked = false;
		this.character = "o";		
	}
}