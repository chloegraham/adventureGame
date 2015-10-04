package gameWorld;

public class GameWorld {
	
	private GameLogic logic;
	private Level level;

	public GameWorld(String gameWorldEncodedString){
		this.logic = new GameLogic();
		
	}
	
	public GameLogic getLogic(){
		return this.logic;
	}
	
	
}
