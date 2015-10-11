package userinterface;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
	public static final int HOST_CARD = 2;			// Only the host player sees this. Returns to Startup while building level
	public static final int READY_CARD = 3;			// Display when the game is ready. Shows Key Bindings.
	public static final int DEATH_CARD = 4;			// Show on player death. Player may close this screen and return to the game.
	public static final int WIN_CARD = 5;			// Show when the game is won. Player cannot return to the game from this card.
	public static final int ABOUT_CARD = 6;			// Shows information about the game to the user.
	public static final int GENERIC_CARD = 7;		// Displays any message to the player.
	private final JPanel[] allPanels = new JPanel[8];
	
	/* Menu buttons and the key events for each */
	private final int LOAD_GAME = 1;		// Position of the Load Game button in array, to disable/enable
	private final JButton[] menuButtons = new JButton[]{new JButton("New Game"), new JButton("Load Game")};
	private final int[] menuMnemonics = new int[]{KeyEvent.VK_N, KeyEvent.VK_L};	// Mnemonics matching each menu button

	/* Labels for screens with changing messages. */
	private final JLabel startupMessage = new JLabel("Waiting for connection ..."); 
	private final JLabel genericMessage = new JLabel("");
	
	private final CardLayout layout = new CardLayout();
	private UserInterface ui;						// UI content needs to be enabled/disabled when a card is open
	private int openCard = STARTUP_CARD;
	
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
		createAboutCard();
		createGenericCard();
		
		/* Add cards to the panel */
		this.add(allPanels[NO_CARD], Integer.toString(NO_CARD));
		this.add(allPanels[STARTUP_CARD], Integer.toString(STARTUP_CARD));
		this.add(allPanels[HOST_CARD], Integer.toString(HOST_CARD));
		this.add(allPanels[READY_CARD], Integer.toString(READY_CARD));
		this.add(allPanels[DEATH_CARD], Integer.toString(DEATH_CARD));
		this.add(allPanels[WIN_CARD], Integer.toString(WIN_CARD));
		this.add(allPanels[ABOUT_CARD], Integer.toString(ABOUT_CARD));
		this.add(allPanels[GENERIC_CARD], Integer.toString(GENERIC_CARD));
		
		setVisibleCard(openCard);
	}
	
	/**
	 * Shows another pane.
	 * Enables/Disables UI content while the splash screen is open.
	 * Will not make any changes to the cards themselves.
	 * Call setVisibleMenu(boolean) to set the menu visible instead.
	 * @param newPane required to call a valid card name from this class's fields
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
	
	/**
	 * Change the startup message and show the card
	 */
	public void showStartup(String message){
		startupMessage.setText(message);
		setVisibleCard(STARTUP_CARD);
	}
	
	/** Returns the card that is currently displaying to the user. */
	public int getOpenCard(){ return openCard; }
	
	/**
	 * Cards that close on key press will be closed if they are open.
	 * If a key event matches a button, return the action command for it.
	 * @return Action command for this key press if there is one, otherwise an empty string
	 */
	public String performKeyPress(int event){
		if (openCard == HOST_CARD){
			if (!menuButtons[LOAD_GAME].isEnabled() && event == KeyEvent.VK_L){ return ""; }	// If load isn't enabled, don't use it.
			for (int i=0; i<menuMnemonics.length; i++){
				if (menuMnemonics[i] == event){ return menuButtons[i].getActionCommand(); }
			}
		}
		else if (openCard == READY_CARD || openCard == DEATH_CARD || openCard == ABOUT_CARD){	// Cards that can be closed by key press
			setVisibleCard(NO_CARD);
		}
		return "";
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
		
		allPanels[STARTUP_CARD].add(Box.createVerticalGlue());
		
		addImage(STARTUP_CARD, "img-game-logo.png");
		
		allPanels[STARTUP_CARD].add(Box.createVerticalGlue());
		
		startupMessage.setAlignmentX(CENTER_ALIGNMENT);						// Centre message on startup pane
		allPanels[STARTUP_CARD].add(startupMessage);
		allPanels[STARTUP_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Builds the screen that allows the host player to create a game
	 * Fully opaque, does not expect a game to be behind this card.
	 */
	private void createMenuCard(Listener listener){
		Dimension btnSize = new Dimension(100, 25);
		allPanels[HOST_CARD] = new JPanel();
		allPanels[HOST_CARD].setBackground(new Color(200, 200, 200));
		allPanels[HOST_CARD].setLayout(new BoxLayout(allPanels[HOST_CARD], BoxLayout.Y_AXIS));
		
		allPanels[HOST_CARD].add(Box.createVerticalGlue());
		
		addImage(HOST_CARD, "img-game-logo.png");
		
		allPanels[HOST_CARD].add(Box.createVerticalGlue());
		
		makeButton(listener, menuButtons[0], btnSize, KeyEvent.VK_N);		// Build buttons and add to card
		makeButton(listener, menuButtons[1], btnSize, KeyEvent.VK_L);
		allPanels[HOST_CARD].add(menuButtons[0]);
		allPanels[HOST_CARD].add(menuButtons[1]);
		
		allPanels[HOST_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Builds the screen that displays the Key movements and lets the player start playing.
	 * Partially transparent, expects a game behind this card.
	 */
	private void createReadyCard(){
		allPanels[READY_CARD] = new JPanel();
		allPanels[READY_CARD].setBackground(new Color(230, 230, 230, 220));	// Game available behind the menu, make partially transparent
		allPanels[READY_CARD].setLayout(new BoxLayout(allPanels[READY_CARD], BoxLayout.Y_AXIS));

		allPanels[READY_CARD].add(Box.createVerticalGlue());
		
		addImage(READY_CARD, "img-key-binding.png");
		
		allPanels[READY_CARD].add(Box.createVerticalGlue());
		
		JLabel message = new JLabel("Press any key to play the game.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[READY_CARD].add(message);
		allPanels[READY_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Build the screen that shows upon the player's death
	 * Partially transparent, expects a game behind this card.
	 */
	private void createDeathCard(){
		allPanels[DEATH_CARD] = new JPanel();
		allPanels[DEATH_CARD].setLayout(new BoxLayout(allPanels[DEATH_CARD], BoxLayout.Y_AXIS));
		allPanels[DEATH_CARD].setBackground(new Color(100, 0, 0, 220));		// Red, partially transparent.
		
		allPanels[DEATH_CARD].add(Box.createVerticalGlue());
		
		addImage(DEATH_CARD, "img-death.png");
		
		allPanels[DEATH_CARD].add(Box.createVerticalGlue());
		
		JLabel message = new JLabel("Press any key to continue.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[DEATH_CARD].add(message);
		
		allPanels[DEATH_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Build the card that shows when the player wins the game.
	 * Partially transparent, expects a game behind this card.
	 */
	private void createWinCard(){
		allPanels[WIN_CARD] = new JPanel();
		allPanels[WIN_CARD].setLayout(new BoxLayout(allPanels[WIN_CARD], BoxLayout.Y_AXIS));
		allPanels[WIN_CARD].setBackground(new Color(250, 200, 100, 240));		// Yellow/Orange, slightly transparent.
		
		allPanels[WIN_CARD].add(Box.createVerticalGlue());
		
		addImage(WIN_CARD, "img-win.png");
		
		allPanels[WIN_CARD].add(Box.createVerticalGlue());

	}
	
	/**
	 * Build the card that shows information about the game to the user.
	 * Partially transparent, expects a game behind this card.
	 */
	private void createAboutCard(){
		allPanels[ABOUT_CARD] = new JPanel();
		allPanels[ABOUT_CARD].setLayout(new BoxLayout(allPanels[ABOUT_CARD], BoxLayout.Y_AXIS));
		allPanels[ABOUT_CARD].setBackground(new Color(200, 200, 200, 200));		// slightly transparent.
		
		allPanels[ABOUT_CARD].add(Box.createVerticalGlue());
		
		addImage(ABOUT_CARD, "img-game-logo.png");
		
		allPanels[ABOUT_CARD].add(Box.createVerticalGlue());
		
		aboutHelper("Chicken Little");
		aboutHelper("SWEN222 Group Project 2015");
		
		allPanels[ABOUT_CARD].add(Box.createVerticalGlue());
		
		aboutHelper("Chloe Graham");
		aboutHelper("Chris Jacques");
		aboutHelper("Benjamin Scully");
		aboutHelper("Eliot Slevin");
		aboutHelper("Kirsty Thorburn");
		
		allPanels[ABOUT_CARD].add(Box.createVerticalGlue());
	}
	
	private void aboutHelper(String msg){
		JLabel message = new JLabel(msg);
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[ABOUT_CARD].add(message);
	}
	
	/**
	 * Build the card that displays any message to the user.
	 * Partially transparent, expects a game behind this card.
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
	 * Loads an image and adds it to the card.
	 * If the image is unable to load, it will not be added.
	 */
	private void addImage(int card, String imageAddr){
		JLabel imageLabel = new JLabel();
		ImageIcon image = UserInterface.loadImageIcon(imageAddr);
		if (image != null){
			imageLabel.setIcon(image);
		}
		imageLabel.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[card].add(imageLabel);
	}
	
	private static final long serialVersionUID = 1L;
	
}
