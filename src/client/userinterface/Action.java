package client.userinterface;

import server.helpers.Actions;

/**
 * Holds the ordinals for possible key events to send to the server.
 * @author Kirsty Thorburn 300316972
 */
public class Action {
	
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
