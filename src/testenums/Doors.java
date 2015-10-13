package testenums;

import java.awt.Point;

public class Doors {
	protected int stage;
	protected int room;
	protected Point location;
	
	public Doors(int stage, int room, Point location) {
		this.stage = stage;
		this.room = room;
		this.location = location;
	}
	
	public int getStage() {
		return stage;
	}
	
	public int getRoom() {
		return room;
	}
	
	public Point getLocation() {
		return location;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Doors) {
			Doors d = (Doors) o;
			if (d.stage == this.stage)
				if (d.room == this.room)
					if (d.location.equals(this.location))
						return true;
		}
		return false;	
	}
	
	@Override
	public int hashCode() {
		int num = 3;
		num += stage;
		num += room;
		num += location.hashCode();
		return num;
	}
	
	@Override
	public String toString() {
		return "asdf:  " + stage + ", " + room + ", " + location.x + ", " + location.y;
	}
}
