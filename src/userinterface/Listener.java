package userinterface;

import gameWorld.Level;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Notifies master connection of player input.
 * Organises camera rotation.
 * @author Kirsty
 */
public class Listener implements KeyListener{
	private Level level;
	
	public Listener(Level l) {
		level = l;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W){
			level.moveUp();
		} else if (e.getKeyCode() == KeyEvent.VK_S){
			level.moveDown();
		} else if (e.getKeyCode() == KeyEvent.VK_A){
			level.moveLeft();
		} else if (e.getKeyCode() == KeyEvent.VK_D){
			level.moveRight();
		} else {
			System.out.println("You fucked up");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
