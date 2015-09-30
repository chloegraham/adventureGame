package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import userinterface.Action.Actions;

/**
 * Notifies master connection of player input.
 * Organises camera rotation.
 * @author Kirsty
 */
public class Listener implements KeyListener, ActionListener {
	private final UserInterface UI;
	// Stores the direction the screen is currently being shown at. Key Events are rotated to match, must remain in this order.
	private final Actions[] ROTATION = new Actions[] { Actions.NORTH, Actions.EAST, Actions.SOUTH, Actions.WEST };
	private int DIR = 0;
	
	private final Actions[] ACTIONS = Actions.values();	// All possible movements the player may make
	
	public Listener(UserInterface ui) {
		this.UI = ui;
	}
	

	
	@Override
	public void keyPressed(KeyEvent e) {
		int event = e.getKeyCode();
		
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
		String ac = e.getActionCommand();
		if (ac.equals("Save")){ UI.sendUIAction(Actions.SAVE.ordinal()); }
		else if (ac.equals("Load")){ UI.sendUIAction(Actions.LOAD.ordinal()); }
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
