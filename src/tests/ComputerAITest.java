package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.*;

import org.junit.jupiter.api.*;

import clueGame.*;

public class ComputerAITest {
	
	private static Board board;
	private static Card colonelMustard, breaker, rope, 
	missScarlett, kitchen, candlestick, profPlum, office, wrench;
	private static ComputerPlayer player1, player2, player3;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
		
		// create cards
		colonelMustard = new Card("Colonel Mustard", CardType.PERSON);
		missScarlett = new Card("Miss Scarlett", CardType.PERSON);
		profPlum = new Card("Professor Plum", CardType.PERSON);
		breaker = new Card("Breaker", CardType.ROOM);
		office = new Card("Office", CardType.ROOM);
		kitchen = new Card("Kitchen", CardType.ROOM);
		rope = new Card("Rope", CardType.WEAPON);
		candlestick = new Card("Candlestick", CardType.WEAPON);
		wrench = new Card("Wrench", CardType.WEAPON);
		
		// create players
		// start not near room
		player1 = new ComputerPlayer("a", 0, 6, Color.YELLOW);
		// start in room
		player2 = new ComputerPlayer("b", 3, 3, Color.BLUE);
		// start at doorway
		player3 = new ComputerPlayer("c", 16, 2, Color.RED);
	}
	
	@Test
	public void testSuggestionCreation() {
		//create groups of players, rooms, and weapons
		ArrayList<Card> deck = board.getDeck();
		// get room that player is in
		Card currRoom = board.roomToCard(board.getRoom(board.getCell(player2.getRow(), player2.getCol())));
		
		ArrayList<Card> seen = new ArrayList<Card>();
		// add cards to seen list
		for (Card card : deck) {
			if (card.getCardType() == CardType.PERSON) {
				seen.add(card);
			}
		}
		for (Card card : deck) {
			if (card.getCardType() == CardType.WEAPON) {
				seen.add(card);
			}
		}
		// remove these two cards; they should be suggested
		Card person = seen.get(0);
		seen.remove(0);
		Card weapon = seen.get(6);
		seen.remove(6);
		// set player's seen list and create suggestion
		player2.setSeenCards(new HashSet<Card>(seen));
		Solution suggestion = player2.createSuggestion();
		
		
		assertEquals(suggestion.getPerson(), person);
		assertEquals(suggestion.getWeapon(), weapon);
		// currRoom should always be suggested
		assertEquals(suggestion.getRoom(), currRoom);
		// remove more cards from seen
		Card person2 = seen.get(0);
		seen.remove(0);
		Card weapon2 = seen.get(6);
		seen.remove(6);
		
		player2.setSeenCards(new HashSet<Card>(seen));
		Set<Card> suggested = new HashSet<Card>();
		
		// should randomly select person and weapon between those two options
		for (int i = 0; i < 200; i++) {
			suggestion = player2.createSuggestion();
			suggested.add(suggestion.getPerson());
			suggested.add(suggestion.getWeapon());
			// room should still always be currRoom
			assertEquals(suggestion.getRoom(), currRoom);
		}
		
		assertTrue(suggested.contains(person));
		assertTrue(suggested.contains(person2));
		assertTrue(suggested.contains(weapon));
		assertTrue(suggested.contains(weapon2));
	}
	
	@Test
	public void testTargetSelection() {	
		// unseen room should be selected
		assertEquals(player3.selectTarget(3), board.getCell(12, 2));
		// walkway cells should be randomly selected when no rooms available
		Set<BoardCell> chosenTargets = new HashSet<BoardCell>();
		for (int i = 0; i < 200; i++) {
			chosenTargets.add(player1.selectTarget(3));
		}
		assertTrue(chosenTargets.contains(board.getCell(1, 6)));
		assertTrue(chosenTargets.contains(board.getCell(3, 6)));
		assertTrue(chosenTargets.contains(board.getCell(2, 7)));
		
		// now player3 has already seen the room
		Room adjacentRoom = board.getRoom(board.getCell(12, 2));
		player3.updateSeen(board.roomToCard(adjacentRoom));
		
		chosenTargets.clear();
		// should select randomly between room and walkways
		for (int i = 0; i < 200; i++) {
			chosenTargets.add(player3.selectTarget(1));
		}
		assertTrue(chosenTargets.contains(board.getCell(12, 2)));
		assertTrue(chosenTargets.contains(board.getCell(16, 3)));
		assertTrue(chosenTargets.contains(board.getCell(17, 2)));
	}
}

