package server.main;

import server.Server;

/**
 * Starts up an instance of the server
 */
public class Main {
	public static void main(String[] args) throws InterruptedException {
		Server server = new Server();
		Thread tServer = new Thread(server);
		tServer.start();
	}
}
