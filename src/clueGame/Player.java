/**
 * @Player: This is an abstract java class that represents a player, which will take care
 * of actions that are taken by a player and store the player's hand and other 
 * information.
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Date: 3/28/2023
 */


package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import clueGame.Board;

public abstract class Player {
	//The Player class have a list of cards they hold. They must have a name. They must have a location.
	private String name;
	private int row, col;
	private Color color;
	// cards player has in hand
	private ArrayList<Card> hand = new ArrayList<Card>();
	private Set<Card> seenCards = new HashSet<Card>();

	public Player(String name, int row, int col, Color color) {
		super();
		this.name = name;
		this.row = row;
		this.col = col;
		this.color = color;
	}
	
	public Card disproveSuggestion(Solution solution) {
		Card[] cardsInSuggestion = {solution.getPerson(), solution.getRoom(), solution.getWeapon()};
		ArrayList<Card> matchingCards = new ArrayList<Card>();
		for (Card card : cardsInSuggestion) {
			if (hand.contains(card)) {
				matchingCards.add(card);
			}
		}
		if (matchingCards.isEmpty()) {
			return null;
		} else if (matchingCards.size() == 1) { // this is technically not needed but might make things easier to read
			return matchingCards.get(0);
		} else {
			Collections.shuffle(matchingCards);
			return matchingCards.get(0);
		}
	}
	
	// add card to player's hand
	public void updateHand(Card card) {
		hand.add(card);
	}
	
	public void updateSeen(Card card) {
		seenCards.add(card);
	}

	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public Set<Card> getSeenCards() {
		return seenCards;
	}
	
	public String getName() {
		return name;
	}

	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}

	public void setSeenCards(Set<Card> seenCards) {
		this.seenCards = seenCards;
	}
}

