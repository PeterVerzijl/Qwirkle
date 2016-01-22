package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourBlockException;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;

/**
 * Player class for the Qwirkle game, contains all the player information.
 * 
 * @author Peter Verzijl
 *
 */
public class Player {

	
	/**
	 * The game the player is playing
	 */

	/**
	 * The blocks that the player has in his/her prosession
	 */
	private List<Block> mHand = new ArrayList<Block>();
	
	private Board mBoard;
	/**
	 * The constructor for the player
	 */
	public Player() {
		mHand.clear();
	}

	/**
	 * Draws the blocks from the bag at the start of the game
	 * 
	 * @param bag
	 *            The bag to draw blocks from
	 * @param blocksToDraw
	 *            The amount of blocks to draw
	 */
	public void initHand(BlockBag bag, int blocksToDraw) {
		for (int i = 0; i < blocksToDraw; i++) {
			mHand.add(bag.drawBlock());
		}
	}

	/**
	 * Adds a block to the players hand
	 * 
	 * @param block
	 *            The block to add
	 */
	public void addBlock(Block block) {
		mHand.add(block);
	}

	/**
	 * Removes a block of the same kind from the players hand.
	 * @param block The block to remove
	 * @throws NotYourBlockException 
	 */
	public void removeBlock(Block block) 
			throws NotYourBlockException {
		for (Block b : mHand) {
			if (b.equals(block)) {
				mHand.remove(b);
				return;
			}
		}
		throw new NotYourBlockException();
	}

	/**
	 * Removes a block by index
	 * 
	 * @param blockNo
	 *            The indexed block to remove
	 */
	public void removeBlock(int blockNo) {
		if (blockNo < mHand.size() && mHand.get(blockNo) != null) {
			mHand.remove(blockNo);
		} else {
			// TODO(Peter): Maybe exception throwing?
			System.err.println("Error: Block number " + blockNo + " either is null or doesn't exist!");
		}
	}

	/* --- Getters and setters --- */


	/**
	 * Returns the current hand of the player
	 * 
	 * @return
	 */
	public List<Block> getHand() {
		return mHand;
	}

	public List<Node> determineMove() {
		return null;
	}


	public static String handToString(List<Block> aHand) {
		String hand = "";
		for (int i = 0; i < aHand.size(); i++) {
			Block b = aHand.get(i);
			hand += b.getColor().toString().charAt(0) + " " + BlockPrinter.getChar(b) + ", ";
		}
		return hand;
	}

	void boardScale(Vector2 aPosition) {
		if (Board.borders[0] > aPosition.getX())
			Board.borders[0] = (int) aPosition.getX() - 7;
		if (Board.borders[1] < aPosition.getX())
			Board.borders[1] = (int) aPosition.getX() + 7;
		if (Board.borders[2] > aPosition.getY())
			Board.borders[2] = (int) aPosition.getY() - 7;
		if (Board.borders[3] < aPosition.getY())
			Board.borders[3] = (int) aPosition.getY() + 7;
	}

	void resetHand() {
		mHand.clear();
	}
	
}
