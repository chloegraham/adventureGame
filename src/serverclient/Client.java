package serverclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

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
			String s = input.readUTF();
			GameState gs = new GameState(s);
			ui.redraw(gs.getLayerStatic());
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
			String s = input.readUTF();
			GameState gs = new GameState(s);
			ui.redraw(gs.getLayerStatic());
			System.out.println("CLIENT: ------------ Here is the Game State from the Server (" + s + ") thoughts?");
		
		} catch (IOException f) {
			System.out.println("IOException: " + f);
		}
	}
}

	
	
	
	
	
//	// TEMP
//	private int uid = 0;
//	// TEMP
//	public int getUID() {
//		return uid;
//	}
//	
//	public int getAction() {
//		return  ui.getAction();
//	}
//	
//	public void updateClient(char[][] gameWorld) {
//		ui.redraw(gameWorld);
//		
//		char[][] testLevel =   {{'w','w','w','w'},
//                {'w','e','e','e'},
//                {'w','e','e','e'},
//                {'w','e','e','e'}};
//    	
//    	char[][] testObjects =   {{'n','n','n','n'},
//    			{'n','n','n','n'},
//    			{'n','c','n','n'}, // Notice i'm using 'n' for nothing
//    			{'n','n','n','n'}};
//    	
//    	char[][] testMoveables =  {{'n','n','n','n'},
//    			{'n','n','n','p'},
//    			{'n','n','n','n'},
//    			{'n','n','n','n'}};
//    	
//		// This method will do same shit as redraw, but from multiple layers
//		//ui.redrawFromLayers(testLevel, testObjects, testMoveables);
//	}
//}
