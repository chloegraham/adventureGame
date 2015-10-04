package testconvert;

public class Messages {

	public void decode(String encodedMessages) {
		String[] parts = encodedMessages.split("%");
		
		String part1 = parts[0];
		String part2 = parts[1];
	}

	public String getDecoded() {
		// TODO Auto-generated method stub
		return null;
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
