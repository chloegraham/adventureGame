package tiles;


public class Door extends Unmoveable implements Tile {
	
	private boolean locked = true;
	private boolean levelChanger = false;
	private boolean nextLevel = true;

	
	
	/*
	 *  Locking & Unlocking Doors changes whether Players can pass through them or not
	 */
	public boolean isLocked(){
		return locked;
	}
	
	public void unlock() {
		locked = false;
		levelChanger = true;
	}
	
	public void lock() {
		locked = true;
		levelChanger = false;
	}
	
	
	
	/*
	 *  LevelChanger logic
	 */
	public boolean isLevelChanger() {
		return levelChanger; 
	}
	
	public boolean isNextLevel() {
		return nextLevel;
	}
	
	public void setNextLevel(boolean isNext) {
		nextLevel = isNext;
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