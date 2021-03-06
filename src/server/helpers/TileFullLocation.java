package server.helpers;

import java.awt.Point;

public class TileFullLocation {
	protected int stage;
	protected int room;
	protected Point location;
	
	public TileFullLocation(int stage, int room, Point location) {
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
		if (o instanceof TileFullLocation) {
			TileFullLocation d = (TileFullLocation) o;
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
