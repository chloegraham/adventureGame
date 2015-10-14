package tests;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import server.gameWorld.GameLogic;
import server.gameWorld.GameWorld;
import server.gameWorld.Room;
import server.gameWorld.Stage;
import server.helpers.Direction;
import server.movable.Boulder;
import server.movable.Player;
import server.saveLoad.XML;
import server.tiles.Chest;
import client.helpers.Actions;

/**
 * Tests for the game world package and save/load.
 * @author Kirsty Thorburn 300316972
 *
 */
public class GameTests {
	private int userID = 101;
	// Layers are north to south, west to east
	private String map = "wew%eee%eee%eee%@" +		// Lowest Layer (e empty tiles and w walls)
				 "nnn%snz%nnc%nnd%@" +		// Middle layer (s spikes, z pressure plates)
			     "nnn%nnn%bnn%nnn%@" +		// Top layer (b boulders and c chests)
			     "<Room>"
			     + "<Stage>" + "<Split>" +
			     userID + "%0%0%1%0%0%0%0%@"	// "%0%0%x%y%0%facing%0%@"
			     + "<Player>" + "<Split>";
	private GameWorld world = new GameWorld(map);
	private GameLogic logic = world.getLogic();

	// ====================================================
	// Load/Save tests
	// ====================================================
	
	@Test public void testRoomEncoding(){
		String expected = "wee%wee%wee%@nnn%nnc%nnc%@nnn%nnn%nnn%@<Room>";
		Room room = new Room(expected, 0, 9);
		String actual = room.getEncodedRoom();
		System.out.println("Room Encoding\nExpect: " + expected);
		System.out.println("Actual: " + actual);
		assertTrue(actual.equals(expected));
	}
	
	@Test public void testStageEncoding(){
		String expected = "wee%wee%wee%@nnn%nnc%nnc%@nnn%nnn%nnn%@<Room><Stage>";
		Stage stage = new Stage(expected, 0);
		String actual = stage.getEncodedStage();
		System.out.println("Stage Encoding\nExpect: " + expected);
		System.out.println("Actual: " + actual);
		assertTrue(actual.equals(expected + "<Split>"));
	}
	
	@Test public void testPlayerEncoding(){
		String expected = "987%9%9%9%9%3%9%1%<Player><Split>";
		Player player = new Player(expected);
		String actual = player.getEncodedPlayer();
		System.out.println("Player Encoding\nExpect: " + expected);
		System.out.println("Actual: " + actual);
		assertTrue(expected.equals(actual));
	}
	
	@Test public void testSaveLoadGame(){
		// Move off start square and save
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		String saved = world.getEncodedGameSave();
		XML.save(saved);
		logic.handleAction(Actions.NORTH.ordinal(), userID);
		// load game and compare with saved
		String loaded = XML.load();
		GameWorld loadedWorld = new GameWorld(loaded);
		System.out.println("Load/Save\nSave: " + saved);
		System.out.println("Load: " + loaded);
		assertTrue(loadedWorld.getEncodedGameSave().equals(saved));
	}
	
	// ====================================================
	// World tests
	// ====================================================
	
	@Test public void testPlayerMoveOutOfBounds(){
		logic.handleAction(Actions.NORTH.ordinal(), userID);
		String actual = world.getEncodedGameSave();
		String expected = "wew%eee%eee%eee%@" +	// Board unchanged
				 "nnn%snz%nnc%nnd%@" +
			     "nin%nnn%bnn%nnn%@" +			// player's new position/direction on board.
			     "<Room>"
			     + "<Stage>" + "<Split>" +
			     userID + "%0%0%1%0%0%0%0%"	// player has not moved
			     + "<Player>" + "<Split>";
		System.out.println("Moving Out Of Bounds\nActual: " + actual);
		System.out.println("Expect: " + expected);
		assertTrue(actual.equals(expected));
	}
	
	@Test public void testPlayerMoveOnWall(){
		logic.handleAction(Actions.WEST.ordinal(), userID);
		String actual = world.getEncodedGameSave();
		String expected = "wew%eee%eee%eee%@" +	// Board unchanged
				 "nnn%snz%nnc%nnd%@" +
			     "njn%nnn%bnn%nnn%@" +			// player's position/direction on board.
			     "<Room>"
			     + "<Stage>" + "<Split>" +
			     userID + "%0%0%1%0%3%0%0%"	// player has not moved
			     + "<Player>" + "<Split>";
		System.out.println("Moving On Wall\nActual: " + actual);
		System.out.println("Expect: " + expected);
		assertTrue(actual.equals(expected));
	}
	
	@Test public void testPlayerMoveOnBoulder(){
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.handleAction(Actions.WEST.ordinal(), userID);
		String actual = world.getEncodedGameSave();
		String expected = "wew%eee%eee%eee%@" +	// Board unchanged
				 "nnn%snz%nnc%nnd%@" +
			     "nnn%nnn%bjn%nnn%@" +			// player's position/direction on board.
			     "<Room>"
			     + "<Stage>" + "<Split>" +
			     userID + "%0%0%1%2%3%0%0%"	// player has not moved
			     + "<Player>" + "<Split>";
		System.out.println("Moving On Boulder\nActual: " + actual);
		System.out.println("Expect: " + expected);
		assertTrue(actual.equals(expected));
	}
	
	@Test public void testPlayerMoveOnChest(){
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.handleAction(Actions.EAST.ordinal(), userID);
		String actual = world.getEncodedGameSave();
		String expected = "wew%eee%eee%eee%@" +	// Board unchanged
				 "nnn%snz%nnc%nnd%@" +
			     "nnn%nnn%bln%nnn%@" +			// player's position/direction on board.
			     "<Room>"
			     + "<Stage>" + "<Split>" +
			     userID + "%0%0%1%2%2%0%0%"	// player has not moved
			     + "<Player>" + "<Split>";
		System.out.println("\nActual: " + actual);
		System.out.println("Expect: " + expected);
		assertTrue(actual.equals(expected));
		System.out.println("Moving On Chest\nActual: " + actual);
		System.out.println("Expect: " + expected);
	}
	
