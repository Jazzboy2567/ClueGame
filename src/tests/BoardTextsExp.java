/*
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Description: This is our test java file, where we run our tests and see if our code
 * alligns with our tests. We run tests to check the adjacient tiles and the tiles in a 
 * 5x5 grid of what you can reach with your range. This also includes with tiles being
 * occupied by other players or a room spot you can enter.
 */

package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.*;

public class BoardTextsExp {
	TestBoard board;
	
	// This method creates a new board each time a test is run. The tests use a 5x5 board
	@BeforeEach
	public void setUp() {
		board = new TestBoard();
	}

	// Test 1, tests spots right next to 0,0, a corner.
	@Test
	public void testAdjacency1() {
		TestBoardCell cell = board.getCell(0,0);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1, 0)));
		Assert.assertTrue(testList.contains(board.getCell(0, 1)));
		Assert.assertEquals(2,  testList.size());
	}
	
	// Test 2, tests spots right next to 3,3 - another corner
	@Test
	public void testAdjacency2() {
		TestBoardCell cell = board.getCell(3,3);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(3, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertEquals(2,  testList.size());
	}
	
	// Test 3, Tests on an edge and gets 4 returned results
	@Test
	public void testAdjacency3() {
		TestBoardCell cell = board.getCell(1,3);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertTrue(testList.contains(board.getCell(0, 3)));
		Assert.assertTrue(testList.contains(board.getCell(1, 2)));
		Assert.assertEquals(3,  testList.size());
	}
	
	// Test 4, gets another corner test result to return
	@Test
	public void testAdjacency4() {
		TestBoardCell cell = board.getCell(3,0);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(2, 0)));
		Assert.assertTrue(testList.contains(board.getCell(3, 1)));
		Assert.assertEquals(2,  testList.size());
	}
	
	// Test 5, gets all 4 spots around the cell
	@Test
	public void testAdjacency5() {
		TestBoardCell cell = board.getCell(2,2);
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 1)));
		Assert.assertTrue(testList.contains(board.getCell(3, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertEquals(4,  testList.size());
	}
	
	// Target test 1, checks all spots in a range of 4
	@Test
	public void calcTarget1() {
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 4);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertEquals(6, targets.size());
	}
	
	// Target test 2, checks all spots from a max range of 6
	@Test
	public void calcTarget2MaxRange() {
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 6);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertEquals(7, targets.size());
	}
	
	// Target test 3, tests if the player can enter a room right next to them
	// and other spots in the vicinity
	@Test
	public void calcTarget3NearRoom() {
		board.getCell(1, 2).setRoom(true);
		TestBoardCell cell = board.getCell(2, 2);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(1, 2))); //This is the room test
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
		Assert.assertEquals(6, targets.size());
	}
	
	// Target test 4, checks if the player can enter a room with both sides blocked off
	@Test
	public void calcTarget4RoomWhileBlocked() {
		board.getCell(1, 0).setOccupied(true);
		board.getCell(0, 1).setOccupied(true);
		board.getCell(0, 0).setRoom(true);	//Room value, blocked by two people on either side
		TestBoardCell cell = board.getCell(1, 1);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 3)));
		Assert.assertEquals(6, targets.size());
	}
	
	// Target test 5, checks where the player can go if they have 3/4 sides blocked by players
	@Test
	public void calcTarget5AlmostSurrounded() {
		board.getCell(0, 2).setOccupied(true);
		board.getCell(1, 1).setOccupied(true);
		board.getCell(1, 3).setOccupied(true);
		TestBoardCell cell = board.getCell(1, 2);
		board.calcTargets(cell, 5);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(0, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));
		Assert.assertEquals(4, targets.size());
	}
}
