/**
 * @Board: This is a java class that calculates target locations,
 * gets target locations and gets individual cells to find out distance calculations. 
 * It also has access to each room, and takes care of the setup itself.
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Date: 2/28/2023
 */

package clueGame;

import java.util.*;
import java.awt.Color;
import java.io.*;  

public class Board {
	private BoardCell[][] grid; // grid of cells on game board
	// targets: sets for calculating targets. visited: squares player can move to
	private Set<BoardCell> targets, visited; 
	private int numRows, numColumns; // dimensions of grid
	private String layoutConfigFile, setupConfigFile; // files for setting up game board
	private Map<Character, Room> roomMap; // mapping room names to characters
	private Solution theAnswer;
	private ArrayList<Player> players;
	private ArrayList<Card> deck;
	
	/*
     * variable and methods used for singleton pattern
     */
    private static Board theInstance = new Board();
    /**
     * Default constructor: creates a Board
     */
    private Board() {
        super();
    }
    /**
     * getInstance: returns the single instance of the Board class
     * @return
     */
    public static Board getInstance() {
           return theInstance;
    }
    /**
     * initialize: runs all methods necessary to set up the game board
     */
    public void initialize() {
    // throw BadConfigFormatException if configuration files are formatted incorrectly
    	try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			System.out.println(e);
		}
 		
