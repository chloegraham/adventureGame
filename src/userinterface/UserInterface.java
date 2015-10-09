package userinterface;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;

import renderer.RenderPane;
import serverclient.Client;
import userinterface.Action.Actions;

/**
 * Organises the content that is displayed to the user, and passes data between the server and the interface.
 * @author Kirsty
 */
public class UserInterface {
	/* Content in the frame */
	private final JMenu file = new JMenu("File");
	private RenderPane graphics = new RenderPane();
	private Listener listener = new Listener(this);
	private final SplashScreen splash = new SplashScreen(this, listener);
	private GameFrame frame = new GameFrame(graphics, listener, file, splash);

	/* For sending data through the connection */
	private Client client;
	private boolean playing = false;		// Set true only when the game state and renderer are ready.
	
	private int action = 99;
	private int keys = 0;		// Number of keys player is holding
	private int uid = -1;		// this player's ID
	
	public UserInterface(Client client) {
		this.client = client;
		addListeners();
		frame.setVisible(true);
	}
	
	public int getAction() {
		int temp = action;
		action = 99;
		return temp;
	}
	
	public void sendUIAction(int action) {
		client.passClientAction(action);
	}

	/** Sets the unique ID for this user */
	public void setUserID(int uid){
		this.uid = uid;
		frame.setTitle("Chicken Little : User " + this.uid);
	}
	
	/* ========================================================
	 * Methods to modify the contents of the frame.
	 * ======================================================== */
	
	/**
	 * Stores and displays a message to the user. Will display up to the 3 most recent messages.
	 */
	public void addMessage(String message){
		if (message != null){
			frame.addMessage(message);
		}
	}
	
	/**
	 * Erases all messages from the text box history.
	 */
	public void clearMessageHistory(){
		frame.clearMessages();
	}
	
	/**
	 * Increments the number of keys displayed to the user
	 */
	public void addKey(){
		keys++;
		frame.updateInventory(keys);
	}
	
	/**
	 * Attempts to decrement the number of keys displayed to the user.
	 * @return true if a key was removed, false otherwise.
	 */
	public boolean removeKey(){
		if (keys > 0){
			keys--;
			frame.updateInventory(keys);
			return true;
		}
		return false;
	}
	
	/**
	 * Directly set the number of keys that show up on UI.
	 * @param keys
	 */
	public void setKeyCount(int keys){
		this.keys = keys;
		frame.updateInventory(keys);
	}
	
	/**
	 * Enable or disable frame content. Used to lock content while a SplashScreen is open.
	 * @param enabled true to enable content, false to disable content
	 */
	public void setContentEnabled(boolean enabled){
		file.setEnabled(enabled);
		listener.setSplashLocked(enabled);
	}
	
	/* ========================================================
	 * Methods to change or modify the current splash screen.
	 * ======================================================== */

	/**
	 * The player cannot close this screen.
	 * Set the message displayed on the startup splash screen.
	 */
	public void setConnected(){
		splash.showStartup("Successfully connected. Waiting for game state ...");
	}
	
	/**
	 * This screen may be closed by the player.
	 * This player is the host, show the host menu and let the player choose new game or load game.
	 * @param loadGame if true, enable the loadGame button, otherwise disable it
	 */
	public void showHostMenu(boolean loadGame){
		splash.setVisibleMenu(loadGame);
	}
	
	/**
	 * This screen may be closed by the player.
	 * Requires a game state to be ready to play.
	 */
	private void setGameReady(){
		playing = true;
		splash.setVisibleCard(SplashScreen.READY_CARD);		// Player can close this card
	}
	
	/**
	 * If the splash screen menu is open, respond the button pressed.
	 * Returns the player to the startup splash screen.
	 */
	public void performSplashActionCommand(String ac){
		if (splash.getOpenCard() != SplashScreen.HOST_CARD){ return; }	// Only the host's menu card has action listeners.
		if (ac.equals("New Game")){
			splash.showStartup("Creating a new game. Waiting for game state ...");
			sendUIAction(Actions.NEWGAME.ordinal());
		}
		else if (ac.equals("Load Game")){
			splash.showStartup("Loading a game. Waiting for game state ...");
			sendUIAction(Actions.LOAD.ordinal());
		}
	}
	
