/*
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Description: This class works with individual locations and checks if they are 
 * occupied or a room, and it can set them as occupied or as a room if needed. 
 * It also has a function to get adjacent cells from a location.
 */

package experiment;

import java.util.*;

public class TestBoardCell {
	private int row, col;
	private Boolean isRoom, isOccupied;
	Set<TestBoardCell> adjList;
	
	// Constructor for TestBoardCell
	// methods empty for testing purposes - tests should fail
	public TestBoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
		this.isRoom = false;
		this.isOccupied = false;
		this.adjList = new HashSet<TestBoardCell>();
	}
	
	// Function to find out what is next to a location
	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}
	
	// function that checks if a location is a part of a room
	public boolean isRoom() {
		return isRoom;
	}
	
	// function that sets a location as a room or not based on our design map
	public void setRoom(boolean room) {
		isRoom = room;
	}
	
	// function that checks to see if a location has a player there
	public boolean isOccupied() {
		return isOccupied;
	}
	
	// function that sets a location as occupied or not based upon if another player is at that location
	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}	
	
	// function that adds adjacent cells (I can not tell what this does, update comment in future)
	public void addAdjacency (TestBoardCell cell) {
		adjList.add(cell);
	}
}
