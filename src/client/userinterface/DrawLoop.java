package client.userinterface;

import java.util.TimerTask;

import client.renderer.RenderPane;

/**
 * @author Eliot
 * 300285842
 * 
 * Simple draw loop for camera animations.
 * 
 * You pass it a RenderPane, then it constantly tells the RenderPane to draw
 *
 */
public class DrawLoop extends TimerTask {
	private RenderPane graphics;
	
	/**
	 * Sets the target RenderPane
	 * @param g
	 */
	public void setGraphics(RenderPane g){
		this.graphics = g;
	}

	@Override
	public void run() {
		if(graphics == null){
			//System.out.println("Graphics not set yet");
		}else{
			graphics.cameraTick();
			graphics.repaint();
		}
	}

}
