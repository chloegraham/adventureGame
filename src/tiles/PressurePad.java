package tiles;

public class PressurePad extends Unmoveable implements Tile {
	
	private String character = "b";
	private boolean activated = false;
	
	public void activate(){
			activated = !activated;
			character = activated ? "b" : "B";	
	}
	
	public String toString() {
		return character;
	}
}
