package gameWorld;

import serverclient.LevelState;

public class GameWorld {
	private String encodedLevelOne;
	private String encodedPlayerOne;
	private GameLogic logic;
	private Level222 level;
	
	public GameWorld(String gameWorldEncodedString){
		String[] split = gameWorldEncodedString.split("<Split>");
		
		encodedLevelOne = split[0];
		encodedPlayerOne = split[1];
				
		LevelState levelState = new LevelState(encodedPlayerOne, encodedLevelOne);
		this.level = new Level222(levelState);
		
		
		this.logic = new GameLogic(level);
	}
	
	public GameLogic getLogic(){
		return this.logic;
	}
	
	public String touchSelf(){
		LevelState lS = new LevelState(level.getStaticLevel(), level.getStateLevel(), level.getMoveableLevel());
		return lS.getEncodedLayers();
	}
	
}