	/**
	 * Alert the user that their character has died. Sleeps for a short period then allows the user to return to the game.
	 */
	public void setPlayerDeath(){
		splash.setVisibleCard(SplashScreen.DEATH_CARD);
		try {
			Thread.sleep(1000);		// Freeze the frame for a short time so key spamming doesn't skip the window.
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Alert the user that they have won the game.
	 * Player will return to the startup screen from this point.
	 */
	public void setPlayerWon(){
		splash.setVisibleCard(SplashScreen.WIN_CARD);
		try {
			Thread.sleep(1000);		// Freeze the frame for a short time so key spamming doesn't skip the window.
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Displays a custom message that the user cannot close. Must call closeGenericScreen() to close it.
	 */
	public void showSplashMessage(String message){
		splash.setVisibleGeneric(message);
	}
	
	/**
	 * Closes currently open generic screen
	 */
	public void closeSplashMessage(){
		splash.setVisibleCard(SplashScreen.NO_CARD);
	}
	
	/**
	 * Call when disconnected or connection cannot be established. Player returns to startup screen. 
	 * @param message Explain connection error to user 
	 */
	public void connectionError(String message){
		playing = false;
		splash.showStartup(message);
	}
	
	/** Tells the splash screen a key has been pressed. */
	public void performKeyPressed(){ splash.performKeyPress(); }
	
	/* ========================================================
	 * Methods for the renderer.
	 * ======================================================== */
	
	/**
	 * Rotates the graphics pane either clockwise or counterclockwise
	 */
	public void rotation(Actions direction){
		if (direction == Actions.CLOCKWISE){ 
			this.graphics.rotateViewClockwise(true);
		}
		else if (direction == Actions.COUNTERCLOCKWISE){
			this.graphics.rotateViewClockwise(false);
		}
		
		graphics.repaint();
	}
	
	/**
	 * Set the level and redraw the pane.
	 */
	public void redraw(char[][] level){
		char[][] testLevel =   	{{'e','i','e','e','e' },
								{'j','e','l','e','e' },
								{'e','k','e','e','e' },
								{'e','e','p','I','e' },
								{'e','e','J','e','L' },
								{'e','e','e','K','e' }};
		
		//level = testLevel;
		
		 int numberOfRows = level.length;
	     int numberOfColums = level[0].length;

		
		int camX = 0;
		int camY = 0;
		
		 for (int i = 0; i < numberOfRows; i++) {
	            for (int j = 0; j < numberOfColums; j++) {
	            	if(level[i][j] == 'p'){
	            		camX = j;
	            		camY = i;
	            	}
	            }
		 }
		
		graphics.setCameraLocation(camX,camY);
		graphics.setLevel(level);
		frame.repaint();
	}
	
	
	/**
	 * Same as redraw, but this time from 3 separate char layers not one.
	 * @param level
	 * @param objects
	 * @param moveables
	 */
	public void redrawFromLayers(char[][]level, char[][]objects, char[][]moveables){
    	int numberOfRows = level.length;
	    int numberOfColums = level[0].length;
    	
		int camX = 0;
		int camY = 0;
		
		 for (int i = 0; i < numberOfRows; i++) {
	            for (int j = 0; j < numberOfColums; j++) {
	            	if(moveables[i][j] == 'i' || 
            			moveables[i][j] == 'j' || 
            			moveables[i][j] == 'k' || 
            			moveables[i][j] == 'l' ||
            			moveables[i][j] == 'I' || 
            			moveables[i][j] == 'J' || 
            			moveables[i][j] == 'K' || 
            			moveables[i][j] == 'L'){
	            		
	            		camX = j;
	            		camY = i;
	            	}
	            }
		 }
		
		//graphics.setCameraLocation(camX,camY);
		graphics.setLayers(level, objects, moveables);
		
		if (!playing){ setGameReady(); }
		frame.repaint();
	}
	
	/* ========================================================
	 * Helper methods to build the user interface.
	 * ======================================================== */
	
	/**
	 * Require rendering window to maintain focus, and assign all listeners to it.
	 * Requests confirmation and closes the system if player tries to close the window.
	 */
	private void addListeners() {
		listener.setFocusable(true);
		listener.addKeyListener(listener);
		listener.addFocusListener(new FocusAdapter() {		// Reclaim focus when lost
	          public void focusLost(FocusEvent ev) {
	        	  listener.requestFocusInWindow();
	          }
	        });
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		// Use a window listener to close the game
		WindowListener exitListener = new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {				// Override closing event. If OK is not selected, don't do anything.
		        listener.exitGame();
		    }
		};
		frame.addWindowListener(exitListener);
	}
}
