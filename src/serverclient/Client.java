package serverclient;

import userinterface.UserInterface;

public class Client {
	private UserInterface ui;
	
	// TEMP
	private int uid = 0;
	// TEMP
	public int getUID() {
		return uid;
	}
	
	
	
	
	
	public int getAction() {
		return  ui.getAction();
	}
	
	public void updateClient(char[][] gameWorld) {
		ui.redraw(gameWorld);
	}
}