	@Test public void testPlayerMoveOnPressurePlate(){
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.handleAction(Actions.EAST.ordinal(), userID);
		String actual = world.getEncodedGameSave();
		String expected = "wew%eee%eee%eee%@" +	// Board unchanged
				 "nnn%snZ%nnc%nnd%@" +			// Pressure plate should be activated
			     "nnn%nnl%bnn%nnn%@" +			// player's position/direction on board.
			     "<Room>"
			     + "<Stage>" + "<Split>" +
			     userID + "%0%0%2%1%2%0%0%"	// player has not moved
			     + "<Player>" + "<Split>";
		System.out.println("Moving On Pressure Plates\nActual: " + actual);
		System.out.println("Expect: " + expected);
		assertTrue(actual.equals(expected));
	}
	
	@Test public void testPlayerMoveOnDeactivatedSpikes(){
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.handleAction(Actions.WEST.ordinal(), userID);
		String actual = world.getEncodedGameSave();
		String expected = "wew%eee%eee%eee%@" +	// Board unchanged
				 "nnn%snz%nnc%nnd%@" +
			     "nnn%jnn%bnn%nnn%@" +			// player's position/direction on board.
			     "<Room>"
			     + "<Stage>" + "<Split>" +
			     userID + "%0%0%0%1%3%0%0%"	// player has not moved
			     + "<Player>" + "<Split>";
		System.out.println("Moving On Deactivated Spikes\nActual: " + actual);
		System.out.println("Expect: " + expected);
		assertTrue(actual.equals(expected));
	}
	
	@Test public void testPlayerMoveOnActivatedSpikes(){
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.activateSpikes();
		logic.handleAction(Actions.WEST.ordinal(), userID);
		String actual = world.getEncodedGameSave();
		String expected = "wew%eee%eee%eee%@" +	// Board unchanged
				 "nnn%Snz%nnc%nnd%@" +
			     "nnn%njn%bnn%nnn%@" +			// player's position/direction on board.
			     "<Room>"
			     + "<Stage>" + "<Split>" +
			     userID + "%0%0%1%1%3%0%0%"	// player has not moved
			     + "<Player>" + "<Split>";
		System.out.println("Moving On Activated Spikes\nActual: " + actual);
		System.out.println("Expect: " + expected);
		assertTrue(actual.equals(expected));
	}
	
	@Test public void testPlayerStandOnActivatedSpikes(){
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.handleAction(Actions.WEST.ordinal(), userID);
		logic.activateSpikes();
		String actual = world.getEncodedGameSave();
		String expected = "wew%eee%eee%eee%@" +	// Board unchanged
				 "nnn%Snz%nnc%nnd%@" +
			     "nnn%jnn%bnn%nnn%@" +			// player's position/direction on board.
			     "<Room>"
			     + "<Stage>" + "<Split>" +
			     userID + "%0%0%0%1%3%0%0%"	// player has not moved
			     + "<Player>" + "<Split>";
		System.out.println("Standing On Activated Spikes\nActual: " + actual);
		System.out.println("Expect: " + expected);
		assertTrue(actual.equals(expected));
	}
	
	@Test public void testPlayerWalkOnDoor(){
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.handleAction(Actions.SOUTH.ordinal(), userID);
		logic.handleAction(Actions.EAST.ordinal(), userID);
		logic.activateSpikes();
		String actual = world.getEncodedGameSave();
		String expected = "wew%eee%eee%eee%@" +	// Board unchanged
				 "nnn%Snz%nnc%nnd%@" +
			     "nnn%nnn%bnn%nln%@" +			// player's position/direction on board.
			     "<Room>"
			     + "<Stage>" + "<Split>" +
			     userID + "%0%0%1%3%2%0%0%"	// player has not moved
			     + "<Player>" + "<Split>";
		System.out.println("Standing On Activated Spikes\nActual: " + actual);
		System.out.println("Expect: " + expected);
		assertTrue(actual.equals(expected));
	}

	// ====================================================
	// Player Tests
	// ====================================================
	
	Player player = new Player(userID + "%0%0%1%0%0%0%0%@");
	
	@Test public void testUseKeySuccess(){
		player.addKey();
		assertTrue(player.useKey());
	}
	
	@Test public void testUseKeyFail(){
		player.addKey();
		player.useKey();		// Added a key and used it.
		assertFalse(player.useKey());
	}
	
	@Test public void testDropBoulderSuccess(){
		player.addBoulder();
		assertTrue(player.dropBoulder());
	}
	
	@Test public void testDropBoulderFail(){
		player.addBoulder();
		player.dropBoulder();	// Added a boulder and used it.
		assertFalse(player.dropBoulder());
	}
	
	@Test public void testToString(){
		player.setDirection(Direction.EAST);
		player.addBoulder();
		assertTrue(player.toString().equals("L"));
	}
	
	// ====================================================
	// Testing tile functions
	// ====================================================

	@Test public void testTakeKeySuccess(){
		Chest chest = new Chest(false);
		assertTrue(chest.takeKey());
	}
	
	@Test public void testTakeKeyFail(){
		Chest chest = new Chest(false);
		chest.takeKey();
		assertFalse(chest.takeKey());
	}
	
	@Test public void testBoulderEquals(){
		Boulder boulderA = new Boulder(new Point(0, 0));
		Boulder boulderB = new Boulder(new Point(0, 1));
		assertFalse(boulderA.equals(boulderB));
	}
	
	
}