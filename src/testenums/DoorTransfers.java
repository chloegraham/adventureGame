package testenums;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class DoorTransfers {
	
	public static Doors getDoors1(Doors doors) {
		Map<Doors, Doors> map = new HashMap<>();
		Doors d1 = new Doors(0, 0, new Point(1,6));
		Doors d2 = new Doors(0, 1, new Point(3,0));
		map.put(d1, d2);
		return map.get(doors);
	}
	
	public static Doors getDoors2(Doors doors) {
		Map<Doors, Doors> map = new HashMap<>();
		Doors d1 = new Doors(0, 0, new Point(1,6));
		Doors d2 = new Doors(0, 1, new Point(3,0));
		map.put(d2, d1);
		return map.get(doors);
	}
	
	public static Doors transfer(int currSt, int currRm, Point currLocation) {
		Doors doors = new Doors(currSt, currRm, currLocation);
		Doors transfer = null;
		transfer = getDoors1(doors);
		if (transfer == null)
			transfer = getDoors2(doors);
		
		assert(transfer != null);
		
		return transfer;
	}
}
