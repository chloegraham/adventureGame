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
	
	public static Direction rotateClockwise(Direction inputDir){
		Direction outputDir = Direction.NORTH;
		
		if(inputDir == Direction.NORTH){ outputDir= Direction.EAST; }
		if(inputDir == Direction.EAST){ outputDir= Direction.SOUTH; }
		if(inputDir == Direction.SOUTH){ outputDir= Direction.WEST; }
		if(inputDir == Direction.WEST){ outputDir= Direction.NORTH; }
		
		return outputDir;
	}
	
	public static Direction rotateCounterClockwise(Direction inputDir){
		Direction outputDir = Direction.NORTH;
		
		if(inputDir == Direction.NORTH){ outputDir= Direction.WEST; }
		if(inputDir == Direction.WEST){ outputDir= Direction.SOUTH; }
		if(inputDir == Direction.SOUTH){ outputDir= Direction.EAST; }
		if(inputDir == Direction.EAST){ outputDir= Direction.NORTH; }
		
		return outputDir;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
