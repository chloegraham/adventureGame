package userinterface;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

import renderer.RenderPane;
import serverclient.Client;
import userinterface.Action.Actions;

/**
 * Organises what displays to the user.
 * @author Kirsty
 */
public class UserInterface {
	private Client client;
	private RenderPane graphics = new RenderPane();
	private Listener listener = new Listener(this);
	
	/* Frame objects */
	private final JMenu file = new JMenu("File");
	private final SplashScreen splash = new SplashScreen(this);
	private GameFrame frame = new GameFrame(graphics, listener, file, splash);
	
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
		if (keys >= 0){
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
	}
	
	/* ========================================================
	 * Methods to change the splash screen currently displayed.
	 * ======================================================== */
	
	/**
	 * Show the screen for player death.
	 * Player will be able to turn off the screen on their own choice.
	 */
	public void setSplashDeath(){
		SplashScreen.setVisible(SplashScreen.DEATH_SCREEN);
	}
	
	/**
	 * Set the splash screen to wait for a second player to join.
	 */
	public void setWaitForPlayer(){
		SplashScreen.setVisible(SplashScreen.DEATH_SCREEN);
	}
	
	/**
	 * Allow the player to begin the game when ready
	 */
	public void setReadyToPlay(){
		SplashScreen.setVisible(SplashScreen.DEATH_SCREEN);
	}
	
	
	/* ========================================================
	 * Methods for the renderer.
	 * ======================================================== */
	/**
	 * Rotates the graphics pane either clockwise or counterclockwise
	 */
	public void rotation(Actions direction){
		if (direction == Actions.CLOCKWISE){ }
		else if (direction == Actions.COUNTERCLOCKWISE){ }
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
		
		graphics.setCameraLocation(camX,camY);
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
		graphics.setFocusable(true);
		
		graphics.addKeyListener(listener);
		graphics.addFocusListener(new FocusAdapter() {		// Reclaim focus when lost
	          public void focusLost(FocusEvent ev) {
	        	  graphics.requestFocusInWindow();
	          }
	        });

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		// Use a window listener to close the game
		WindowListener exitListener = new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {				// Override closing event. If OK is not selected, don't do anything.
		        int confirm = JOptionPane.showOptionDialog(null,
		        	"Are you sure you want to exit the game?\nProgress since last save will be lost.\nConnection to server will be closed.", 
		        	"Exit Game", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		        if (confirm == 0) {
		           System.exit(0);
		        }
		    }
		};
		frame.addWindowListener(exitListener);
	}
}
