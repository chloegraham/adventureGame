package userinterface;

import java.util.TimerTask;

import renderer.RenderPane;

public class DrawLoop extends TimerTask {
	private RenderPane graphics;
	
	public void setGraphics(RenderPane g){
		this.graphics = g;
	}

	@Override
	public void run() {
		if(graphics == null){
			System.out.println("nothing set yet");
		}else{
			graphics.update();
			graphics.repaint();
		}
	}

}
