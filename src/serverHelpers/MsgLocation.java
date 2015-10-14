package serverHelpers;

public enum MsgLocation {
	FOREST ("Forest"),
	DESERT ("Desert"),
	CAVES ("Caves");
	
	private String description;
	
	private MsgLocation(String desc) {
		this.description = desc;
	}
	
	public static MsgLocation getMsg(int n) {
		for (MsgLocation m : values())
			if (m.ordinal() == n)
				return m;
		throw new RuntimeException("Passed an invalid integer that doesn't relate to an ordinal in MsgLocation.enum");
	}
	
	@Override
	public String toString() {
		return description;
	}
}
