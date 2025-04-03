/*
 * @FileInitTest: This is our java tests that clue board with our design we came up with. 
 * It tests our room labels, dimensions, door directions, number or doorways,
 * hallways, and room information including the name location and player tile location.
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Date: 2/25/2023
 */

package tests;

/*
 * This program tests that config files are loaded properly.
 */

// Doing a static import allows me to write assertEquals rather than
// Assert.assertEquals
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

public class FileInitTest {
	// Constants that I will use to test whether the file was loaded correctly
//	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 24;
	public static final int NUM_COLUMNS = 24;

	// NOTE: I made Board static because I only want to set it up one
	// time (using @BeforeAll), no need to do setup before each test.
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	public void testRoomLabels() {
		// To ensure data is correctly loaded, test retrieving a few rooms
		// from the hash, including the first and last in the file and a few others
		assertEquals("Office", board.getRoom('O').getName() );
		assertEquals("Game Area", board.getRoom('G').getName() );
		assertEquals("Electronics Room", board.getRoom('E').getName() );
		assertEquals("Breaker", board.getRoom('B').getName() );
		assertEquals("Living Room", board.getRoom('L').getName() );
		assertEquals("Walkway", board.getRoom('W').getName());
		assertEquals("Unused", board.getRoom('X').getName());
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus
	// two cells that are not a doorway.
	// These cells are white on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		BoardCell cell = board.getCell(9, 4);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(5, 8);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(3, 18);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(16, 20);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(7, 7);
		assertFalse(cell.isDoorway());
	}
	

	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(21, numDoors);
	}

	// Test a few room cells to ensure the room initial is correct.
	@Test
	public void testRooms() {
		// just test a standard room location
		BoardCell cell = board.getCell(5, 12);
		Room room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Pantry" ) ;
		assertFalse( cell.isLabel() );
		assertFalse( cell.isRoomCenter() ) ;
		assertFalse( cell.isDoorway()) ;

		// this is a label cell to test
		cell = board.getCell(10, 11);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Game Area" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		
		// this is a room center cell to test
		cell = board.getCell(21, 18);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Master Bedroom" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );
		
		// this is a secret passage test
		cell = board.getCell(0, 22);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Breaker" ) ;
		assertTrue( cell.getSecretPassage() == 'E' );
		
		// test a walkway
		cell = board.getCell(7, 17);
		room = board.getRoom( cell ) ;
		// Note for our purposes, walkways and closets are rooms
		assertTrue( room != null );
		assertEquals( room.getName(), "Walkway" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		
		// test a closet
		cell = board.getCell(9, 14);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Unused" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		
	}

}
