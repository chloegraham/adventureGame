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
	public static final int WAITFORPLAYER_SCREEN = 3;
	public static final int DEATH_SCREEN = 4;
	public static final int COMPLETED_SCREEN = 5;
	public static final int GENERIC_SCREEN = 6;			// Use for save/load
	
	private static final JPanel[] allPanels = new JPanel[7];
	
	private static UserInterface ui;
	
	private static int openPane = STARTUP_SCREEN;
	
	public SplashScreen(UserInterface ui){
		SplashScreen.ui = ui;
		this.setLayout(new CardLayout());
		this.setOpaque(false);
		
		/* Build invisible pane */
		allPanels[NO_SCREEN] = new JPanel();
		allPanels[NO_SCREEN].setOpaque(false);
		
		/* Build individual panes */
		buildStartUpScreen();
		buildDeathScreen();
		
		/* Add panes to the panel */
		this.add(allPanels[NO_SCREEN]);
		this.add(allPanels[STARTUP_SCREEN]);
		this.add(allPanels[DEATH_SCREEN]);
		
		allPanels[openPane].setVisible(true);
	}
	
	/**
	 * Initial screen that displays while the game is loading for the first time.
	 * Player must wait for connection to be established before doing anything.
	 */
	private static void buildStartUpScreen(){
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
		
		ui.setContentEnabled(false);
	}
	
	/**
	 * Build the screen that shows upon the player's death
	 * Player can press any key to go back to the game normally.
	 */
	private static void buildDeathScreen(){
		allPanels[DEATH_SCREEN] = new JPanel();
		allPanels[DEATH_SCREEN].setFocusable(true);				// Must be focusable for the key listener to register
		allPanels[DEATH_SCREEN].addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				SplashScreen.setVisible(NO_SCREEN);
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent arg0) {}
		});
		
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
	private JButton makeButton(Listener listener, String action){
		JButton btn = new JButton(action);
		btn.setMaximumSize(buttonSize);
		btn.setActionCommand(action);
		btn.addActionListener(listener);
		btn.setAlignmentX(CENTER_ALIGNMENT);
		return btn;
	}*/
	
	/**
	 * Hide the originally open pane and display the passed in pane instead.
	 */
	public static void setVisible(int newPane){
		allPanels[openPane].setVisible(false);
		openPane = newPane;
		allPanels[openPane].setVisible(true);
		if (openPane == NO_SCREEN){ ui.setContentEnabled(true); }
	}
	
	/**
	 * Close the current pane and show the menu screen instead.
	 */
	public static void setMenuScreenVisible(){
		allPanels[openPane].setVisible(false);
		allPanels[MENU_SCREEN].setVisible(true);
	}

	private static final long serialVersionUID = 1L;
	
}
