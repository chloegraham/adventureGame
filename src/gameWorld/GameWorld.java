package gameWorld;

import movable.Player;
import testconvert.ConvertPlayer;
import testconvert.Messages;

public class GameWorld {
	private GameLogic logic;
	private Level[] levels;
	private Player[] players;
	
	public GameWorld(String encodedGameWorld){
		String[] split = encodedGameWorld.split(Messages.DELIM_SPLIT);
		
		int levelAmount = 0;
		int playerAmount = 0;
		
		for (String s : split) {
			if (s.contains(Messages.DELIM_LEVEL)) {
				levelAmount++;
			}
			else if (s.contains(Messages.DELIM_PLAYER)) {
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
			if (s.contains(Messages.DELIM_LEVEL)) {
				String encodedLevel = s;
				levels[levelIndex++] = new Level(encodedLevel);
			}
			else if (s.contains(Messages.DELIM_PLAYER)) {
				String encodedPlayer = s;
				players[playerIndex++] = ConvertPlayer.toPlayer(encodedPlayer);
			}
			else {
				throw new IllegalArgumentException();
			}
		}
		
		// TODO testing 
		levels[0].addPlayers(players);
		logic = new GameLogic(levels, players);
	}
	
	public GameLogic getLogic() { return logic; }
	
	
	public String getEncodedGameWorld(int userID){
		String str = "";
		//player.getLevel
		Player currentPlayer = null;
		for(Player p: this.players){
			if(p.getUserID() == userID){
				currentPlayer = p;
			}
		}
		if(currentPlayer == null){
			throw new IllegalArgumentException("Passed in userID doesn't belong to a player");
		}

		int levelID = currentPlayer.getLevelID();
		for(int i = 0; i < levels.length; i++){
			if(levels[i].getLevelID() == levelID){
				return str += levels[i].getEncodedLevel();
			}
		} 
		throw new IllegalArgumentException("No such level that matches player's level");
		//get the level that the player's on
	}
	
	
	public String getEncodedGameSave(){
		String str = "";
		
		for (int i = 0; i != levels.length; i++)
			str += levels[0].getEncodedLevel();
		
		for (int i = 0; i != players.length; i++)
			str += players[0].getEncodedPlayer();
		
		return str;
	}
}