 		// loop through grid and create adjacency list
 		for (int row = 0; row < numRows; row++) {
 			for (int column = 0; column < numColumns; column++) { 	
 				// always going to be run
 				walkwayAdjacency(row, column);
 				// only run if square is a doorway
 				if (grid[row][column].isDoorway()) {
 					doorwayAdjacency(row, column);
 				}
 				// only add secret passage if character is not '=' which is the default
 				if (grid[row][column].getSecretPassage() != ' ') {
 					secretPassageAdjacency(row,column);	
 				}		
 			}
 		}
    }
     
    /**
     * walkwayAdjacency: adds adjacent squares for walkway square
     * @param row: row number of square's location
     * @param column: column number of square's location
     */
 	private void walkwayAdjacency(int row, int column) {
 		// indices of adjacent cell locations
 		int up = row - 1;
 		int down = row + 1;
 		int left = column - 1;
 		int right = column + 1;
 		// these if statements ensure that cells with negative indices
 		// or cells outside of the grid are not added to the adjacency list
 		if (row >= 1 && grid[up][column].getInitial() == 'W') {
 			grid[row][column].addAdjacency(getCell(up, column));
 		}
 		if (column >= 1 && grid[row][left].getInitial() == 'W') {
 			grid[row][column].addAdjacency(getCell(row, left));
 		}
 		if (row < numRows - 1 && grid[down][column].getInitial() == 'W') {
 			grid[row][column].addAdjacency(getCell(down, column));
 		}
 		if (column < numColumns - 1 && grid[row][right].getInitial() == 'W') {
 			grid[row][column].addAdjacency(getCell(row, right));
 		}
 	}
	
    /**
     * walkwayAdjacency: adds adjacent squares for doorway square
     * @param row: row number of square's location
     * @param column: column number of square's location
     */
	private void doorwayAdjacency(int row, int column) {
		// indices of adjacent cell locations
		int up = row - 1;
		int down = row + 1;
		int left = column - 1;
		int right = column + 1;
		// get door direction and add center of respective room to adjacency list
		DoorDirection direction = grid[row][column].getDoorDirection();
		BoardCell roomCenter;
		switch(direction) {
			case UP:
				roomCenter = roomMap.get(getCell(up, column).getInitial()).getCenterCell();
				grid[row][column].addAdjacency(roomCenter);
				roomCenter.addAdjacency(getCell(row, column));
				break;
			case LEFT:
				roomCenter = roomMap.get(getCell(row, left).getInitial()).getCenterCell();
				grid[row][column].addAdjacency(roomCenter);
				roomCenter.addAdjacency(getCell(row, column));
				break;
			case DOWN:
				roomCenter = roomMap.get(getCell(down, column).getInitial()).getCenterCell();
				grid[row][column].addAdjacency(roomCenter);
				roomCenter.addAdjacency(getCell(row, column));
				break;
			case RIGHT:
				roomCenter = roomMap.get(getCell(row, right).getInitial()).getCenterCell();
				grid[row][column].addAdjacency(roomCenter);
				roomCenter.addAdjacency(getCell(row, column));
			default:
				break;
		}
	}
     
    /**
     * walkwayAdjacency: adds adjacent squares for secret passage
     * @param row: row number of square's location
     * @param column: column number of square's location
     */
	private void secretPassageAdjacency(int i, int j) {
		// find center of current room, find center of new room, add targets
		BoardCell roomCenter = roomMap.get(grid[i][j].getInitial()).getCenterCell();
		BoardCell newRoomCenter = roomMap.get(grid[i][j].getSecretPassage()).getCenterCell();
		roomCenter.addAdjacency(newRoomCenter);
	}
	
	/**
	 * calcTargets: calculate the targets from one cell based on the length of the path
	 * @param startCell: current cell before movement
	 * @param pathLength: length of path for target generation, or what number the player rolls
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
        visited = new HashSet<BoardCell>();
        targets = new HashSet<BoardCell>();
        visited.add(startCell);
        findAllTargets(startCell, pathLength);
    }

	/**
	 * findAllTargets: Recursive function that goes through cells, and finds target locations
	 * @param thisCell: starting cell
	 * @param numSteps: number of steps remaining
	 */
	private void findAllTargets(BoardCell thisCell, int numSteps) {
		// loop through each cell in adjacency list (starting path)
        for (BoardCell cell : thisCell.getAdjList()) {
        	// rooms are still targets but they end the path, skip occupied cells
        	if (cell.isRoom() || cell.isOccupied()) {
        		if (!visited.contains(cell) && cell.isRoom()) {
        			targets.add(cell);
        		}
        		continue;
        	}
        	
        	// only visiting unvisited cells
            if (!visited.contains(cell)) {
                visited.add(cell);
                // add cell to targets if we are at the end of path
                if (numSteps == 1) {
                    targets.add(cell);
                } else {
                	// continue path by recursive call
                    findAllTargets(cell, numSteps - 1);
                }
                // remove from visited once we have finished computing paths
                // from the cell / left the cell
                visited.remove(cell);
            }
        }
    }
	
	// Returns a cell inside the board
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	/**
	 * setConfigFiles: Sets configuration files, adds "ClueInitFiles/data/" to open from correct path
	 * @param layout: layout file name
	 * @param setup: setup file name
	 */
	public void setConfigFiles(String layout, String setup) {
		layoutConfigFile = "ClueInitFiles/data/" + layout;
		setupConfigFile = "ClueInitFiles/data/" + setup;
	}
	
	/**
	 * Loads room map from file
	 * @throws BadConfigFormatException: throws exception if setup file is not configured correctly
	 */
	public void loadSetupConfig() throws BadConfigFormatException {		
		Scanner sc = null;
		
		// throw exception if file not found
		try {
			sc = new Scanner(new File(setupConfigFile));
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		// initialize roomMap, player, and deck
		roomMap = new HashMap<Character, Room>();
		players = new ArrayList<Player>();
		deck = new ArrayList<Card>();
		
		// get input from file line by line
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			// first two characters // means we don't do anything
			if (line.substring(0, 2).equals("//")) {
				continue;
			}
			// variable for last index of line for convenience
			int lastIndex = line.length() - 1;
			// Room and Space are the two options to add to roomMap
			if (line.startsWith("Room, ")) {
				roomMap.put(line.charAt(lastIndex), new Room(line.substring(6, lastIndex - 2)));
				deck.add(new Card(line.substring(6, lastIndex - 2), CardType.ROOM));
			} else if (line.startsWith("Space, ")){
				roomMap.put(line.charAt(lastIndex), new Room(line.substring(7, lastIndex - 2)));
				// keep track that cell is not a room
				BoardCell.spaceChars.add(line.charAt(lastIndex));
			} else if (line.startsWith("#") ){ // signifies a player
				// split line for parsing
				String[] rest = line.substring(1).split(", ");
				// create color from rgb values
				String colorHex = rest[4].toUpperCase();
				Color color = Color.decode(colorHex);
				// initialize object based on second element of list of strings
				if (rest[1].equals("Human")) {
					players.add(new HumanPlayer(rest[0], Integer.parseInt(rest[2]), Integer.parseInt(rest[3]), color));
				} else {
					players.add(new ComputerPlayer(rest[0], Integer.parseInt(rest[2]), Integer.parseInt(rest[3]), color));
				}
				deck.add(new Card(rest[0], CardType.PERSON));
			} else if (line.startsWith("@")) {
				// add weapon to deck
				deck.add(new Card(line.substring(1), CardType.WEAPON));
			}
			else { // anything else will throw an exception
				throw new BadConfigFormatException("Improper setup file format");
			}
		}
		sc.close();	
	}
	/**
	 * Loads grid from csv file
	 * @throws BadConfigFormatException: throws exception if layout file is not configured correctly
	 */
	public void loadLayoutConfig() throws BadConfigFormatException {
		Scanner sc = null;
		// throw exception if file not found
		try {
			sc = new Scanner(new File(layoutConfigFile));
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		// make ArrayList to temporarily store lines from csv file
		ArrayList<String> csvText = new ArrayList<String>();
		// read each line into the ArrayList
		while (sc.hasNextLine()){  
			csvText.add(sc.nextLine());    
		}   
		sc.close();  //closes the scanner  
		
		// size of ArrayList is number of lines in csv file, which is the number of rows
		numRows = csvText.size();
		
		// loop through number of rows
		for (int row = 0; row < numRows; row++) {
			// each row becomes a list of strings
			String[] rowText = csvText.get(row).split(","); 
			if (row == 0) {
				// setting numColumns, should be the same for every row
				numColumns = rowText.length;
				// instantiating grid now that we have the numbers of rows and columns
				grid = new BoardCell[numRows][numColumns];
			} else if (rowText.length != numColumns) { 
				// throw exception if row length is not the same for every row
				throw new BadConfigFormatException("Rows have different numbers of cells");
			}
			// loop through number of columns (looping through each string in row)
			for (int column = 0; column < numColumns; column++) {
				// add cell to grid at current location, string is coming from the file
				grid[row][column] = new BoardCell(row, column, rowText[column]);
				// throw exception if cell initial not in roomMap
				if (roomMap.get(rowText[column].charAt(0)) == null) {
					throw new BadConfigFormatException("Cell initial not specified in setup file");
				}
				// check if cell is a label or center and give information to corresponding room
				if (grid[row][column].isLabel()) {
					roomMap.get(rowText[column].charAt(0)).setLabelCell(grid[row][column]);
				} else if (grid[row][column].isRoomCenter()) {
					roomMap.get(rowText[column].charAt(0)).setCenterCell(grid[row][column]);
				}
			}
		}
	}
	
	
	/**
	 * Deals cards to player and to solution
	 */
	public void deal() {
		// shuffle the deck
		Collections.shuffle(deck);
		// use array to create solution
		Card[] answer = new Card[3];
		// solution does not have these types yet
		boolean hasRoom = false;
		boolean hasPerson = false;
		boolean hasWeapon = false;
		// loop through deck and add first card of each type to solution
		for (Card currCard : deck) {
			if (currCard.getCardType() == CardType.ROOM && !hasRoom) {
				answer[1] = currCard;
				hasRoom = true;
			} else if (currCard.getCardType() == CardType.PERSON && !hasPerson) {
				answer[0] = currCard;
				hasPerson = true;
			} else if (currCard.getCardType() == CardType.WEAPON && !hasWeapon) {
				answer[2] = currCard;
				hasWeapon = true;
			}
			if (hasRoom && hasWeapon && hasPerson) {
				break;
			}
		}
		// remove solution cards from deck
		for (Card card : answer) {
			deck.remove(card);
		}
		// create solution from array
		theAnswer = new Solution(answer[0], answer[1], answer[2]);
		int playerNum = 0;
		// loop through cards and deal them to each player
		for (Card cardToDeal : deck) {
			int numPlayers = players.size();
			// use % so we go back to the first player
			players.get(playerNum % numPlayers).updateHand(cardToDeal);
			playerNum++;
		}
		
		for (Card card : answer) {
			deck.add(card);
		}
	}
	
	public boolean checkAccusation(Solution solution) {
		Card person = solution.getPerson();
		Card room = solution.getRoom();
		Card weapon = solution.getWeapon();
		return person == theAnswer.getPerson() && room == theAnswer.getRoom() && weapon == theAnswer.getWeapon();
	}

	public Card handleSuggestion(Player player, Solution suggestion) {
		ArrayList<Player> players = getPlayers();
		
		// Error with the player we gave
		if (!players.contains(player)) {
			return null; 
		}
		
		// This is used for getting the next player in line, from least to start to query the questions
		int playerLocInArray = players.indexOf(player);
		players.remove(playerLocInArray);
		
		/*
		// Orders arrayList to be the next person in line, making a circle
		ArrayList<Player> otherPlayers = new ArrayList<Player>();
		
		while (!players.isEmpty()) {
			if (players.size() > playerLocInArray) {
				otherPlayers.add(players.get(playerLocInArray));
				players.remove(playerLocInArray);
			}
			else {
				otherPlayers.add(players.get(players.size()));
				players.remove(players.size());
			}
		}
		*/
		
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getHand().contains(suggestion.getPerson())) {
				return suggestion.getPerson();
			}
			else if (players.get(i).getHand().contains(suggestion.getRoom())) {
				return suggestion.getRoom();
			}
			else if (players.get(i).getHand().contains(suggestion.getWeapon())) {
				return suggestion.getWeapon();
			}
			else {
				//They do not know the solution
			}
		}
		
		// returns null if no one can disprove
		return null;
	}
	
	// two getters for room - one with the char as an input and one with the cell
	// as an input. roomMap uses chars so for the second one getInitial gets the char
	
	public Room getRoom(char c) {
		return roomMap.get(c);
	}
	
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public Set<BoardCell> getAdjList(int i, int j) {
		return getCell(i, j).getAdjList();
	}
	
	// following methods likely only for testing
	public Solution getTheAnswer() {
		return theAnswer;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public ArrayList<Card> getDeck() {
		return deck;
	}
	
	public void setTheAnswer(Solution theAnswer) {
		this.theAnswer = theAnswer;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public Card findCard(String cardName, CardType cardType) {
		for (Card card : getDeck()) {
			if (card.getCardType().equals(cardType) && (card.getCardName().equals(cardName))) {
				return card;
			}
		}
		return null;
	}
	
	public Card roomToCard(Room room) {
		return findCard(room.getName(), CardType.ROOM);
	}
}
