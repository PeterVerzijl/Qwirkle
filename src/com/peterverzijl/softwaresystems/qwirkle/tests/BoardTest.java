package com.peterverzijl.softwaresystems.qwirkle.tests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

import com.peterverzijl.softwaresystems.qwirkle.Block;
import com.peterverzijl.softwaresystems.qwirkle.Board;
import com.peterverzijl.softwaresystems.qwirkle.Direction;
import com.peterverzijl.softwaresystems.qwirkle.Node;

public class BoardTest {

	Board board;
	Node node1 = new Node();
	Node node2 = new Node();
	Node node3 = new Node();
	Node node4 = new Node();
	Node node5 = new Node();
	Node node6 = new Node();
	Node node7 = new Node();
	Node node8 = new Node();
	Node node9 = new Node();
	Node node10 = new Node();
	Node node11 = new Node();
	Node node12 = new Node();
	Node node13 = new Node();
	Node node14 = new Node();
	Node node15 = new Node();
	Node node16 = new Node();
	List<Node> setColor = new ArrayList<Node>();
	List<Node> setShape = new ArrayList<Node>();

	@Before
	public void setUp() {
		board = new Board();
		node1.setPosition(0, 0);
		node1.setBlock(new Block(Block.Shape.PLUS, Block.Color.BLUE));
		node2.setPosition(1, 0);
		node2.setBlock(new Block(Block.Shape.CIRCLE, Block.Color.BLUE));
		node3.setPosition(0, 0);
		node3.setBlock(new Block(Block.Shape.CIRCLE, Block.Color.BLUE));
		node4.setPosition(2, 0);
		node4.setBlock(new Block(Block.Shape.STAR, Block.Color.BLUE));
		node5.setPosition(3, 0);
		node5.setBlock(new Block(Block.Shape.SQUARE, Block.Color.BLUE));
		node6.setPosition(4, 0);
		node6.setBlock(new Block(Block.Shape.DIAMOND, Block.Color.BLUE));
		node7.setPosition(5, 0);
		node7.setBlock(new Block(Block.Shape.CROSS, Block.Color.BLUE));
		node8.setPosition(6, 0);
		node8.setBlock(new Block(Block.Shape.CIRCLE, Block.Color.BLUE));
		node9.setPosition(0, 1);
		node9.setBlock(new Block(Block.Shape.PLUS, Block.Color.RED));
		node10.setPosition(0, 2);
		node10.setBlock(new Block(Block.Shape.PLUS, Block.Color.ORANGE));
		node11.setPosition(0, 3);
		node11.setBlock(new Block(Block.Shape.PLUS, Block.Color.PURPLE));
		node12.setPosition(0, 4);
		node12.setBlock(new Block(Block.Shape.PLUS, Block.Color.GREEN));
		node13.setPosition(0, 5);
		node13.setBlock(new Block(Block.Shape.PLUS, Block.Color.YELLOW));
		node14.setPosition(0, 6);
		node14.setBlock(new Block(Block.Shape.PLUS, Block.Color.BLUE));
		node15.setPosition(-1, 1);
		node15.setBlock(new Block(Block.Shape.PLUS, Block.Color.BLUE));
		node16.setPosition(1, 1);
		node16.setBlock(new Block(Block.Shape.PLUS, Block.Color.RED));

		Node[] nodes = new Node[] { node1, node2, node4, node5, node6, node7, node8, node9, node10 };
		Collections.addAll(setColor, nodes);
		nodes = new Node[] { node1, node9, node10, node11, node12, node13, node14 };
		Collections.addAll(setShape, nodes);
		// System.out.println("De set bevat "+set.size()+" items.");
	}

	@Test
	public void constructorsTest() {
		assertTrue(board.getEmptySpaces().get(0).getPosition().equals(node1.getPosition()));
		assertEquals(board.getEmptySpaces().get(0).getBlock(), (Block) null);
	}

	@Test
	public void setStoneTest() {
		board.setStone(node1);
		// System.out.println(board.getPlacedBlocks().get(0).getBlock());
		// System.out.println(node1.getBlock());

		assertTrue(board.getPlacedBlocks().get(0).equals(node1));
		board.setStone(node3);

		// check if first stone can be replaced and if the neigbornodes get
		// replaced
		assertFalse(board.getPlacedBlocks().get(board.getPlacedBlocks().size() - 1).equals(node3));
		assertTrue(board.getPlacedBlocks().get(board.getPlacedBlocks().size() - 1).equals(node1));
		assertTrue(board.getPlacedBlocks().size() == 1);
		for (int i = 0; i < Direction.values().length; i++) {
			assertTrue(board.getPlacedBlocks().get(0).getNeighborNodes()[i] != null);
		}

		// Check if one of the stones in the new position can be placed.
		// Current Board
		// []
		// []B+[]
		// []
		board.setStone(node2);
		assertTrue(board.getPlacedBlocks().get(board.getPlacedBlocks().size() - 1).equals(node2));
		assertTrue(board.getPlacedBlocks().size() == 2);
		for (Node n : board.getPlacedBlocks()) {
			for (int i = 0; i < Direction.values().length; i++) {
				assertTrue(n.getNeighborNodes()[i] != null);
			}
		}
		board = new Board();
		for (int i = 0; i < setColor.size(); i++) {
			// System.out.println("steen " + i + " Position: " +
			// set.get(i).getPosition());
			assertTrue(board.setStone(setColor.get(i)));
		}
	}

	@Test
	public void setPlacedBlockTest() {

		board = new Board();

		assertFalse(board.setPlacedBlock(node4));

	}

	@Test
	public void isValidTest() {

		board = new Board();
		List<Node> move = new ArrayList<Node>();

		// Set 6 valid moves of same color
		for (int i = 0; i < 6; i++) {
			if (board.setStone(setColor.get(i))) {
				move.add(board.getPlacedBlocks().get(board.getPlacedBlocks().size() - 1));
				assertTrue(board.isValid(move));
			}
		}

		// add a 7th stone with the same color
		board.setStone(setColor.get(6));
		move.add(board.getPlacedBlocks().get(board.getPlacedBlocks().size() - 1));
		assertFalse(board.isValid(move));

		// Set 6 valid moves of same shape
		board = new Board();
		move.clear();
		for (int i = 0; i < 6; i++) {
			if (board.setStone(setShape.get(i))) {
				move.add(board.getPlacedBlocks().get(board.getPlacedBlocks().size() - 1));
				assertTrue(board.isValid(move));
			}
		}

		board.setStone(setShape.get(6));
		move.add(board.getPlacedBlocks().get(board.getPlacedBlocks().size() - 1));
		assertFalse(board.isValid(move));

		// adding a stone out of order
		move.remove(board.getPlacedBlocks().size() - 1);
		System.out.println(board.toString(board.getPlacedBlocks(), board.getEmptySpaces()));
		board.setStone(node15);

		// add a new valid stone in a new set to the left of a stone already
		// placed on the board
		move.clear();
		board.setStone(node15);
		move.add(board.getPlacedBlocks().get(board.getPlacedBlocks().size() - 1));
		assertTrue(board.isValid(move));

		// add duplicate type in same row as the one above.
		board.setStone(node16);
		move.add(board.getPlacedBlocks().get(board.getPlacedBlocks().size() - 1)); 
		assertFalse(board.isValid(move));
	}

	public void setEmptySpacesTest(){
		//setPlacedBlock calls emptyspaces
		board = new Board();
		board.setPlacedBlock(node1);
		assertEquals(board.getEmptySpaces().size(),4);
	}
	
}
