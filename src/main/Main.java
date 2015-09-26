package main;


import gameWorld.GameLogic;
import serverclient.Server;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		GameLogic logic = new GameLogic();
		Server server = new Server(logic);
		Thread t = new Thread(server);
		t.start();
	}
}
