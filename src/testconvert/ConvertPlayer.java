package testconvert;

import gameWorld.Direction;

import java.awt.Point;

import movable.Player;

public class ConvertPlayer {
	
	public static Player toPlayer(String stmt) {
		String[] parts = stmt.split("%");
		
		int userID = Integer.parseInt(parts[0]);
		int levelID = Integer.parseInt(parts[1]);
		int keyAmount = Integer.parseInt(parts[2]);
		boolean boulder = Integer.parseInt(parts[3]) == 1;
		
		Direction direction = Direction.getMsg( Integer.parseInt(parts[4]) );
		
		int x = Integer.parseInt(parts[5]);
		int y = Integer.parseInt(parts[6]);
		Point point = new Point(x, y);
		
		System.out.println(userID + " " + levelID + " " + keyAmount + " " + boulder + " " +  direction + " " + point);
		Player player = new Player(userID, levelID, keyAmount, boulder, direction, point);
		return player;
	}
	
	public static String fromPlayer(Player player) {
		String str = "";
		
		int userID = player.getUserID();
		str += userID + "%";
		
		int levelID = player.getLevelID();
		str += levelID + "%";
		
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
		
		str += Messages.DELIM_PLAYER;
		return str;
	}
}