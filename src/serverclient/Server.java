package serverclient;

import gameWorld.GameLogic;
import gameWorld.GameWorld;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import saveload.XML;

public class Server implements Runnable {
	private GameWorld gameWorld;
	private GameLogic logic;
	
	private static ServerSocket socket1;
	private final static int port = 19999;
	private static Socket connection;
 
	private DataOutputStream output;
	private DataInputStream input;
	
	public void run() {
	    try{
	    	System.out.println("SERVER: SingleSocketServer Initialized");
	    	socket1 = new ServerSocket(port);


	    	/*
	    	 *  Do NOTHING until a Client connects (just one at this point)
	    	 */
	    	System.out.println("SERVER: Waiting for Client");
	    	
	    	connection = socket1.accept();
	    	input = new DataInputStream(connection.getInputStream());
	    	output = new DataOutputStream(connection.getOutputStream());
	    	
	    	System.out.println("SERVER: Client found me :)");


	    	/*
	    	 *  Send the Client(s) the initial GameState
	    	 */
	    	System.out.println("SERVER: Sending Client initial GameState");

	    	String encodedNewGame = newGame();						    // Get encoded String of a New Game from XML file.
	    	gameWorld = new GameWorld(encodedNewGame);				    // Create a new GameWorld by decoding the New Game String
	    	logic = gameWorld.getLogic();							    // Get a GameLogic reference to communicate with GameWorld
				    
	    	String encodedGameState = gameWorld.getEncodedGameWorld();  // Get a copy of the GameWorld at one point in time (referred to as GameState)
	    	output.writeUTF(encodedGameState);						    // Send encoded String through the Socket

	    	System.out.println("SERVER: I've sent the INITIAL GameState.");


	    	/*
	    	 *  DUMMY Method for now on an Infinite LOOP
	    	 */
	    	while (true) {
	    		handleActions();
	    	}
	   
//	    	connection.close(); 	
	    } catch (IOException e) {
	    	
	    }
    }
	
	private void handleActions() {
		try {
			/*
			 *  Listen for Client/User action & ask GameLogic to handle it
			 */
			System.out.println("SERVER: Checking for an action");
	        int action = input.readInt();
	        String actionResponse = logic.handleAction(action, 101);
	        System.out.println("SERVER: I sent action: - " + action + " to the Client.");
	    
	        
	        
	        /*
	         *  Return Client GameState including Messages about chosen Action
	         */
	        System.out.println("SERVER: Sending Client the GameState");   
	       
	        String encodedGameWorld = gameWorld.getEncodedGameWorld();  // Convert GameState to an encoded String to send through Socket
	        encodedGameWorld += actionResponse;							// Add Message returned from ActionResponse
	        output.writeUTF(encodedGameWorld);						    // Send encoded String through the Socket
	       
	        System.out.println("SERVER: I've sent the GameState. Lets see if Client gets it");
	        
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String load(){
		//TODO: call load() inside run at some point, if return == null issue loading
		return XML.load();
	}
	
	private boolean save(String gameState){
		//TODO: call save(String representing the gameState) inside run at some point
		return XML.save(gameState);
	}
	
	private String newGame(){
		//TODO: call newGame() inside run at some point, ATM XML.newGame() hardcoded
		return XML.newGame();
	}
}