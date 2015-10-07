package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import userinterface.Action.Actions;

/**
 * Notifies master connection of player input.
 * Organises camera rotation.
 * @author Kirsty
 */
public class Listener extends JPanel implements KeyListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final UserInterface UI;
	// Stores the direction the screen is currently being shown at. Key Events are rotated to match, must remain in this order.
	private final Actions[] ROTATION = new Actions[] { Actions.NORTH, Actions.EAST, Actions.SOUTH, Actions.WEST };
	private int DIR = 0;
	
	private final Actions[] ACTIONS = Actions.values();	// All possible movements the player may make
	
	private boolean splashLocked = true;				// Assumes game is displayed, no splash screens.
	
	public Listener(UserInterface ui) {
		this.UI = ui;
	}
	
	/**
	 * Checks if the user really wants to quit the game. If so, shuts down the system.
	 */
	public void exitGame(){
		int confirm = JOptionPane.showOptionDialog(null,
				"Are you sure you want to exit the game?\nProgress since last save will be lost.\nConnection to server will be closed.", 
				"Exit Game", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (confirm == 0) {
			System.exit(0);
		}
	}
	
	/**
	 * If true, the splash screen will be locked and data will be passed through the server.
	 * If false, only splash screen actions will be considered.
	 * Set to true by default.
	 */
	public void setSplashLocked(boolean locked){
		this.splashLocked = locked;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int event = e.getKeyCode();
		
		/* Splash Screen controls */
		if (!splashLocked){
			UI.performKeyPressed();
			return;				// If splash screen is unlocked, prevent any further keys from being used.
		}

		/* Game controls */
		// TODO Refactor rotation, change KeyCode inside Action class instead
		/* First let's check for camera rotations. Does not yet rotate the actual view. */
		if (Actions.COUNTERCLOCKWISE.getKeyCode() == event){
			DIR--;
			if (DIR < 0) DIR = ROTATION.length-1;
			UI.rotation(Actions.COUNTERCLOCKWISE);
			return;
		}
		if (Actions.CLOCKWISE.getKeyCode() == event){
			DIR++;
			if (DIR >= ROTATION.length) DIR = 0;
			UI.rotation(Actions.CLOCKWISE);
			return;
		}
		
		/* Change the movement direction (if player has chosen to rotate the screen) */
		for (int i=0; i<ROTATION.length; i++){
			if (ROTATION[i].getKeyCode() == event){
				int send = i + DIR;			// If the screen has not been rotated, the direction will not be changed.
				if (send > 3){ send -= 4; }
				if (send < 0){ send += 4; }
				UI.sendUIAction(ROTATION[send].ordinal());
				return;
			}
		}
		
		//TODO Movement keycodes will be changed, they should be checked manually
		/* All other keys must be sent directly through the server. Find the ordinal and send it. */
		for (Actions ac : ACTIONS){
			if (ac.getKeyCode() == event){
				UI.sendUIAction(ac.ordinal());
				return;
			}
		}
		// Any other key pressed is not applicable. Ignore it.
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		
		/* Splash screen controls */
		if (!splashLocked){
			if (ac.equals("New Game")){ UI.sendUIAction(Actions.NEWGAME.ordinal()); }
			else if (ac.equals("Join Game")){ UI.sendUIAction(Actions.JOINGAME.ordinal()); }
			else if (ac.equals("Load Game")){ UI.sendUIAction(Actions.LOAD.ordinal()); }
			return;				// If splash screen is unlocked, prevent any further keys from being used.
		}
		
		/* Game controls */
		if (ac.equals("Save")){ UI.sendUIAction(Actions.SAVE.ordinal()); }
		else if (ac.equals("Load")){ UI.sendUIAction(Actions.LOAD.ordinal()); }
		else if (ac.equals("Exit")){ exitGame(); }
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
