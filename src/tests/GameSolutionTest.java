package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.*;

import org.junit.jupiter.api.*;

import clueGame.*;

public class GameSolutionTest {
	
	private static Board board;
	private static Card colonelMustard, breaker, rope, 
	missScarlett, kitchen, candlestick, profPlum, office, wrench;
	private static Player human1, player2, player3;
	
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
		human1 = new HumanPlayer("a", 0, 6, Color.YELLOW);
		player2 = new ComputerPlayer("b", 0, 17, Color.BLUE);
		player3 = new ComputerPlayer("c", 0, 18, Color.RED);
		
		// put cards in players' hands
		Card[] cards1 = {missScarlett, rope};
		for (Card card : cards1) {
			human1.updateHand(card);
		}
		Card[] cards2 = {colonelMustard, breaker};
		for (Card card : cards2) {
			player2.updateHand(card);
		}
		Card[] cards3 = {kitchen, wrench};
		for (Card card : cards3) {
			player3.updateHand(card);
		}
	}
	
	@Test
	public void testAccusation() {
		// set solution
		board.setTheAnswer(new Solution(colonelMustard, breaker, rope));
		// everything correct
		assertTrue(board.checkAccusation(new Solution(colonelMustard, breaker, rope)));
		// one thing is incorrect
		assertFalse(board.checkAccusation(new Solution(missScarlett, breaker, rope)));
		assertFalse(board.checkAccusation(new Solution(colonelMustard, kitchen, rope)));
		assertFalse(board.checkAccusation(new Solution(colonelMustard, breaker, candlestick)));
	}
	
	@Test
	public void testDisprove() {
		// player has a card to disprove
		assertEquals(
				player2.disproveSuggestion(new Solution(colonelMustard, kitchen, candlestick)), 
				colonelMustard);
		// player has no card to disprove
		assertEquals(
				player2.disproveSuggestion(new Solution(missScarlett, kitchen, candlestick)), 
				null);
		
		// player has multiple cards to disprove, should pick at random
		Set<Card> cardsDisproved = new HashSet<Card>();
		for (int i = 0; i < 100; i++) {
			cardsDisproved.add(
					player2.disproveSuggestion(new Solution(colonelMustard, breaker, candlestick))
					);
		}
		assertTrue(cardsDisproved.contains(colonelMustard));
		assertTrue(cardsDisproved.contains(breaker));
	}
	
	@Test
	public void testSuggestion() {
		// make list with two players
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(human1);
		players.add(player2);
		
		board.setPlayers(players);
		// no one can disprove
		assertEquals(board.handleSuggestion(player2, new Solution(profPlum, office, candlestick)), null);
		// only suggesting player can disprove
		assertEquals(board.handleSuggestion(player2, new Solution(colonelMustard, office, wrench)), null);
		// only human can disprove
		//assertEquals(board.handleSuggestion(player2, new Solution(missScarlett, candlestick, kitchen)), missScarlett);
		// add another player
		players.add(player3);
		board.setPlayers(players);
		// make sure player2 has priority over player3
		assertEquals(board.handleSuggestion(human1, new Solution(colonelMustard, kitchen, candlestick)), colonelMustard);
	}
}

