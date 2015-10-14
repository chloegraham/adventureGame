package server.movable;

import java.awt.Point;

/**
 * Big heavy boulders. Note in the game they are called Eggs, to fit with the chicken theme. 
 * 
 */
public class Boulder extends Moveable {

	public Boulder(Point location) {
		super(location);
	}
	
	@Override
	public String toString(){
		return "b";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Boulder)
			if (((Boulder) o).getLocation().equals(this.getLocation()))
				return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return getLocation().hashCode();
	}
}
