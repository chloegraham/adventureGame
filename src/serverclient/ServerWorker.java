package serverclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerWorker implements Runnable {
 
	private Server server;
	private Socket clientSocket;
	
	private DataOutputStream output;
	private DataInputStream input;
	
	private int userID;
	
	public ServerWorker(Server server, Socket clientSocket, int userID) {
		this.server = server;
		this.clientSocket = clientSocket;
		this.userID = userID;
	}
	
	public void run() {
	    try{
	    	/*
	    	 *  Create DataStreams to pass information between Client & Server.
	    	 */
	    	input = new DataInputStream(clientSocket.getInputStream());
	    	output = new DataOutputStream(clientSocket.getOutputStream());
	    	System.out.println(toString() + " " + clientSocket.toString());
	    	
	    	
	    	
	    	/*
	    	 *  Tell the Client which Player they are.
	    	 */
	    	output.writeInt(userID);
	    	
	    	
	    	
	    	/*
	    	 *  Listen for the NewGame or LoadGame
	    	 */
	    	String newORload = input.readUTF();
	    	String encodedGameWorld;
	    	
	    	if (newORload.equals("NewGame"))
	    		encodedGameWorld = server.newGame();
	    	else if (newORload.equals("LoadGame"))
	    		encodedGameWorld = server.load();
	    	else
	    		throw new IllegalArgumentException();
	    	
	    	
	    	
	    	/*
	    	 *  Send first render to the UI
	    	 */
	    	output.writeUTF(encodedGameWorld);
	    	
	    	
	    	
	    	/*
	    	 *  The game should have begun so just Listen for Actions
	    	 */
	    	while (true) {
	    		handleActions();
	    	}
	    	
	    	
	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {
	    	try {
				clientSocket.close();
			} catch (IOException e) { e.printStackTrace(); }
	    }
    }
	
	
	@Override
	public String toString() {
		return "Worker " + userID;
	}
	
	
	private void handleActions() {
		try {
			
			
	        int action = input.readInt();
	        String actionMsg = server.handleAction(action, userID);
	 
	        
	        
	        String encodedGameWorld = server.getEncodedGameWorld();
	        encodedGameWorld += actionMsg;		
	        output.writeUTF(encodedGameWorld);
	        
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}