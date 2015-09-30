package tiles;

public class PressurePad extends Unmoveable implements Tile {
	
	private String character = "b";
	private boolean notActivated = true;
	
	public void activate(){
		
		notActivated = !notActivated;
		character = notActivated ? "b" : "a";	//B if activated, otherwise b

	}
	
	public String toString() {
		return character;
	}
}
