package userinterface;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
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
import javax.swing.JPanel;
import javax.swing.JTextPane;

import renderer.RenderPane;

/**
 * The application window for the game. Sets up window sizes and buttons available to the user.
 * @author Kirsty
 */
public class GameFrame extends JFrame {
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private final int frameWidth;
	private final int frameHeight;
	private final int textHeight = 110;
	private final int inventoryWidth = 100;
	
	private final Icon iconKey = loadImage("icon-key.png");
	
	private final Dimension frameSize;
	private final Dimension buttonSize = new Dimension(inventoryWidth, 25);
	
	private final JTextPane messagePane = new JTextPane();
	private final JLayeredPane layerPane = new JLayeredPane();
	private final JPanel inventoryPane = new JPanel();
	
	private ArrayList<String> messages = new ArrayList<String>();
	private final JButton[] inventory = new JButton[]{new JButton("0")};
	
	/**
	 * Sets up the window to display the game and all controls/menus.
	 * Adds Action, Key & Mouse listeners.
	 */
	public GameFrame(RenderPane graphics) {
		super("Adventure Game");
		Dimension dim = graphics.getPreferredSize();
		int renderWidth = (int) dim.getWidth();
		int renderHeight = (int) dim.getHeight();
		
		/* Position (centre) and size of frame */
		frameWidth = (int) (dim.getWidth() + inventoryWidth + 16);		// Needs extra width for border
		frameHeight = (int) (dim.getHeight() + textHeight);
		frameSize = new Dimension(frameWidth, frameHeight);
		this.setPreferredSize(frameSize);
		int frameX = (int) ((screenSize.getWidth()/2)-(frameWidth/2));
		int frameY = (int) ((screenSize.getHeight()/2)-(frameHeight/2));
		this.setLocation(frameX, frameY);	// Position in centre of screen
		
		/* Position and size of panels */
		graphics.setBounds(0, 0, renderWidth, renderHeight);
		messagePane.setBounds(0, renderHeight, renderWidth, textHeight);
		buildInventoryPane(renderWidth);
		
		add(layerPane);

		/* Add panes from furthest to closest */
		layerPane.add(graphics, new Integer(0));
		layerPane.add(inventoryPane, new Integer(1));
		layerPane.add(messagePane, new Integer(2));

		/* Set decorations */
		messagePane.setOpaque(false);
		messagePane.setEditable(false);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
	}
	
	/**
	 * Create the inventory panel to display number of keys the player is carrying.
	 * @param xPos
	 */
	private void buildInventoryPane(int xPos){
		inventoryPane.setBounds(xPos, 0, 100, 100);
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
	 * Stores and displays a message to the user. Will display at most the 4 most recent messages.
	 */
	public void addMessage(String message){
		messages.add(message);
		String msg = "";
		for (int pos=messages.size()-1, max=0; pos>=0 && max < 4; pos--, max++){
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