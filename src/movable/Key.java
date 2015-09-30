package movable;

public class Key implements Item {
	
	private String description;
	private String id;
	
	/*public Key(String description, int id) {
		this.description = description;
		this.id = id;
	}*/
	
	public Key(String id) {
		this.id = id;
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

	@Override
	public String getDescription() {
		return this.description;
	}
}
