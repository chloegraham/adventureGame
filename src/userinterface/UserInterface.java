package userinterface;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import renderer.RenderPane;
import userinterface.Action.Actions;
import controller.Controller;

/**
 * Organises what displays to the user.
 * @author Kirsty
 */
public class UserInterface {
	RenderPane graphics = new RenderPane();
	GameFrame frame = new GameFrame(graphics);
	Controller controller;
	
	public UserInterface(Controller controller) {
		this.controller = controller;
		addListeners();
		frame.setVisible(true);
	}
	
	/**
	 * Require rendering window to maintain focus, and assign all listeners to it.
	 * Requests confirmation and closes the system if player tries to close the window.
	 */
	private void addListeners() {
		graphics.setFocusable(true);
		
		graphics.addKeyListener(new Listener(controller, this));
		graphics.addFocusListener(new FocusAdapter() {		// Reclaim focus when lost
	          public void focusLost(FocusEvent ev) {
	        	  graphics.requestFocus();
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
	
	/**
	 * Rotates the graphics pane either clockwise or counterclockwise
	 */
	public void rotation(Actions direction){
		if (direction == Actions.CLOCKWISE){ }
		else if (direction == Actions.COUNTERCLOCKWISE){ }
	}
	
	/**
	 * Display a message on the graphics window
	 * @param message 
	 */
	public void addMessage(String message){
	}
	
	/**
	 * Display a list of items that the player is currently holding
	 * @param inventory
	 */
	public void showInventory(List<String> inventory){
	}

	/**
	 * Set the level and redraw the pane
	 */
	public void redraw(char[][] level){
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
}
