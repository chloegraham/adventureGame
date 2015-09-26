package item;

public class Key implements Item {
	
	private String id;
	
	public Key(String identifier){
		this.id = identifier;
	}
	
	public String toString(){
		return id;
	}
}
