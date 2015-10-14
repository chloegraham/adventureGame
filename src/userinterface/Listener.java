package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import renderer.RenderPane;
import serverHelpers.Actions;
import client.Client;

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
		
		// TESTING KEYS
		if (event == KeyEvent.VK_F1){
			splash.setVisibleCard(SplashScreen.DEATH_CARD);
			return;
		}
		else if (event == KeyEvent.VK_F2){
			splash.setVisibleCard(SplashScreen.WIN_CARD);
			return;
		}

		/* Game controls */
		
		// Ctrl is used to access menu events. Some menu events share a key with a game, so don't perform game events when control is down.
		if (e.isControlDown()){ return; }
		
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
	 * actionPerformed in splash screen is restricted.
	 */
	public void actionPerformed(String ac){
		/* Exit command needs to be able to override splash screen. */
		if (ac.equals("Exit")){
			splash.setSavedCard();
			splash.setVisibleConfirm("Quit", KeyEvent.VK_Q, "Are you sure you want to quit? Current game state will be lost.");
			return;
		}
		
		/* Splash screen controls */
		if (!splashLocked){
			int openCard = splash.getOpenCard();
			if (openCard == SplashScreen.HOST_CARD || openCard == SplashScreen.CONFIRM_CARD ){
				if (ac.equals("New Game")){
					splash.setVisibleStartup("Creating a new game. Waiting for game state ...");
					client.handleAction(Actions.NEWGAME.ordinal());
				}
				else if (ac.equals("Load Game")){
					splash.setVisibleStartup("Loading a game. Waiting for game state ...");
					client.handleAction(Actions.LOAD.ordinal());
				}
			}
			if (openCard == SplashScreen.CONFIRM_CARD){
				if (ac.equals("Cancel")){
					splash.loadSavedCard();		// if no saved card, will go straight to game. Needed for exit case. 
				}	// Changed mind, ignore
				else if (ac.equals("Restart")){
					splash.setVisibleStartup("Restarting the level. Waiting for game state ...");
					client.handleAction(Actions.RESTART.ordinal());
				}
				else if (ac.equals("Quit")){
					System.exit(0);
				}
			}
			return;				// If splash screen is unlocked, do not check game controls
		}
		
		/* Game controls */
		if (ac.equals("New Game")){
			splash.setVisibleConfirm("New Game", KeyEvent.VK_N, "Start a new game? Current game state will be lost.");
		}
		else if (ac.equals("Save")){
			client.handleAction(Actions.SAVE.ordinal());
		}
		else if (ac.equals("Load")){
			splash.setVisibleConfirm("Load Game", KeyEvent.VK_L, "Load the saved game? Current game state will be lost.");
		}
		else if (ac.equals("Restart Level")){
			splash.setVisibleConfirm("Restart", KeyEvent.VK_R, "Restart this level? Current game state will be lost.");
		}
		else if (ac.equals("Controls")){ splash.setVisibleCard(SplashScreen.READY_CARD); }
		else if (ac.equals("About")){ splash.setVisibleCard(SplashScreen.ABOUT_CARD); }
	}
	
	@Override		// Overload so it takes the action command instead.
	public void actionPerformed(ActionEvent e) { actionPerformed(e.getActionCommand()); }

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	private static final long serialVersionUID = 1L;
}
