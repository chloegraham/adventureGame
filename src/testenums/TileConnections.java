package testenums;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class TileConnections {
	
	private static TileFullLocation getConnectedTile1(TileFullLocation tileFullLocation) {
		Map<TileFullLocation, TileFullLocation> map = new HashMap<>();
		TileFullLocation d1 = new TileFullLocation(0, 0, new Point(1,6));
		TileFullLocation d2 = new TileFullLocation(0, 1, new Point(3,0));
		map.put(d1, d2);
		return map.get(tileFullLocation);
	}
	
	private static TileFullLocation getConnectedTile2(TileFullLocation tileFullLocation) {
		Map<TileFullLocation, TileFullLocation> map = new HashMap<>();
		TileFullLocation d1 = new TileFullLocation(0, 0, new Point(1,6));
		TileFullLocation d2 = new TileFullLocation(0, 1, new Point(3,0));
		map.put(d2, d1);
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
