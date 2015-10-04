package gameWorld;

public enum Direction {
	NORTH ("North"),
	SOUTH ("South"),
	EAST ("East"),
	WEST ("West");
	
	private String description;
	
	private Direction(String desc) {
		this.description = desc;
	}
	
	public static Direction getMsg(int n) {
		for (Direction m : values())
			if (m.ordinal() == n)
				return m;
		throw new RuntimeException("Passed an invalid integer that doesn't relate to an ordinal in MsgDirection.enum");
	}
	
	@Override
	public String toString() {
		return description;
	}
}
