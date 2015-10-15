package client.userinterface;

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
 * Displays one splash screen or menu at a time. Each splash screen has its own requirements for closing.
 * Locks the user interface while any screen is open.
 * STARTUP_CARD		Initial card. Displays message changes.
 * HOST_CARD		Only the host player sees this. Returns to Startup while building level
 * READY_CARD		Display when the game is ready. Shows Key Bindings.
 * DEATH_CARD		Show on player death. Player may close this screen and return to the game.
 * WIN_CARD			Show when the game is won. Player cannot return to the game from this card.
 * ABOUT_CARD		Shows information about the game to the user.
 * CONFIRM_CARD		Request confirmation from the user when they have made a game state change request.
 * INFORM_CARD	 	Inform this player of an action that the other player has performed (such as loading)
 * GENERIC_CARD		Displays any message to the player.
 * @author Kirsty Thorburn 300316972
 */
public class SplashScreen extends JPanel {
	public static final int NO_CARD = 0;
	public static final int STARTUP_CARD = 1;
	public static final int HOST_CARD = 2;
	public static final int READY_CARD = 3;
	public static final int DEATH_CARD = 4;
	public static final int WIN_CARD = 5;
	public static final int ABOUT_CARD = 6;
	public static final int CONFIRM_CARD = 7;
	public static final int INFORM_CARD = 8;
	public static final int GENERIC_CARD = 9;
	private final JPanel[] allPanels = new JPanel[10];
	
	/* Buttons and key events */
	private final int LOAD_GAME = 1;		// Position of the Load Game button in array, to disable/enable
	private final JButton[] menuButtons = new JButton[]{new JButton("New Game"), new JButton("Load Game")};
	private final int[] menuMnemonics = new int[]{KeyEvent.VK_N, KeyEvent.VK_L};	// Mnemonics matching each menu button
	private final JButton confirmButton = new JButton();
	private final JButton[] winButtons = new JButton[]{ new JButton("Chicken"), new JButton("Egg"), new JButton("") };
	private final int[] winMnemonics = new int[]{ KeyEvent.VK_C, KeyEvent.VK_E, KeyEvent.VK_D };

	/* Labels for screens with changing messages. */
	private final JLabel startupMessage = new JLabel("Waiting for connection ...");
	private final JLabel confirmMessage = new JLabel("");
	private final JLabel informMessage = new JLabel("");
	private final JLabel winMessage = new JLabel("Congratulations, you've almost beaten the game! Just one last question ... ");
	private final JLabel genericMessage = new JLabel("");
	
	private final CardLayout layout = new CardLayout();
	private UserInterface ui;				// UI content needs to be enabled/disabled when a card is open
	private int savedCard = -1;				// If another splash screen needs to open, save the screen the player's currently on.
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
		createWinCard(listener);
		createAboutCard();
		createConfirmCard(listener);
		createInformCard();
		createGenericCard();
		
