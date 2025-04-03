/*
 * @Room: This class contains the information for a room, including the name,
 * the center cell, and the label cell.
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Date: 2/28/2023
 */
package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell, labelCell;
	
	public Room(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}
}
