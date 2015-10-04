package testconvert;

import movable.Player;


public class ConvertKey {

	public static void main(String[] args) {
		String str = "#Key202%4%@";
		System.out.println(str);
		int[] ints = fromServer(str);
		System.out.println(ints[0] + " " + ints[1]);
	}
	
	public static int[] fromServer(String stmt) {
		stmt = gobble(stmt, "#Key");
		
		String[] parts = stmt.split("%");
		
		int uid = Integer.parseInt(parts[0]);
		int keyAmount = Integer.parseInt(parts[1]);
		int[] ints = {uid, keyAmount};
		System.out.println(uid + " " + keyAmount);
		return ints;
	}
	
	public static String toServer(Player player) {
		String str = "#Key";
		
		int uid = player.getUID();
		str += uid + "%";
		
		int keyAmount = player.numberOfKeys();
		str += keyAmount + "%";
		
		str += "@";
		return str;
	}
	
	private static String gobble(String stmt, String prefix) {
		if (stmt.startsWith(prefix)) 
			return stmt.substring(prefix.length());
		else 
			throw new IllegalArgumentException("gobble(stmt,prefix) - Grammer of " + stmt + " suppose to contain " + prefix + " but didn't.");
	}
}
