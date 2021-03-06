package server.gameWorld;

import java.util.ArrayList;
import java.util.List;

import server.movable.Player;
import sharedHelpers.Msgs;


/**
 * A stage is a collection of connected rooms. 
 * 
 * A players goal in a stage is to reach the end tile, which 
 * transports them to the next stage. 
 *
 */
public class Stage {
	private int stageID;
	private List<Room> rooms;
	
	public Stage(String encodedStage, int stageID) {
		this.stageID = stageID;
		encodedStage = encodedStage.replace(Msgs.DELIM_STAGE, "");
		String[] split = encodedStage.split(Msgs.DELIM_ROOM);
		
		rooms = new ArrayList<>();
		
		int index = 0;
		for (String s : split)
			rooms.add(new Room(s, stageID, index++));
		
		System.out.println("\n  Constructor-" + toString());
	}
	
	
	
	public int getStageID() { return stageID; }
	public List<Room> getRooms() { return rooms; }
	
	
	
	public String getEncodedStage() {
		StringBuilder sb = new StringBuilder();
		
		for (Room r : rooms) 
			sb.append(r.getEncodedRoom());
		
		return sb.toString() +
			   Msgs.DELIM_STAGE +
			   Msgs.DELIM_SPLIT;
	}
	
	
	
	public boolean addPlayerToRoom(Player p) {
		Room room = rooms.get(p.getRoomID());
		return room.addPlayer(p);
	}
	
	
	
	public boolean removePlayerFromRoom(Player p) {
		Room room = rooms.get(p.getRoomID());
		return room.removePlayer(p);
	}
	
	
	
	@Override
	public String toString() {
		String str = "  STAGE-  StageID: " + stageID + "  #Rooms: " + rooms.size() + ".   RoomIDs: "; 
		
		for (Room r : rooms)
			str += r.getRoomID() + ", ";
		
		return str;
	}
}
