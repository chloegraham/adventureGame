package userinterface;

import renderer.RenderPane;
import controller.Controller;

/**
 * Organises what displays to the user.
 * @author Kirsty
 */
public class UserInterface {
	Controller controller;
	RenderPane graphics = new RenderPane();
	
	public UserInterface(Controller controller) {
		this.controller = controller;
	}
	
	public void redraw(char[][] level){
		graphics.setLevel(level);
	}
}
