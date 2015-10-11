package tiles;


public class Door extends Unmoveable implements Tile {
	
	private boolean locked;
	private boolean levelChanger;
	private boolean nextLevel;

	
	public Door(String symbol) {
		if (symbol.equals("d")){
			locked = true;
			nextLevel = false;
			levelChanger = false;
		}
		else if (symbol.equals("D")) {
			locked = false;
			nextLevel = false;
			levelChanger = false;
		}
		else if (symbol.equals("m")) {
			locked = true;
			nextLevel = false;
			levelChanger = true;
		}
		else if (symbol.equals("M")) {
			locked = false;
			nextLevel = false;
			levelChanger = true;
		}
		else if (symbol.equals("x")) {
			locked = true;
			nextLevel = true;
			levelChanger = true;
		}
		else if (symbol.equals("X")) {
			locked = false;
			nextLevel = true;
			levelChanger = true;
		}
	}

	/*
	 *  Locking & Unlocking Doors changes whether Players can pass through them or not
	 */
	public boolean isLocked(){
		return locked;
	}
	
	public void unlock() {
		locked = false;
	}
	
	public void lock() {
		locked = true;
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
	
	
	
	@Override
	public String toString() {
		if (locked && !levelChanger)
			return "d";
		else if (!locked && !levelChanger)
			return "D";
		else if (locked && levelChanger && !nextLevel)
			return "m";
		else if (!locked && !nextLevel && levelChanger)
			return "M";
		else if (locked && nextLevel && levelChanger)
			return "x";
		else if (!locked && nextLevel && levelChanger)
			return "X";
		else
			throw new IllegalArgumentException("Door toString must be faulty. None of it's conditions were met.");
	}
}