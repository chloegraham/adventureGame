package userinterface;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Displays one splash screen at a time.
 * @author Kirsty
 */
public class SplashScreen extends JPanel {
	public static final int NO_SCREEN = 0;
	public static final int STARTUP_SCREEN = 1;			// Shows during loading.
	public static final int MENU_SCREEN = 2;
	public static final int WAIT_SCREEN = 3;
	public static final int DEATH_SCREEN = 4;
	public static final int COMPLETED_SCREEN = 5;
	public static final int GENERIC_SCREEN = 6;			// Use for save/load
	
	private final JPanel[] allPanels = new JPanel[7];
	private final JLabel waitForPlayer = new JLabel("Waiting for Player 2 ...");
	
	private UserInterface ui;
	
	private int openPane = NO_SCREEN;
	
	public SplashScreen(UserInterface ui){		// TODO listener for testing
		this.ui = ui;
		this.setLayout(new CardLayout());
		this.setOpaque(false);
		
		/* Build invisible pane */
		allPanels[NO_SCREEN] = new JPanel();
		allPanels[NO_SCREEN].setOpaque(false);
		
		/* Build individual panes */
		buildStartUpScreen();
		buildMenuScreen();
		buildWaitForPlayerScreen();
		buildDeathScreen();
		
		/* Add panes to the panel */
		this.add(allPanels[NO_SCREEN]);
		this.add(allPanels[STARTUP_SCREEN]);
		this.add(allPanels[MENU_SCREEN]);
		this.add(allPanels[WAIT_SCREEN]);
		this.add(allPanels[DEATH_SCREEN]);
		
		allPanels[openPane].setVisible(true);
	}
	
	/**
	 * Initial screen that displays while the game is loading for the first time.
	 * Player must wait for connection to be established before doing anything.
	 */
	private void buildStartUpScreen(){
		allPanels[STARTUP_SCREEN] = new JPanel();
		allPanels[STARTUP_SCREEN].setFocusable(true);			// Lock key input while this screen is open
		allPanels[STARTUP_SCREEN].setBackground(new Color(200, 200, 200));	// No game behind this splash, don't make transparant.
		allPanels[STARTUP_SCREEN].setLayout(new BoxLayout(allPanels[STARTUP_SCREEN], BoxLayout.Y_AXIS));
		
		JLabel message = new JLabel("CHICKEN LITTLE");
		message.setFont(new Font("Serif", Font.BOLD, 35));
		message.setAlignmentX(CENTER_ALIGNMENT);
		
		allPanels[STARTUP_SCREEN].add(Box.createVerticalGlue());
		allPanels[STARTUP_SCREEN].add(message);
		allPanels[STARTUP_SCREEN].add(Box.createVerticalGlue());
		
		message = new JLabel("Waiting for connection ...");
		message.setAlignmentX(CENTER_ALIGNMENT);
		
		allPanels[STARTUP_SCREEN].add(message);
		allPanels[STARTUP_SCREEN].add(Box.createVerticalGlue());
		
		//ui.setContentEnabled(false);
	}
	
	/**
	 * Builds the screen that allows the player to start a new game
	 * Creates buttons and adds a new ActionListener to them.
	 */
	private void buildMenuScreen(){
		//Dimension btnSize = new Dimension(100, 25);
		//ActionListener menuListener = new ActionListener(){		// Send user's actions to the UI.
		//	public void actionPerformed(ActionEvent e) {
		//		String ac = e.getActionCommand();
		//		if (ac.equals("New Game")){ ui.sendUIAction(Actions.NEWGAME.ordinal()); }
		//		else if (ac.equals("Load Game")){ ui.sendUIAction(Actions.LOAD.ordinal()); }
		//		else if (ac.equals("Join Game")){ ui.sendUIAction(Actions.JOINGAME.ordinal()); }
		//	}			
		//};
		allPanels[MENU_SCREEN] = new JPanel();
		allPanels[MENU_SCREEN].setFocusable(true);				// Locks Listener input while the menu is open
		allPanels[MENU_SCREEN].setBackground(new Color(200, 200, 200));		// No game behind the menu, don't make transparent
		allPanels[MENU_SCREEN].setLayout(new BoxLayout(allPanels[MENU_SCREEN], BoxLayout.Y_AXIS));
		
		JLabel message = new JLabel("CHICKEN LITTLE");
		message.setFont(new Font("Serif", Font.BOLD, 35));
		message.setAlignmentX(CENTER_ALIGNMENT);
		
		allPanels[MENU_SCREEN].add(Box.createVerticalGlue());
		allPanels[MENU_SCREEN].add(message);
		allPanels[MENU_SCREEN].add(Box.createVerticalGlue());
		//allPanels[MENU_SCREEN].add(makeButton(menuListener, "New Game", btnSize));
		//allPanels[MENU_SCREEN].add(makeButton(menuListener, "Join Game", btnSize));
		//allPanels[MENU_SCREEN].add(makeButton(menuListener, "Load Game", btnSize));
		allPanels[MENU_SCREEN].add(Box.createVerticalGlue());
		
	}
	
