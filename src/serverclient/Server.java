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
	
	public static final int PLAYER_ONE = 1111;
	public static final int PLAYER_TWO = 2222;
	public static final String HOST = "Host";
	public static final String FOLLOWER = "Follower";
	
	public void run() {
	    try{
	    	// ONLY ONE - Initialize ServerSocket
	    	serverSocket = new ServerSocket(port);
	    	System.out.println(toString() + ": ServerSocket initialised   |||   ACTION: n/a  ");
	    	
	    	
	    	
	    	/*
	    	 *  Listen for first Client connection
	    	 */
	    	socketOne = serverSocket.accept();
	    	System.out.println(toString() + ": PlayerOne successful connection " + socketOne.getPort() + "   |||   ACTION: Worker111 should tell Client " + socketOne.getPort() + " they are PlayerONE");
	    	
	    	playerOne = new ServerWorker(this, socketOne, PLAYER_ONE);
	    	Thread t1 = new Thread(playerOne);
	    	t1.start();
	    	// Tell Client connecting to SocketOne they are PlayerONE
	    	// DONE IN WORKER
	    	
	    	
	    	
	    	
	    	/*
	    	 *  Listen for second Client connection
	    	 */
	    	socketTwo = serverSocket.accept();
	    	System.out.println(toString() + ": PlayerTWO successful connection " + socketOne.getPort() + "   |||   ACTION: Worker222 should tell Client " + socketOne.getPort() + " they are PlayerTWO");
	    	
	    	playerTwo = new ServerWorker(this, socketTwo, PLAYER_TWO);
	    	Thread t2 = new Thread(playerTwo);
	    	t2.start();
	    	// Tell Client connecting to SocketOne they are PlayerONE
	    	// DONE IN WORKER
	    	
	    	
	    	
	    	// Stop Server Closing
	    	 while (true) { 
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
		return "--- server";
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