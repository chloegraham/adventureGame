package tiles;

import movable.Key;
//either set tile as empty once open so can walk through it, otherwise door implememnts walk throughable
public class Door extends Unmoveable implements Tile {
	
	private boolean locked = true;
	private boolean levelChanger = false;
	private boolean nextLevel = true;
	
	private Key key = new Key("1234", "I'm the key to your heart");

	public boolean isLocked(){
		return locked;
	}
	
	public boolean isLevelChanger() {
		return levelChanger; 
	}
	
	public boolean isNextLevel() {
		return nextLevel;
	}
	
	public void setNextLevel(boolean isNext) {
		nextLevel = isNext;
	}
	
	public void openDoor(Key pKey){
		if(key.equals(pKey))
			openDoor();
	}

	public void openWithPad() {
		openDoor();
	}
	
	public void closeWithPad(){
		closeDoor();		
	}
	
	private void closeDoor(){
		locked = true;
		levelChanger = false;
	}
	
	private void openDoor() {
		locked = false;
		levelChanger = true;
	}
	
	@Override
	public String toString() {
		if (levelChanger)
			return "x";
		if (locked) 	 
			return "d";
		return "D";
	}
}