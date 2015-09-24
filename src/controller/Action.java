package controller;

import java.awt.event.KeyEvent;

public class Action {
	
	/**
	 * Possible actions by player. Send through network connection using ordinal, holds key event.
	 * Any new action added here will be available to the server immediately. Make sure to add the key to the readme file.
	 * Do not overwrite any existing keys (check in the README file), Do not use CTRL or ALT at all.
	 * 
	 * INSTRUCTIONS FOR USE:
	 * Import the Actions enum to the class that needs to use it:
	 * 		import chickenlittle.control.Action.Actions;
	 * Use the server input to retrieve the action type. Input will be an int.
	 * Do not try to hard code in an int! Always use .ordinal()
	 * Example:
	 * 	if (Actions.NORTH.ordinal() == input){ System.out.println("Valid input"); }
	 * 
	 * @author Kirsty
	 */
	public enum Actions {
		// Add extra events here...
		INTERACT (KeyEvent.VK_E),
		SAVE (KeyEvent.VK_Q),
		LOAD (KeyEvent.VK_L),
		/* Adding new events below this line will increase the number of unnecessary checks.
		 * Better to place above instead. */
		CAMROTATELEFT (KeyEvent.VK_NUMPAD7),
		CAMROTATERIGHT (KeyEvent.VK_NUMPAD9),
		EXIT (KeyEvent.VK_ESCAPE),
		NORTH (KeyEvent.VK_W),
		EAST (KeyEvent.VK_D),
		SOUTH (KeyEvent.VK_S),
		WEST (KeyEvent.VK_A);

		private int keyCode;			// KeyEvent.VK_EVENT
		
		Actions(int keyCode){
			this.keyCode = keyCode;
		}
		
		/**
		 * @return keyCode matching KeyEvent.VK_EVENT
		 */
		public int getKeyCode(){
			return keyCode;
		}
		
	}

}
