package userinterface;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultCaret;

import renderer.IsoHelper;
import renderer.RenderPane;
import serverclient.Client;

/**
 * Organises the content that is displayed to the user, and passes data between the server and the interface.
 * @author Kirsty
 */
public class UserInterface extends JFrame {
	private RenderPane graphics = new RenderPane();
	private Listener listener;
	private SplashScreen splash;
	
	/* Images */
	private final ImageIcon iconKey = loadImageIcon("icon-key.png");
	private final ImageIcon iconBoulT = loadImageIcon("icon-boulder-true.png");
	private final ImageIcon iconBoulF = loadImageIcon("icon-boulder-false.png");

	/* Panel content */
	private final JTextArea messagePane = new JTextArea();
	private final JLayeredPane contentPane = new JLayeredPane();
	private final JPanel inventoryPane = new JPanel();
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu file = new JMenu("File");
	private final JMenu help = new JMenu("Help");
	
	/* Inventory pane */
	private final JLabel keys = new JLabel("0");
	private final JLabel boulder = new JLabel();
	private final int contentHeight = 82;			// Height of the inventory pane/message box
	
	private boolean playing = false;	// Set true only when the game state and renderer are ready.
	private int keyCount = 0;				// Number of keys player is currently holding
	private int uid = -1;				// this player's ID
	
	public UserInterface(Client client) {
		super("Chicken Little");
		
		listener = new Listener(client, graphics);
		splash = new SplashScreen(this, listener);
		listener.addSplash(splash);
		setListeners();
	
		/* Build panes */
		buildInventoryPane();
		buildMenuBar();
		JScrollPane scrollPane = buildMessagePane();
		setBounds(scrollPane);
		
		/* Add panes to content pane with z coordinate */
		this.add(contentPane);
		contentPane.add(graphics, new Integer(0));
		contentPane.add(menuBar, new Integer(1));
		contentPane.add(scrollPane, new Integer(2));
		contentPane.add(inventoryPane, new Integer(3));
		contentPane.add(splash, new Integer(4));
		contentPane.add(listener, new Integer(5));
		listener.setOpaque(false);
		
		this.pack();
		setVisible(true);	// Finished building the frame. Show it and wait until the userID is added.
	}
	
	/** Sets the unique ID for this user and changes the SplashScreen. */
	public void setUserID(int uid, boolean loadGame){
		this.uid = uid;
		if (uid == 101){ splash.setVisibleMenu(loadGame); }													// Host Player
		else if (uid == 202){ splash.showStartup("Successfully connected. Waiting for game state ..."); }	// Remote player
		this.setTitle("Chicken Little : User " + this.uid);
	}
	
	// ========================================================
	// Methods to modify the game panels
	// ========================================================
	
	/** Stores and displays a message to the user in a scroll pane. */
	public void addMessage(String message){
		if (message != null){ messagePane.append(message + "\n"); }
	}
	
	/** Erases all messages from the text box history. */
	public void clearMessageHistory(){ messagePane.setText(""); }
	
	/** Increments the number of keys displayed to the user */
	public void addKey(){ keys.setText(Integer.toString(++keyCount)); }
	
	/**
	 * Attempts to decrement the number of keys displayed to the user.
	 * @return true if a key was removed, false otherwise.
	 */
	public boolean removeKey(){
		if (keyCount > 0){
			keys.setText(Integer.toString(--keyCount));
			return true;
		}
		return false;
	}
	
	/** Directly set the number of keys that show up on UI. */
	public void setKeyCount(int keyCount){
		this.keyCount = keyCount;
		keys.setText(Integer.toString(keyCount));
	}
	
	/** If true, show a boulder being carried in inventory. Otherwise, show no boulder. */
	public void setBoulder(boolean carrying){
		if (carrying){
			if (iconBoulT != null){ boulder.setIcon(iconBoulT); }
		}
		else{ if (iconBoulF != null){ boulder.setIcon(iconBoulF); } }
		
	}
	
	// ========================================================
	// Methods for the Splash Screen.
	// ========================================================
	
