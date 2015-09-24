package userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import renderer.RenderPane;

/**
 * The application window for the game. Sets up window sizes and buttons available to the user.
 * @author Kirsty
 */
public class GameFrame extends JFrame {
	/* Determine size of game window */
	private static final int frameWidth = 850;
	private static final int frameHeight = 650;
	private static final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static final Dimension FRAMESIZE = new Dimension(frameWidth, frameHeight);
	public static final int FRAMEX = (int) ((dim.getWidth() / 2)-(frameWidth / 2));
	public static final int FRAMEY = (int) ((dim.getHeight() / 2)-(frameHeight / 2));
	
	private final JPanel graphicsPane = new JPanel();
	
	/**
	 * Sets up the window to display the game and all controls/menus.
	 * Adds Action, Key & Mouse listeners.
	 */
	public GameFrame(RenderPane graphics) {
		super("Chicken Little");
		
		buildGraphicsPane(graphics);
		
		/* TODO Set up layered game menus */
		
		add(graphicsPane);
		
		/* Set sizes, locations and decoration options for the full frame. */
		this.setPreferredSize(FRAMESIZE);
		this.setLocation(FRAMEX, FRAMEY);	// Position in centre of screen
		//this.setUndecorated(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
	}
	
	/**
	 * Builds the Panel that displays the game graphics. Includes a solid black border around the Renderer.  
	 */
	private void buildGraphicsPane(RenderPane graphics){
		BoxLayout layout = new BoxLayout(graphicsPane, BoxLayout.Y_AXIS);
		graphicsPane.setLayout(layout);
		
		// Add Renderer to the centre of the panel
		graphicsPane.add(Box.createVerticalGlue());
		graphicsPane.add(graphics);
		graphicsPane.add(Box.createVerticalGlue());
		
		// Set size, alignment and background colour
		Dimension rendererSize = new Dimension(frameWidth-50, frameHeight-50);
		graphics.setMaximumSize(rendererSize);		// leave a small border around the Renderer
		graphics.setPreferredSize(rendererSize);
		graphics.setAlignmentX(CENTER_ALIGNMENT);
		
		graphicsPane.setPreferredSize(FRAMESIZE);
		graphicsPane.setBackground(Color.black);
	}
	
	private static final long serialVersionUID = 1L;

}