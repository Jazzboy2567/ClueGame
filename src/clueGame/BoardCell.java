/*
 * BoardCell: This class works with individual locations and checks if they are 
 * occupied or a room, and it can set them as occupied or as a room if needed. 
 * It also has a function to get adjacent cells from a location.
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Date: 2/28/2023
 */

package clueGame;

import java.util.*;

public class BoardCell {
	private int row, col; // not currently being used but keeping since we may need later
	//checks if the cell is a type of something
	private boolean isRoom, isOccupied, roomLabel, roomCenter, roomDoorway;
	// set which contains characters that aren't rooms
	public static Set<Character> spaceChars = new HashSet<Character>();
	private Set<BoardCell> adjList; // adjacency list of cell
	// Initial: character which represents type of cell. SecretPassage: where cell's secret passage goes to
	private char initial, secretPassage; 
	private DoorDirection doorDirection; // whether cell is a doorway and what direction
	
	/**
	 * Default constructor: sets row and column based on input, sets all other variables based on input string
	 * @param row: row number of cell location
	 * @param col: column number of cell location
	 * @param inputString: string of cell from layout file, first character represents type of cell and second
	 * represents other properties which are evaluated within the constructor
	 */
	public BoardCell(int row, int col, String inputString) {
		super();
		this.row = row;
		this.col = col;
		// we don't need to calculate the adjacency list yet
		this.adjList = new HashSet<BoardCell>();
		// bools are false by default
		this.isRoom = false;
		this.isOccupied = false;
		this.roomLabel = false;
		this.roomCenter = false;
		this.roomDoorway = false;
		this.doorDirection = DoorDirection.NONE;
		// default character for secret passage is going to be a space
		this.secretPassage = ' ';
		
		// first character of input string is initial
		this.initial = inputString.charAt(0);
		// set to room if not walkway or unused
		if (!(spaceChars.contains(initial))) {
			this.isRoom = true;
		}

		// second character of input string determines special properties
		if (inputString.length() > 1) {
			char secondChar = inputString.charAt(1);
			switch (secondChar) {
				case '#':
					roomLabel = true;
					break;
				case '*':
					roomCenter = true;
					break;
				case '<':
					roomDoorway = true;
					doorDirection = DoorDirection.LEFT;
					break;
				case '>':
					roomDoorway = true;
					doorDirection = DoorDirection.RIGHT;
					break;
				case '^':
					roomDoorway = true;
					doorDirection = DoorDirection.UP;
					break;
				case 'v':
					roomDoorway = true;
					doorDirection = DoorDirection.DOWN;
					break;
				default:
					secretPassage = secondChar;
					break;
			}
		}
	}
	
	/**
	 * getAdjList: find out what is next to a location
	 * @return returns set of BoardCell adjList
	 */
	public Set<BoardCell> getAdjList() {
		return adjList;
	}
	
	/**
	 * isRoom: checks if a location is a part of a room
	 * @return returns bool value isRoom for current cell
	 */
	public boolean isRoom() {
		return isRoom;
	}

	/**
	 * setRoom: sets a location as a room or not based on our design map
	 * @param room is to be set as isRoom variable
	 */
	public void setRoom(boolean room) {
		isRoom = room;
	}
	
	/**
	 * isOccupied: checks to see if a location has a player there
	 * @return returns isOccupied Variable
	 */
	public boolean isOccupied() {
		return isOccupied;
	}
	
	/**
	 * setOccupied: sets a location as occupied or not based upon if another player is at that location
	 * @param occupied is to be set as isOccupied variable
	 */
	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}	
	
	/**
	 * addAdjacency: adds adjacent cells to list
	 * @param cell to access another function
	 */
	public void addAdjacency (BoardCell cell) {
		adjList.add(cell);
	}
	
	/**
	 * isDoorway: getter to check if a cell has a doorway to a room
	 * @return roomDoorway boolean value of cell
	 */
	public boolean isDoorway() {
		return roomDoorway;
	}
	
	/**
	 * getDoorDirection: getter for doorDirection
	 * @return returns doorDirection value
	 */
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	/**
	 * isLabel: getter for roomLabel
	 * @return returns roomLabel value
	 */
	public boolean isLabel() {
		return roomLabel;
	}
	
	/**
	 * isRoomCenter: getter for roomCenter
	 * @return returns roomCenter value
	 */
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	/**
	 * getSecretPassage: getter for secretPassage
	 * @return returns secretPassage value
	 */
	public char getSecretPassage() {
		return secretPassage;
	}
	
	/**
	 * getInitial: getter for initial
	 * @return returns initial value
	 */
	public char getInitial() {
		return initial;
	}
}
