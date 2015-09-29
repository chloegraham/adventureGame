package serverclient;

import userinterface.UserInterface;

public class Client {
	private UserInterface ui;
	
	public Client() {
		ui = new UserInterface();
	}
	
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
		
		char[][] testLevel =   {{'w','w','w','w'},
                {'w','e','e','e'},
                {'w','e','e','e'},
                {'w','e','e','e'}};
    	
    	char[][] testObjects =   {{'n','n','n','n'},
    			{'n','n','n','n'},
    			{'n','c','n','n'}, // Notice i'm using 'n' for nothing
    			{'n','n','n','n'}};
    	
    	char[][] testMoveables =  {{'n','n','n','n'},
    			{'n','n','n','p'},
    			{'n','n','n','n'},
    			{'n','n','n','n'}};
    	
		// This method will do same shit as redraw, but from multiple layers
		//ui.redrawFromLayers(testLevel, testObjects, testMoveables);
	}
}
