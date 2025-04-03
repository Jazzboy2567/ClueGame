/*
 * @BoardAdjTargetTest: This is a JUnit test class to test adjacency and target
 * calculation
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Date: 3/6/2023
 */

package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are DARK RED on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different rooms.
		// First, the family room that only has a single door but a secret room
		// Find a room with a * as that is the room center
		Set<BoardCell> testList = board.getAdjList(3, 3);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(6, 7)));
		assertTrue(testList.contains(board.getCell(21, 18))); // changed, goes to center
		
		// now test the Office
		testList = board.getAdjList(21, 2);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(20, 5)));
		assertTrue(testList.contains(board.getCell(17, 6)));
		
		// one more room, the Living Room
		testList = board.getAdjList(10, 21);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(14, 21)));
		assertTrue(testList.contains(board.getCell(6, 21)));
		assertTrue(testList.contains(board.getCell(21, 10)));
	}

	
	// Ensure door locations include their rooms and also additional walkways
	// These cells are DARK RED on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		// shifted over one
		Set<BoardCell> testList = board.getAdjList(18, 9);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(18, 8)));
		assertTrue(testList.contains(board.getCell(18, 10)));
		assertTrue(testList.contains(board.getCell(17, 9)));
		assertTrue(testList.contains(board.getCell(21, 10)));

		testList = board.getAdjList(12, 16);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(12, 11)));
		assertTrue(testList.contains(board.getCell(12, 17)));
		assertTrue(testList.contains(board.getCell(13, 16)));
		assertTrue(testList.contains(board.getCell(11, 16)));
		
		testList = board.getAdjList(6, 7);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(3, 3)));
		assertTrue(testList.contains(board.getCell(5, 7)));
		assertTrue(testList.contains(board.getCell(7, 7)));
		assertTrue(testList.contains(board.getCell(6, 8)));
	}
	
	// Test a variety of walkway scenarios
	// These tests are YELLOW on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on bottom edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(23, 13);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(22, 13)));
		
		// Test near a door but not adjacent
		testList = board.getAdjList(6, 15);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(6, 14)));
		assertTrue(testList.contains(board.getCell(6, 16)));
		assertTrue(testList.contains(board.getCell(7, 15)));

		// Test adjacent to walkways
		testList = board.getAdjList(17, 10);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(17, 11)));
		assertTrue(testList.contains(board.getCell(18, 10)));
		assertTrue(testList.contains(board.getCell(17, 9)));

		// Test next to unused
		testList = board.getAdjList(7,1);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(7, 0)));
		assertTrue(testList.contains(board.getCell(7, 2)));
	}
	
	
	// Tests out of room center, 1, 3 and 4
	// These are PINK on the planning spreadsheet
	@Test
	public void testTargetsInGameArea() {
		// test a roll of 1
		board.calcTargets(board.getCell(12, 11), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(12, 6)));
		assertTrue(targets.contains(board.getCell(12, 16)));	
		assertTrue(targets.contains(board.getCell(7, 11)));	
		assertTrue(targets.contains(board.getCell(17, 11)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(12, 11), 3);
		targets= board.getTargets();
		assertEquals(17, targets.size());
		assertTrue(targets.contains(board.getCell(12, 4)));
		assertTrue(targets.contains(board.getCell(13, 17)));	
		assertTrue(targets.contains(board.getCell(18, 12)));
		assertTrue(targets.contains(board.getCell(7, 9)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(12, 11), 4);
		targets= board.getTargets();
		assertEquals(39, targets.size());
		assertTrue(targets.contains(board.getCell(13, 6)));
		assertTrue(targets.contains(board.getCell(7, 8)));	
		assertTrue(targets.contains(board.getCell(21, 10)));
		assertTrue(targets.contains(board.getCell(14, 7)));	
	}
	
	@Test
	public void testTargetsInPantry() {
		// test a roll of 1
		board.calcTargets(board.getCell(4, 11), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(5, 8)));
		assertTrue(targets.contains(board.getCell(5, 16)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(4, 11), 3);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(4, 7)));
		assertTrue(targets.contains(board.getCell(5, 18)));	
		assertTrue(targets.contains(board.getCell(6, 17)));
		assertTrue(targets.contains(board.getCell(6, 9)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(4, 11), 4);
		targets= board.getTargets();
		assertEquals(19, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(8, 8)));	
		assertTrue(targets.contains(board.getCell(8, 16)));
		assertTrue(targets.contains(board.getCell(4, 18)));	
	}

	// Tests out of room center, 1, 3 and 4
	// These are PINK on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(16, 2), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(12, 2)));
		assertTrue(targets.contains(board.getCell(16, 3)));	
		assertTrue(targets.contains(board.getCell(17, 2)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(16, 2), 3);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(12, 2)));
		assertTrue(targets.contains(board.getCell(17, 0)));
		assertTrue(targets.contains(board.getCell(18, 1)));	
		assertTrue(targets.contains(board.getCell(14, 3)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(16, 2), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(12, 2)));
		assertTrue(targets.contains(board.getCell(15, 3)));
		assertTrue(targets.contains(board.getCell(16, 4)));	
		assertTrue(targets.contains(board.getCell(16, 6)));
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(0, 7), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(0, 6)));
		assertTrue(targets.contains(board.getCell(1, 7)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(0, 7), 3);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 7)));
		assertTrue(targets.contains(board.getCell(1, 7)));
		assertTrue(targets.contains(board.getCell(0, 6)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(0, 7), 4);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(4, 7)));
		assertTrue(targets.contains(board.getCell(2, 7)));
		assertTrue(targets.contains(board.getCell(3, 6)));	
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(9, 16), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(9, 15)));
		assertTrue(targets.contains(board.getCell(8, 16)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(9, 16), 3);
		targets= board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(12, 16)));
		assertTrue(targets.contains(board.getCell(9, 15)));
		assertTrue(targets.contains(board.getCell(8, 14)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(9, 16), 4);
		targets= board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCell(12, 11)));
		assertTrue(targets.contains(board.getCell(13, 16)));
		assertTrue(targets.contains(board.getCell(6, 17)));	
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(7, 10).setOccupied(true);
		board.getCell(6, 11).setOccupied(true);
		board.calcTargets(board.getCell(12, 11), 4);
		board.getCell(7, 10).setOccupied(false);
		board.getCell(6, 11).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(33, targets.size());
		assertTrue(targets.contains(board.getCell(13, 6)));
		assertFalse(targets.contains(board.getCell(7, 8)));	
		assertTrue(targets.contains(board.getCell(21, 10)));
		assertTrue(targets.contains(board.getCell(6, 13)));	
	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(4, 11).setOccupied(true);
		board.getCell(6, 16).setOccupied(true);
		board.getCell(5, 17).setOccupied(true);
		board.calcTargets(board.getCell(5, 16), 1);
		board.getCell(4, 11).setOccupied(false);
		board.getCell(6, 16).setOccupied(false);
		board.getCell(5, 17).setOccupied(false);
		targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(4, 11)));	
		assertFalse(targets.contains(board.getCell(6, 16)));	
		
		// check leaving a room with a blocked doorway
		board.getCell(5, 8).setOccupied(true);
		board.calcTargets(board.getCell(4, 11), 3);
		board.getCell(5, 8).setOccupied(false);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(6, 15)));
		assertTrue(targets.contains(board.getCell(7, 16)));
		assertTrue(targets.contains(board.getCell(4, 17)));
	}
}
