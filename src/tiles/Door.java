package tiles;

public class Door implements Tile {
	private boolean Closed = true;
	
	public String toString() {
		return "d";
	}
	
	public void openDoor(){
		this.Closed = false;
	}
	
	public boolean getState(){
		return Closed; 
	}
}