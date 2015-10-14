package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import client.userinterface.UserInterface;
import server.helpers.Actions;
import server.helpers.Msgs;

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
	    	boolean isLoadValid = false;
	    	
	    	if (hostORguest.equals(Msgs.DELIM_HOST)) {
	    		userID = Msgs.PLAYER_ONE;
	    	} else if (hostORguest.equals(Msgs.DELIM_HOSTLOAD)) {
	    		userID = Msgs.PLAYER_ONE;
	    		isLoadValid = true;
	    	} else if (hostORguest.equals(Msgs.DELIM_GUEST)) {
	    		userID = Msgs.PLAYER_TWO;
	    	} else {
	    		throw new IllegalArgumentException("Expected host or guest");
	    	}
	    	
	    	ui.setUserID(userID, isLoadValid);
    		output.writeUTF(hostORguest);
    		System.out.println(toString());
    
	    	
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
	
	
	
	// vguvguvgh
	private void handleAction(int ordinal, int userID) throws IOException {
		System.out.println("\n*** Client-  Player" + userID + " is trying to do Action " + Actions.getName(ordinal));
		output.writeUTF(Msgs.DELIM_ACTION + ordinal);
	}
	
	
	
	private void decodePassGameToUI() {
		try {
			String encodedInput = input.readUTF();
			if (encodedInput.equals(String.valueOf(Actions.LOAD.ordinal()))){
				ui.setChangedGameState(Actions.LOAD.ordinal());
				return;
			}
			if (encodedInput.equals(String.valueOf(Actions.NEWGAME.ordinal()))){
				ui.setChangedGameState(Actions.NEWGAME.ordinal());
				return;
			}
			
			String[] encodedSplit = encodedInput.split("<Split>");
			
			String encodedLayers = encodedSplit[0];
			String encodedPlayer;
			String encodedMessages;
			
			Layers layers;
			
			int playerX = 0;
			int playerY = 0;
			
			if (encodedSplit.length > 1) {
				encodedPlayer = encodedSplit[1];
				String[] details = encodedPlayer.split(Msgs.DELIM_DETAILS);
				ui.setBoulder(details[0].equals("true"));
				ui.setKeyCount(Integer.parseInt(details[1]));
				playerX = Integer.parseInt(details[2]);
				playerY = Integer.parseInt(details[3]);
			}
			
			layers = new Layers();
			layers.decode(encodedLayers);
			ui.setLayersWithCoordinate(layers.getDecodedWallsEmpties(), layers.getDecodedFurniture(), layers.getDecodedBouldersPlayers(), playerX, playerY);
			
			if (encodedSplit.length > 2) {
				encodedMessages = encodedSplit[2];
				String[] lines = encodedMessages.split("%");
				String str = "";
				for (int i = 0; i != lines.length; i++)
					str += lines[i] + "\n";
				ui.addMessage(str);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public String toString() {
		if (clientSocket == null)
			return "*** Client( testid-" +testID+ "  userid-" +userID+ "):    socketStatus- NOT CONNECTED    isHost?- " +hostORguest;
		return "*** Client( testid-" +testID+ "  userid-" +userID+ "):    socketStatus- CONNECTED " +clientSocket.getPort()+"/"+clientSocket.getLocalPort()+ "    isHost?- " +hostORguest;
	}
}
