/**
 * @Card: This is a java class that represents a card, which will be dealt
 * to each player and to the solution and will have 3 different types.
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Date: 3/28/2023
 */

package clueGame;

import clueGame.CardType;

public class Card {
	//We will have a Card class to describe the game cards. 
	//We’ll use an enumerated type for card type. Cards also need a name.
	private String cardName;
	private CardType cardType;
	
	public Card(String cardName, CardType cardType) {
		super();
		this.cardName = cardName;
		this.cardType = cardType;
	}

	public CardType getCardType() {
		return cardType;
	}
	
	public String getCardName() {
		return cardName;
	}
	
	// override equals based on name of card
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Card)) {
			return false;
		}
		Card cardToCompare = (Card)o;
		return (cardToCompare.cardName == cardName);
	}
	
	
}
