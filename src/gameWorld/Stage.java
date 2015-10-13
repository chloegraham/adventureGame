package gameWorld;

import java.util.ArrayList;
import java.util.List;

import movable.Player;
import convertors.Msgs;

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
			rooms.add(new Room(s, index++));
		
		System.out.println("	Constructor-" + toString());
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
	
	
	
	public boolean removePlayerFromRoom(Player p) {
		Room room = rooms.get(p.getRoomID());
		return room.removePlayer(p);
	}
	
	
	
	@Override
	public String toString() {
		String str = "   STAGE:   ID of this Stage:   " + stageID + "#Rooms:  " + rooms.size() + ".   IDs of those Rooms:  "; 
		
		for (Room r : rooms)
			str += r.getRoomID() + ",  ";
		
		return str;
	}
}
