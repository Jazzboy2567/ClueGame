/**
 * @ComputerPlayer: This is a java class that represents a computer player, which
 * will make game decisions on its own and extends the Player class.
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Date: 3/28/2023
 */

package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

import clueGame.Solution;

public class ComputerPlayer extends Player {
	private static Board board = Board.getInstance();
	private int row, col;
	
	
	public ComputerPlayer(String name, int row, int col, Color color) {
		super(name, row, col, color);
		this.row = row;
		this.col = col;
	}
	
	public Solution createSuggestion() {
		Card currentRoom = board.roomToCard(board.getRoom(board.getCell(row, col)));
		ArrayList<Card> hand = getHand();
		Set<Card> seenCards = getSeenCards();
		ArrayList<Card> peopleNotInHand = new ArrayList<Card>();
		ArrayList<Card> weaponsNotInHand = new ArrayList<Card>();
		for (Card card : board.getDeck()) {
			if (card.getCardType() == CardType.PERSON) {
				peopleNotInHand.add(card);
			}
			else if (card.getCardType() == CardType.WEAPON) {
				weaponsNotInHand.add(card);
			}
		}
		
		for (Card card : hand) {
			if (card.getCardType() == CardType.PERSON) {
				peopleNotInHand.remove(card);
			}
			else if (card.getCardType() == CardType.WEAPON) {
				weaponsNotInHand.remove(card);
			}
		}
		for (Card card : seenCards) {
			if (card.getCardType() == CardType.PERSON) {
				peopleNotInHand.remove(card);
			}
			else if (card.getCardType() == CardType.WEAPON) {
				weaponsNotInHand.remove(card);
			}
		}
		Collections.shuffle(peopleNotInHand);
		Collections.shuffle(weaponsNotInHand);
		
		return new Solution(peopleNotInHand.get(0), currentRoom, weaponsNotInHand.get(0));
	}
	
	public BoardCell selectTarget(int pathLength) {
		ArrayList<Card> hand = getHand();
		Set<Card> seenCards = getSeenCards();
		ArrayList<Card> rooms = new ArrayList<Card>();
		
		//adds all possible rooms
		for (Card card : board.getDeck()) {
			if (card.getCardType() == CardType.ROOM) {
				rooms.add(card);
			}
		}
		
		//remove all room cards from hand
		for (Card card : hand) {
			if (card.getCardType() == CardType.ROOM) {
				rooms.remove(card);
			}
		}
		
		//remove all room cards from hand
		for (Card card : seenCards) {
			if (card.getCardType() == CardType.ROOM) {
				rooms.remove(card);
			}
		}
		
		board.calcTargets(board.getCell(row, col), pathLength);
		Set<BoardCell> targets = board.getTargets();
		
		for (BoardCell targetCell : targets) {
			if (targetCell.isRoomCenter()) {
				Card room = board.roomToCard(board.getRoom(targetCell));
				if (rooms.contains(room)) {
					return targetCell;
				}
			}
		}
		// random element from a set here: 
		// https://stackoverflow.com/questions/124671/picking-a-random-element-from-a-set
		int randomTarget = new Random().nextInt(targets.size());
		int i = 0;
		for(BoardCell target : targets)
		{
		    if (i == randomTarget)
		        return target;
		    i++;
		}
		return null;
	}
}
