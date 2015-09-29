package tiles;

public class Spikes extends Unmoveable implements Tile {

	private boolean active = true;
	private String character = "s";
	
	public String toString() {
		return character;
	}
	
	public void cycleSpikes(){
		if (active == true){
			this.active = false;
			this.character = "s";
		} else {
			this.active = true;
			this.character = "S";
			
		}
	}
	
	public boolean isActive(){
		return this.active;
	}
}
