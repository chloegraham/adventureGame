package main;


import serverclient.Client;
import serverclient.Server;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Server server = new Server();
		Client client = new Client(888);
		Client client2 = new Client(555);
		
		Thread tServer = new Thread(server);
		Thread tClient = new Thread(client);
		Thread tClient2 = new Thread(client2);
		
		tServer.start();
		tClient.start();
		tClient2.start();
	}
}
