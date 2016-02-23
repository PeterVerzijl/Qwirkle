package com.peterverzijl.softwaresystems.qwirkle.tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

import com.peterverzijl.softwaresystems.qwirkle.Block;
import com.peterverzijl.softwaresystems.qwirkle.Game;
import com.peterverzijl.softwaresystems.qwirkle.HumanTUIPlayer;
import com.peterverzijl.softwaresystems.qwirkle.Node;
import com.peterverzijl.softwaresystems.qwirkle.Player;
import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourBlockException;

public class GameTest {

	Game game;
	List<Player> playerList = new ArrayList<Player>();
	List<Node> aSetP1 = new ArrayList<Node>();
	List<Node> aSetP2 = new ArrayList<Node>();
	List<Node> aSetP3 = new ArrayList<Node>();
	List<Node> aSetP4 = new ArrayList<Node>();
	Node node1 = new Node();
	Node node2 = new Node();
	Node node3 = new Node();
	Node node4 = new Node();
	Node node5 = new Node();
	Node node6 = new Node();

	Map<Player, List<Node>> firstMoves = new HashMap<Player, List<Node>>();
	Map<Player, List<Node>> startingplayer;

	@Before
	public void setUp() {
		playerList.add(new HumanTUIPlayer());
		playerList.add(new HumanTUIPlayer());
		playerList.add(new HumanTUIPlayer());
		playerList.add(new HumanTUIPlayer());

		game = new Game(playerList);

		node1.setPosition(0, 0);
		node1.setBlock(new Block(Block.Shape.CROSS, Block.Color.BLUE));

		node2.setPosition(1, 0);
		node2.setBlock(new Block(Block.Shape.CIRCLE, Block.Color.BLUE));

		node3.setPosition(2, 0);
		node3.setBlock(new Block(Block.Shape.SQUARE, Block.Color.BLUE));

		node4.setPosition(3, 0);
		node4.setBlock(new Block(Block.Shape.CROSS, Block.Color.BLUE));

		node5.setPosition(0, 0);
		node5.setBlock(new Block(Block.Shape.CROSS, Block.Color.BLUE));

		node6.setPosition(1, 0);
		node6.setBlock(new Block(Block.Shape.CIRCLE, Block.Color.BLUE));

		// removes all blocks
		for (Player p : game.getPlayers()) {
			for (int i = 0; i < 6; i++) {
				p.removeBlock(0);
			}
		}

		aSetP1.add(node1);
		aSetP2.add(node1);
		aSetP3.add(node1);
		aSetP4.add(node1);
		firstMoves.put(playerList.get(0), aSetP1);
		aSetP2.add(node2);
		aSetP3.add(node2);
		aSetP4.add(node2);
		firstMoves.put(playerList.get(1), aSetP2);
		aSetP3.add(node3);
		aSetP4.add(node3);
		firstMoves.put(playerList.get(2), aSetP3);
		aSetP4.add(node4);
		firstMoves.put(playerList.get(3), aSetP4);

		for (Player p : game.getPlayers()) {
			for (Node node : aSetP4) {
				p.addBlock(node.getBlock());
			}
		}
		// aSet.remove(aSet.size() - 1);

	}

	@Test
	public void constructorsTest() {
		assertEquals(game.getNumStonesInBag(), 108 - (game.getPlayers().size() * 6));
		assertNotEquals(game.getCopyBoard(), null);
	}

	@Test
	public void startingPlayerTest() {
		// TODO: Fix starting player if wrong move is done.
		aSetP4.remove(aSetP4.size() - 1);

		startingplayer = game.startingPlayer(firstMoves);
		System.out.println(startingplayer.size());



		/*
		 * Player pl = startingplayer.keySet().iterator().next(); for (Player
		 * player : playerList) { System.out.println(player); }
		 */

		assertTrue(startingplayer.containsKey(playerList.get(3)));
		assertEquals(startingplayer.get(playerList.get(3)), aSetP4);
	}

	@Test
	public void getCurrentPlayerTest() {
		assertEquals(
				game.getPlayers().get((game.getPlayers().indexOf(game.getCurrentPlayer()) + 1) % playerList.size()),
				game.nextPlayer());
	}

	@Test
	public void hasEndedTest() {
		game.addStone(game.getNumStonesInBag());
		
		assertFalse(game.hasEnded());
		for(Player p: game.getPlayers()){
			p.getHand().clear();
		}
		assertTrue(game.hasEnded());
	}

	@Test
	public void checkHandTest() {
		for (Node block : firstMoves.get(game.getCurrentPlayer())) {
			assertTrue(game.checkHand(game.getCurrentPlayer(), block.getBlock()));
		}
	}

	@Test
	public void getPlayersTest() {
		assertEquals(game.getPlayers().size(), playerList.size());

		for (Player p : game.getPlayers()) {
			assertTrue(playerList.contains(p));
		}
	}

	@Test
	public void removePlayersTest() {
		game.removePlayer(playerList.get(0));
		assertEquals(game.getPlayers().size(), 3);
	}

	@Test
	public void addBlocksTest() {
		try {
			game.getCurrentPlayer().removeBlock(node1.getBlock());
			game.getCurrentPlayer().removeBlock(node2.getBlock());
		} catch (NotYourBlockException e) {
			System.err.println(e.getMessage());
		}

		assertNotEquals(game.getCurrentPlayer().getHand().size(), 6);
		game.addBlocks(game.getCurrentPlayer());
		assertEquals(game.getCurrentPlayer().getHand().size(), 6);

	}
}
