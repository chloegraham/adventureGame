package userinterface;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Displays one splash screen or menu at a time. Does not contain any listeners, needs method
 * calls to respond to user input.
 * @author Kirsty
 */
public class SplashScreen extends JPanel {
	public static final int NO_CARD = 0;
	// TODO Change so user presses a key to move to the menu. Put "Waiting for connection ..." and messages in separate JLabels
	public static final int STARTUP_CARD = 1;		// Shows during loading.
	public static final int MENU_CARD = 2;
	public static final int WAIT_CARD = 3;
	public static final int DEATH_CARD = 4;
	public static final int WIN_CARD = 5;
	public static final int GENERIC_CARD = 6;		// Displays any string
	private final JPanel[] allPanels = new JPanel[7];
	
	private final int NEW_GAME = 0;
	private final int JOIN_GAME = 1;
	private final int LOAD_GAME = 2;
	private final JButton[] menuButtons = new JButton[]{new JButton("New Game"), new JButton("Join Game"), new JButton("Load Game")};
	
	private final JLabel waitForPlayer = new JLabel("Waiting for Player 2 ...");	// Change when player may start the game. 
	private boolean allowCloseWaitCard = false;		// if true, player may press any key to close the wait card and play the game
	
	// startup message may be changed if the player experiences a disconnection.
	private final JLabel startupMessage = new JLabel("Waiting for connection ...");
	// Label to display text on the generic card.
	private final JLabel genericMessage = new JLabel("");
	
	private UserInterface ui;						// Used to enable/disable content while a card is displayed
	
	private int openCard;
	
	public SplashScreen(UserInterface ui, Listener listener){
		this.ui = ui;
		this.setLayout(new CardLayout());
		this.setOpaque(false);						// If this is opaque, the invisible card cannot be invisible.
		
		/* Build invisible pane */
		allPanels[NO_CARD] = new JPanel();
		allPanels[NO_CARD].setOpaque(false);
		
		/* Build splash screens and menus */
		createStartUpCard();
		createMenuCard(listener);
		createWaitCard();
		createDeathCard();
		createWinCard();
		createGenericCard();
		
		/* Add cards to the panel */
		this.add(allPanels[NO_CARD]);
		this.add(allPanels[STARTUP_CARD]);
		this.add(allPanels[MENU_CARD]);
		this.add(allPanels[WAIT_CARD]);
		this.add(allPanels[DEATH_CARD]);
		this.add(allPanels[WIN_CARD]);
		this.add(allPanels[GENERIC_CARD]);
		
		/* Initialise splash screen. */
		openCard = NO_CARD;
		//ui.setContentEnabled(false);				// Disable content when any card is open except NO_CARD
		allPanels[openCard].setVisible(true);
	}
	
	/**
	 * Enables/Disables UI content depending on which card is open.
	 * Hides previously available card and shows the new one.
	 * Will not make any changes to the cards themselves.
	 */
	public void setVisibleCard(int newPane){
		// Disable content while splash pane is open, enable when closed
		if (newPane == NO_CARD){ ui.setContentEnabled(true); }
		else { ui.setContentEnabled(false); }
		// Hide the old card and open the new one
		allPanels[openCard].setVisible(false);
		openCard = newPane;
		allPanels[openCard].setVisible(true);
	}
	
	/**
	 * Sets the Menu Card visible.
	 * For each button, boolean passed in determines if button is available.
	 */
	public void setVisibleMenu(boolean newGame, boolean joinGame, boolean loadGame){
		menuButtons[NEW_GAME].setEnabled(newGame);
		menuButtons[JOIN_GAME].setEnabled(joinGame);
		menuButtons[LOAD_GAME].setEnabled(loadGame);
		setVisibleCard(MENU_CARD);
	}
	
	/**
	 * Sets the message on the generic screen and displays it. Player cannot close this screen.
	 */
	public void setVisibleGeneric(String message){
		genericMessage.setText(message);
		setVisibleCard(GENERIC_CARD);
	}
	
	/**
	 * Returns the player to the startup screen with an error message displayed.
	 * Do not use any special characters in the message.
	 */
	public void connectionError(String message){
		startupMessage.setText(message + " Attempting to reconnect ...");
		setVisibleCard(STARTUP_CARD);
	}
	
	/**
	 * Changes text displayed on Wait Card and determines whether the player may close the card.
	 */
	public void setWaitCardClosable(boolean closable){
		if (closable){ waitForPlayer.setText("Press any key to continue."); }
		else { waitForPlayer.setText("Waiting for Player 2 ..."); }
		allowCloseWaitCard = closable;
	}
	
	/** Returns the card that is currently displaying to the user. */
	public int getOpenCard(){ return openCard; }
	
	/**
	 * Cards that close on key press will be closed if they are open.
	 */
	public void performKeyPress(){
		if (openCard == DEATH_CARD){
			setVisibleCard(NO_CARD);
		}
		else if (openCard == WAIT_CARD){
			if (allowCloseWaitCard){			// Cannot close until the second player has joined the game.
				setWaitCardClosable(false);
				setVisibleCard(NO_CARD);
			}
		}
		else if (openCard == WIN_CARD){
			//TODO Set startup card visibility, immediately able to continue to main menu
		}
	}
	
	// ==========================================
	// Methods for creating the card panels.
	// ==========================================
	
