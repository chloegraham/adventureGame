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
	      socket1 = new ServerSocket(port);
	      System.out.println("SERVER: SingleSocketServer Initialized");
	      
	      	  // Do nothing until connected to ONE client
	    	  System.out.println("SERVER: Waiting for Client");
	          connection = socket1.accept();
	          System.out.println("SERVER: Client found me :)");

	          input = new DataInputStream(connection.getInputStream());
	          output = new DataOutputStream(connection.getOutputStream());
		         
	          System.out.println("SERVER: Sending Client initial GameState");
	         
	          String newGameEncodedString = newGame();
	          gameWorld = new GameWorld(newGameEncodedString);
	          logic = gameWorld.getLogic();
	          
	          String gameWorldEncoded = gameWorld.touchSelf();
	          output.writeUTF(gameWorldEncoded);
	          System.out.println("SERVER: I've sent the INITIAL GameState.");
		          
	          // Every 5 seconds check for an Action from Client and then send Client the GameState  
	          while (true) {
	        	  
	        	  handleActions();
	          }
		          
	      } catch (IOException e) {}
		      	try {
		      		connection.close();
		        } catch (IOException e) {}
    }
	
	private void handleActions() {
		try {
			// Listen for Client/User action & ask GameLogic to handle it
			System.out.println("SERVER: Checking for an action");
	        int action = input.readInt();
	        String actionResponse = logic.handleAction(action, 999);
	        System.out.println("SERVER: I sent action: - " + action + " to the Client.");
	    
	        
	        // Send Client the updated GameState
	        System.out.println("SERVER: Sending Client the GameState");
	        
	        // Need to send the GameState for the Renderer & a Message for UI
	        
	        GameState gameWorld = logic.getGameWorld();
	        gameWorld.addMessage(actionResponse);
	        String gameWorldRenderAndMessageEncoded = gameWorld.getEncodedLayers();
	        output.writeUTF(gameWorldRenderAndMessageEncoded);
	        
	        System.out.println("SERVER: I've sent the GameState. Lets see if Client gets it");
        
		} catch (IOException e) {
			// TODO Auto-generated catch block
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