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
 * Displays one splash screen at a time.
 * @author Kirsty
 */
public class SplashScreen extends JPanel {
	public final int NO_SCREEN = 0;
	public final int DEATH_SCREEN = 1;
	private final JPanel[] allPanels = new JPanel[2];
	
	private final Dimension buttonSize = new Dimension(100, 25);
	
	private int openPane = NO_SCREEN;		// Pane to show on initial startup
	
	public SplashScreen(Listener listener){
		this.setLayout(new CardLayout());
		this.setOpaque(false);
		
		// Build invisible pane
		allPanels[NO_SCREEN] = new JPanel();
		allPanels[NO_SCREEN].setOpaque(false);
		
		buildDeathScreen(listener);
		
		this.add(allPanels[NO_SCREEN]);
		this.add(allPanels[DEATH_SCREEN]);
		
		allPanels[openPane].setVisible(true);
	}
	
	/**
	 * Build the screen that shows upon the player's death
	 */
	private void buildDeathScreen(Listener listener){
		allPanels[DEATH_SCREEN] = new JPanel();
		allPanels[DEATH_SCREEN].setBackground(new Color(200, 0, 0, 200));
		
		allPanels[DEATH_SCREEN].setLayout(new BoxLayout(allPanels[DEATH_SCREEN], BoxLayout.Y_AXIS));
		
		JLabel message = new JLabel("You died!");
		message.setFont(new Font("Serif", Font.BOLD, 35));
		message.setAlignmentX(CENTER_ALIGNMENT);
		
		allPanels[DEATH_SCREEN].add(Box.createVerticalGlue());
		allPanels[DEATH_SCREEN].add(message);
		allPanels[DEATH_SCREEN].add(Box.createVerticalGlue());
		allPanels[DEATH_SCREEN].add(makeButton(listener, "Continue"));
		allPanels[DEATH_SCREEN].add(makeButton(listener, "Exit"));
		allPanels[DEATH_SCREEN].add(Box.createVerticalGlue());
	}
	
	/**
	 * Builds a new button with the given listener and Action command, and an x alignment in the centre
	 */
	private JButton makeButton(Listener listener, String action){
		JButton btn = new JButton(action);
		btn.setMaximumSize(buttonSize);
		btn.setActionCommand(action);
		btn.addActionListener(listener);
		btn.setAlignmentX(CENTER_ALIGNMENT);
		return btn;
	}
	
	/**
	 * Hide the originally open pane and display the passed in pane instead.
	 */
	public void setVisible(int newPane){
		allPanels[openPane].setVisible(false);
		openPane = newPane;
		allPanels[openPane].setVisible(true);
	}

	private static final long serialVersionUID = 1L;
	
}
