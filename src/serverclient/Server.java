package serverclient;

import gameWorld.GameLogic;
import gameWorld.GameWorld;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import saveload.XML;
import userinterface.Action.Actions;
import convertors.Msgs;

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
	
	private static Lock lock;
	
	public void run() {
	    try{
	    	lock = new ReentrantLock(true);
	    	
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
	    	if (isLoadValid())
	    		outputOne.writeUTF(Msgs.DELIM_HOSTLOAD);
	    	else
	    		outputOne.writeUTF(Msgs.DELIM_HOST);
	    	String confirmHost = inputOne.readUTF();
	    	if (!confirmHost.equals(Msgs.DELIM_HOST) && !confirmHost.equals(Msgs.DELIM_HOSTLOAD))
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
	    	outputTwo.writeUTF(Msgs.DELIM_GUEST);
	    	String confirmGuest = inputTwo.readUTF();
	    	if (!confirmGuest.equals(Msgs.DELIM_GUEST))
	    		throw new IllegalArgumentException("Attempt to confirm the Guest failed.");
	    	System.out.println(toString());

	    	
	    	
	    	/*
	    	 *  Listen for Actions from Clients
	    	 */
	    	while (true) { 
	    		
	    		if (inputOne.available() > 0) {
			    	lock.lock();
	    			
	    			// Listen for an action to handle
	    			String handleAction = inputOne.readUTF();
			    	if (!handleAction.contains(Msgs.DELIM_ACTION))
			    		throw new IllegalArgumentException("A handle action was sent without 'action' in the string (or wasn't even an action.)");
			    	int ordinal = Integer.parseInt(handleAction.substring(Msgs.DELIM_ACTION.length()));
			    	
			    	// Print chosen handle action to Server for testing & understanding
			    	System.out.println("--- Server   InputONE received action: " + Actions.getName(ordinal) + "   Action sent to Logic (except New & Load done in Server)");
			    	
			    	
			    	if (gameWorld == null)
		    			if (ordinal != Actions.NEWGAME.ordinal() && ordinal != Actions.LOAD.ordinal())
		    				throw new IllegalArgumentException("GameWorld still null and Client is trying to send Actions that aren't New or Load");
			    	
			    	
			    	if (ordinal == Actions.NEWGAME.ordinal())
			    		newGame();
			    	else if (ordinal == Actions.LOAD.ordinal())
			    		load();
			    	else if (ordinal == Actions.SAVE.ordinal())
			    		save();
			    	else
			    		handleAction(ordinal, Msgs.PLAYER_ONE);
			    	
			    	lock.unlock();
	    		}
		    	
	    		
		    	if (inputTwo.available() > 0) {
		    		lock.lock();
		    		
		    		// Listen for an action to handle
			    	String handleAction = inputTwo.readUTF();
			    	if (!handleAction.contains(Msgs.DELIM_ACTION))
			    		throw new IllegalArgumentException("A handle action was sent without 'action' in the string (or wasn't even an action.)");
			    	int ordinal = Integer.parseInt(handleAction.substring(Msgs.DELIM_ACTION.length()));
			    	
			    	
			    	// Print chosen handle action to Server for testing & understanding
			    	System.out.println("--- Server   InputTWO received action: " + Actions.getName(ordinal) + "   Action sent to Logic (except New & Load done in Server)");
			    	
			    	
			    	// Checks that PlayerTWO doesn't attempt invalid actions
			    	if (gameWorld == null || ordinal == Actions.NEWGAME.ordinal() || ordinal == Actions.LOAD.ordinal())
			    			throw new IllegalArgumentException("Player TWO should never be able to send Action with null GameWorld. Also Player TWO should never be able to New or Load Game.");
			    	
			    	handleAction(ordinal, Msgs.PLAYER_TWO);
			    	
			    	lock.unlock();
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
	
	
	
	/*
	 * 
	 */
	public void activateSpikes() throws IOException {
		lock.lock();
		
		logic.activateSpikes();				// activate all the spikes
		broadcast(Msgs.PLAYER_ONE, "");		// broadcast to both Players
		broadcast(Msgs.PLAYER_TWO, "");		// broadcast to both Players
		
		lock.unlock();
	}
	
	
	
	/*
	 *  Ask Logic to handle users action & then Broadcast the results back to the Clients
	 */
	private void handleAction(int ordinal, int userID) throws IOException {
//		if (ordinal == Actions.SAVE.ordinal())
//			if (save())
//				// tell the game to stop
		String message = logic.handleAction(ordinal, userID);
		broadcast(userID, message);
	}
	

	private void broadcast(int userID, String message) throws IOException {
		String current = gameWorld.getEncodedGameWorld(userID);
		current += message;
		
		int otherUserID = Msgs.PLAYER_TWO;
		if (userID == Msgs.PLAYER_TWO)
			otherUserID = Msgs.PLAYER_ONE;
		
		String other = gameWorld.getEncodedGameWorld(otherUserID);
		
		if (userID == Msgs.PLAYER_ONE) {
			//System.out.println("--- Server:    broadcasting game after PlayerONE handle action.");
					
			outputOne.writeUTF(current);
			outputTwo.writeUTF(other);
		} else {
			//System.out.println("--- Server:    broadcasting game after PlayerTWO handle action.");
			
			outputOne.writeUTF(other);
			outputTwo.writeUTF(current);
		}
	}



	/*
	 *  New, Load, Save
	 */
	private void newGame() throws IOException {
		// Get encoded gameWorld of the standard new game
		String encodedGameWorld = XML.newGame();
		System.out.println(encodedGameWorld);
		
		// Create the GameWorld based of the encoded new game + initialize Game Logic
		gameWorld = new GameWorld(encodedGameWorld);
		logic = gameWorld.getLogic();
		System.out.println("--- Server:    NewGame created.");
		
		handleAction(Actions.NEWGAME.ordinal(), Msgs.PLAYER_ONE);
		
		TimerSpikes timer = new TimerSpikes(this);
    	Thread timerThread = new Thread(timer);
		timerThread.start();
	}
	
	
	private void load() throws IOException {
		// Get encoded gameWorld of the standard new game
		String encodedGameWorld = XML.load();
		System.out.println(encodedGameWorld);
		
		// Create the GameWorld based of the encoded previously saved game + initialize Game Logic
		gameWorld = new GameWorld(encodedGameWorld);
		logic = gameWorld.getLogic();
		System.out.println("--- Server:    Loaded Game created.");
						
		TimerSpikes timer = new TimerSpikes(this);
		Thread timerThread = new Thread(timer);
		timerThread.start();
	}
	
	
	private boolean save() {
		System.out.println("--- Server:    attempting to Save Game.");
		return XML.save(gameWorld.getEncodedGameSave());
	}

	
	private boolean isLoadValid() {
		return XML.load() != null;
	}
	
	
	@Override
	public String toString() {
		return "--- Server(ServerSocket- " +(serverSocket!=null)+ "):    socketONEstatus- " +(socketOne!=null)+ "    socketTWOstatus- " +(socketTwo!=null)+ "  ";
	}
}