		/* Add cards to the panel */
		this.add(allPanels[NO_CARD], Integer.toString(NO_CARD));
		this.add(allPanels[STARTUP_CARD], Integer.toString(STARTUP_CARD));
		this.add(allPanels[HOST_CARD], Integer.toString(HOST_CARD));
		this.add(allPanels[READY_CARD], Integer.toString(READY_CARD));
		this.add(allPanels[DEATH_CARD], Integer.toString(DEATH_CARD));
		this.add(allPanels[WIN_CARD], Integer.toString(WIN_CARD));
		this.add(allPanels[ABOUT_CARD], Integer.toString(ABOUT_CARD));
		this.add(allPanels[CONFIRM_CARD], Integer.toString(CONFIRM_CARD));
		this.add(allPanels[INFORM_CARD], Integer.toString(INFORM_CARD));
		this.add(allPanels[GENERIC_CARD], Integer.toString(GENERIC_CARD));
	}
	
	/**
	 * Shows another screen.
	 * Disables UI content while the splash screen is open.
	 * Will not make any changes to the cards themselves.
	 * Cards that have changing parameters should be called using their setVisible____ method.
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
	 * Sets the variables in the confirm menu and displays it.
	 * @param action Action command the confirm button should respond to.
	 * @param keyevent Key event the confirm button should respond to.
	 */
	public void setVisibleConfirm(String action, int keyevent, String message){
		confirmMessage.setText(message);
		confirmButton.setActionCommand(action);
		confirmButton.setText(action);
		confirmButton.setMnemonic(keyevent);
		setVisibleCard(CONFIRM_CARD);
	}
	
	/**
	 * Sets the message on the inform card and displays it.
	 */
	public void setVisibleInform(String message){
		informMessage.setText(message);
		setVisibleCard(INFORM_CARD);
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
	public void setVisibleStartup(String message){
		startupMessage.setText(message);
		setVisibleCard(STARTUP_CARD);
		ui.setPlayingFalse();		// Reenable the game when the next level comes through.
	}
	
	/**
	 * Change the message displayed on the end game card.
	 */
	public void updateWinCard(String message){
		winMessage.setText(message);
	}
	
	/** Returns the card that is currently displaying to the user. */
	public int getOpenCard(){ return openCard; }
	
	/** Save the currently opened card. If card is an inform card, will not save it. */
	public void setSavedCard(){
		if (openCard != INFORM_CARD && openCard != STARTUP_CARD){ savedCard = openCard; }
	}
	
	/** Load the saved card. If nothing is saved, shows invisible card. */
	public void loadSavedCard(){
		if (savedCard == -1){ setVisibleCard(NO_CARD); }
		else if (savedCard == STARTUP_CARD){			// Startup isn't redisplayed if the game came through
			if (ui.getPlaying()){ setVisibleCard(READY_CARD); }
			else { setVisibleCard(STARTUP_CARD); }
		}
		else {
			setVisibleCard(savedCard);
		}
		savedCard = -1;
	}
	
	/**
	 * Cards that close on key press will be closed if they are open.
	 * If a key event matches a button, return the action command for it.
	 * @return Action command for this key press if there is one, otherwise an empty string
	 */
	public String performKeyPress(int event){
		if (openCard == HOST_CARD){
			// If the load button is disabled, don't use it.
			if (!menuButtons[LOAD_GAME].isEnabled() && event == KeyEvent.VK_L){ return ""; }
			for (int i=0; i<menuMnemonics.length; i++){
				if (menuMnemonics[i] == event){ return menuButtons[i].getActionCommand(); }
			}
		}
		else if (openCard == CONFIRM_CARD){
			if (event == KeyEvent.VK_C){ setVisibleCard(NO_CARD); }	// Cancel
			else if (event == confirmButton.getMnemonic()){ return confirmButton.getActionCommand(); }
		}
		else if (openCard == READY_CARD || openCard == ABOUT_CARD || openCard == DEATH_CARD){	// Cards that can be closed by key press
			if (ui.getPlaying()){ setVisibleCard(NO_CARD); }
			else { setVisibleStartup("Cannot return to game. Waiting for game state ..."); }
		}
		else if (openCard == DEATH_CARD){
			return "New Game";	// Player must restart the game upon death
		}
		else if (openCard == INFORM_CARD){
			if (ui.getPlaying()){
				loadSavedCard();
			}
			else {		// still waiting on a game state. Show the startup menu.
				setVisibleStartup("Waiting for game state ...");
			}
		}
		else if (openCard == WIN_CARD){
			for (int i=0; i<winMnemonics.length; i++){
				if (winMnemonics[i] == event){ return winButtons[i].getActionCommand(); }
			}
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
		allPanels[STARTUP_CARD].setBackground(new Color(105, 206, 236));
		allPanels[STARTUP_CARD].setLayout(new BoxLayout(allPanels[STARTUP_CARD], BoxLayout.Y_AXIS));
		
		allPanels[STARTUP_CARD].add(Box.createVerticalGlue());
		
		addImage(STARTUP_CARD, "src/images/img-game-logo.png");
		
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
		allPanels[HOST_CARD].setBackground(new Color(105, 206, 236));
		allPanels[HOST_CARD].setLayout(new BoxLayout(allPanels[HOST_CARD], BoxLayout.Y_AXIS));
		
		allPanels[HOST_CARD].add(Box.createVerticalGlue());
		
		addImage(HOST_CARD, "src/images/img-game-logo.png");
		
		allPanels[HOST_CARD].add(Box.createVerticalGlue());
		
		makeButton(listener, menuButtons[0], btnSize, menuMnemonics[0]);		// Build buttons and add to card
		makeButton(listener, menuButtons[1], btnSize, menuMnemonics[1]);
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
		
		addImage(READY_CARD, "src/images/img-key-binding.png");
		
		allPanels[READY_CARD].add(Box.createVerticalGlue());
		
		JLabel message = new JLabel("Press any key to continue.");
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
		allPanels[DEATH_CARD].setBackground(new Color(150, 0, 0, 200));		// Red, partially transparent.
		
		allPanels[DEATH_CARD].add(Box.createVerticalGlue());
		
		JLabel message = new JLabel("A chicken has died and been transported to KFC.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[DEATH_CARD].add(message);
		message = new JLabel("Press any key to get new chickens.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[DEATH_CARD].add(message);
		
		allPanels[DEATH_CARD].add(Box.createVerticalGlue());
		
		addImage(DEATH_CARD, "src/images/endishere.png");
		
		allPanels[DEATH_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Build the card that shows when the player wins the game.
	 * Partially transparent, expects a game behind this card.
	 */
	private void createWinCard(Listener listener){
		Dimension size = new Dimension(100, 25);
		allPanels[WIN_CARD] = new JPanel();
		allPanels[WIN_CARD].setLayout(new BoxLayout(allPanels[WIN_CARD], BoxLayout.Y_AXIS));
		allPanels[WIN_CARD].setBackground(new Color(250, 200, 100, 240));		// Yellow/Orange, slightly transparent.
		
		allPanels[WIN_CARD].add(Box.createVerticalGlue());
		
		addImage(WIN_CARD, "src/images/img-win.png");
		
		allPanels[WIN_CARD].add(Box.createVerticalGlue());
		
		winMessage.setAlignmentX(CENTER_ALIGNMENT);		// Message changes with each button press.
		allPanels[WIN_CARD].add(winMessage);
		
		allPanels[WIN_CARD].add(Box.createVerticalGlue());
		
		JLabel message = new JLabel("Which came first, the chicken or the egg?");
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[WIN_CARD].add(message);
		allPanels[WIN_CARD].add(Box.createVerticalGlue());

		/* Create and add buttons */
		makeButton(listener, winButtons[0], size, winMnemonics[0]);
		makeButton(listener, winButtons[1], size, winMnemonics[1]);
		makeButton(listener, winButtons[2], size, winMnemonics[2]);
		allPanels[WIN_CARD].add(winButtons[0]);
		allPanels[WIN_CARD].add(winButtons[1]);
		allPanels[WIN_CARD].add(winButtons[2]);
		
		winButtons[0].setToolTipText("Chicken");
		winButtons[1].setToolTipText("Egg");
		winButtons[2].setToolTipText("Dinosaur");

		/* Hide secret answer */
		winButtons[2].setBorderPainted(false);
		winButtons[2].setContentAreaFilled(false);
		winButtons[2].setActionCommand("Dinosaur");
		
		allPanels[WIN_CARD].add(Box.createVerticalGlue());

	}
	
	/**
	 * Build the card that shows information about the game to the user.
	 * Partially transparent, expects a game behind this card.
	 */
	private void createAboutCard(){
		allPanels[ABOUT_CARD] = new JPanel();
		allPanels[ABOUT_CARD].setLayout(new BoxLayout(allPanels[ABOUT_CARD], BoxLayout.Y_AXIS));
		allPanels[ABOUT_CARD].setBackground(new Color(105, 206, 236, 220));		// slightly transparent.
		
		allPanels[ABOUT_CARD].add(Box.createVerticalGlue());
		
		addImage(ABOUT_CARD, "src/images/img-game-logo.png");
		
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
	
	/** Turns messages into centre aligned buttons */
	private void aboutHelper(String msg){
		JLabel message = new JLabel(msg);
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[ABOUT_CARD].add(message);
	}
	
	/**
	 * Builds the screen that requests confirmation before a player changes game state.
	 */
	private void createConfirmCard(Listener listener){
		Dimension btnSize = new Dimension(100, 25);
		allPanels[CONFIRM_CARD] = new JPanel();
		allPanels[CONFIRM_CARD].setBackground(new Color(230, 230, 230, 220));
		allPanels[CONFIRM_CARD].setLayout(new BoxLayout(allPanels[CONFIRM_CARD], BoxLayout.Y_AXIS));
		
		allPanels[CONFIRM_CARD].add(Box.createVerticalGlue());
		
		confirmMessage.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[CONFIRM_CARD].add(confirmMessage);
		
		allPanels[CONFIRM_CARD].add(Box.createVerticalGlue());
		
		JButton cancelButton = new JButton("Cancel");
		makeButton(listener, confirmButton, btnSize, -1);		// Build buttons and add to card
		makeButton(listener, cancelButton, btnSize, KeyEvent.VK_C);
		allPanels[CONFIRM_CARD].add(confirmButton);
		allPanels[CONFIRM_CARD].add(cancelButton);
		
		allPanels[CONFIRM_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Build the card that informs a player of another player's action.
	 * This card can be closed by the player.
	 */
	private void createInformCard(){
		allPanels[INFORM_CARD] = new JPanel();
		allPanels[INFORM_CARD].setLayout(new BoxLayout(allPanels[INFORM_CARD], BoxLayout.Y_AXIS));
		allPanels[INFORM_CARD].setBackground(new Color(230, 230, 230, 220));		// slightly transparent.
		
		allPanels[INFORM_CARD].add(Box.createVerticalGlue());
		
		addImage(INFORM_CARD, "src/images/disneytext.png");
		
		allPanels[INFORM_CARD].add(Box.createVerticalGlue());
		
		informMessage.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[INFORM_CARD].add(informMessage);
		
		JLabel message = new JLabel("Press any key to continue.");
		message.setAlignmentX(CENTER_ALIGNMENT);
		allPanels[INFORM_CARD].add(message);
		
		allPanels[INFORM_CARD].add(Box.createVerticalGlue());
	}
	
	/**
	 * Build the card that displays any message to the user.
	 * Partially transparent, expects a game behind this card.
	 */
	private void createGenericCard(){
		allPanels[GENERIC_CARD] = new JPanel();
		allPanels[GENERIC_CARD].setLayout(new BoxLayout(allPanels[GENERIC_CARD], BoxLayout.Y_AXIS));
		allPanels[GENERIC_CARD].setBackground(new Color(230, 230, 230, 220));		// slightly transparent.
		
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
