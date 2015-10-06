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
	private String host = "localhost";
	private int  port = 19999;
	private InetAddress address; 
	
	private DataOutputStream output;
	private DataInputStream input;
	
	private int userID;
	private UserInterface ui;
	
	// TODO just for testing
	private int testID;
	
	public Client(int test) {
		testID = test;
	}
	
	public void run() {
	    try {
	    	// Create UI which puts up Splash with "waiting for connection"
	    	// ui = new UserInterface(this);
	    	
	    	// Try and connect to Server
	    	address = InetAddress.getByName(host);				// Convert host to address
	    	Socket clientSocket = new Socket(address, port); 	// Try connect to the Server
	        
	    	// Means of talking to Server
	    	output = new DataOutputStream(clientSocket.getOutputStream());
	    	input = new DataInputStream(clientSocket.getInputStream());
 
	    	System.out.println(toString() + " " + clientSocket.toString());
	    	
	    	userID = 0;
	    	while (userID == 0)
	    		userID = input.readInt();
	 
	    	
	    	// if (userID==1111) UI changes to Menu screen = new, load, save
	    	// ui.addUserID(userUD);
	    	// message that waiting for Player Two to connect
	    	
	    	
	    	// if (userID==2222) UI changes to tell Player One that Player Two is ready now
	    	// ui.addUserID(userUD);
	    	// new/load/save become enabled for Player One
	    	// Player Two has Splash saying that Player One is choosing a game
	    	
	    	
	    	System.out.println(userID +  " " + toString() + " " + clientSocket.toString());
	    	
	    	clientSocket.close();
	    	
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
	}
	
	@Override
	public String toString() {
		return "Client " + testID;
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
