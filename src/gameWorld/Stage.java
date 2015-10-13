package gameWorld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import movable.Player;
import convertors.Msgs;

public class Stage {
	private int stageID;
	private List<Room> rooms;
	private Map<DoorAddress, DoorAddress> doorToDoor;	// Need to know about moving between Rooms(called Levels atm)
	
	public Stage(String encodedStage, int stageID) {
		this.stageID = stageID;
		encodedStage = encodedStage.replace(Msgs.DELIM_STAGE, "");
		String[] split = encodedStage.split(Msgs.DELIM_ROOM);
		
		rooms = new ArrayList<>();
		
		int index = 0;
		for (String s : split)
			rooms.add(new Room(s, index++));
		
		System.out.println(toString());
	}
	
	
	
	public int getStageID() { return stageID; }
	public List<Room> getRooms() { return rooms; }
	
	
	
	public String getEncodedStage() {
		StringBuilder sb = new StringBuilder();
		
		for (Room r : rooms) 
			sb.append(r.getEncodedRoom());
		
		sb.append('@');
		sb.append(stageID);
		sb.append('@');
		return sb.toString() +
			   Msgs.DELIM_STAGE +
			   Msgs.DELIM_SPLIT;
	}
	
	
	
	public boolean addPlayerToRoom(Player p) {
		Room room = rooms.get(p.getRoomID());
		return room.addPlayer(p);
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
