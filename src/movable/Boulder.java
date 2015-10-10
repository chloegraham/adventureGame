package movable;

import java.awt.Point;

public class Boulder extends Moveable implements Item {

	public Boulder(Point location) {
		super(location);
	}
	
	@Override
	public String toString(){
		return "b";
	}
}
