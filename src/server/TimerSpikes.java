package server;

import java.io.IOException;

/**
 * This timer makes the spikes go up and down once a second. 
 *
 */
public class TimerSpikes implements Runnable {
	private Server server;
	
	private boolean pause = false;
	
	/**
	 * @param s The server who's spikes will be activated
	 */
	public TimerSpikes(Server s) {
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
