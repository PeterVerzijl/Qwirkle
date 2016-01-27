package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourBlockException;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;

/**
 * Player class for the Qwirkle game, contains all the player information.
 * 
 * @author Peter Verzijl en Dennis Vinke
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
	 * Removes a block from a players hand
	 * @param aList
	 */
	public void removeBlocksFromHand(List<Node> aList) {
		for (Node n : aList) {
			try {
				removeBlock(n.getBlock());
			} catch (NotYourBlockException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Removes a block of the same kind from the players hand.
	 * 
	 * @param block
	 *            The block to remove
	 * @throws NotYourBlockException
	 */
	public void removeBlock(Block block) throws NotYourBlockException {
		for (Block b : mHand) {
			if (b.equals(block)) {
				mHand.remove(b);
				return;
			}
		}
		throw new NotYourBlockException("This block: " + block.getColor().toString() + "" + block.getShape().toString()
				+ " was not in your hand");
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

	/**
	 * Class used by the child classes that extend this player class
	 * @return a move made by the player  
	 */
	public List<Node> determineMove() {
		return null;
	}

	/**
	 * Makes a string representation of the hand
	 * @param aHand
	 * @return
	 */
	public static String handToString(List<Block> aHand) {
		String hand = "";
		for (int i = 0; i < aHand.size(); i++) {
			Block b = aHand.get(i);
			hand += i + ": " + b.getColor().toString().charAt(0) + " " + BlockPrinter.getChar(b) + ", ";
		}
		return hand;
	}

	/**
	 * Removes all the stones of a hand
	 */
	void resetHand() {
		mHand.clear();
	}

	/**
	 * Sets the hand of the player to a block list. NOTE: This should NEVER be
	 * used other then to reset it to the previous state.
	 * 
	 * @param handBackup
	 *            The backup of the hand to reset the hand to.
	 */
	public void setHand(List<Block> handBackup) {
		if (handBackup.size() <= 6) {
			mHand = handBackup;
		}
	}

}
