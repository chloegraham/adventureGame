package server.helpers;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class TileConnections {
	private static final TileFullLocation rm1out = new TileFullLocation(0, 0, new Point(3,0));
	private static final TileFullLocation rm2in = new TileFullLocation(0, 1, new Point(3,4));
	private static final TileFullLocation rm2out = new TileFullLocation(0, 1, new Point(2,3));
	private static final TileFullLocation rm3pp = new TileFullLocation(0, 2, new Point(0,2));
	private static final TileFullLocation rm3in = new TileFullLocation(0, 2, new Point(2,1));
	private static final TileFullLocation rm3out = new TileFullLocation(0, 2, new Point(5,1));
	private static final TileFullLocation rm4in = new TileFullLocation(0, 3, new Point(0,0));
	
	
	private static TileFullLocation getConnectedTile1(TileFullLocation tileFullLocation) {
		Map<TileFullLocation, TileFullLocation> map = new HashMap<>();
		map.put(rm1out, rm2in);
		map.put(rm2out, rm3in);
		map.put(rm3pp, rm3out);
		map.put(rm3out, rm4in);
		return map.get(tileFullLocation);
	}
	
	private static TileFullLocation getConnectedTile2(TileFullLocation tileFullLocation) {
		Map<TileFullLocation, TileFullLocation> map = new HashMap<>();
		map.put(rm2in, rm1out);
		map.put(rm3in, rm2out);
		map.put(rm4in, rm3out);
		return map.get(tileFullLocation);
	}
	
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
