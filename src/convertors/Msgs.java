package convertors;

import gameWorld.Direction;
import movable.Boulder;
import movable.Moveable;
import movable.Player;
import tiles.Furniture;

public class Msgs {
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
	public static final String BOULDER = "boulder";
	public static final String FACE = "You are facing ";
	public static final String MOVE = "You moved ";
	public static final String NOMOVE = "You couldn't move ";
	
	public static final String DOOR_CLOSED = "The door is locked & closed ";
	public static final String DOOR_OPEN = "The door is unlocked & open. You may enter.";
	public static final String DOOR_USED_KEY = "You have a key & you used it to unlock the door ";
	public static final String DOOR_NO_KEY = "You don't have a key. You can't unlock the door. You can't enter.";
	
	public static final String CHEST_OPEN = "Chest already open ";
	public static final String CHEST_CLOSED = "You opened the chest ";
	public static final String CHEST_OPENED = "Chest is closed and you can't open it and you can't look inside.";
	public static final String CHEST_KEY = "There is a key inside & you have picked it up.";
	public static final String CHEST_NO_KEY = "There is no key inside this chest. It may have already been taken.";
	
	private static final String INSPECT_OPEN_CHEST = "This chest is open, you already have its contents you greedy bastard";
	private static final String INSPECT_CLOSED_CHEST = "This hairy chest is locked.";
	private static final String INSPECT_CLOSED_DOOR = "Work out how to open this door";
	private static final String INSPECT_OPEN_DOOR = "Where does this lead to..?";
	private static final String INSPECT_PRESSURE_PAD = "Hint: This pressure pad may open a door..";
	private static final String INSPECT_SPIKES = "This looks dangerous!";
	private static final String INSPECT_WALL = "This is a wall. Good job good looking";
	private static final String INSPECT_BOUDLER = "This is a boulder, you may find some use for it";
	private static final String INSPECT_BOUDLER_ON_PAD = "This boulder is currently activating a pressure pad";
	private static final String INSPECT_PLAYER = "HI FRIEND";
	private static final String INSPECT_PLAYER_WITH_BOULDER = "HI FRIEND WITH BOULDER";
	private static final String INSPECT_PLAYER_ON_PAD = "HI FRIEND ON PRESSURE PAD";
	
	
	
	public static String moveMsg(Direction dir, boolean moved) {
		String str = "";
		str += Msgs.FACE + dir.toString() + "%";
		
		if (moved) str += Msgs.MOVE + dir.toString() + "%";
		else       str += Msgs.NOMOVE + dir.toString() + "%";
		return str;
	}
	
	
	
	public static String boulderPickUpMsg() {
		return Msgs.PICK + Msgs.BOULDER + Msgs.DELIM_SPLIT;
	}
	
	public static String boulderPutDownMsg(boolean infrontBoulder, boolean emptyORpressure) {
		String str = "";
		
		if(infrontBoulder) {
			str += "You already have a boulder, so can't pick up another." + "%";
		}else if(emptyORpressure) {
			str += "You dropped your boulder." + "%";
		} 
			
		return str += Msgs.DELIM_SPLIT;
	}
	
	
	
	public static String doorMsg(boolean isLocked, boolean hasKey) {
		String str = "";
		
		// ISLOCKED
		if (isLocked) str += Msgs.DOOR_CLOSED + "%";							// true - "The Door is locked"
		else	      return str += Msgs.DOOR_OPEN + "%" + Msgs.DELIM_SPLIT;	// false - "The Door is unlocked. You may enter." RETURN
		
		// HASKEY
		if (hasKey) str += Msgs.DOOR_USED_KEY + "%" + Msgs.DOOR_OPEN + "%";		// true - "You used a Key" + "The Door is unlocked. You may enter."
		else	    str += Msgs.DOOR_NO_KEY + "%";								// false - "You don't have a Key. You can't unlock the door. You can't enter."
		
		return str += Msgs.DELIM_SPLIT;
	}
	
	
	
	public static String chestMsg(boolean open, boolean opened, boolean isKey) {
		String str = "";
		
		// OPEN
		if (open) str += Msgs.CHEST_OPEN + "%";			// true - "Chest already open"
		else 	  str += Msgs.CHEST_CLOSED + "%";		// false - "You opened the Chest"
		
		
		// OPENED
		if (opened) str += "";							// true - no comment needed
		else		return str += Msgs.CHEST_OPENED + "%" + Msgs.DELIM_SPLIT;	// false - chest is closed and you can't open it and you can't look inside
					
					
		// ISKEY		
		if (isKey) str += Msgs.CHEST_KEY + "%";			// true - "there is a key inside & player has picked it up"
		else 	   str += Msgs.CHEST_NO_KEY + "%";		// false - "there is no key inside this chest"
		
		return str += Msgs.DELIM_SPLIT;
	}
	
	
	
	public static String inspectUnmovable(Furniture item) {
		
		String str = "";
//		
//		if(item instanceof Chest){
//			
//			if(((Chest) item).isOpen()){
//				return str += Msgs.INSPECT_OPEN_CHEST;
//			}
//			else {
//				return str += Msgs.INSPECT_CLOSED_CHEST;
//			}
//		} else if(item instanceof Door){
//			
//			if(((Door) item).isLocked()){
//				return str += Msgs.INSPECT_CLOSED_DOOR;
//			}
//			else {
//				return str += Msgs.INSPECT_OPEN_DOOR;
//			}
//		}else if(item instanceof Spikes){
//			return str += Msgs.INSPECT_SPIKES;
//		}else if(item instanceof Wall){
//			return str += Msgs.INSPECT_WALL;
//		}else{
//			return "this isn't an unmoveable object??";
//		}
		return "BEN HAS HACKED THIS TOO";
	}

	public static String inspectMoveable(Moveable moveable, boolean onPressurePad) {
		
		String str = "";
		//if inspecting a boulder on a pressure pad or otherwise
		if(moveable instanceof Boulder){
			if(onPressurePad){
				return str += Msgs.INSPECT_BOUDLER_ON_PAD;
			} else{
				return str += Msgs.INSPECT_BOUDLER;
			}
		//if inspecting player 2 on a pressure pad, holding a boulder, or otherwise
		} else if(moveable instanceof Player){
			if(onPressurePad){
				return str += Msgs.INSPECT_PLAYER_ON_PAD;
			}
			else if(((Player) moveable).hasBoulder()){
				return str += Msgs.INSPECT_PLAYER_WITH_BOULDER;
			} 
			return str += Msgs.INSPECT_PLAYER;
		}
		//TODO: roaming object
		return str;
	}

	public static String inspectPressurePad() {
		return Msgs.INSPECT_PRESSURE_PAD;
	}
}
