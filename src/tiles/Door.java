package tiles;

import item.Key;
//either set tile as empty once open so can walk through it, otherwise door implememnts walk throughable
public class Door implements Tile {
	
	private boolean locked = true;
	private String character = "d";
	private Key key = new Key("1234");
	
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
}