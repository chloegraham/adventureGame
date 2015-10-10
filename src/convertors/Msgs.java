package convertors;

import gameWorld.Direction;

public class Msgs {
	private String decodedMessages;
	
	public String getDecoded() { return decodedMessages; }
	
	public void decode(String encodedMessages) {
		decodedMessages = "";
		String[] lines = encodedMessages.split("%");
		
		String line1 = lines[0];
		decodedMessages += line1 + "\n";
		String line2;
		String line3;
		String line4;
		
		if (lines.length > 1) {
			line2 = lines[1];
			decodedMessages += line2 + "\n";
		}
		
		if (lines.length > 2) {
			line3 = lines[2];
			decodedMessages += line3 + "\n";
		}
		
		if (lines.length > 3) {
			line4 = lines[3];
			decodedMessages += line4 + "\n";
		}
	}
	
	
	public static final int PLAYER_ONE = 101;
	public static final int PLAYER_TWO = 202;
	
	public static final String DELIM_SPLIT = "<Split>";
	public static final String DELIM_MSG = "<Msg>";
	public static final String DELIM_PLAYER = "<Player>";
	public static final String DELIM_LEVEL = "<Level>";
	public static final String DELIM_HOST = "<Host>";
	public static final String DELIM_HOSTLOAD = "<HostLoad>";
	public static final String DELIM_GUEST = "<Guest>";
	public static final String DELIM_ACTION = "<Action>";
	public static final String DELIM_DETAILS = "<Details>";
	
	public static final String PICK = "You picked up a ";
	public static final String NOPICK = "You can't pick up a ";
	public static final String DOWN = "You put down a ";
	public static final String BOULDER = "Boulder";
	public static final String FACE = "You are facing ";
	public static final String MOVE = "You moved ";
	public static final String NOMOVE = "You couldn't move ";
	
	public static final String DOOR_CLOSED = "The Door is Locked & Closed ";
	public static final String DOOR_OPEN = "The Door is Unlocked & Open. You may pass.";
	public static final String DOOR_USED_KEY = "You have a Key & you used it to Unlock the Door ";
	public static final String DOOR_NO_KEY = "You don't have a Key to Unlock the Door ";
	
	public static final String CHEST_OPEN = "The Chest is already Open ";
	public static final String CHEST_CLOSED = "You Opened the Chest ";
	
	public static final String CHEST_KEY_INSDE = "You collected a Key from inside the Chest.";
	public static final String CHEST_KEY_NOT_INSDE = "There is no Key inside the Chest.";
	
	
	
	public static String moveMsg(Direction dir, boolean moved) {
		String str = "";
		str += Msgs.FACE + dir.toString() + "%";
		
		if (moved) str += Msgs.MOVE + dir.toString() + "%";
		else       str += Msgs.NOMOVE + dir.toString() + "%";
		
		str += "Your location is: " + "!!!!! ALWAYS THE SAME UNTIL WE WRITE CODE !!!!" + "%";
		str += Msgs.DELIM_SPLIT;
		return str;
	}
	
	public static String boulderMsg(boolean pickup, boolean carrying) {
		String str = "";
		if (pickup) {
			
			if (carrying) str += Msgs.PICK + Msgs.BOULDER + "%";
			else		  str += Msgs.NOPICK + Msgs.BOULDER + "becuase you're already carrying one." + "%";
		
		} else {    
			str += Msgs.DOWN + Msgs.BOULDER + "%";
		}
		str += Msgs.DELIM_SPLIT;
		return str;
	}
	
	public static String openDoorMsg(boolean initiallyOpen, boolean finallyOpen, int keyAmount) {
		String str = "";
		if (initiallyOpen) {
			str += Msgs.DOOR_OPEN + "%";
			str += Msgs.DELIM_SPLIT;
			return str;
		}
			
		
		str += Msgs.DOOR_CLOSED + "%";	
		if (finallyOpen) {
			str += Msgs.DOOR_USED_KEY + Msgs.DOOR_OPEN + "%";
			str += Msgs.DELIM_SPLIT;
			
			str += keyAmount;
			str += Msgs.DELIM_SPLIT;
			return str;
		}
		
		str += Msgs.DOOR_NO_KEY + Msgs.DOOR_CLOSED + "%";
		str += Msgs.DELIM_SPLIT;
		return str;
	}
	
	public static String openChestMsg(boolean alreadyOpen, boolean isKeyInside, int keyAmount) {
		String str = "";
		if (alreadyOpen) str += Msgs.CHEST_OPEN + "%";
		else 			 str += Msgs.CHEST_CLOSED + "%";
		
		if (isKeyInside) {
			str += Msgs.CHEST_KEY_INSDE;
			str += Msgs.DELIM_SPLIT;
			
			str += keyAmount;
			str += Msgs.DELIM_SPLIT;
			return str;
		} else {
			str += Msgs.CHEST_KEY_NOT_INSDE;
			return str;
		}
	}
	
	public static String inspectMsg() {
		String str = "";
		str += "!!!__INSpesct Logic Not WriTTen Yetgfd!!_" + "%";
		str += Msgs.DELIM_SPLIT;
		return str;
	}
}
