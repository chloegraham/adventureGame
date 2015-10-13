package main;

import java.util.TimerTask;
import serverclient.Client;

public class DrawLoop extends TimerTask {
	public Client c1;
	public Client c2;
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(c1 == null || c2 == null){
			System.out.println("nothing set yet");
		}else{
			c1.reDraw();
			c2.reDraw();
		}
	}

}
