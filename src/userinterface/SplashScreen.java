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

import com.sun.glass.events.KeyEvent;

/**
 * Displays one splash screen or menu at a time. Does not contain any listeners, needs method
 * calls to respond to user input.
 * @author Kirsty
 */
public class SplashScreen extends JPanel {
	public static final int NO_CARD = 0;
	public static final int STARTUP_CARD = 1;		// Initial card. Displayed message changes.
	public static final int HOST_CARD = 2;			// Only the host player sees this. After player clicks a button, show startup.
	public static final int READY_CARD = 3;			// Player may close this screen and play the game.
	public static final int DEATH_CARD = 4;			// Show on player death. Player may close this screen and return to the game.
	public static final int WIN_CARD = 5;			// Show when the game is won. Player cannot return to the game from this card.
	public static final int GENERIC_CARD = 6;		// Displays any message to the player.
	private final JPanel[] allPanels = new JPanel[7];
	
	// Menu buttons and the key events for each
	private final int LOAD_GAME = 1;
	private final JButton[] menuButtons = new JButton[]{new JButton("New Game"), new JButton("Load Game")};
	private final int[] menuMnemonics = new int[]{KeyEvent.VK_N, KeyEvent.VK_L};

	/* Labels for screens with changing messages. */
	private final JLabel startupMessage = new JLabel("Waiting for connection ..."); 
	private final JLabel genericMessage = new JLabel("");
	
	private final CardLayout layout = new CardLayout();
	private UserInterface ui;						// Used to enable/disable content while a card is displayed
	private int openCard = STARTUP_CARD;			// Card to display when the game starts.
	
	public SplashScreen(UserInterface ui, Listener listener){
		this.ui = ui;
		this.setLayout(layout);
		this.setOpaque(false);						// If this is opaque, the invisible card cannot be invisible.
		
		/* Build invisible pane */
		allPanels[NO_CARD] = new JPanel();
		allPanels[NO_CARD].setOpaque(false);
		
		/* Build splash screens and menus */
		createStartUpCard();
		createMenuCard(listener);
		createReadyCard();
		createDeathCard();
		createWinCard();
		createGenericCard();
		
		/* Add cards to the panel */
		this.add(allPanels[NO_CARD], Integer.toString(NO_CARD));
		this.add(allPanels[STARTUP_CARD], Integer.toString(STARTUP_CARD));
		this.add(allPanels[HOST_CARD], Integer.toString(HOST_CARD));
		this.add(allPanels[READY_CARD], Integer.toString(READY_CARD));
		this.add(allPanels[DEATH_CARD], Integer.toString(DEATH_CARD));
		this.add(allPanels[WIN_CARD], Integer.toString(WIN_CARD));
		this.add(allPanels[GENERIC_CARD], Integer.toString(GENERIC_CARD));
		
		setVisibleCard(openCard);
	}
	
	/**
	 * Enables/Disables UI content depending on which card is open.
	 * Changes the visible card to the new one.
	 * Will not make any changes to the cards themselves.
	 * Call setVisibleMenu(boolean) to set the menu visible instead.
	 */
	public void setVisibleCard(int newPane){
		if (newPane == NO_CARD){ ui.setContentEnabled(true); }
		else { ui.setContentEnabled(false); }
		openCard = newPane;
		layout.show(this, Integer.toString(openCard));
	}
	
	/**
	 * Sets the Menu Card visible.
	 * Enable or disable the load game button with parameter
	 */
	public void setVisibleMenu(boolean loadGame){
		menuButtons[LOAD_GAME].setEnabled(loadGame);
		setVisibleCard(HOST_CARD);
	}
	
	/**
	 * Sets the message on the generic screen and displays it. Player cannot close this screen.
	 */
	public void setVisibleGeneric(String message){
		genericMessage.setText(message);
		setVisibleCard(GENERIC_CARD);
	}
	
	/** Returns the card that is currently displaying to the user. */
	public int getOpenCard(){ return openCard; }
	
	/** Returns true if the Load button is enabled, false otherwise */
	public boolean getLoadEnabled(){ return menuButtons[LOAD_GAME].isEnabled(); }
	
	/**
	 * Cards that close on key press will be closed if they are open.
	 * If a key event matches a button, return the action command for it.
	 */
	public String performKeyPress(int event){
		if (openCard == HOST_CARD){
			if (!menuButtons[LOAD_GAME].isEnabled() && event == KeyEvent.VK_L){ return ""; }	// If load isn't enabled, don't use it.
			for (int i=0; i<menuMnemonics.length; i++){
				if (menuMnemonics[i] == event){ return menuButtons[i].getActionCommand(); }
			}
		}
		else if (openCard == READY_CARD || openCard == DEATH_CARD){
			setVisibleCard(NO_CARD);
		}
		return "";
	}
	
	/**
	 * Change the startup message and change to it.
	 */
	public void showStartup(String message){
		startupMessage.setText(message);
		setVisibleCard(STARTUP_CARD);
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
		
		startupMessage.setAlignmentX(CENTER_ALIGNMENT);						// Centre message on startup pane
		allPanels[STARTUP_CARD].add(startupMessage);
		allPanels[STARTUP_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Builds the screen that allows the host player to start a game
	 * Buttons: New Game and Load Game
	 * Buttons are enabled by default
	 */
	private void createMenuCard(Listener listener){
		Dimension btnSize = new Dimension(100, 25);
		allPanels[HOST_CARD] = new JPanel();
		allPanels[HOST_CARD].setBackground(new Color(200, 200, 200));
		allPanels[HOST_CARD].setLayout(new BoxLayout(allPanels[HOST_CARD], BoxLayout.Y_AXIS));
		
		makeTitleLabels(allPanels[HOST_CARD]);
		
		makeButton(listener, menuButtons[0], btnSize, KeyEvent.VK_N);		// Set key events
		makeButton(listener, menuButtons[1], btnSize, KeyEvent.VK_L);
		allPanels[HOST_CARD].add(menuButtons[0]);
		allPanels[HOST_CARD].add(menuButtons[1]);
		
		allPanels[HOST_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Builds the screen that displays the Key movements and lets the player start playing.
	 */
	private void createReadyCard(){
		allPanels[READY_CARD] = new JPanel();
		allPanels[READY_CARD].setBackground(new Color(200, 200, 200, 200));	// Game available behind the menu, make partially transparent
		allPanels[READY_CARD].setLayout(new BoxLayout(allPanels[READY_CARD], BoxLayout.Y_AXIS));
		
		makeKeyBindings(allPanels[READY_CARD]);
		
		JLabel message = new JLabel("Press any key to start the game.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[READY_CARD].add(message);
		allPanels[READY_CARD].add(Box.createVerticalGlue());
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
	 * Build the card that displays any message to the user.
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
	private void makeButton(Listener listener, JButton btn, Dimension size, int mnemonic){
		btn.setMaximumSize(size);
		btn.setMnemonic(mnemonic);		// Will display to the user the key to press
		btn.addActionListener(listener);
		btn.setAlignmentX(CENTER_ALIGNMENT);
		btn.setFocusable(true);
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
