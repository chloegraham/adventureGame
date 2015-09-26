package serverclient;

import gameWorld.GameLogic;

public class Server implements Runnable {
	private Client client;
	private GameLogic logic;
	
	public Server(GameLogic logic) {
		this.client = new Client();
		this.logic = logic;
	}
	
	public void run() {
		while (true) {
			// Get int.action from Client and tell GameLogic
			int action = client.getAction();
			int uid = client.getUID();
			logic.handleAction(action, uid);
			
			
			// Get char[][].gameWorld and send it to the Client/Renderer
			char[][] gameWorld = logic.getGameWorld();
			client.updateClient(gameWorld);
			
			
		}
	}
}
