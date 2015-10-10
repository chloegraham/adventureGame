package serverclient;

import gameWorld.GameLogic;
import gameWorld.GameWorld;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import saveload.XML;
import userinterface.Action.Actions;

public class Server implements Runnable {
	
	// Only ONE GameWorld & GameLogic
	private static GameWorld gameWorld;
	private static GameLogic logic;
	
	// Only ONE ServerSocket so = static
	private final static int port = 19999;
	private static ServerSocket serverSocket;
	
	
	// x1 Socket for each Player/Client
	private static Socket socketOne;
	private DataOutputStream outputOne;
	private DataInputStream inputOne;
	
	
	// x1 Thread for each Player/Client
	private static Socket socketTwo;
	private DataOutputStream outputTwo;
	private DataInputStream inputTwo;
	
	
	public static final int PLAYER_ONE = 101;
	public static final int PLAYER_TWO = 202;
	
	
	public void run() {
	    try{
	    	// ONLY ONE - Initialize ServerSocket
	    	serverSocket = new ServerSocket(port);
	    	System.out.println(toString());
	    	
	    	
	    	
	    	/*
	    	 *  Listen for first Client connection
	    	 */
	    	socketOne = serverSocket.accept();
	    	inputOne = new DataInputStream(socketOne.getInputStream());
	    	outputOne = new DataOutputStream(socketOne.getOutputStream());
	    	System.out.println(toString());
	    	
	    	
	    	
	    	/*
	    	 *  Tell the Client who connected to socketOne that they are PlayerONE and Host
	    	 *  that PlayerONE can see the Menu-new,load
	    	 *  (when they choose one until a render comes back show "waiting for player two")
	    	 */
	    	outputOne.writeUTF("host");
	    	String confirmHost = inputOne.readUTF();
	    	if (!confirmHost.equals("host"))
	    		throw new IllegalArgumentException("Attempt to confirm the Host failed.");
	    	
	    	
	    	
	    	/*
	    	 *  Listen for second Client connection
	    	 */
	    	socketTwo = serverSocket.accept();
	    	inputTwo = new DataInputStream(socketTwo.getInputStream());
	    	outputTwo = new DataOutputStream(socketTwo.getOutputStream());
	    	System.out.println(toString());
	    	
	    	
	    	
	    	/*
	    	 *  Tell the Client who connected to socketTwo that they are PlayerTWO and Guest
	    	 *  that PlayerTWO has a splash that they are waiting for game render
	    	 */
	    	outputTwo.writeUTF("guest");
	    	String confirmGuest = inputTwo.readUTF();
	    	if (!confirmGuest.equals("guest"))
	    		throw new IllegalArgumentException("Attempt to confirm the Guest failed.");
	    	System.out.println(toString());
	    	
	
	    	
	    	/*
	    	 *  Listen for Actions from Clients
	    	 */
	    	while (true) { 
	    		
	    		if (inputOne.available() > 0) {
			    	String action = inputOne.readUTF();
			    	if (!action.contains("<action>"))
			    		throw new IllegalArgumentException("A handle action was sent without 'action' in the string (or wasn't even an action.)");
			    	System.out.println(inputOne.toString() + "  " + action);
			    	int ordinal = Integer.parseInt(action.substring("<action>".length()));
			    	System.out.println(ordinal);
			    	
			    	if (gameWorld == null)
		    			if (ordinal != Actions.NEWGAME.ordinal() && ordinal != Actions.LOAD.ordinal())
		    				throw new IllegalArgumentException("GameWorld still null and Client is trying to send Actions that aren't New or Load");
			    	
			    	if (ordinal == Actions.NEWGAME.ordinal())
			    		newGame();
			    	else if (ordinal == Actions.LOAD.ordinal())
			    		load();
			    	else
			    		handleAction(ordinal, Server.PLAYER_ONE);
	    		}
		    	
		    	if (inputTwo.available() > 0) {
			    	String action = inputTwo.readUTF();
			    	if (!action.contains("<action>"))
			    		throw new IllegalArgumentException("A handle action was sent without 'action' in the string (or wasn't even an action.)");
			    	System.out.println(inputOne.toString() + "  " + action);
			    	int ordinal = Integer.parseInt(action.substring("<action>".length()));
			    	System.out.println(ordinal);
			    	
			    	if (gameWorld == null || ordinal == Actions.NEWGAME.ordinal() || ordinal == Actions.LOAD.ordinal())
			    			throw new IllegalArgumentException("Player TWO should never be able to send Action with null GameWorld. Also Player TWO should never be able to New or Load Game.");
			    	handleAction(ordinal, Server.PLAYER_TWO);
		    	}
	    	}

	    	
	    	
	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {
	    	System.out.println(toString() + " finally.");
	    	try {
				serverSocket.close();
			} catch (IOException e) { e.printStackTrace(); }
	    }
    }
	
	
	
	private void handleAction(int ordinal, int userID) throws IOException {
		logic.handleAction(ordinal, userID);
		broadcast();
	}



	private void broadcast() throws IOException {
		String game = gameWorld.getEncodedGameWorld();
		outputOne.writeUTF(game);
		outputTwo.writeUTF(game);
	}



	/*
	 *  New, Load, Save
	 */
	public void newGame() throws IOException {
		//TODO: call newGame() inside run at some point, ATM XML.newGame() hardcoded
		String encodedGameWorld = XML.newGame();
		gameWorld = new GameWorld(encodedGameWorld);
		logic = gameWorld.getLogic();
		broadcast();
	}
	
	public void load() throws IOException {
		//TODO: call load() inside run at some point, if return == null issue loading
		String encodedGameWorld = XML.load();
		gameWorld = new GameWorld(encodedGameWorld);
		logic = gameWorld.getLogic();
		broadcast();
	}
	
	public boolean save() {
		//TODO: call save(String representing the gameState) inside run at some point
		return XML.save(gameWorld.getEncodedGameSave());
	}

	
	
	
	@Override
	public String toString() {
		return "--- Server(ServerSocket- " +(serverSocket!=null)+ "):    socketONEstatus- " +(socketOne!=null)+ "    socketTWOstatus- " +(socketTwo!=null)+ "  ";
	}
}