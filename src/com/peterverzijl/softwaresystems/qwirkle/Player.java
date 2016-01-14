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
	 * The game the player is playing
	 */
	protected static Game mGame;

	/**
	 * The blocks that the player has in his/her prosession
	 */
	private List<Block> mHand = new ArrayList<Block>();

	/**
	 * The constructor for the player
	 */
	public Player(Game aGame,int mId) {
		// Initialize player
		this.mID = mId;
		mGame = aGame;
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
		if (mHand.contains(block)) {
			mHand.remove(block);
		} else {
			// TODO(Peter): else throw an error?
		}
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

	public abstract List<Block> determineMove(List<Block> aFrontier);

	public boolean checkHand(List<Block> set) {
		boolean inHand = true;
		for (Block b : set) {
			if (!mHand.contains(b)) {
				System.err.println("Hand did not contain all the blocks");
				inHand = false;
			}
		}
		return inHand;
	}

	public void setMove(List<Block> aFrontier) {
		List<Block> set = determineMove(aFrontier);

		if (checkHand(set)) {
			for (int i = 0; i < set.size(); i++) {
				if (set.get(set.size() - 1) != null) {
					mGame.getSetStones().add(set.get(i));
				} else {
					System.out.println("Now trading");
				}
				mHand.remove(set.get(i));
			}
		}
		System.out.println("De zet is gezet");
	}

	public static String handToString(List<Block> aHand) {
		String hand = "";
		for (int i = 0; i < aHand.size(); i++) {
			Block b = aHand.get(i);
			hand += b.getColor().toString().charAt(0) + " " + BlockPrinter.getChar(b) + ", ";
		}
		return hand;
	}
	
	public Game getGame(){
		return mGame;
	}
}
