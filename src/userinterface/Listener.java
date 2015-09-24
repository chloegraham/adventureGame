package userinterface;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import controller.Action.Actions;
import controller.Controller;

/**
 * Notifies master connection of player input.
 * Organises camera rotation.
 * @author Kirsty
 */
public class Listener implements KeyListener{
	// Stores the direction the screen is currently being shown at. Key Events are rotated to match, must remain in this order.
	private final Actions[] ROTATION = new Actions[]{Actions.NORTH, Actions.EAST, Actions.SOUTH, Actions.WEST};
	private int DIR = 0;
	
	private final Actions[] ACTIONS = Actions.values();	// All possible movements the player may make
	private final Controller controller;
	
	public Listener(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int event = e.getKeyCode();
		
		/* First let's check for camera rotations. Does not yet rotate the actual view. */
		if (Actions.CAMROTATELEFT.getKeyCode() == event){
			DIR--;
			if (DIR < 0) DIR = ROTATION.length-1;
			return;
		}
		if (Actions.CAMROTATERIGHT.getKeyCode() == event){
			DIR--;
			if (DIR >= ROTATION.length) DIR = 0;
			return;
		}
		
		/* Change the movement direction (if player has chosen to rotate the screen) */
		for (int i=0; i<ROTATION.length; i++){
			if (ROTATION[i].getKeyCode() == event){
				int send = i + DIR;			// If the screen has not been rotated, the direction will not be changed.
				if (send > 3){ send -= 4; }
				controller.passUserAction(ROTATION[send].ordinal());
				return;
			}
		}
		
		/* All other keys must be sent directly through the server. Find the ordinal and send it. */
		for (Actions ac : ACTIONS){
			if (ac.getKeyCode() == event){
				controller.passUserAction(ac.ordinal());
				return;
			}
		}
		// Any other key pressed is not applicable. Ignore it.
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
