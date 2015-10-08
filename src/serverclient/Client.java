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
	
	private Socket clientSocket;
	
	private DataOutputStream output;
	private DataInputStream input;
	
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
	    	// ui = new UserInterface(this);
	    	System.out.println(toString() + ": just before connection attempted   |||   ACTION: open UI Frame + Splash [Splash text: 'waiting to connect to Server']");
	    	
	    	
	    	// Try and connect to Server
	    	address = InetAddress.getByName(host);		// Convert host to address
	    	clientSocket = new Socket(address, port); 	// Try connect to the Server
	    	System.out.println(toString() + ": successful connection to " + clientSocket.getLocalPort() + " but don't have 'userID' yet.    |||    ACTION: no change yet");
	    	
	    	// Means of talking to Server
	    	output = new DataOutputStream(clientSocket.getOutputStream());
	    	input = new DataInputStream(clientSocket.getInputStream());
	    	System.out.println(toString() + ": create DataStreams " + clientSocket.getLocalPort() + "   |||    ACTION: no change yet");
	    	
	    	
	    	
	    	/*
	    	 *  Listen for UserID -> Set Clients UserID -> return UserID to Server for confirmation
	    	 */
	    	userID = 0;
	    	while (userID == 0)
	    		userID = input.readInt();
	 
	    	if (userID == Server.PLAYER_ONE)
	    		System.out.println(toString() + ": set UserID to " + userID + " of port-" + clientSocket.getLocalPort() + "   |||    ACTION: update Splash  [Splash text: 'You're Player ONE. You connected to Server']");
	    	else if (userID == Server.PLAYER_TWO)
	    		System.out.println(toString() + ": set UserID to " + userID + " of port-" + clientSocket.getLocalPort() + "   |||    ACTION: update Splash  [Splash text: 'You're Player TWO. You connected to Server. Waiting for Player ONE to start game']");
	    	else
	    		throw new RuntimeException(toString() + "??");
	    	
	    	output.writeInt(userID);
	    	
	    	
	    	
	    	/*
	    	 * 	Broadcast that Client wants to be the 'Host' (party host) only one will ever become the Hots
	    	 */
	    	output.writeUTF(Server.HOST);
	    	
	    	String host = "z";
	    	while (host.equals("z"))
	    		host = input.readUTF();
	    	
	    	if (host.equals(Server.HOST)) {
	    		System.out.println(toString() + "  I'm the HOST. <host string from serverWorker> " + host);
	    		// Tell the UI that they are the Host [so they can see and press the menu 'new' & 'load']
	    		
	    		// listen for menu choice FROM UI and then pass it to the server to build the GameWorld
	    		passServerMenuChoice("new");
	    	} else {
	    		System.out.println(toString() + "  I'm a FOLLOWER waiting for a game. <host string from serverWorker> " + host);
	    		
	    		// show a message that we/Client is waiting for the other player
	    		
	    	}
	    	
	    	// expecting serverWorker to send us initial game for render
	    	// listen for a initial game to render
	    	decodePassGameToUI();
	    	
	    	
	    	while (true) {
	    		// listen for new game renders
	    		// OR
	    		// u.i. passing us actions
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
	
	
	public void passServerMenuChoice(String menuChoice) {
		try {
			
			if (menuChoice.equals("new"))
				output.writeUTF("new");
			else if (menuChoice.equals("load"))
				output.writeUTF("load");
			else
				throw new IllegalArgumentException("Invalid menuChoice");
			
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	
	public void passClientAction(int action) {
		try {
			while (output == null) {
			}
			output.writeInt(action);
			decodePassGameToUI();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void decodePassGameToUI() {
		try {
			System.out.println("here?!?");
			String encodedInput = input.readUTF();
			// I'll receive an ENCODED String from the Server
			// ENCODED either: levelImage & message
			//				   levelImage & message & keyUpdate	
			// Client decode
			// x3 splits = levelImage & message & keyUpdate
			// x2 splits = levelImage & message
			// x1 splits = levelImage
			
			
			
			// TODO testing
			System.out.println("here?!?");
			
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
		return "*** client " + testID;
	}
}
