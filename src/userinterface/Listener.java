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
	
	private boolean locked = false;
	
	public Listener(UserInterface ui) {
		this.UI = ui;
	}
	
	/**
	 * Checks if the user really wants to quit the game and ends the function.
	 */
	private void exitGame(){
		int confirm = JOptionPane.showOptionDialog(null,
				"Are you sure you want to exit the game?\nProgress since last save will be lost.\nConnection to server will be closed.", 
				"Exit Game", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (confirm == 0) {
			System.exit(0);
		}
	}
	
	public void lockListener(boolean locked){
		this.locked = locked;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int event = e.getKeyCode();
		
		// TODO Test code
		if (event == Actions.TESTA.getKeyCode()){ UI.TESTMETHOD(); }		// Start splash screen test cycle
		else if (event == Actions.TESTB.getKeyCode()){ UI.setConnectionOpen(); }
		else if (event == Actions.TESTC.getKeyCode()){ UI.setWaitForPlayer(); }
		else if (event == Actions.TESTD.getKeyCode()){ UI.setReadyToPlay(); }
		else if (event == Actions.TESTE.getKeyCode()){ UI.setPlayerDeath(); }
		
		if (locked){
			UI.sendSplashAction(e);
			return;
		}
		
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
		if (locked){
			return;
		}
		String ac = e.getActionCommand();
		if (ac.equals("Save")){ UI.sendUIAction(Actions.SAVE.ordinal()); }
		else if (ac.equals("Load")){ UI.sendUIAction(Actions.LOAD.ordinal()); }
		if (ac.equals("Exit")){ exitGame(); }
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
