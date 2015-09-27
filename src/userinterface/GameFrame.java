package userinterface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
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
	
	private final Dimension frameSize;
	
	private final JTextPane messagePane = new JTextPane();
	private final JLayeredPane layerPane = new JLayeredPane();
	
	private ArrayList<String> messages = new ArrayList<String>();
	
	/**
	 * Sets up the window to display the game and all controls/menus.
	 * Adds Action, Key & Mouse listeners.
	 */
	public GameFrame(RenderPane graphics) {
		super("Adventure Game");
		Dimension dim = graphics.getPreferredSize();
		int renderWidth = (int) dim.getWidth();
		int renderHeight = (int) dim.getHeight();
		
		/* Position and size of panels */
		graphics.setBounds(0, 0, renderWidth, renderHeight);
		messagePane.setBounds(0, renderHeight, renderWidth, textHeight);
		
		/* Position (centre) and size of frame */
		frameWidth = (int) (dim.getWidth() + 16);		// Needs extra width for border
		frameHeight = (int) (dim.getHeight() + textHeight);
		frameSize = new Dimension(frameWidth, frameHeight);
		this.setPreferredSize(frameSize);
		int frameX = (int) ((screenSize.getWidth()/2)-(frameWidth/2));
		int frameY = (int) ((screenSize.getHeight()/2)-(frameHeight/2));
		this.setLocation(frameX, frameY);	// Position in centre of screen
		
		add(layerPane);

		/* Add panes from furthest to closest */
		layerPane.add(graphics, new Integer(0));
		layerPane.add(messagePane, new Integer(1));

		/* Set decorations */
		messagePane.setOpaque(false);
		messagePane.setEditable(false);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
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
	
	public void clearMessages(){
		messages = new ArrayList<String>();
	}

	private static final long serialVersionUID = 1L;

}