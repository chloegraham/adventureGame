package movable;

import tiles.Tile;

public abstract class Item implements Tile{

	protected String id;
	
	public Item(String identifier){
		this.id = identifier;
	}
	
}
