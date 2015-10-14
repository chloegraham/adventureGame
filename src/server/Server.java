package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import server.gameWorld.GameLogic;
import server.gameWorld.GameWorld;
import server.saveLoad.XML;
import sharedHelpers.Actions;
import sharedHelpers.Msgs;

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
	
	private TimerSpikes timer;
	private Thread timerThread;
	
	private static Lock lock;
	
	public void run() {
    	lock = new ReentrantLock(true);
    	
    	// Initialize ServerSocket
    	initializeServerSocket();
    	
    	// Listen for first Client connection
    	establishSocketOne();
    	
    	//  Listen for second Client connection
    	establishSocketTwo();

    	// Listen for Actions from Clients
    	while (true) { 
			try {
				
				handleActionSocketOne();
				handleActionSocketTwo();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
	
	
	
	/*
	 *  Handle Action
	 */
	private void handleAction(int ordinal, int userID) throws IOException {
		if (ordinal == Actions.NEWGAME.ordinal()) {
    		newGame();
		} else if (ordinal == Actions.LOAD.ordinal()) {
    		load();
		} else if (ordinal == Actions.SAVE.ordinal()) {
    		save();
		} else {
			String message = logic.handleAction(ordinal, userID);
			if(message.equals("You're dead")){
				outputOne.writeUTF("You're dead");
				outputTwo.writeUTF("You're dead");
			}
			if(message.equals("You've won")){
				outputOne.writeUTF("You've won");
				outputTwo.writeUTF("You've won");
			}
			broadcast(userID, message);
		}
	}

	private void broadcast(int userID, String message) {
		String current = gameWorld.getEncodedGameWorld(userID);
		current += message;
		//System.out.println("current = " + current);
		int otherUserID = Msgs.PLAYER_TWO;
		if (userID == Msgs.PLAYER_TWO)
			otherUserID = Msgs.PLAYER_ONE;
		
		String other = gameWorld.getEncodedGameWorld(otherUserID);
		
		if (userID == Msgs.PLAYER_ONE) {
			socketOneWriteUTF(current);
			socketTwoWriteUTF(other);
			
		} else {
			socketOneWriteUTF(other);
			socketTwoWriteUTF(current);
		}
	}
	
	private void handleActionSocketOne() throws IOException {
		if (inputOne.available() > 0) {
	    	lock.lock();
			
			String handleAction = socketOneReadString();	// Listen for an action to handle
	    	int ordinal = Integer.parseInt(handleAction.substring(Msgs.DELIM_ACTION.length()));
	    	
	    	System.out.println("--- Server-  PlayerONE is trying to do Action " + Actions.getName(ordinal)); // Message to watch Servers progress
	    	handleAction(ordinal, Msgs.PLAYER_ONE);
	    	lock.unlock();
		}
	}
	
	private void handleActionSocketTwo() throws IOException {
		if (inputTwo.available() > 0) {
	    	lock.lock();
			
			String handleAction = socketTwoReadString();	// Listen for an action to handle
	    	int ordinal = Integer.parseInt(handleAction.substring(Msgs.DELIM_ACTION.length()));
	    	
	    	System.out.println("--- Server-  PlayerTWO is trying to do Action " + Actions.getName(ordinal)); // Message to watch Servers progress
	    	handleAction(ordinal, Msgs.PLAYER_TWO);
	    	lock.unlock();
		}
	}

	
	
	/*
	 *  New, Load, Save
	 */
	private void newGame() throws IOException {
		// Get encoded gameWorld of the standard new game
		String encodedGameWorld = XML.newGame();
		
		// Create the GameWorld based of the encoded new game + initialize Game Logic
		System.out.println("--- Server:    attempting to start a NewGame");
		gameWorld = new GameWorld(encodedGameWorld);
		logic = gameWorld.getLogic();
		System.out.println("--- Server:    expected that a NewGame has stated");

		socketOneWriteUTF(String.valueOf(Actions.NEWGAME.ordinal()));
		socketTwoWriteUTF(String.valueOf(Actions.NEWGAME.ordinal()));
		
		startSpikesTimer();
	}
	
	private void load() throws IOException {
		// Get encoded gameWorld of the standard new game
		String encodedGameWorld = XML.load();
		
		// Create the GameWorld based of the encoded previously saved game + initialize Game Logic
		System.out.println("--- Server:    attempting to start a Loaded Game");
		gameWorld = new GameWorld(encodedGameWorld);
		logic = gameWorld.getLogic();
		System.out.println("--- Server:    expected that a Loaded Game has stated");

		socketOneWriteUTF(String.valueOf(Actions.LOAD.ordinal()));
		socketTwoWriteUTF(String.valueOf(Actions.LOAD.ordinal()));
		
		startSpikesTimer();
	}
	
	private void save() throws IOException {
		System.out.println("--- Server:    attempting to Save Game.");
		
		if (XML.save(gameWorld.getEncodedGameSave())) {
			broadcast(Msgs.PLAYER_ONE, "Player one called save successfully");
			broadcast(Msgs.PLAYER_TWO, "Player one called save successfully");
		} else {
			broadcast(Msgs.PLAYER_ONE, "Player one failed to save successfully");
			broadcast(Msgs.PLAYER_TWO, "Player one failed to save successfully");
		}
	}

	private boolean isLoadValid() {
		return XML.load() != null;
	}
	
	
	
	/*
	 *  Spikes
	 */
	public void activateSpikes() throws IOException {
		lock.lock();
		
		String temp = logic.activateSpikes();	// activate all the spikes
		if(temp.equals("You're dead")){
			socketOneWriteUTF(temp);
			socketTwoWriteUTF(temp);
		}
		broadcast(Msgs.PLAYER_ONE, "");			// broadcast to both Players
		broadcast(Msgs.PLAYER_TWO, "");			// broadcast to both Players
		
		lock.unlock();
	}
	
	private void startSpikesTimer() {
		timer = new TimerSpikes(this);
		timerThread = new Thread(timer);
		timerThread.start();
	}
	
	
	
	/*
	 *  Establishing client socket connections
	 */
	private void initializeServerSocket() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println(toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void establishSocketOne() {
		try {
			acceptSocketOne();
			tellSocketOneItsHost();
		} catch (IOException e) {
			e.printStackTrace();
			
			establishSocketOne();
		}
	}
	
	private void establishSocketTwo() {
		try {
			acceptSocketTwo();
			tellSocketTwoItsGuest();
		} catch (IOException e) {
			e.printStackTrace();
			
			establishSocketTwo();
		}
	}
	
	private void acceptSocketOne() throws IOException {
		socketOne = serverSocket.accept();
    	inputOne = new DataInputStream(socketOne.getInputStream());
    	outputOne = new DataOutputStream(socketOne.getOutputStream());
    	System.out.println(toString());
	}
	
	private void acceptSocketTwo() throws IOException {
		socketTwo = serverSocket.accept();
    	inputTwo = new DataInputStream(socketTwo.getInputStream());
    	outputTwo = new DataOutputStream(socketTwo.getOutputStream());
    	System.out.println(toString());
	}
	
	private void tellSocketOneItsHost() throws IOException {
		if (isLoadValid())
    		socketOneWriteUTF(Msgs.DELIM_HOSTLOAD);
    	else
			socketOneWriteUTF(Msgs.DELIM_HOST);
    	String confirmHost = socketOneReadString();
    	if (!confirmHost.equals(Msgs.DELIM_HOST) && !confirmHost.equals(Msgs.DELIM_HOSTLOAD))
    		throw new IllegalArgumentException("Attempt to confirm the Host failed.");
    	System.out.println(toString());
	}
	
	private void tellSocketTwoItsGuest() throws IOException {
		socketTwoWriteUTF(Msgs.DELIM_GUEST);
    	String confirmGuest = socketTwoReadString();
    	if (!confirmGuest.equals(Msgs.DELIM_GUEST))
    		throw new IllegalArgumentException("Attempt to confirm the Guest failed.");
    	System.out.println(toString());
	}
	
	
	
	/*
	 *  Read & Write for the Sockets
	 */
	private void socketOneWriteUTF(String write) {
		try {
			outputOne.writeUTF(write);
		} catch (IOException e) {
			e.printStackTrace();
			
			establishSocketOne();
		}
	}
	
	private void socketTwoWriteUTF(String write) {
		try {
			outputTwo.writeUTF(write);
		} catch (IOException e) {
			e.printStackTrace();
			
			establishSocketTwo();
		}
	}
	
	private String socketOneReadString() {
		String str = null;
		try {
			str = inputOne.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
			
			establishSocketOne();
		}
		return str;
	}
	
	private String socketTwoReadString() {
		String str = null;
		try {
			str = inputTwo.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
			
			establishSocketOne();
		}
		return str;
	}
	
	
	
	@Override
	public String toString() {
		String serverSock = "NOT STARTED";
		if (serverSocket != null)
			serverSock = "RUNNING";
		
		String sockOne = "WAITING";
		if (socketOne != null)
			sockOne = "CONNECTED";
		
		String sockTwo = "WAITING";
		if (socketTwo != null)
			sockTwo = "CONNECTED";
			
		return "--- Server(ServerSocket- " + serverSock + "):    socketONEstatus- " + sockOne + "    socketTWOstatus- " + sockTwo;
	}
}