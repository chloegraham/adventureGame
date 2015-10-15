package client.main;

import client.Client;

public class Main {
	public static void main(String[] args) {
		Client client = new Client(7);
		Thread tClient = new Thread(client);
		tClient.start();
	}
}
