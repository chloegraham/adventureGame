package server.main;

import server.Server;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Server server = new Server();
		Thread tServer = new Thread(server);
		tServer.start();
	}
}
