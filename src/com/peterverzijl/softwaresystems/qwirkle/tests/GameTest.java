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

public class GameTest {

	Game game;
	List<Player> playerList = new ArrayList<Player>();
	List<Node> aSet = new ArrayList<Node>();
	Node node1 = new Node();
	Node node2 = new Node();
	Node node3 = new Node();
	Node node4 = new Node();
	Node node5 = new Node();
	Node node6 = new Node();

	Map<Player, List<Node>> firstMoves = new HashMap<Player, List<Node>>();

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

		aSet.add(node1);
		firstMoves.put(playerList.get(0), aSet);
		aSet.add(node2);
		firstMoves.put(playerList.get(1), aSet);
		aSet.add(node3);
		firstMoves.put(playerList.get(2), aSet);
		aSet.add(node4);
		firstMoves.put(playerList.get(3), aSet);
	}

	@Test
	public void constructorsTest() {
		assertEquals(game.getNumStonesInBag(), 108 - (game.getPlayers().size() * 6));
		assertNotEquals(game.getCopyBoard(), null);
	}

	@Test
	public void startingPlayerTest() {
		aSet.remove(aSet.size() - 1);
		for (Player p : playerList) {
			System.out.println(game.startingPlayer(firstMoves).get(p));
		}
		
		for (Map.Entry<Player,List<Node>> p : firstMoves.entrySet()) {
			System.out.println(p.getKey());
			System.out.println(p.getValue());
		}
		
		assertEquals(game.startingPlayer(firstMoves).get(playerList.get(2)), aSet);
		assertEquals(game.startingPlayer(firstMoves).keySet().iterator(), playerList.get(2));
	}

	@Test
	public void setPlacedBlockTest() {
	}

	@Test
	public void isValidTest() {
	}

	public void setEmptySpacesTest() {
	}

}
