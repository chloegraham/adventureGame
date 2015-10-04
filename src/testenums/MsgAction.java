package testenums;

public enum MsgAction {
	
	PICK ("You picked up a "),
	DOWN ("You put down a "),
	OPEN ("You opened a "),
	NOOPEN ("You can't open that "),
	CLOSE ("You closed "),
	NOCLOSE ("You can't close that "),
	FACE ("You're facing "),
	MOVE ("You moved "),
	NOMOVE ("You can't move "),
	LOCATION ("Your location is "),
	INSPECT ("You're looking at a ");
	
	private String description;
	
	private MsgAction(String desc) {
		this.description = desc;
	}
	
	public static MsgAction getMsg(int n) {
		for (MsgAction m : values())
			if (m.ordinal() == n)
				return m;
		throw new RuntimeException("Passed an invalid integer that doesn't relate to an ordinal in MsgAction.enum");
	}
	
	@Override
	public String toString() {
		return description;
	}
}
