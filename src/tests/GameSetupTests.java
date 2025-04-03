package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Player;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Board;
import clueGame.Card;
import clueGame.Solution;
import clueGame.CardType;
import clueGame.Solution;

public class GameSetupTests {
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	@Test
	public void testDeckSetup() {	
		ArrayList<Card> deck = board.getDeck();
		
		for (Card card: deck) {
			boolean isType = false;
			CardType cardType = card.getCardType();
			if (cardType == CardType.ROOM || cardType == CardType.PERSON || cardType == CardType.WEAPON) {
				isType = true;
			}
			assertTrue(isType);
		}
		assertEquals(21, board.getDeck().size());		
	}
	
	@Test
	public void testPlayers() {
		assertEquals(6, board.getPlayers().size());
		
		assertTrue(board.getPlayers().get(0) instanceof HumanPlayer);
		assertTrue(board.getPlayers().get(5) instanceof ComputerPlayer);
	}
	
	@Test
	public void testCardDealing() {
		board.deal();
		for (Player player : board.getPlayers()) {
			assertEquals(player.getHand().size(), 3);
		}
		
		Solution solution = board.getTheAnswer();
		assertNotEquals(solution.getRoom(), null);
		assertNotEquals(solution.getPerson(), null);
		assertNotEquals(solution.getWeapon(), null);
	}
}
