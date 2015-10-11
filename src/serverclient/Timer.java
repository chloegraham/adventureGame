package serverclient;

import java.io.IOException;

public class Timer implements Runnable {
	private Server server;
	
	private boolean pause = false;
	
	public Timer(Server s) {
		server = s;
	}

	public void run() {
		try {
			while (!pause) {
				// every xxx seconds
				Thread.sleep(1000);
			
				// activate all the spikes
				server.activateSpikes();
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 *  A way to stop and start the Timer
	 */
	public void pause() {
		pause = true;
	}
	
	public void unpause() {
		pause = false;
		run();
	}
}
