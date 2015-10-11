package userinterface;

import java.awt.event.KeyEvent;

public class Action {
	
	/**
	 * Possible actions by player. Send through network connection using ordinal, holds key event (other key events may
	 * refer to the same action, and not all actions have a single key event).
	 * 
	 * Neither Ctrl nor Alt will be sent through.
	 * Please don't use the same key twice.
	 * 
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
		INSPECT (KeyEvent.VK_R),
		INTERACT (KeyEvent.VK_E),
		LOAD (-1),
		SAVE (-1),
		NEWGAME (-1),
		/* Events for the User Interface. Add events for the Server or Game World above. */
		NORTH (KeyEvent.VK_W),
		EAST (KeyEvent.VK_D),
		SOUTH (KeyEvent.VK_S),
		WEST (KeyEvent.VK_A),
		COUNTERCLOCKWISE (KeyEvent.VK_O),		// Graphics rotation
		CLOCKWISE (KeyEvent.VK_P);

		private int keyCode;			// KeyEvent.VK_EVENT
		
		/**
		 * @param keyCode KeyEvent.VK_EVENT, or -1 if no key event associated with this key.
		 */
		Actions(int keyCode){
			this.keyCode = keyCode;
		}
		
		/**
		 * @return keyCode matching KeyEvent.VK_EVENT, or -1 if no key code associated with this Action
		 */
		public int getKeyCode(){
			return keyCode;
		}
		
		public static String getName(int ordinal) {
			for (Actions a : values())
				if (a.ordinal() == ordinal)
					return a.toString();
			throw new IllegalArgumentException("Invalid ordinal. No enum with that ordinal.");
		}
	}
	
	// ============================================================
	// Directions need to be kept separate when there are multiple
	// windows on one screen. Stored directions for rotation.
	// ============================================================
	
	private int NORTH = Actions.NORTH.getKeyCode();
	private int EAST = Actions.EAST.getKeyCode();
	private int SOUTH = Actions.SOUTH.getKeyCode();
	private int WEST = Actions.WEST.getKeyCode();
	
	/**
	 * Rotates keycodes for actions. If param is true, rotates clockwise. Else rotates aticlockwise.
	 */
	public void rotate(boolean clockwise){
		int north = NORTH;
		int east = EAST;
		int south = SOUTH;
		int west = WEST;
		
		NORTH = (clockwise) ? west : east;
		SOUTH = (clockwise) ? east : west;
		EAST = (clockwise) ? north : south;
		WEST = (clockwise) ? south : north;
	}
	
	/**
	 * Returns the ordinal associated with the given key code.
	 * If key code is not a match, returns -1.
	 */
	public int getDirectionOrdinal(int keyCode){
		if (NORTH == keyCode){ return Actions.NORTH.ordinal(); }
		else if (EAST == keyCode){ return Actions.EAST.ordinal(); }
		else if (SOUTH == keyCode){ return Actions.SOUTH.ordinal(); }
		else if (WEST == keyCode){ return Actions.WEST.ordinal(); }
		return -1;	// This keyCode is not a direction.
	}

}
