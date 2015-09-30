package movable;

import java.awt.Point;

public class Boulder extends Moveable implements Item{
	
	private String description;
	private String id;
	private boolean manHandled;

	public Boulder(Point location, String description, String id) {
		this(location, "b");
		this.description = description;
		this.id = id;
	}
	
	public Boulder(Point location, String c) {
		super(location, c);
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public void manHandle() {
		manHandled = !manHandled;
	}
}
