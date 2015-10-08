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
	
	private int action = 99;
	private int keys = 0;		// Number of keys player is holding
	
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
	 * Once a connection has been established, allow the player to begin a game.
	 * Enables/Disables each button in the menu and allows display of menu splash screen.
	 * TODO Refactor so player can press button before moving to menu
	 */
	public void setMenu(boolean newGame, boolean joinGame, boolean loadGame){
		splash.setVisibleMenu(newGame, joinGame, loadGame);
	}
	
	/**
	 * If the player is currently on the wait screen, allow the player to close the wait screen and 
	 * send key presses / actions through the client.
	 */
	public void setGameReadyToPlay(){
		if (splash.getOpenCard() != SplashScreen.WAIT_CARD){ return; }
		splash.setWaitCardClosable(true);
	}
	
	/**
	 * If the splash screen menu is open, respond the the button pressed.
	 */
	public void performSplashActionCommand(String ac){
		if (splash.getOpenCard() != SplashScreen.MENU_CARD){ return; }	// Only the menu card has action listeners.
		if (ac.equals("New Game")){
			showWaitCardHelper();
			sendUIAction(Actions.NEWGAME.ordinal());
		}
		else if (ac.equals("Join Game")){
			showWaitCardHelper();
			sendUIAction(Actions.JOINGAME.ordinal());
		}
		else if (ac.equals("Load Game")){
			showWaitCardHelper();
			sendUIAction(Actions.LOAD.ordinal());
		}
	}
	
	/**
	 * Displays a Wait Card to the user that must be closed by the server.
	 */
	private void showWaitCardHelper(){
		splash.setWaitCardClosable(false);
		splash.setVisibleCard(SplashScreen.WAIT_CARD);
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
	 * Displays a custom message that the user cannot close. Must call closeGenericScreen() to close this.
	 * TODO not yet implemented
	 * @param message
	 */
	public void openGenericMessage(String message){}
	
	/**
	 * Closes currently open generic screen
	 * TODO not yet implemented
	 */
	public void closeGenericScreen(){}
	
	/**
	 * Call when disconnected or connection cannot be established. Player returns to startup screen. 
	 * TODO not yet implemented
	 * @param message Explain connection error to user 
	 */
	public void connectionError(String message){}
	
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
