package userinterface;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultCaret;

import renderer.RenderPane;

/**
 * The application window for the game. Sets up window sizes and buttons available to the user.
 * @author Kirsty
 */
public class GameFrame extends JFrame {
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private final int frameWidth;
	private final int frameHeight;
	private final int renderWidth;
	private final int renderHeight;
	private final int menuHeight = 20;
	private final int textHeight = 80;
	private final int inventoryWidth = 70;
	private final int inventoryHeight = textHeight;
	
	private final Icon iconKey = loadImage("icon-key.png");
	
	private final Dimension frameSize;
	private final Dimension labelSize = new Dimension(inventoryWidth, 25);
	
	private final JTextArea messagePane = new JTextArea();
	private final JLayeredPane contentPane = new JLayeredPane();
	private final JPanel inventoryPane = new JPanel();
	private final JMenuBar menuBar = new JMenuBar();
	private final SplashScreen splash;
	
	private final JLabel keys = new JLabel("0");
	
	/**
	 * Sets up the window to display the game and all controls/menus.
	 * Adds Action, Key & Mouse listeners.
	 */
	public GameFrame(RenderPane graphics, Listener listener) {
		super("Adventure Game");

		/* Position and size of Render Pane */
		Dimension dim = graphics.getPreferredSize();
		renderWidth = (int) dim.getWidth();
		renderHeight = (int) dim.getHeight();
		graphics.setBounds(0, menuHeight, renderWidth, renderHeight);
		
		/* Position (centre) and size of frame */
		frameWidth = (int) (dim.getWidth() + inventoryWidth + 16);		// Needs extra width for border
		frameHeight = (int) (dim.getHeight() + textHeight + menuHeight + 16);
		frameSize = new Dimension(frameWidth, frameHeight);
		this.setPreferredSize(frameSize);
		int frameX = (int) ((screenSize.getWidth()/2)-(frameWidth/2));
		int frameY = (int) ((screenSize.getHeight()/2)-(frameHeight/2));
		this.setLocation(frameX, frameY);	// Position in centre of screen
		this.add(contentPane);
		
		/* Build panels */
		buildInventoryPane();
		buildMenuBar(listener);
		JScrollPane scrollPane = buildMessagePane();
		splash = new SplashScreen(listener);
		splash.setBounds(0, 0, frameWidth, frameHeight);

		/* Add panes to content pane with z coordinate */
		contentPane.add(graphics, new Integer(0));
		contentPane.add(menuBar, new Integer(1));
		contentPane.add(scrollPane, new Integer(2));
		contentPane.add(inventoryPane, new Integer(3));
		contentPane.add(splash, new Integer(4));
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
	}
	
	/**
	 * Create the inventory panel to display number of keys the player is carrying.
	 */
	private void buildInventoryPane(){
		inventoryPane.setLayout(new BoxLayout(inventoryPane, BoxLayout.Y_AXIS));
		inventoryPane.setBorder(BorderFactory.createTitledBorder("Inventory"));
		if (iconKey != null){ keys.setIcon(iconKey); }
		keys.setMaximumSize(labelSize);
		inventoryPane.add(keys);
		inventoryPane.setOpaque(false);
		inventoryPane.setBounds(renderWidth, 20, inventoryWidth, inventoryHeight);
	}
	
	/**
	 * Create all items inside the menu bar
	 */
	private void buildMenuBar(Listener listener){
		JMenu file = new JMenu("File");
		menuBar.add(file);
		
		file.add(menuItemHelper(listener, "Save", KeyEvent.VK_S));
		file.add(menuItemHelper(listener, "Load", KeyEvent.VK_L));
		file.addSeparator();
		file.add(menuItemHelper(listener, "Exit", KeyEvent.VK_X));
		
		menuBar.setBounds(0, 0, frameWidth, menuHeight);
	}
	
	/**
	 * Build the pane to display messages to the player
	 */
	private JScrollPane buildMessagePane(){
		JScrollPane scrollPane = new JScrollPane(messagePane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(0, renderHeight, renderWidth, textHeight);
		messagePane.setOpaque(false);
		messagePane.setEditable(false);
		
		((DefaultCaret)messagePane.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		return scrollPane;
	}

	/**
	 * Create a menu item with a listener, and a Ctrl+KeyEvent accelerator
	 */
	private JMenuItem menuItemHelper(Listener listener, String name, int keyEvent){
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(listener);
		item.setAccelerator(KeyStroke.getKeyStroke(keyEvent, ActionEvent.CTRL_MASK));
		return item;
	}
	
	/**
	 * Stores and displays a message to the user. Will display at most the 4 most recent messages.
	 */
	public void addMessage(String message){
		messagePane.append(message + "\n");
	}
	
	/**
	 * Erase message history
	 */
	public void clearMessages(){
		messagePane.setText("");
	}
	
	/**
	 * Update the number of keys appearing
	 * @param keys
	 */
	public void updateInventory(int keys){
		this.keys.setText(Integer.toString(keys));
	}
	
	/**
	 * Loads the image given by the String name. If failed, will return null.
	 */
	private ImageIcon loadImage(String imageAddress){
		Image img = null;
		ImageIcon icon = null;
		try {
			img = ImageIO.read(new File(imageAddress));
			icon = new ImageIcon(img);
		} catch (IOException e) { e.printStackTrace(); }
		return icon;
	}

	private static final long serialVersionUID = 1L;

}