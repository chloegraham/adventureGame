package server.gameWorld;

import java.util.ArrayList;
import java.util.List;

import server.helpers.Msgs;
import server.movable.Player;

public class GameWorld {
	private GameLogic logic;
	private List<Stage> stages;
	private List<Player> players;
	
	public GameWorld(String encodedGameWorld){
		String[] split = encodedGameWorld.split(Msgs.DELIM_SPLIT);
		
		stages = new ArrayList<Stage>(); 
		players = new ArrayList<Player>();
		//populate the stages and players lists appropriately
		int index = 0;
		for (String s: split) {
			if (s.contains(Msgs.DELIM_STAGE)) {
				stages.add(new Stage(s, index++));
			}
			else if (s.contains(Msgs.DELIM_PLAYER)) {
				players.add(new Player(s));
			}
		}
		
		addPlayersToRooms();
		
		logic = new GameLogic(this, stages, players);
		System.out.println("\n  Constructor-" + toString());
	}
	
	
	
	/**
	 * 
	 */
	public void addPlayersToRooms() {
		for (Player p : players) {
			Stage stage = stages.get(p.getStageID());
			stage.addPlayerToRoom(p);
		}
	}
	
	
	
	public void removePlayers() {
		for (Player p : players) {
			Stage stage = stages.get(p.getStageID());
			stage.removePlayerFromRoom(p);
		}
	}

	/*
	 * 
	 */
	public GameLogic getLogic() { return logic; }
	
	/*
	 * 
	 */
	public String getEncodedGameWorld(int userID){
		// find player with given userID
		Player currentPlayer = null;
		for (Player p : players)
			if (p.getUserID() == userID)
				currentPlayer = p;
				
		assert(currentPlayer != null);
		
		
		// get the stage player is in
		int stageID = currentPlayer.getStageID();
		Stage stage = null;
		for (Stage s : stages)
			if (s.getStageID() == stageID)
				stage = s;
		
		assert(stage != null);
		
		
		// get the room of given stage player is in
		int roomID = currentPlayer.getRoomID();
		Room room = null;
		for (Room r : stage.getRooms())
			if (r.getRoomID() == roomID)
				room = r;
		
		assert(room != null);
		
		// return the encoded room which the player is on
		String encodedRoom = room.getEncodedRoom();
		encodedRoom += Msgs.DELIM_SPLIT + logic.bouldersKeysLocation(userID);
		
		return encodedRoom;
	}
	
	
	
	/*
	 * 
	 */
	public String getEncodedGameSave(){
		StringBuilder sb = new StringBuilder();
		
		for (Stage s : stages) 
			sb.append(s.getEncodedStage());
		
		for (Player p : players)
			sb.append(p.getEncodedPlayer());
		
		return sb.toString();
	}
	
	@Override
	public String toString() {
		String str = "  GAMEWORLD-  #Stages: " + stages.size() + ".  StageID's: "; 
		
		for (Stage s : stages)
			str += s.getStageID() + ", ";
			
		str += "\n  #Players: " + players.size() + "\n  'toString()' of Players:\n";
		
		for (Player p : players)
			str += p.toStringConstructor() + "\n";
		
		return str;
	}
}
