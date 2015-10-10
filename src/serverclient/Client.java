package serverclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import testconvert.Layers;
import testconvert.Messages;
import userinterface.Action.Actions;
import userinterface.UserInterface;

/**
 * Opens the Users UserInterface and connects to the Game Server.
 * 
 * @author benscully
 */
public class Client implements Runnable {
	
	// Server address details
	private String host = "localhost";
	private int  port = 19999;
	private InetAddress address; 
	
	// Socket & Streams for sending/receiving data to the Server
	private Socket clientSocket;
	private DataOutputStream output;
	private DataInputStream input;
	
	// UserID of both the Client and the UI - Server sets UserID later on
	private int userID;
	private UserInterface ui;
	
	
	// TODO just for testing
	private String hostORguest = "n/a";  	// TODO put this inside run() once it's no longer used for testing
	private int testID;						// TODO remove 'testID' field & @param once testing finished
	public Client(int test) {
		testID = test;
	}
	

	
	public void run() {
	    try {
	    	// Open the UserInterface and tell the User/Player they are "waiting to connect to Server"
	    	ui = new UserInterface(this);
	    	System.out.println(toString());
	    	
	    	
	    	
	    	// Try and connect to Server
	    	address = InetAddress.getByName(host);	
	    	clientSocket = new Socket(address, port); 
	    	System.out.println(toString());
	    	
	    	
	    	
	    	// create DataStream for communicating with the Server
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
	    	
	    	ui.setUserID(userID, false);
    		output.writeUTF(hostORguest);
	    	
    
	    	
	    	/*
	    	 *  Listen for Renders of the GameWorld
	    	 */
	    	while (true)
		    	decodePassGameToUI();
	    	
	    	
	   } catch (IOException e) {
		   e.printStackTrace();
	   } finally {
		   System.out.println(toString() + " finally.");
		   try {
			   clientSocket.close();
		   } catch (IOException e) { e.printStackTrace(); }
	   }
	}
	
	
	
	/**
	 * @param ordinal of the Actions Enum (e.g. Move.West, Interact)
	 */
	public void handleAction(int ordinal) {
		try {
			handleAction(ordinal, userID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	// Manage some rules before sending to the Server
	private void handleAction(int ordinal, int userID) throws IOException {
		if (ordinal == Actions.LOAD.ordinal() || ordinal == Actions.NEWGAME.ordinal())
			if (userID != Server.PLAYER_ONE && this.userID != Server.PLAYER_ONE)
				throw new IllegalArgumentException("Only player one should be able to Start a NewGame or Load a Game.");
		output.writeUTF("<action>" + ordinal);
	}
	
	
	
	private void decodePassGameToUI() {
		try {
			String encodedInput = input.readUTF();
			
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
