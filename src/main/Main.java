package main;


import gameWorld.GameLogic;
import serverclient.Client;
import serverclient.Server;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		GameLogic logic = new GameLogic();
		Server server = new Server(logic);
		Client client = new Client();
		
		Thread tServer = new Thread(server);
		Thread tClient = new Thread(client);
		
		tServer.start();
		tClient.start();
	}
}
