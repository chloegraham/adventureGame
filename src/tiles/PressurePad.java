package tiles;

public class PressurePad extends Unmoveable implements Tile {
	
	private boolean notActivated = true;
	
	public void activate(){		
		notActivated = !notActivated;
	}
	
	public boolean isActivated(){
		return notActivated;
	}
	
	@Override
	public String toString() {
		if(notActivated){
			return "z";
		}
		return "Z";
	}
}