	/**
	 * Splash screen that's displayed while attempting to establish connection and create the game.
	 * Fully opaque, does not expect a game to be behind this card.
	 */
	private void createStartUpCard(){
		allPanels[STARTUP_CARD] = new JPanel();
		allPanels[STARTUP_CARD].setBackground(new Color(200, 200, 200));
		allPanels[STARTUP_CARD].setLayout(new BoxLayout(allPanels[STARTUP_CARD], BoxLayout.Y_AXIS));
		
		makeTitleLabels(allPanels[STARTUP_CARD]);
		
		startupMessage.setAlignmentX(CENTER_ALIGNMENT);						// Message on startup pane
		allPanels[STARTUP_CARD].add(startupMessage);
		allPanels[STARTUP_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Builds the screen that allows the player to choose how to enter the game
	 * Buttons: New Game, Join Game and Load Game
	 * Buttons are enabled by default
	 */
	private void createMenuCard(Listener listener){
		Dimension btnSize = new Dimension(100, 25);
		allPanels[MENU_CARD] = new JPanel();
		allPanels[MENU_CARD].setBackground(new Color(200, 200, 200));
		allPanels[MENU_CARD].setLayout(new BoxLayout(allPanels[MENU_CARD], BoxLayout.Y_AXIS));
		
		makeTitleLabels(allPanels[MENU_CARD]);
		
		for (JButton btn : menuButtons){									// Menu buttons
			makeButton(listener, btn, btnSize);
			allPanels[MENU_CARD].add(btn);
		}
		allPanels[MENU_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Builds the screen that displays the Key movements while waiting for the second player to join.
	 */
	private void createWaitCard(){
		allPanels[WAIT_CARD] = new JPanel();
		allPanels[WAIT_CARD].setBackground(new Color(200, 200, 200, 200));	// Game available behind the menu, make partially transparent
		allPanels[WAIT_CARD].setLayout(new BoxLayout(allPanels[WAIT_CARD], BoxLayout.Y_AXIS));
		
		makeKeyBindings(allPanels[WAIT_CARD]);
		
		waitForPlayer.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[WAIT_CARD].add(waitForPlayer);
		allPanels[WAIT_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Build the screen that shows upon the player's death
	 */
	private void createDeathCard(){
		allPanels[DEATH_CARD] = new JPanel();
		allPanels[DEATH_CARD].setLayout(new BoxLayout(allPanels[DEATH_CARD], BoxLayout.Y_AXIS));
		allPanels[DEATH_CARD].setBackground(new Color(190, 0, 0, 200));		// Red, partially transparent.
		
		allPanels[DEATH_CARD].add(Box.createVerticalGlue());
		
		JLabel message = new JLabel("You died!");
		message.setFont(new Font("Serif", Font.BOLD, 35));
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[DEATH_CARD].add(message);
		
		allPanels[DEATH_CARD].add(Box.createVerticalGlue());
		
		message = new JLabel("Press any key to continue.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[DEATH_CARD].add(message);
		
		allPanels[DEATH_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Build the card that shows when the player wins the game.
	 */
	private void createWinCard(){
		allPanels[WIN_CARD] = new JPanel();
		allPanels[WIN_CARD].setLayout(new BoxLayout(allPanels[WIN_CARD], BoxLayout.Y_AXIS));
		allPanels[WIN_CARD].setBackground(new Color(250, 200, 0, 240));		// Yellow/Orange, slightly transparent.
		
		allPanels[WIN_CARD].add(Box.createVerticalGlue());
		
		JLabel message = new JLabel("Congratulations!");
		message.setFont(new Font("Serif", Font.BOLD, 35));
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[WIN_CARD].add(message);
		
		allPanels[WIN_CARD].add(Box.createVerticalGlue());
		
		message = new JLabel("You beat the game!");
		message.setFont(new Font("Serif", Font.BOLD, 25));
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[WIN_CARD].add(message);
		
		allPanels[WIN_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Build the card that displays a changable message to the user.
	 */
	private void createGenericCard(){
		allPanels[GENERIC_CARD] = new JPanel();
		allPanels[GENERIC_CARD].setLayout(new BoxLayout(allPanels[GENERIC_CARD], BoxLayout.Y_AXIS));
		allPanels[GENERIC_CARD].setBackground(new Color(200, 200, 200, 200));		// slightly transparent.
		
		allPanels[GENERIC_CARD].add(Box.createVerticalGlue());
		
		genericMessage.setFont(new Font("Serif", Font.BOLD, 35));
		genericMessage.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[GENERIC_CARD].add(genericMessage);
		
		allPanels[GENERIC_CARD].add(Box.createVerticalGlue());
	}
	
	// ==========================================
	// Helper methods for creating card panels.
	// ==========================================
	
	/**
	 * Create centred JLabels containing the title of the game in the given panel.
	 */
	private void makeTitleLabels(JPanel card){
		JLabel message = new JLabel("CHICKEN LITTLE");						// Game title label
		message.setFont(new Font("Serif", Font.BOLD, 35));
		message.setAlignmentX(CENTER_ALIGNMENT);
		card.add(Box.createVerticalGlue());
		card.add(message);
		
		message = new JLabel("The Boy Who Cried Wolf");						// subtitle label
		message.setFont(new Font("Serif", Font.PLAIN, 20));
		message.setAlignmentX(CENTER_ALIGNMENT);
		card.add(message);
		card.add(Box.createVerticalGlue());
	}
	
	/**
	 * Assigns listener, size, position and focusable to a button
	 */
	private void makeButton(Listener listener, JButton btn, Dimension size){
		btn.setMaximumSize(size);
		btn.addActionListener(listener);
		btn.setAlignmentX(CENTER_ALIGNMENT);
		btn.setFocusable(false);
	}
	
	/**
	 * Create centred Key Bindings on a given panel.
	 */
	private void makeKeyBindings(JPanel card){
		JLabel message = new JLabel("Key bindings will go here ...");
		message.setAlignmentX(CENTER_ALIGNMENT);	

		card.add(Box.createVerticalGlue());
		card.add(message);
		card.add(Box.createVerticalGlue());
	}
	
	private static final long serialVersionUID = 1L;
	
}