	/**
	 * Alert the user that their character has died. Allows player to return to the game afterward.
	 */
	public void setPlayerDeath(){
		splash.setVisibleCard(SplashScreen.DEATH_CARD);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Alert the user that they have won the game. User cannot close this screen.
	 */
	public void setPlayerWon(){
		splash.setVisibleCard(SplashScreen.WIN_CARD);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
	}
	
	//TODO: test method implemented for checking save process
	public int getPlayerResponse(){
		int temp = JOptionPane.showConfirmDialog(getFocusOwner(), "Annie are you ok, are you ok Annie?");
		System.out.println("int value is: " + String.valueOf(temp));
		return temp;
	}
	
	/**
	 * Call when disconnected or connection cannot be established. Player returns to startup screen. 
	 * @param message Explain connection error to user 
	 */
	public void connectionError(String message){
		playing = false;
		splash.showStartup(message);
	}

	/** Displays a custom message that the user cannot close. Must call closeGenericScreen() to close it. */
	public void showSplashMessage(String message){ splash.setVisibleGeneric(message); }
	
	/** Closes currently open generic screen */
	public void closeSplashMessage(){ splash.setVisibleCard(SplashScreen.NO_CARD); }
	
	/**
	 * Enable or disable frame content. Used to lock content while a SplashScreen is open.
	 * @param enabled true to enable content, false to disable content
	 */
	public void setContentEnabled(boolean enabled){
		file.setEnabled(enabled);
		help.setEnabled(enabled);
		listener.setSplashLocked(enabled);
	}
	
	// ========================================================
	// Methods for building the frame
	// ========================================================
	
	/**
	 * Set the sizes and positions of all items in the frame.
	 */
	private void setBounds(JScrollPane scrollPane){
		Dimension dim = graphics.getPreferredSize();
		int menuHeight = 20;
		int renderWidth = (int) dim.getWidth();
		int renderHeight = (int) dim.getHeight();

		int frameWidth = (renderWidth + 16);		// Needs extra width for border
		int frameHeight = (renderHeight + contentHeight + menuHeight + 16);
		setPreferredSize(new Dimension(frameWidth, frameHeight));
		
		// Position Frame in screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int frameX = (int) ((screen.getWidth()/2)-(frameWidth/2));
		int frameY = (int) ((screen.getHeight()/2)-(frameHeight/2));
		setLocation(frameX, frameY);	// Position in centre of screen
		
		// Set position and size of items inside frame
		graphics.setBounds(0, menuHeight, renderWidth, renderHeight);
		splash.setBounds(0, 0, frameWidth, frameHeight);
		inventoryPane.setBounds((renderWidth-contentHeight), renderHeight, contentHeight, contentHeight);
		scrollPane.setBounds(0, renderHeight, (renderWidth-contentHeight), contentHeight);
		menuBar.setBounds(0, 0, frameWidth, menuHeight);
	}
	
	/**
	 * Create the inventory panel to display number of keys the player is carrying
	 * and whether the player is carrying a boulder.
	 */
	private void buildInventoryPane(){
		Dimension dim = new Dimension(contentHeight, 25);
		inventoryPane.setLayout(new BoxLayout(inventoryPane, BoxLayout.Y_AXIS));
		inventoryPane.setBorder(BorderFactory.createTitledBorder("Inventory"));
		//Keys
		if (iconKey != null){ keys.setIcon(iconKey); }
		keys.setMaximumSize(dim);
		keys.setToolTipText("Keys");
		inventoryPane.add(keys);
		//Boulder
		if (iconBoulF != null){ boulder.setIcon(iconBoulF); }
		boulder.setMaximumSize(dim);
		inventoryPane.add(boulder);
		boulder.setToolTipText("Boulder");
		
		inventoryPane.setOpaque(false);
	}
	
	/**
	 * Build the pane to display messages to the player
	 */
	private JScrollPane buildMessagePane(){
		JScrollPane scrollPane = new JScrollPane(messagePane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messagePane.setOpaque(false);
		messagePane.setEditable(false);
		// Set the pane to always be scrolled to the end
		((DefaultCaret)messagePane.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return scrollPane;
	}
	
	/**
	 * Create all items inside the menu bar. MenuItems are created using CTRL+(key event)
	 */
	private void buildMenuBar(){
		menuBar.add(file);
		menuBar.add(help);
		// File menu
		file.add(createMenuItem("Save", KeyEvent.VK_S));
		file.add(createMenuItem("Load", KeyEvent.VK_L));
		file.addSeparator();
		file.add(createMenuItem("Exit", KeyEvent.VK_X));
		// Help menu
		help.add(createMenuItem("Controls", KeyEvent.VK_C));
		help.add(createMenuItem("About", KeyEvent.VK_A));
	}
	
	/**
	 * Create a menu item with a listener, and a Ctrl+KeyEvent accelerator.
	 */
	private JMenuItem createMenuItem(String name, int keyEvent){
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(listener);
		item.setAccelerator(KeyStroke.getKeyStroke(keyEvent, ActionEvent.CTRL_MASK));
		return item;
	}
	
	/**
	 * Require the listener pane to maintain focus.
	 * Set a window closing listener to the frame.
	 */
	private void setListeners(){
		listener.setFocusable(true);
		listener.addKeyListener(listener);
		listener.addFocusListener(new FocusAdapter() {		// Reclaim focus when lost
	          public void focusLost(FocusEvent ev) {
	        	  listener.requestFocusInWindow();
	          }
	        });
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		// Use a new window listener to close the game.
		WindowListener exitListener = new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {				// Override closing event.
		        Listener.exitGame();
		    }
		};
		addWindowListener(exitListener);
	}
	
	// ========================================================
	// ImageIcon loader
	// ========================================================
	
	/**
	 * Loads the image given by the String name. If failed, will return null.
	 */
	public static ImageIcon loadImageIcon(String imageAddress){
		Image img = null;
		ImageIcon icon = null;
		try {
			img = ImageIO.read(new File(imageAddress));
			icon = new ImageIcon(img);
		} catch (IOException e) { e.printStackTrace(); }
		return icon;
	}
	
	// ========================================================
	// Methods for the renderer.
	// ========================================================
	
	/**
	 * Redraws the renderer from 3 char layers
	 * @param level
	 * @param objects
	 * @param moveables
	 */
	public void redrawFromLayers(char[][]level, char[][]objects, char[][]moveables){
		graphics.setLayers(level, objects, moveables);
		if (!playing){
			playing = true;
			splash.setVisibleCard(SplashScreen.READY_CARD);		// Show player key bindings and allow them to start
		}
		this.repaint();
	}
	
	
	
	/**
	 * 
	 * @param level
	 * @param objects
	 * @param moveables
	 */
	public void redrawFromLayersWithCoordinate(char[][]level, char[][]objects, char[][]moveables, int x, int y){
		graphics.setCamOffset(x, y);
		graphics.setLayers(level, objects, moveables);
		if (!playing){
			playing = true;
			splash.setVisibleCard(SplashScreen.READY_CARD);		// Show player key bindings and allow them to start
		}
		this.repaint();
	}
	
	
	
	
	private static final long serialVersionUID = 1L;

}