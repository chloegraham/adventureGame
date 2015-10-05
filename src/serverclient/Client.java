package serverclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import testconvert.Layers;
import testconvert.Messages;
import userinterface.UserInterface;

public class Client implements Runnable {
	private String host;
	private int port;
	private Socket connection;
	private InetAddress address;
	
	private DataOutputStream output;
	private DataInputStream input;
	
	private UserInterface ui;
	
	public Client() {
		ui = new UserInterface(this);
	}
	
	public void run() {
	    host = "localhost";  // Define a host server
	    port = 19999;	// Define a port
	 
	    try {
	    	System.out.println("CLIENT: SocketClient initialized");	// Try and connect to the Server
	        address = InetAddress.getByName(host);	// Obtain an address object of the server
	        connection = new Socket(address, port); // Establish a socket connection 
	        System.out.println("CLIENT: I've connected to the Server :)"); 
	        
	    	output = new DataOutputStream(connection.getOutputStream());
	    	input = new DataInputStream(connection.getInputStream());
 
	    	while (input.available() == 0) {
	    	}
	    	// Get initial GameState from Server
	    	System.out.println("CLIENT: Waiting for initial GameState from Server");
			clientDecode();
			System.out.println("CLIENT: I should have printed the initial GameState by now.");
	    	
	   } catch (IOException f) {
		   System.out.println("IOException: " + f);
	   } 
	}
	
	public void passClientAction(int action) {
		try {
			while (output == null) {
			}
			
			output.writeInt(action);
			System.out.println("CLIENT: -- Action sent --- It was (" + action + ") now wait");
			
			System.out.println("CLIENT: Waiting for Game State from the Server");
			clientDecode();
			System.out.println("CLIENT: ------------ Here is the Game State from the Server");
		
		} catch (IOException f) {
			System.out.println("IOException: " + f);
		}
	}
	
	private void clientDecode() {
		try {
			String encodedInput = input.readUTF();
			// I'll receive an ENCODED String from the Server
			// ENCODED either: levelImage & message
			//				   levelImage & message & keyUpdate	
			// Client decode
			// x3 splits = levelImage & message & keyUpdate
			// x2 splits = levelImage & message
			// x1 splits = levelImage
			
			String[] encodedSplit = encodedInput.split("<Split>");
			
			String encodedLayers = encodedSplit[0];
			String encodedMessages;
			String encodedKeys;
			
			Layers layers;
			Messages msgs;
			
			layers = new Layers();
			layers.decode(encodedLayers);
			ui.redrawFromLayers(layers.getDecodedLevel(), layers.getDecodedObjects(), layers.getDecodedMovables());
			
			if (encodedSplit.length > 1) {
				encodedMessages = encodedSplit[1];
				msgs = new Messages();
				msgs.decode(encodedMessages);
				ui.addMessage(msgs.getDecoded());
			}
			
			if (encodedSplit.length == 3) {
				encodedKeys = encodedSplit[2];
				ui.setKeyCount(Integer.parseInt(encodedKeys));
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
