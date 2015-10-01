package serverclient;

public class GameState {
	private char[][] layerStatic;
	private char[][] layerStaticWithStates;
	private char[][] layerDynamic;
	private String allThreeLayers;
	
	public GameState(char[][] layerStatic, char[][] layerStaticWithStates, char[][] layerDynamic) {
		this.layerStatic = layerStatic;
		this.layerStaticWithStates = layerStaticWithStates;
		this.layerDynamic = layerDynamic;
		convertCharToString();
	}
	
	public GameState(String allThreeLayers) {
		this.allThreeLayers = allThreeLayers;
		convertStringToChar();
	}
	
	public char[][] getLayerStatic() {
		return layerStatic;
	}
	
	public char[][] getLayerStaticWithStates() {
		return layerStaticWithStates;
	}
	
	public char[][] getLayerDynamic() {
		return layerDynamic;
	}
	
	public String getGameStateString() {
		return allThreeLayers;
	}
	
	private void convertCharToString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < layerStatic.length; x++) {
			sb.append(layerStatic[x]);
			sb.append('%');
		}
		sb.append('@');
		
		for (int x = 0; x < layerStaticWithStates.length; x++) {
			sb.append(layerStaticWithStates[x]);
			sb.append('%');
		}
		sb.append('@');
		
		for (int x = 0; x < layerDynamic.length; x++) {
			sb.append(layerDynamic[x]);
			sb.append('%');
		}
		sb.append('@');
		allThreeLayers = sb.toString();
		
		System.out.println("test");
	}
	
	private void convertStringToChar() {
		
		// Split String up in to x3 Strings which will be converted to char[][]
		String[] layers = allThreeLayers.split("@");
	
		// Split up the 2d-char[][] in to 1d-char[] (they are still Strings at the moment)
		String[] subLayers1 = layers[0].split("%");
		String[] subLayers2 = layers[1].split("%");
		String[] subLayers3 = layers[2].split("%");
		
		// Now build the actual 2d-char[][] from the broken down Strings
		layerStatic = new char[subLayers1.length][];
		for (int x = 0; x < layerStatic.length; x++)
			layerStatic[x] = subLayers1[x].toCharArray();
		
		layerStaticWithStates = new char[subLayers2.length][];
		for (int x = 0; x < layerStaticWithStates.length; x++)
			layerStaticWithStates[x] = subLayers2[x].toCharArray();
		
		layerDynamic = new char[subLayers3.length][];
		for (int x = 0; x < layerDynamic.length; x++)
			layerDynamic[x] = subLayers3[x].toCharArray();
	}
}
