package renderer;

import gameWorld.Level;

import javax.swing.JFrame;

import userinterface.Listener;

public class Render {
	private RenderPane gameCanvas;
	private Level level;
	
	public Render() {
		this.level = new Level(this);
		
		JFrame f = new JFrame("Elliots Awesome Renderer");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(new Listener(level));
       
        this.gameCanvas = new RenderPane();
        
        
       
//        char[][] level =    {{'w','w','e','e'},
//                            {'w','e','p','e'},
//                            {'e','e','e','e'},
//                            {'e','e','e','e'}};


        f.add(gameCanvas);
        f.pack();
        f.setVisible(true);
	}
	
	public void redraw(char[][] level){
		this.gameCanvas.setLevel(level);
		this.gameCanvas.repaint();
	}
	
	public void horriblemethod() {
		level.horriblemethod();
	}
}
