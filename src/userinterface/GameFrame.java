package userinterface;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import renderer.RenderPane;

/**
 * The application window for the game. Sets up window sizes and buttons available to the user.
 * @author Kirsty
 */
public class GameFrame extends JFrame {
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private final int frameWidth;
	private final int frameHeight;
	private final int menuHeight = 20;
	private final int textHeight = 70;
	private final int inventoryWidth = 100;
	
	private final Icon iconKey = loadImage("icon-key.png");
	
	private final Dimension frameSize;
	private final Dimension buttonSize = new Dimension(inventoryWidth, 25);
	
	private final JTextArea messagePane = new JTextArea();
	private final JLayeredPane layerPane = new JLayeredPane();
	private final JPanel inventoryPane = new JPanel();
	private final JMenuBar menuBar = new JMenuBar();
	
	private ArrayList<String> messages = new ArrayList<String>();
	private final JButton[] inventory = new JButton[]{new JButton("0")};
	
	/**
	 * Sets up the window to display the game and all controls/menus.
	 * Adds Action, Key & Mouse listeners.
	 */
	public GameFrame(RenderPane graphics, Listener listener) {
		super("Adventure Game");
		
		Dimension dim = graphics.getPreferredSize();
		int renderWidth = (int) dim.getWidth();
		int renderHeight = (int) dim.getHeight();
		
		/* Position (centre) and size of frame */
		frameWidth = (int) (dim.getWidth() + inventoryWidth + 16);		// Needs extra width for border
		frameHeight = (int) (dim.getHeight() + textHeight + menuHeight + 16);
		frameSize = new Dimension(frameWidth, frameHeight);
		this.setPreferredSize(frameSize);
		int frameX = (int) ((screenSize.getWidth()/2)-(frameWidth/2));
		int frameY = (int) ((screenSize.getHeight()/2)-(frameHeight/2));
		this.setLocation(frameX, frameY);	// Position in centre of screen
		
		/* Position and size of panels */
		graphics.setBounds(0, 20, renderWidth, renderHeight);
		//messagePane.setBounds(0, renderHeight, renderWidth, textHeight);
		menuBar.setBounds(0, 0, frameWidth, menuHeight);
		
		buildInventoryPane(renderWidth);
		buildMenuBar(listener);
		JScrollPane scrollPane = buildMessagePane();
		scrollPane.setBounds(0, renderHeight, renderWidth, textHeight);
		scrollPane.setPreferredSize(new Dimension(renderWidth, textHeight));
		scrollPane.setMaximumSize(new Dimension(renderWidth, textHeight));
		scrollPane.setMinimumSize(new Dimension(renderWidth, textHeight));
		
		add(layerPane);

		/* Add panes from furthest to closest */
		layerPane.add(graphics, new Integer(0));
		layerPane.add(inventoryPane, new Integer(1));
		layerPane.add(menuBar, new Integer(2));
		layerPane.add(scrollPane, new Integer(3));
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
	}
	
	/**
	 * Create the inventory panel to display number of keys the player is carrying.
	 * @param xPos
	 */
	private void buildInventoryPane(int xPos){
		inventoryPane.setBounds(xPos, 20, 100, 100);
		inventoryPane.setLayout(new BoxLayout(inventoryPane, BoxLayout.Y_AXIS));
		inventoryPane.setBorder(BorderFactory.createTitledBorder("Inventory"));
		if (iconKey != null){ inventory[0].setIcon(iconKey); }
		inventory[0].setMaximumSize(buttonSize);
		inventory[0].setBorderPainted(false);
		inventory[0].setContentAreaFilled(false);
		inventoryPane.add(inventory[0]);
		inventoryPane.setOpaque(false);
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
	}
	
	/**
	 * Build the pane to display messages to the player
	 */
	private JScrollPane buildMessagePane(){
		JScrollPane scrollPane = new JScrollPane(messagePane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messagePane.setOpaque(false);
		messagePane.setEditable(false);
		
		/*
		addMessage("Test message A");
		addMessage("Test message B");
		addMessage("Test message C");
		addMessage("Test message D");
		addMessage("Test message E");
		addMessage("Test message A");
		addMessage("Test message B");
		addMessage("Test message C");
		addMessage("Test message D");
		addMessage("Test message E");
		addMessage("Test message A");
		addMessage("Test message B");
		addMessage("Test message C");
		addMessage("Test message D");
		addMessage("Test message E");
		*/
		
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
		messages.add(message);
		String msg = "";
		//for (int pos=messages.size()-1, max=0; pos>=0 && max < 4; pos--, max++){
		//	msg = msg + messages.get(pos) + "\n";
		//}
		for (int pos=messages.size()-1; pos>=0; pos--){
			msg = msg + messages.get(pos) + "\n";
		}	
		messagePane.setText(msg);
	}
	
	/**
	 * Erase message history
	 */
	public void clearMessages(){
		messages = new ArrayList<String>();
	}
	
	/**
	 * Update the number of keys appearing
	 * @param keys
	 */
	public void updateInventory(int keys){
		inventory[0].setText(Integer.toString(keys));
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