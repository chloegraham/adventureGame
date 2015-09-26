package tiles;

public class Door implements Tile {
	
	private boolean closed = true;
	
	public String toString() {
		return "d";
	}
	
	public void openDoor(){
		this.closed = false;
	}
	
	public boolean getState(){
		return closed; 
	}
}