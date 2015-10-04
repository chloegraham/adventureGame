package testenums;

public enum MsgObject {
	BOULDER ("Boulder"),
	KEY ("Key"),
	DOOR ("Unlocked Door"),
	NODOOR ("Locked Door"),
	CHEST ("Open Chest"),
	NOCHEST ("Closed Chest"),
	SPIKES ("Spikes"),
	PAD ("Activated Pressure Pad"),
	NOPAD ("Deactivated Pressure Pad");
	
	private String description;
	
	private MsgObject(String desc) {
		description = desc;
	}
	
	public static MsgObject getMsg(int n) {
		for (MsgObject m : values())
			if (m.ordinal() == n)
				return m;
		throw new RuntimeException("Passed an invalid integer that doesn't relate to an ordinal in MsgObject");
	}
	
	@Override
	public String toString() {
		return description;
	}
}
