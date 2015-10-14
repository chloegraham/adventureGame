package server.tiles;

/**
 * An interface for our two types of doors.
 * 
 * Doors are either locked, or unlocked. 
 * A player cannot pass through a locked door
 *
 */
public interface Door{
	public boolean isLocked();
	public void lock();
	public void unlock();
}
