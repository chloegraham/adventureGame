package serverclient;

import gameWorld.GameLogic;
import gameWorld.GameWorld;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import saveload.XML;

public class Server implements Runnable {
	
	// Only ONE GameWorld & GameLogic
	private static GameWorld gameWorld;
	private static GameLogic logic;
	
	// Only ONE ServerSocket so = static
	private final static int port = 19999;
	private static ServerSocket serverSocket;
	
	// x1 Thread for each Player/Client
	private ServerWorker playerOne;
	private ServerWorker playerTwo;
	
	// x1 Socket for each Player/Client
	private static Socket socketOne;
	private static Socket socketTwo;
	
	private static final int PLAYER_ONE = 1111;
	private static final int PLAYER_TWO = 2222;
	
	
	public void run() {
	    try{
	    	// ONLY ONE - Initialize ServerSocket
	    	serverSocket = new ServerSocket(port);
	    	System.out.println(toString());
	    	
	    	/*
	    	 *  Listen for first Client connection
	    	 */
	    	socketOne = serverSocket.accept();
	    	
	    	playerOne = new ServerWorker(this, socketOne, PLAYER_ONE);
	    	Thread t1 = new Thread(playerOne);
	    	t1.start();
	    	// Tell first Client they have connected, and are first
	    	// ---- menu opens + message that waiting for player two to connect
	    	
	    	
	    	
	    	
	    	/*
	    	 *  Listen for second Client connection
	    	 */
	    	socketTwo = serverSocket.accept();
	    	
	    	playerTwo = new ServerWorker(this, socketTwo, PLAYER_TWO);
	    	Thread t2 = new Thread(playerTwo);
	    	t2.start();
	    	// Second Client should know it's second
	    	// ---- menu opens + message that waiting for player one to choose 'new / load / save'
	    	// Tell first Client second has now connected.
	    	// ---- 1st menu should now be enabled for 'new / load' selection
	    	
	    	
	    	/*
	    	 *  Listen for 'new/load' selection
	    	 */
	    	
	    	
	    	
	    	/*
	    	 *  Broadcast initial game state
	    	 */
	    	
	    	
	    	
	    	/*
	    	 *  Listen for Client actions
	    	 *  [maybe go get them from Client]
	    	 */
	    	
	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {
	    	try {
				serverSocket.close();
			} catch (IOException e) { e.printStackTrace(); }
	    }
    }
	
	public void newGame() {
		//TODO: call newGame() inside run at some point, ATM XML.newGame() hardcoded
		String encodedGameWorld = XML.newGame();
		gameWorld = new GameWorld(encodedGameWorld);
	}
	
	public void load() {
		//TODO: call load() inside run at some point, if return == null issue loading
		String encodedGameWorld = XML.load();
		gameWorld = new GameWorld(encodedGameWorld);
	}
	
	public boolean save(String gameState) {
		//TODO: call save(String representing the gameState) inside run at some point
		return XML.save(gameState);
	}
	
	@Override
	public String toString() {
		return "Server";
	}
	
	
	
//	/*
//	 *  Send the Client(s) the initial GameState
//	 */
//	System.out.println("SERVER: Sending Client initial GameState");
//
//	String encodedNewGame = newGame();						    // Get encoded String of a New Game from XML file.
//	gameWorld = new GameWorld(encodedNewGame);				    // Create a new GameWorld by decoding the New Game String
//	logic = gameWorld.getLogic();							    // Get a GameLogic reference to communicate with GameWorld
//		    
//	String encodedGameState = gameWorld.getEncodedGameWorld();  // Get a copy of the GameWorld at one point in time (referred to as GameState)
//	output.writeUTF(encodedGameState);						    // Send encoded String through the Socket
//
//	System.out.println("SERVER: I've sent the INITIAL GameState.");
}