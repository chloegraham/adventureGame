package testenums;

public enum MsgDirection {
	NORTH ("North"),
	SOUTH ("South"),
	EAST ("East"),
	WEST ("West");
	
	private String description;
	
	private MsgDirection(String desc) {
		this.description = desc;
	}
	
	public static MsgDirection getMsg(int n) {
		for (MsgDirection m : values())
			if (m.ordinal() == n)
				return m;
		throw new RuntimeException("Passed an invalid integer that doesn't relate to an ordinal in MsgDirection.enum");
	}
	
	@Override
	public String toString() {
		return description;
	}
}
