package gameWorld;

import serverclient.LevelState;

public class GameWorld {
	
	private GameLogic logic;
	private Level222 level;
	
	public GameWorld(String gameWorldEncodedString){
		LevelState levelState = new LevelState(gameWorldEncodedString);
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
