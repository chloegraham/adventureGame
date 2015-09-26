package controller;

import userinterface.UserInterface;
import gameWorld.Level;

/**
 * Transfer data between UI and Game logic.
 */
public class Controller {
	
	Level level;
	UserInterface ui;
	
	/**
	 * Set up a game and initialise the UI.
	 */
	public Controller(){
//		this.level = new Level(this);
//		this.ui = new UserInterface(this);
//		updateUI(level.getLevelImg());
	} 

	/**
	 * Update the displayed game level.
	 */
	public void updateUI(char[][] array){
		ui.redraw(array);
	}

	/**
	 * Pass the user input to the key listener.
	 */
	public void passUserAction(int action){
		level.handleAction(action);
	}
	
	/**
	 * Add a message to the user interface
	 */
	public void addMessage(String message){
		ui.addMessage(message);
	}

}
