package tiles;

public class Spikes extends Unmoveable implements Tile {

	private boolean active = false;
	private String character = "s";
	private boolean notActivated = true;
	
	public String toString() {
		return character;
	}
	
	/*public void cycleSpikes(){
		if (active == true){
			this.active = false;
			this.character = "s";
		} else {
			this.active = true;
			this.character = "S";
			
		}
	}*/
	
	public boolean isActive(){
		return this.active;
	}
	public void activate(){
		notActivated = !notActivated;
		character = notActivated ? "s" : "S";
	}
}
