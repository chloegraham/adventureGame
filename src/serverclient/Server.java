package serverclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import gameWorld.GameLogic;

public class Server implements Runnable {
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
	         
//	          char[][] gs1 = logic.getGameWorld();
//	          GameState gs2 = new GameState(gs1, gs1, gs1);
//	          String gs3 = gs2.getGameStateString();
	         
	          GameState version2a = logic.getGameWorld2();
	          String s1 = version2a.getGameStateString();
	          output.writeUTF(s1);
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
}