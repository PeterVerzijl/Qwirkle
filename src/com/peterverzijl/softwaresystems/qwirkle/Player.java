package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.NotYourBlockException;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.NotYourTurnException;

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
	protected Game mGame;

	/**
	 * The blocks that the player has in his/her prosession
	 */
	private List<Block> mHand = new ArrayList<Block>();

	/**
	 * The constructor for the player
	 */
	public Player() {
	//	mGame = aGame;
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
	public List<Block> getmHand() {
		return mHand;
	}

	public List<Block> determineMove(List<Block> aFrontier){
		return null;
	}
	
	/**
	 * Checks if the given block is in the hand of the player.
	 * @param block The block to check.
	 * @return Weighter the block is in the hand of the player.
	 */
	public boolean checkHand(Block block) {
		boolean result = false;
		for (Block b : mHand) {
			if (b.equals(block)) {
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * Checks if all the given blocks are in the hand of the player.
	 * @param set The set of blocks to check.
	 * @return Weighter all the blocks are in the hand of the player.
	 */
	public boolean checkHand(List<Block> set) {
		boolean result = true;
		for (Block b : set) {
			if (!checkHand(b)) {
				result = false;
			}
		}
		return result;
	}

	public void setMove(List<Block> aFrontier) {
		List<Block> set = determineMove(aFrontier);
		boolean trade = false;
		if (set.size()>0&&set.get(set.size() - 1) == null) {
			set.remove(set.size() - 1);
			trade = true;
		}
		if (checkHand(set)) {
			for (int i = 0; i < set.size(); i++) {
				if (!trade) {
					boardScale(set.get(i).getPosition());
					mGame.getSetStones().add(set.get(i));
				} else {
					System.out.println("Now trading");
					try {
						mGame.tradeBlock(this, set.get(i));
					} catch (NotYourTurnException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotYourBlockException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mHand.remove(set.get(i));
			}
		}
		System.out.println("De zet is gezet");
	}
	
	public void setGame(Game aGame){
		mGame=aGame;
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
		if (mGame.borders[0] > aPosition.getX())
			mGame.borders[0] = (int) aPosition.getX()-7;
		if (mGame.borders[1] < aPosition.getX())
			mGame.borders[1] = (int) aPosition.getX()+7;
		if (mGame.borders[2] > aPosition.getY())
			mGame.borders[2] = (int) aPosition.getY()-7;
		if (mGame.borders[3] < aPosition.getY())
			mGame.borders[3] = (int) aPosition.getY()+7;
	}

	public Game getGame() {
		return mGame;
	}
	
	void resetHand(){
		mHand.clear();
	}
}
