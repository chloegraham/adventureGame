package gameWorld;

import java.awt.Point;
import java.util.Map;

public class Stage {
	private Level[] level;
	private Map<DoorAddress, DoorAddress> doorToDoor;	// Need to know about moving between Rooms(called Levels atm)
	
	
	
	public Stage(String encodedStage) {
		/*
		 * 
		 * Decode the string in to Levels
		 * 
		 * 
		 */
	}
	
	
	
	public String getEncodedStage() {
		
		/*
		 * 	Encode this stage object as a String to pass to gameworld
		 */
		
		return "";
	}
	
	
	
	protected class DoorAddress {
		private int roomID;
		private Point locationInsideRoom;
		
		public DoorAddress(int roomID, Point locationInsideRoom) {
			this.roomID = roomID;
			this.locationInsideRoom = locationInsideRoom;
		}
		
		public int getRoomID() {
			return roomID;
		}
		
		public Point getLocationInsideRoom() {
			return locationInsideRoom;
		}
	}
}
