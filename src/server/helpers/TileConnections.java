package server.helpers;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/* 
 * A class to create a two way map structure to simulate a linked list between rooms and levels.
 * The class also handles the relationship between pressure pads and doors which are locked
 * and unlocked by pressure pads.
 * 
 * Slightly hard-coded. Borderline.
 * 
 * @author benscully & Chris
 */


public class TileConnections {
	
	//Final fields of all the connections in level 1
	private static final TileFullLocation lv0rm1out = new TileFullLocation(0, 0, new Point(3,0));
	private static final TileFullLocation lv0rm2in = new TileFullLocation(0, 1, new Point(3,4));
	private static final TileFullLocation lv0rm2out = new TileFullLocation(0, 1, new Point(2,3));
	private static final TileFullLocation lv0rm3pp = new TileFullLocation(0, 2, new Point(0,2));
	private static final TileFullLocation lv0rm3in = new TileFullLocation(0, 2, new Point(2,1));
	private static final TileFullLocation lv0rm3out = new TileFullLocation(0, 2, new Point(5,1));
	private static final TileFullLocation lv0rm4in = new TileFullLocation(0, 3, new Point(0,0));
	private static final TileFullLocation lv0rm4out = new TileFullLocation(0, 3, new Point(9,0));
	
	//Final fields of all the connections to move between rooms in level 2
	private static final TileFullLocation rm1in = new TileFullLocation(1, 0, new Point(0,7));
	private static final TileFullLocation rm1out = new TileFullLocation(1, 0, new Point(3,8));
	private static final TileFullLocation rm1ppDoor = new TileFullLocation(1, 0, new Point(3,3));
	private static final TileFullLocation rm2in = new TileFullLocation(1, 1, new Point(0,1));
	private static final TileFullLocation rm2out = new TileFullLocation(1, 1, new Point(8,1));
	private static final TileFullLocation rm3in = new TileFullLocation(1, 2, new Point(0,2));
	private static final TileFullLocation rm3out = new TileFullLocation(1, 2, new Point(18,2));
	private static final TileFullLocation rm3pp = new TileFullLocation(1, 2, new Point(1,1));
	
	//Final fields for every pressure pad inside level 2
	private static final TileFullLocation rm1pp = new TileFullLocation(1, 0, new Point(5,7));
	private static final TileFullLocation rm3pp2 = new TileFullLocation(1, 2, new Point(3,1));
	private static final TileFullLocation rm3pp3 = new TileFullLocation(1, 2, new Point(4,1));
	private static final TileFullLocation rm3pp4 = new TileFullLocation(1, 2, new Point(5,1));
	private static final TileFullLocation rm3pp5 = new TileFullLocation(1, 2, new Point(6,1));
	private static final TileFullLocation rm3pp6 = new TileFullLocation(1, 2, new Point(7,1));
	private static final TileFullLocation rm3pp7 = new TileFullLocation(1, 2, new Point(8,1));
	private static final TileFullLocation rm3pp8 = new TileFullLocation(1, 2, new Point(9,1));
	private static final TileFullLocation rm3pp9 = new TileFullLocation(1, 2, new Point(10,1));
	private static final TileFullLocation rm3pp10 = new TileFullLocation(1, 2, new Point(11,1));
	private static final TileFullLocation rm3pp11 = new TileFullLocation(1, 2, new Point(12,1));
	
	//Final fields for every door that is controlled by a pressure pad in level 2
	private static final TileFullLocation rm3Door2 = new TileFullLocation(1, 3, new Point(17,2));
	private static final TileFullLocation rm3Door3 = new TileFullLocation(1, 3, new Point(17,1));
	private static final TileFullLocation rm3Door4 = new TileFullLocation(1, 3, new Point(17,0));
	private static final TileFullLocation rm3Door5 = new TileFullLocation(1, 3, new Point(15,0));
	private static final TileFullLocation rm3Door6 = new TileFullLocation(1, 3, new Point(14,0));
	private static final TileFullLocation rm3Door7 = new TileFullLocation(1, 3, new Point(13,0));
	private static final TileFullLocation rm3Door8 = new TileFullLocation(1, 3, new Point(13,1));
	private static final TileFullLocation rm3Door9 = new TileFullLocation(1, 3, new Point(13,2));
	private static final TileFullLocation rm3Door10 = new TileFullLocation(1, 3, new Point(14,2));
	private static final TileFullLocation rm3Door11 = new TileFullLocation(1, 3, new Point(15,2));
	
	
	
	/**
	 * @param tileFullLocation the variables storing all the information describing a tiles location 
	 * @return a map of every pairing of tiles, pressure pad and door or door and door for movement between rooms/level
	 */
	private static TileFullLocation getConnectedTile1(TileFullLocation tileFullLocation) {
		Map<TileFullLocation, TileFullLocation> map = new HashMap<>();
		map.put(lv0rm1out, lv0rm2in);
		map.put(lv0rm2out, lv0rm3in);
		map.put(lv0rm3pp, lv0rm3out);
		map.put(lv0rm3out, lv0rm4in);
		map.put(lv0rm4out, rm1in);
		map.put(rm1pp, rm1ppDoor);
		map.put(rm1out, rm2in);
		map.put(rm2out, rm3in);
		map.put(rm3pp, rm3out);
		map.put(rm3pp2, rm3Door2);
		map.put(rm3pp3, rm3Door3);
		map.put(rm3pp4, rm3Door4);
		map.put(rm3pp5, rm3Door5);
		map.put(rm3pp6, rm3Door6);
		map.put(rm3pp7, rm3Door7);
		map.put(rm3pp8, rm3Door8);
		map.put(rm3pp9, rm3Door9);
		map.put(rm3pp10, rm3Door10);
		map.put(rm3pp11, rm3Door11);
		return map.get(tileFullLocation);
	}
	
	/**
	 * @param tileFullLocation the variables storing all the information describing a tiles location 
	 * @return a map of every pairing of tiles, pressure pad and door or door and door for movement between rooms/level
	 */
	private static TileFullLocation getConnectedTile2(TileFullLocation tileFullLocation) {
		Map<TileFullLocation, TileFullLocation> map = new HashMap<>();
		map.put(lv0rm2in, lv0rm1out);
		map.put(lv0rm3in, lv0rm2out);
		map.put(lv0rm4in, lv0rm3out);
		map.put(rm2in, rm1out);
		map.put(rm3in, rm2out);
		return map.get(tileFullLocation);
	}
	
	/**
	 * @param currStage 
	 * @param currRoom
	 * @param currLocation
	 * @return the pair of the current tile, based on the map populated inside this class
	 */
	public static TileFullLocation getConnectedTile(int currStage, int currRoom, Point currLocation) {
		TileFullLocation tileFullLocation = new TileFullLocation(currStage, currRoom, currLocation);
		TileFullLocation transfer = null;
		transfer = getConnectedTile1(tileFullLocation);
		if (transfer == null)
			transfer = getConnectedTile2(tileFullLocation);
		
		assert(transfer != null);
		
		return transfer;
	}
}
