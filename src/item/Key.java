package item;

public class Key implements Item {
	
	private String id;
	
	public Key(String identifier){
		this.id = identifier;
	}
	
	public String toString(){
		return id;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Key){
			return this.id == ((Key)o).id;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		
		int n = 3;
		n += n * id.hashCode();
		return n;
	}
}
