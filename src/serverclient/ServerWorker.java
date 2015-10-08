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
	
	private static Lock lock;
	private static boolean hostAvailable;
	private int userID;
	
	public ServerWorker(Server server, Socket clientSocket, int userID) {
		this.server = server;
		this.clientSocket = clientSocket;
		this.userID = userID;
		this.hostAvailable = true;
		this.lock = new ReentrantLock();
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
	    	} else {
	    		System.out.println(toString() + " Second to send/receive host so NOW FOLLOWER is " + userID + ".");
	    		output.writeUTF(Server.FOLLOWER);
	    	}
	    	
	    	
	    	
	    	/*
	    	 *  Listen for 'New' / 'Load'
	    	 */
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
	    	
	    	
	    	while (true) {
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

	@Override
	public String toString() {
		return "^^^ worker " + userID;
	}
}