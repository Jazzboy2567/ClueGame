/*
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Description: This is a java class that calculates target locations,
 * gets target locations and gets individual cells to find out distance calculations 
 */

package experiment;

import java.util.*;

public class TestBoard {
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	final static int COLS = 4;
	final static int ROWS = 4;
	
	
	public TestBoard() {
		super();
		this.grid = new TestBoardCell[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i, j);
			}
		}
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				int lessThanI = i - 1;
				int lessThanJ = j - 1;
				int greaterThanI = i + 1;
				int greaterThanJ = j + 1;
				if (lessThanI >= 0) {
					grid[i][j].addAdjacency(getCell(lessThanI, j));
				}
				if (lessThanJ >= 0) {
					grid[i][j].addAdjacency(getCell(i, lessThanJ));
				}
				if (greaterThanI < ROWS) {
					grid[i][j].addAdjacency(getCell(greaterThanI, j));
				}
				if (greaterThanJ < COLS) {
					grid[i][j].addAdjacency(getCell(i, greaterThanJ));
				}
			}
		}
	}
	
	// Checks how far the player can move and sees all possible options
	public void calcTargets (TestBoardCell startCell, int pathLength) {
        visited = new HashSet<TestBoardCell>();
        targets = new HashSet<TestBoardCell>();
        visited.add(startCell);
        findAllTargets(startCell, pathLength);
    }
	
	// Recursive function that goes through cells, and finds target locations based
	// on how many steps we have
	private void findAllTargets(TestBoardCell thisCell, int numSteps) {
        for (TestBoardCell cell : thisCell.getAdjList()) {
        	if (cell.isOccupied()) {
        		continue;
        	}
        	if (cell.isRoom()) {
        		targets.add(cell);
        		continue;
        	}
            if (!visited.contains(cell)) {
                visited.add(cell);
                if (numSteps == 1) {
                    targets.add(cell);
                } else {
                    findAllTargets(cell, numSteps - 1);
                }
                visited.remove(cell);
            }
        }
    }
	
	// Returns a cell inside the board
	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	// Uses calcTargets to find the actual cell locations to find the available
	// spaces to move
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
}
