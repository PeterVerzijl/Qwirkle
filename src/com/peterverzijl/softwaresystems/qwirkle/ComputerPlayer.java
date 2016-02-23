package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.peterverzijl.softwaresystems.qwirkle.exceptions.IllegalMoveException;

/**
 * The computer player class. 
 * This class represents the AI player who can play automatically and 
 * makes clever decisions based on his hand and the board.
 * @author Peter Verzijl
 */
public class ComputerPlayer extends Player {
	
	Board mBoard;
	
	/**
	 * Constructor, creates a board and initializes the player.
	 */
	public ComputerPlayer() {
		super();
		mBoard = new Board();
	}
	
	/**
	 * Determines a move based on the board and the hand of the player.
	 * @return The placed blocks at their repsective positions.
	 */
	public List<Node> determineMove() {
		List<Block> possibleMoves = new ArrayList<Block>(getHand());
		List<Node> possiblePositions = new ArrayList<Node>(mBoard.getEmptySpaces());
		List<Node> set = new ArrayList<Node>();
		Map<Integer, List<Node>> possibleSets = new HashMap<Integer, List<Node>>(
						determineBestMove(possibleMoves, possiblePositions, set));
		int longestSet = 0;

		for (Map.Entry<Integer, List<Node>> e : possibleSets.entrySet()) {
			System.out.print(e.getKey());
			System.out.print(" " + e.getValue() + "\n");
		}

		for (int i : possibleSets.keySet()) {

			if (i > longestSet) {
				longestSet = i;
			}
		}
		
		return possibleSets.get(longestSet);
	}
	
	/**
	 * Determines the best move based on all possible moves, 
	 * the possible positions and the set.
	 * @param aPossibleMoves All possible stones that can be placed.
	 * @param aPossiblePositions All possible positions where stones can be placed.
	 * @param aSet A set of moves of which the best needs to be determined.
	 * @return A map of the score regarding a particular move.
	 */
	public Map<Integer, List<Node>> determineBestMove(List<Block> aPossibleMoves, 
														List<Node> aPossiblePositions,
														List<Node> aSet) {
		Map<Integer, List<Node>> map = new HashMap<Integer, List<Node>>();
		for (Block block : aPossibleMoves) {
			List<Node> possiblePositions = 
							new ArrayList<Node>(mBoard.giveHint(aPossiblePositions, 
																aSet, block));
			List<Block> possibleMoves = new ArrayList<Block>(aPossibleMoves);
			List<Node> set = new ArrayList<Node>(aSet);
			if (possiblePositions.size() > 0) {
				possibleMoves.remove(block);
				for (Node n : possiblePositions) {
					Node newNode = new Node();
					newNode.setBlock(block);
					newNode.setPosition((int) n.getPosition().getX(), 
										(int) n.getPosition().getY());
					mBoard.setStone(newNode);
					set.add(newNode);
					if (mBoard.isValid(set)) {
						map.putAll(determineBestMove(possibleMoves, possiblePositions, set));
					} else {
						set.remove(n);
						map.put(set.size(), set);
					}
				}
			} else {
				map.put(set.size(), set);
			}

		}
		return map;
	}
	
	/**
	 * Do a particular move.
	 * @param aNode The move. A block position pair.
	 */
	public void setMove(List<Node> aNode) {
		if (aNode.size() < 1) {
			return;
		}
		try {
			mBoard.setStones(aNode);
		} catch (IllegalMoveException e) {
			System.err.println("The Server send an INVALID MOVE");
		}
	}
}