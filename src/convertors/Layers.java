package convertors;

public class Layers {
	private char[][] level;
	private char[][] objects;
	private char[][] movables;
	
	public char[][] getDecodedLevel() { return level; }
	public char[][] getDecodedObjects() { return objects; }
	public char[][] getDecodedMovables() { return movables; }
	
	public void decode(String encodedLayers) {
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
}
