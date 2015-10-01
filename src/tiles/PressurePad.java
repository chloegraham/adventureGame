package tiles;

public class PressurePad extends Unmoveable implements Tile {
	
	private String character = "p";
	private boolean notActivated = true;
	
	public void activate(){
		
		notActivated = !notActivated;
		character = notActivated ? "p" : "P";	//B if activated, otherwise b

	}
	
	public String toString() {
		return character;
	}
}