	/**
	 * Builds the screen that displays the Key movements while waiting for the second player to join.
	 */
	private void buildWaitForPlayerScreen(){
		allPanels[WAIT_SCREEN] = new JPanel();
		allPanels[WAIT_SCREEN].setFocusable(true);				// ???
		allPanels[WAIT_SCREEN].setBackground(new Color(200, 200, 200, 200));		// Game available behind the menu, make transparent
		allPanels[WAIT_SCREEN].setLayout(new BoxLayout(allPanels[WAIT_SCREEN], BoxLayout.Y_AXIS));
		
		JLabel message = new JLabel("Key bindings will go here ...");
		message.setAlignmentX(CENTER_ALIGNMENT);
		waitForPlayer.setAlignmentX(CENTER_ALIGNMENT);
		
		allPanels[WAIT_SCREEN].add(Box.createVerticalGlue());
		allPanels[WAIT_SCREEN].add(message);
		allPanels[WAIT_SCREEN].add(Box.createVerticalGlue());
		allPanels[WAIT_SCREEN].add(waitForPlayer);
		allPanels[WAIT_SCREEN].add(Box.createVerticalGlue());
	}
	
	/**
	 * Build the screen that shows upon the player's death
	 * Player can press any key to go back to the game normally.
	 */
	private void buildDeathScreen(){
		allPanels[DEATH_SCREEN] = new JPanel();
		allPanels[DEATH_SCREEN].setFocusable(true);				// Must be focusable for the key listener to register
		
		/* Decorate the screen */
		allPanels[DEATH_SCREEN].setLayout(new BoxLayout(allPanels[DEATH_SCREEN], BoxLayout.Y_AXIS));
		allPanels[DEATH_SCREEN].setBackground(new Color(200, 0, 0, 200));	// Partially transparent
		
		JLabel message = new JLabel("You died!");
		message.setFont(new Font("Serif", Font.BOLD, 35));
		message.setAlignmentX(CENTER_ALIGNMENT);
		
		allPanels[DEATH_SCREEN].add(Box.createVerticalGlue());
		allPanels[DEATH_SCREEN].add(message);
		
		message = new JLabel("Press any key to continue.");
		message.setAlignmentX(CENTER_ALIGNMENT);

		allPanels[DEATH_SCREEN].add(Box.createVerticalGlue());
		allPanels[DEATH_SCREEN].add(message);
		allPanels[DEATH_SCREEN].add(Box.createVerticalGlue());
	}
	
	/**
	 * Builds a new button with the given listener and Action command, and an x alignment in the centre
	 *
	private JButton makeButton(ActionListener listener, String action, Dimension size){
		JButton btn = new JButton(action);
		btn.setMaximumSize(size);
		btn.setActionCommand(action);
		btn.addActionListener(listener);
		btn.setAlignmentX(CENTER_ALIGNMENT);
		btn.setFocusable(false);
		return btn;
	}*/
	
	/**
	 * Hide the originally open pane and display the passed in pane instead.
	 */
	public void setVisible(int newPane){
		if (newPane == NO_SCREEN){ ui.setContentEnabled(true); }
		else { ui.setContentEnabled(false); }		// Disable menus while splash screen is open
		allPanels[openPane].setVisible(false);
		openPane = newPane;
		allPanels[openPane].setVisible(true);
		//setFocusOnCard();
	}
	
	/**
	 * Close the current pane and show the menu screen instead.
	 */
	public void setMenuVisible(){
		allPanels[openPane].setVisible(false);
		ui.setContentEnabled(false);
		openPane = MENU_SCREEN;
		allPanels[MENU_SCREEN].setVisible(true);
		//setFocusOnCard();
	}
	
	public void setBeginGame(){
		waitForPlayer.setText("Press any key to begin the game.");
		allPanels[WAIT_SCREEN].setFocusable(true);
		allPanels[WAIT_SCREEN].grabFocus();
		//allPanels[WAIT_SCREEN].addKeyListener(new KeyListener(){
		//	public void keyPressed(KeyEvent e) {
		//		SplashScreen.setVisible(NO_SCREEN);
		//		waitForPlayer.setText("Waiting for Player 2 ...");
		//	}
		//	public void keyReleased(KeyEvent e) {}
		//	public void keyTyped(KeyEvent e) {}
		//});
	}
	
	public void setFocusOnCard(){
		allPanels[openPane].requestFocusInWindow();
	}
	
	//private static boolean getSplashOpen(){
	//	if (openPane == NO_SCREEN){ return false; }
	//	else { return true; }
	//}

	private static final long serialVersionUID = 1L;
	
	public void readAction(KeyEvent e){
		System.out.println("Printing stuff.");
	}
	
}
