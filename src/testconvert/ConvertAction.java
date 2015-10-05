package testconvert;

import gameWorld.Direction;

public class ConvertAction {
	
	public static String moveMsg(Direction dir, boolean moved) {
		String str = "";
		str += Messages.FACE + dir.toString() + "%";
		
		if (moved) str += Messages.MOVE + dir.toString() + "%";
		else       str += Messages.NOMOVE + dir.toString() + "%";
		
		str += "Your location is: " + "!!!!! ALWAYS THE SAME UNTIL WE WRITE CODE !!!!" + "%";
		str += Messages.DELIM_SPLIT;
		return str;
	}
	
	public static String boulderMsg(boolean pickup, boolean carrying) {
		String str = "";
		if (pickup) {
			
			if (carrying) str += Messages.PICK + Messages.BOULDER + "%";
			else		  str += Messages.NOPICK + Messages.BOULDER + "becuase you're already carrying one." + "%";
		
		} else {    
			str += Messages.DOWN + Messages.BOULDER + "%";
		}
		str += Messages.DELIM_SPLIT;
		return str;
	}
	
	public static String openDoorMsg(boolean initiallyOpen, boolean finallyOpen, int keyAmount) {
		String str = "";
		if (initiallyOpen) {
			str += Messages.DOOR_OPEN + "%";
			str += Messages.DELIM_SPLIT;
			return str;
		}
			
		
		str += Messages.DOOR_CLOSED + "%";	
		if (finallyOpen) {
			str += Messages.DOOR_USED_KEY + Messages.DOOR_OPEN + "%";
			str += Messages.DELIM_SPLIT;
			
			str += keyAmount;
			str += Messages.DELIM_SPLIT;
			return str;
		}
		
		str += Messages.DOOR_NO_KEY + Messages.DOOR_CLOSED + "%";
		str += Messages.DELIM_SPLIT;
		return str;
	}
	
	public static String openChestMsg(boolean alreadyOpen, boolean isKeyInside, int keyAmount) {
		String str = "";
		if (alreadyOpen) str += Messages.CHEST_OPEN + "%";
		else 			 str += Messages.CHEST_CLOSED + "%";
		
		if (isKeyInside) {
			str += Messages.CHEST_KEY_INSDE;
			str += Messages.DELIM_SPLIT;
			
			str += keyAmount;
			str += Messages.DELIM_SPLIT;
			return str;
		} else {
			str += Messages.CHEST_KEY_NOT_INSDE;
			return str;
		}
	}
	
	public static String inspectMsg() {
		String str = "";
		str += "!!!__INSpesct Logic Not WriTTen Yetgfd!!_" + "%";
		str += Messages.DELIM_SPLIT;
		return str;
	}
}
