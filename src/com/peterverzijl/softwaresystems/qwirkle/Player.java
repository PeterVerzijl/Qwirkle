package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;

/**
 * Player class for the Qwirkle game, contains all the player information.
 * 
 * @author Peter Verzijl
 *
 */
public abstract class Player {

	/**
	 * The id of the player.
	 */
	private int mID = -1;
	/**
	 * The total amount of players
	 */
	public static int playerCount = 0;

	/**
	 * The blocks that the player has in his/her prosession
	 */
	private List<Block> mHand = new ArrayList<Block>();

	/**
	 * The constructor for the player
	 */
	public Player(int mId) {
		// Initialize player
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
	 * Removes a certain block from the players hand
	 * 
	 * @param block
	 *            The block to remove
	 */
	public void removeBlock(Block block) {
		// TODO(Peter): See if the block exists, else throw an error?
		mHand.remove(block);
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
	 * Gets the ID of the current player
	 * 
	 * @return The player's ID
	 */
	public int getmID() {
		return mID;
	}

	/**
	 * Returns the player count
	 * 
	 * @return Player count
	 */
	public static int getPlayerCount() {
		return playerCount;
	}

	/**
	 * Returns the current hand of the player
	 * 
	 * @return
	 */
	public List<Block> getmHand() {
		return mHand;
	}

	public abstract List<Block> determineMove();

	public boolean checkHand(List<Block> set) {
		boolean inHand = true;
		for (Block b : set) {
			if (!mHand.contains(b)) {
				inHand = false;
			}
		}
		return inHand;
	}

	public void setMove() {
		List<Block> set = determineMove();
		while(set==(null)){
			set =determineMove();
		}
		if (checkHand(set)) {
			for (int i = 0; i < set.size(); i++) {
			//	set.get(i).finalizeMove();
			}
		}
	}
}
