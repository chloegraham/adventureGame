package testconvert;

import gameWorld.Direction;
import movable.Player;
import testenums.MsgAction;
import testenums.MsgLocation;
import testenums.MsgObject;



public class ConvertAction {
	private static final String DELIM = "<Msg>";
	
	public static String inspectMsg(int object) {
		String str = "#Msg";
		str += MsgAction.INSPECT.toString() + "%";
		str += MsgObject.getMsg(object).toString() + "%";
		str += DELIM;
		return str;
	}
	
	public static String keyMsg() {
		String str = "#Msg";
		str += MsgAction.PICK.toString() + "%";
		str += MsgObject.KEY.toString() + "%";
		str += DELIM;
		return str;
	}
	
	public static String boulderMsg(boolean pickup) {
		String str = "#Msg";
		if (pickup) str += MsgAction.PICK.toString() + "%";
		else        str += MsgAction.DOWN.toString() + "%";
		str += MsgObject.BOULDER.toString() + "%";
		str += DELIM;
		return str;
	}
	
	public static String openMsg(boolean open, boolean chest) {
		String str = "#Msg";
		if (open) str += MsgAction.OPEN.toString() + "%";
		else      str += MsgAction.NOOPEN.toString() + "%";
		
		if (chest) str += MsgObject.CHEST.toString() + "%";	// chest true = chest
		else       str += MsgObject.DOOR.toString() + "%";  // chest false = door
		str += DELIM;
		return str;
	}
	
	public static String moveMsg(Player player, boolean moved, int location) {
		String str = "#Msg";
		str += MsgAction.FACE.toString() + player.getDirection().toString() + "%";
		
		if (moved) str += MsgAction.MOVE.toString();
		else      str += MsgAction.NOMOVE.toString();
		
		str += MsgLocation.getMsg(location).toString() + "%";
		str += DELIM;
		return str;
	}
	
	public static String moveMsg(Direction dir, boolean moved, int location) {
		String str = "#Msg";
		str += MsgAction.FACE.toString() + dir.toString() + "%";
		
		if (moved) str += MsgAction.MOVE.toString() + dir.toString();
		else      str += MsgAction.NOMOVE.toString() + dir.toString();
		
		str += MsgAction.LOCATION.toString() + MsgLocation.getMsg(location).toString() + "%";
		str += DELIM;
		return str;
	}
	
	public static String fromServer(String stmt) {
		stmt = gobble(stmt, "#Msg");
		
		String[] parts = stmt.split("%");
		
		String part1 = parts[0];
		String part2 = parts[1];
		
		return part1 + part2;
	}
	
	private static String gobble(String stmt, String prefix) {
		if (stmt.startsWith(prefix)) 
			return stmt.substring(prefix.length());
		else 
			throw new IllegalArgumentException("gobble(stmt,prefix) - Grammer of " + stmt + " suppose to contain " + prefix + " but didn't.");
	}
}
