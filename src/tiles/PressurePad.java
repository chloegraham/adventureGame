package tiles;

public class PressurePad extends Unmoveable implements Tile {
	
	private boolean isActivated = true;
	
	public void activate(){		
		isActivated = !isActivated;
	}
	
	public boolean isActivated(){
		return isActivated;
	}
	
	@Override
	public String toString() {
		if(isActivated){
			return "z";
		}
		return "Z";
	}
}
