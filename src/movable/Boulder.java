package movable;

import java.awt.Point;

public class Boulder extends Moveable implements Item{
	
	private String description;
	private String id;
	private String character = "b";

	public Boulder(Point location, String description, String id) {
		this(location);
		this.description = description;
		this.id = id;
		this.character = "b";
	}
	
	public Boulder(Point location) {
		super(location);
	}

	@Override
	public String getDescription() {
		return this.description;
	}
	
	public String getId() {
		return this.id;
	}
	
	@Override
	public String toString(){
		return this.character;
	}
}
