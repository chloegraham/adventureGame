package testconvert;

import gameWorld.Direction;

import java.awt.Point;

import movable.Player;

public class ConvertPlayer {
	
	public static Player toPlayer(String stmt) {
		String[] parts = stmt.split("%");
		
		int uid = Integer.parseInt(parts[0]);
		int keyAmount = Integer.parseInt(parts[1]);
		boolean boulder = Integer.parseInt(parts[2]) == 1;
		
		Direction direction = Direction.getMsg( Integer.parseInt(parts[3]) );
		
		int x = Integer.parseInt(parts[4]);
		int y = Integer.parseInt(parts[5]);
		Point point = new Point(x, y);
		
		System.out.println(uid + " " + keyAmount + " " + boulder + " " +  direction + " " + point);
		Player player = new Player(uid, keyAmount, boulder, direction, point);
		return player;
	}
	
	public static String fromPlayer(Player player) {
		String str = "";
		
		int uid = player.getUID();
		str += uid + "%";
		
		int keyAmount = player.numberOfKeys();
		str += keyAmount + "%";
		
		int boulder = player.containsBoulder() ? 1 : 0;
		str += boulder + "%";
		
		Direction direction = player.getDirection();
		str += direction.ordinal() + "%";
		
		Point point = player.getLocation();
		int x = point.x;
		str += x + "%";
		
		int y = point.y;
		str += y + "%";
		
		str += "<Play>";
		return str;
	}
	
	private static String gobble(String stmt, String prefix) {
		if (stmt.startsWith(prefix)) 
			return stmt.substring(prefix.length());
		else 
			throw new IllegalArgumentException("gobble(stmt,prefix) - Grammer of " + stmt + " suppose to contain " + prefix + " but didn't.");
	}
}