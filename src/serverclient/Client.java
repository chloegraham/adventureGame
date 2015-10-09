package serverclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import testconvert.Layers;
import testconvert.Messages;
import userinterface.Action;
import userinterface.Action.Actions;
import userinterface.UserInterface;

public class Client implements Runnable {
	private String host = "localhost";
	private int  port = 19999;
	private InetAddress address; 
	
	private Socket clientSocket;
	private DataOutputStream output;
	private DataInputStream input;
	
	private String hostORguest = "n/a";
	private int userID;
	private UserInterface ui;
	
	
	// TODO just for testing
	private int testID;
	// TODO just for testing
	public Client(int test) {
		testID = test;
	}
	
	public void run() {
	    try {
	    	// Open UI Frame
	    	// Show Splash = "waiting to connect to Server"
	    	ui = new UserInterface(this);
	    	System.out.println(toString());
	    	
	    	
	    	
	    	// Try and connect to Server
	    	address = InetAddress.getByName(host);		// Convert host to address
	    	clientSocket = new Socket(address, port); 	// Try connect to the Server
	    	System.out.println(toString());
	    	
	    	
	    	
	    	// Means of talking to Server
	    	output = new DataOutputStream(clientSocket.getOutputStream());
	    	input = new DataInputStream(clientSocket.getInputStream());
	    	System.out.println(toString());
	    	
	    	
	    	
	    	/*
	    	 *  Server will send one Client "host".
	    	 *  If Client receives "host" then they are Player ONE and the host with access to the menu
	    	 *  BUT need to handle the fact that "host" message might never come
	    	 *  If doesn't contain "host" if will be a game render
	    	 */
	    	hostORguest = input.readUTF();
	    	
	    	if (hostORguest.equals("host"))
	    		userID = Server.PLAYER_ONE;
	    	else if (hostORguest.equals("guest")) 
	    		userID = Server.PLAYER_TWO;
	    	else 
	    		throw new IllegalArgumentException("Expected host or guest");
	    	
	    	ui.setUserID(userID);
    		output.writeUTF(hostORguest);
	    	
    	
	    	
	    	
	    	
	    	/*
	    	 *  Listen for Renders
	    	 */
	    	while (true) { 
		    	decodePassGameToUI();
	    	}
	    	
	    	
	   } catch (IOException e) {
		   e.printStackTrace();
	   } finally {
		   System.out.println(toString() + " finally.");
		   try {
			   clientSocket.close();
		   } catch (IOException e) { e.printStackTrace(); }
	   }
	}
	
	public void handleAction(int ordinal) {
		try {
			handleAction(ordinal, userID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleAction(int ordinal, int userID) throws IOException {
		if (ordinal == Actions.LOAD.ordinal() || ordinal == Actions.NEW.ordinal())
			if (userID != Server.PLAYER_ONE && this.userID != Server.PLAYER_ONE)
				throw new IllegalArgumentException("Only player one should be able to Start a NewGame or Load a Game.");
		output.writeUTF("<action>" + ordinal);
	}
	
	private void decodePassGameToUI() {
		try {
			String encodedInput = input.readUTF();
			// I'll receive an ENCODED String from the Server
			// ENCODED either: levelImage & message
			//				   levelImage & message & keyUpdate	
			// Client decode
			// x3 splits = levelImage & message & keyUpdate
			// x2 splits = levelImage & message
			// x1 splits = levelImage
			System.out.println(toString() + " || " + encodedInput);
			
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
	
	
	
	@Override
	public String toString() {
		if (clientSocket == null)
			return "*** Client( testid-" +testID+ "  userid-" +userID+ "):    socketStatus- null    isHost?- " +hostORguest;
		return "*** Client( testid-" +testID+ "  userid-" +userID+ "):    socketStatus- " +clientSocket.getPort()+"/"+clientSocket.getLocalPort()+ "    isHost?- " +hostORguest;
	}
}
