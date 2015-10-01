package tiles;

public class PressurePad extends Unmoveable implements Tile {
	
	private String character = "z";
	private boolean notActivated = true;
	
	public void activate(){
		
		notActivated = !notActivated;
		character = notActivated ? "z" : "Z";	//B if activated, otherwise b
	}
	
	public String toString() {
		return character;
	}
}
