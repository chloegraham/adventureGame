package client;

/**
 * This class stores the 3 char layers, for the RenderPane to draw.
 * @author Eliot
 *
 */
public class Layers {
	private char[][] wallsEmpties;
	private char[][] furniture;
	private char[][] bouldersPlayers;
	
	public char[][] getDecodedWallsEmpties() { return wallsEmpties; }
	public char[][] getDecodedFurniture() { return furniture; }
	public char[][] getDecodedBouldersPlayers() { return bouldersPlayers; }
	
	public void decode(String encodedLayers) {
		// Split String up in to x3 Strings which will be converted to char[][]
		String[] layers = encodedLayers.split("@");
	
		// Split up the 2d-char[][] in to 1d-char[] (they are still Strings at the moment)
		String[] wallsEmptiesARRAY = layers[0].split("%");
		String[] furnitureARRAY = layers[1].split("%");
		String[] bouldersPlayersARRAY = layers[2].split("%");
		
		// Now build the actual 2d-char[][] from the broken down Strings
		wallsEmpties = new char[wallsEmptiesARRAY.length][];
		for (int x = 0; x < wallsEmpties.length; x++)
			wallsEmpties[x] = wallsEmptiesARRAY[x].toCharArray();
		
		furniture = new char[furnitureARRAY.length][];
		for (int x = 0; x < furniture.length; x++)
			furniture[x] = furnitureARRAY[x].toCharArray();
		
		bouldersPlayers = new char[bouldersPlayersARRAY.length][];
		for (int x = 0; x < bouldersPlayers.length; x++)
			bouldersPlayers[x] = bouldersPlayersARRAY[x].toCharArray();
	}
}
