package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import renderer.RenderPane;
import serverclient.Client;
import userinterface.Action.Actions;

/**
 * Notifies master connection of player input.
 * Organises camera rotation.
 * @author Kirsty
 */
public class Listener extends JPanel implements KeyListener, ActionListener {
	private final Action directions = new Action(); 
	private final Actions[] acValues = Actions.values();	// All possible movements the player may make
	private final Client client;						// Where to send data for the master connection
	private final RenderPane graphics;
	private SplashScreen splash;
	
	private boolean splashLocked = true;				// If false: a splash is open, don't allow key input for the GameWorld
	
	/**
	 * Required to call addSplash(SplashScreen).
	 */
	public Listener(Client client, RenderPane graphics) {
		this.client = client;
		this.graphics = graphics;
	}
	
	/** Add SplashScreen. */
	public void addSplash(SplashScreen splash){ this.splash = splash; }
	
	/**
	 * If true, key input will be sent to the game world
	 * If false, only splash screen actions will be considered.
	 * Set to true by default.
	 */
	public void setSplashLocked(boolean locked){
		this.splashLocked = locked;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int event = e.getKeyCode();
		
		/* Splash Screen controls */
		if (!splashLocked){
			String ac = splash.performKeyPress(event);
			actionPerformed(ac);
			return;				// If splash screen is open, don't check game controls.
		}

		/* Game controls */
		
		// Rotation
		if (Actions.CLOCKWISE.getKeyCode() == event){
			directions.rotate(true);
			graphics.rotateViewClockwise(true);
			graphics.repaint();
			return;
		}
		else if (Actions.COUNTERCLOCKWISE.getKeyCode() == event){
			directions.rotate(false);
			graphics.rotateViewClockwise(false);
			graphics.repaint();
			return;
		}
		
		// Check directional keys first
		int dir  = directions.getDirectionOrdinal(event);
		if (dir != -1){
			client.handleAction(dir);
			return;
		}
		
		// All other actions should go directly through the server
		for (Actions ac : acValues){
			if (ac.getKeyCode() == event){
				client.handleAction(ac.ordinal());
				return;
			}
		}
	}
	
	/**
	 * Override ActionPerformed using a String instead of an ActionEvent.
	 */
	public void actionPerformed(String ac){
		/* Splash screen controls */
		if (!splashLocked){
			if (splash.getOpenCard() != SplashScreen.HOST_CARD){ return; }	// Only the splash menu has action listeners.
			else if (ac.equals("New Game")){
				splash.showStartup("Creating a new game. Waiting for game state ...");
				client.handleAction(Actions.NEWGAME.ordinal());
			}
			else if (ac.equals("Load Game")){
				splash.showStartup("Loading a game. Waiting for game state ...");
				client.handleAction(Actions.LOAD.ordinal());
			}
			
			return;				// If splash screen is unlocked, do not check game controls
		}
		
		/* Game controls */
		if (ac.equals("New Game")){ client.handleAction(Actions.NEWGAME.ordinal()); }
		else if (ac.equals("Save")){ client.handleAction(Actions.SAVE.ordinal()); }
		else if (ac.equals("Load")){ client.handleAction(Actions.LOAD.ordinal()); }
		else if (ac.equals("Controls")){ splash.setVisibleCard(SplashScreen.READY_CARD); }
		else if (ac.equals("About")){ splash.setVisibleCard(SplashScreen.ABOUT_CARD); }
		else if (ac.equals("Exit")){ exitGame(); }
	}
	
	@Override		// Overload so it takes the action command instead.
	public void actionPerformed(ActionEvent e) { actionPerformed(e.getActionCommand()); }

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	/**
	 * Checks if the user really wants to quit the game. If so, shuts down the system.
	 */
	public static void exitGame(){
		int confirm = JOptionPane.showOptionDialog(null,
				"Are you sure you want to exit the game?\nProgress since last save will be lost.\nConnection to server will be closed.", 
				"Exit Game", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (confirm == 0) {
			System.exit(0);
		}
	}
	
	private static final long serialVersionUID = 1L;
}
