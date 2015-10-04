package serverclient;

import movable.Player;
import testconvert.ConvertPlayer;

public class LevelState {
	private char[][] level;
	private char[][] objects;
	private char[][] movables;
	private String encodedLayers;
	private Player players;
	private String encodedPlayers;
	
	public LevelState(char[][] level, char[][] objects, char[][] movables) {
		this.level = level;
		this.objects = objects;
		this.movables = movables;
		convertCharsToEncodedString();
	}
	
	public LevelState(String encodedLayers) {
		this.encodedLayers = encodedLayers;
		convertEncodedStringToChars();
	}
	
	public LevelState(Player players, char[][] level, char[][] objects, char[][] movables) {
		this.players = players;
		convertPlayersToEncodedString();
		this.level = level;
		this.objects = objects;
		this.movables = movables;
		convertCharsToEncodedString();
	}
	
	public LevelState(String encodedPlayers, String encodedLayers) {
		this.encodedPlayers = encodedPlayers;
		convertEncodedStringToPlayers();
		this.encodedLayers = encodedLayers;
		convertEncodedStringToChars();
	}
	
	public char[][] getLevel() { return level; }
	public char[][] getObjects() { return objects; }
	public char[][] getMovables() { return movables; }
	public String getEncodedLayers() { return encodedLayers; }
	public String getEncodedPlayers() { return encodedPlayers; }
	public String getEncodedPlayersAndLayers() { return encodedPlayers + "!" + encodedLayers; }
	
	private void convertCharsToEncodedString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < level.length; x++) {
			sb.append(level[x]);
			sb.append('%');
		}
		sb.append('@');
		
		for (int x = 0; x < objects.length; x++) {
			sb.append(objects[x]);
			sb.append('%');
		}
		sb.append('@');
		
		for (int x = 0; x < movables.length; x++) {
			sb.append(movables[x]);
			sb.append('%');
		}
		sb.append('@');
		encodedLayers = sb.toString() + "<Split>";
	}
	
	private void convertEncodedStringToChars() {
		// Split String up in to x3 Strings which will be converted to char[][]
		String[] layers = encodedLayers.split("@");
	
		// Split up the 2d-char[][] in to 1d-char[] (they are still Strings at the moment)
		String[] subLayers1 = layers[0].split("%");
		String[] subLayers2 = layers[1].split("%");
		String[] subLayers3 = layers[2].split("%");
		
		// Now build the actual 2d-char[][] from the broken down Strings
		level = new char[subLayers1.length][];
		for (int x = 0; x < level.length; x++)
			level[x] = subLayers1[x].toCharArray();
		
		objects = new char[subLayers2.length][];
		for (int x = 0; x < objects.length; x++)
			objects[x] = subLayers2[x].toCharArray();
		
		movables = new char[subLayers3.length][];
		for (int x = 0; x < movables.length; x++)
			movables[x] = subLayers3[x].toCharArray();
	}
	
	private void convertPlayersToEncodedString() {
		encodedPlayers = ConvertPlayer.fromPlayer(players);
	}
	
	private void convertEncodedStringToPlayers() {
		players = ConvertPlayer.toPlayer(encodedPlayers);
	}
}
