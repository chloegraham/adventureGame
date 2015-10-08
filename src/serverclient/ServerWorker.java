package serverclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerWorker implements Runnable {
 
	private Server server;
	private Socket clientSocket;
	
	private DataOutputStream output;
	private DataInputStream input;
	
	private static Lock lock  = new ReentrantLock();
	private static boolean hostAvailable = true;
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
	    	System.out.println(toString() + ":    port = " + clientSocket.getPort());
	    	
	    	
	    	
	    	/*
	    	 *  Tell the Client which Player they are.
	    	 */
	    	output.writeInt(userID);
	    	
	    	int confirm = 0;
	    	while (confirm == 0)
	    		confirm = input.readInt();
	    	
	    	if (confirm!= userID)
	    		throw new IllegalArgumentException("wtf why not same userID reply?");
	    	else
	    		System.out.println(toString() + "confirmation :)");
	   	
	    	
	    	
	    	/*
	    	 *  Listen for Client asking to be the Host. Whoever replies first becomes the Host
	    	 */
	    	String host = "z";
	    	while (host.equals("z"))
	    		host = input.readUTF();
	    	
	    	if (isHostAvailable()) {
	    		System.out.println(toString() + " First to send/receive Host is " + userID + ".");
	    		output.writeUTF(Server.HOST);
	    		
	    		String newORload = "z";
		    	while (newORload.equals("z"))
		    		newORload = input.readUTF();
		    	
		    	System.out.println(toString() + "   " + newORload);
		    	if (newORload.equals("new"))
		    		server.newGame();
		    	else if (newORload.equals("load"))
		    		server.load();
		    	else
		    		throw new IllegalArgumentException(toString() + "  Received incorrect string related to menu choice from Client");
		    	
	    	}
	    	
	    	
	    	server.broadcastGame();
	    	
	    	
	    	
	    	while (true) {
	    		if (input.available() > 0) {
	    			int actionOrdinal = input.readInt();
	    			server.handleAction(actionOrdinal, userID);
	    		}
	    	}
	    	
	    	
	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {
	    	System.out.println(toString() + " finally.");
	    	try {
				clientSocket.close();
			} catch (IOException e) { e.printStackTrace(); }
	    }
    }

	private boolean isHostAvailable() {
		boolean result = false;
		lock.lock();
		if (hostAvailable) {
			result = true;
			hostAvailable = false;
		}
		lock.unlock();
		return result;
	}

	public void broadcastGame() {
		try {
			
			output.writeUTF(server.getGameWorld());
		
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	@Override
	public String toString() {
		return "^^^ worker " + userID;
	}
}