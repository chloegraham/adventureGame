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
	
	public Server(GameLogic logic) {
		this.logic = logic;
	}
	
	public void run() {
	    try{
	      socket1 = new ServerSocket(port);
	      System.out.println("SERVER: SingleSocketServer Initialized");
	      
	      	  // Do nothing until connected to ONE client
	    	  System.out.println("SERVER: Waiting for Client");
	          connection = socket1.accept();
	          System.out.println("SERVER: Client found me :)");

	          input = new DataInputStream(connection.getInputStream());
	          output = new DataOutputStream(connection.getOutputStream());
		         
	          System.out.println("SERVER: Sending Client initial GameState");
	         
	          String newGameEncodedString = XML.newGame();
	          gameWorld = new GameWorld(newGameEncodedString);
	          logic = gameWorld.getLogic();
	          
	          String gameWorldEncoded = gameWorld.touchSelf();
	          output.writeUTF(gameWorldEncoded);
	          System.out.println("SERVER: I've sent the INITIAL GameState.");
		          
	          // Every 5 seconds check for an Action from Client and then send Client the GameState  
	          while (true) {
	        	  
//	        	  // 5 second timer
//	        	  try {	Thread.sleep(5000);	} catch (Exception e) {}
	        	  
	        	  // Check for an Action
	        	  System.out.println("SERVER: Checking for an action");
		          int action = input.readInt();
		          logic.handleAction(action, 999);
		          System.out.println("SERVER: I sent action: - " + action + " to the Client.");
		      
		          // Send Client the GameState
		          System.out.println("SERVER: Sending Client the GameState");
		          
//		          char[][] gsArray = logic.getGameWorld();
//		          GameState gs = new GameState(gsArray, gsArray, gsArray);
//		          String gameState = gs.getGameStateString();
		          
		          GameState version2b = logic.getGameWorld2();
		          String s2 = version2b.getGameStateString();
		          output.writeUTF(s2);
		          System.out.println("SERVER: I've sent the GameState. Lets see if Client gets it");
	          }
		          
	      } catch (IOException e) {}
		      	try {
		      		connection.close();
		        } catch (IOException e) {}
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