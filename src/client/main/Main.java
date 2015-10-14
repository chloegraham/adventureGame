package client.main;

import client.Client;

public class Main {
	public static void main(String[] args) {
		Client client = new Client(7);
		Client client2 = new Client(8);
		Thread tClient = new Thread(client);
		Thread tClient2 = new Thread(client2);
		tClient.start();
		tClient2.start();
		
	}
}
