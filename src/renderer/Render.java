package renderer;

import javax.swing.JFrame;

public class Render {
	private RenderPane gameCanvas;
	
	public Render() {
		JFrame f = new JFrame("Elliots Awesome Renderer");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //f.addKeyListener(new Listener(level));
       
        this.gameCanvas = new RenderPane();
     

        f.add(gameCanvas);
        f.pack();
        f.setVisible(true);
	}
	
	public void redraw(char[][] level){
		this.gameCanvas.setLevel(level);
		this.gameCanvas.repaint();
	}
}
