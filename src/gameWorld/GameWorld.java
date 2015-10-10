package gameWorld;

import movable.Player;
import convertors.Msgs;

public class GameWorld {
	private GameLogic logic;
	private Level[] levels;
	private Player[] players;
	
	public GameWorld(String encodedGameWorld){
		String[] split = encodedGameWorld.split(Msgs.DELIM_SPLIT);
		
		int levelAmount = 0;
		int playerAmount = 0;
		
		for (String s : split) {
			if (s.contains(Msgs.DELIM_LEVEL)) {
				levelAmount++;
			}
			else if (s.contains(Msgs.DELIM_PLAYER)) {
				playerAmount++;
			}
			else {
				throw new IllegalArgumentException();
			}
		}
		
		levels = new Level[levelAmount];
		players = new Player[playerAmount];
		
		int levelIndex = 0;
		int playerIndex = 0;
		for (String s  : split) {
			if (s.contains(Msgs.DELIM_LEVEL)) {
				String encodedLevel = s;
				levels[levelIndex++] = new Level(encodedLevel);
			}
			else if (s.contains(Msgs.DELIM_PLAYER)) {
				String encodedPlayer = s;
				players[playerIndex++] = new Player(encodedPlayer);
			}
			else {
				throw new IllegalArgumentException();
			}
		}
		
		int playerOnelvlID = players[0].getLevelID();
		int playerTwolvlID = players[1].getLevelID();
		for (Level l : levels) {
			if (l.getLevelID() == playerOnelvlID)
				l.addPlayer(players[0]);
			if (l.getLevelID() == playerTwolvlID)
				l.addPlayer(players[1]);
		}
			
		logic = new GameLogic(levels, players);
		
		System.out.println(toString());
	}
	
	public GameLogic getLogic() { return logic; }
	
	
	public String getEncodedGameWorld(int userID){
		String str = "";
		
		// Find player using userid
		Player currentPlayer = null;
		for (Player p : players)
			if (p.getUserID() == userID)
				currentPlayer = p;
				
		if(currentPlayer == null)
			throw new IllegalArgumentException("Passed in userID doesn't belong to a player");

		// Find level using players levelid
		int levelID = currentPlayer.getLevelID();
		
		// return the encoded level which the player is on
		for(int i = 0; i < levels.length; i++)
			if(levels[i].getLevelID() == levelID)
				return str += levels[i].getEncodedLevel();
			
		throw new IllegalArgumentException("No such level that matches player's level");
	}
	
	
	public String getEncodedGameSave(){
		String str = "";
		
		for (int i = 0; i != levels.length; i++)
			str += levels[0].getEncodedLevel();
		
		for (int i = 0; i != players.length; i++)
			str += players[0].getEncodedPlayer();
		
		return str;
	}
	
	@Override
	public String toString() {
		return "   GameWorld:   #levels:  " + levels.length + "   #players:  " + players.length;
	}
}
