/**
 * @Solution: This is a java class that represents the solution to the game, which
 * will be a room, a person, and a weapon.
 * @Authors: Matthew Nielsen and Levi Sprung
 * @Date: 3/28/2023
 */

package clueGame;

public class Solution {
	// the solution consists of three cards
	Card room; 
	Card person;
	Card weapon;
	
	public Solution(Card person, Card room, Card weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	public Card getRoom() {
		return room;
	}
	
	public Card getPerson() {
		return person;
	}
	
	public Card getWeapon() {
		return weapon;
	}
	
	//create method for found solution
}
