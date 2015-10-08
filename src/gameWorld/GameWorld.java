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
		
		for (String s  : split) {
			if (s.contains(Messages.DELIM_LEVEL)) {
				String encodedLevel = s;
				levels[levels.length-1] = new Level(encodedLevel);
			}
			else if (s.contains(Messages.DELIM_PLAYER)) {
				String encodedPlayer = s;
				players[players.length-1] = ConvertPlayer.toPlayer(encodedPlayer);
			}
			else {
				throw new IllegalArgumentException();
			}
		}	
		levels[0].addPlayers(players);
		logic = new GameLogic(levels, players);
	}
	
	public GameLogic getLogic() { return logic; }
		
	public String getEncodedGameWorld(){
		String str = "";
		
		for (int i = 0; i != levels.length; i++)
			str += levels[0].getEncodedLevel();
		
		return str;
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
