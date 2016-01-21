package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;

//import com.peterverzijl.softwaresystems.qwirkle.collision.RectangleCollider;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.ui.Sprite;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameObject;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Rect;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Transform;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Bitmap;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Camera;
import com.peterverzijl.softwaresystems.qwirkle.graphics.SpriteRenderer;
import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourBlockException;
import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourTurnException;
import com.peterverzijl.softwaresystems.qwirkle.scripts.MoveOnMouse;

/**
 * Master class for the game, this handles the setting up and running of the
 * game.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Game implements Runnable {

	public static final int HAND_SIZE = 6;

	private BlockBag mBag;

	private List<Player> mPlayers = new ArrayList<Player>();
	private List<Block> mFrontier = new ArrayList<Block>();
	private List<Block> setBlocks = new ArrayList<Block>(); // RAGERAGERAGERAGE
	private Bitmap mTilemap;
	private Sprite tileSprite;

	private Camera mMainCamera;
	
	// TODO (dennis) : Change this to a reference to the player object, please!
	private int mCurrentPlayer = 0;

	private Node mStartNode;
	
	public GameObject currentBlock;
	
	public int[] borders = { -7, 7, -7, 7 };
	
	/**
	 * Create a game with a list of players.
	 * @param aPlayerList The players of the game.
	 */
	public Game(List<Player> aPlayerList) {
		mBag = new BlockBag();
		// TODO: DENNIS score resetten
		//
		mPlayers = aPlayerList;
		for (int i = 0; i < mPlayers.size(); i++) {
			mPlayers.get(i).resetHand();
			mPlayers.get(i).setGame(this);
			mPlayers.get(i).initHand(mBag, 6);
		}
		// Add first possible move
		mFrontier.add(new Block(null, null));
		mFrontier.get(0).setPosition(0, 0);
	}
	
	/**
	 * The main thread method.
	 */
	@Override
	public void run() {
		while(!hasEnded()) {
			mPlayers.get(mCurrentPlayer).setMove(mFrontier);
			addBlocks(mPlayers.get(mCurrentPlayer));
			mCurrentPlayer = ((mCurrentPlayer + 1) % mPlayers.size());
		}
		//doe iets als de game klaar is
	}
	
	/**
	 * Returns weighter the game has ended.
	 * @return Weighter the game has ended.
	 */
	public boolean hasEnded() {
		// TODO (peter) : To be implemented
		return false;
	}
	
	/**
	 * Returns a list of empty fields on which blocks can be placed.
	 * @return A list of empty fields where you can place blocks.
	 */
	public List<Block> getFrontier() {
		return mFrontier;
	}
	
	/**
	 * Adds a block to the frontier.
	 * @param aBlock The block to add to the frontier.
	 */
	public void addFrontier(Block aBlock) {
		mFrontier.add(aBlock);
	}
	
	/**
	 * Removes a block from the frontier.
	 * @param aBlock The block to remove from the frontier.
	 */
	public void removeFrontier(Block aBlock) {
		if (mFrontier.contains(aBlock)) {
			mFrontier.remove(aBlock);
		}
	}

	
	public void addBlocks(Player aPlayer) {
		while (aPlayer.getHand().size() != 6 && mBag.blocks.size() - (6 - aPlayer.getHand().size()) > -1) {
			aPlayer.addBlock(mBag.drawBlock());
		}
	}

	public List<Block> addStone(int aAmount) {
		List<Block> newBlocks = new ArrayList<Block>();
		for (int i = 0; i < aAmount; i++) {
			newBlocks.add(mBag.drawBlock());
		}
		return newBlocks;
	}

	// voor in het verslag Time Complexity: Since every node is visited at most
	// twice, the time complexity is O(n) where n is the number of nodes in
	// given linked list.
	// @ensure (aBlock instanceof Block) ==true.
	public void boardToString(List<Block> aBlockList, List<Block> aFrontierList) {
		int x =  (borders[1] - borders[0]);
		int y =  (borders[3] - borders[2]);

		System.out.println("X: " + borders[1] +" - " +borders[0]+ " "+x);

		System.out.println("Y: "+y);

		int midX = x / 2 +1;// (Math.abs(borders[0])+borders[1])/2;
		int midY = y / 2 +1;// (Math.abs(borders[2])+borders[3])/2;

		String boardToString[][] = new String[x + 1][y + 1];

		// Block currentBlock = aBlock;

		/*
		 * int t = 0; do { System.out.println(++t); boardToString[(int)
		 * aBlock.getPosition().getX()][(int) aBlock.getPosition().getY()] =
		 * aBlock.getShape() .toString(); currentBlock = (Block)
		 * currentBlock.getParent(); } while (currentBlock != null);
		 */

		for (int i = 0; i < aBlockList.size(); i++) {
			boardToString[(int) aBlockList.get(i).getPosition().getX()
					+ midX][(int) aBlockList.get(i).getPosition().getY() + midY] = aBlockList.get(i).getColor()
							.toString().charAt(0) + "" + BlockPrinter.getChar(aBlockList.get(i));
		}

		for (int i = 0; i < aFrontierList.size(); i++) {
			boardToString[(int) aFrontierList.get(i).getPosition().getX()
					+ midX][(int) aFrontierList.get(i).getPosition().getY() + midY] = " " + i + " ";
		}

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < x; j++) {
				if (boardToString[i][j] == null) {
					boardToString[i][j] = "  ";
				}
				System.out.print(boardToString[i][j] + "");
			}
			System.out.println("");
		}
	}

	public List<Block> getSetStones() {
		return setBlocks;
	}
	
	/**
	 * Trades a block with the bag.
	 * @param player The player who does the trade.
	 * @param block The block type the player wants to trade.
	 * @throws NotYourTurnException Thrown if this function is called when the player is not the current player.
	 * @throws NotYourBlockException Thrown if you try to trade a block that is not yours.
	 */
	public Block tradeBlock(Player player, Block block) 
			throws NotYourTurnException, 
			NotYourBlockException {
		// Look if we are the current player
		if (player == mPlayers.get(mCurrentPlayer)) {
			if (player.checkHand(block)) {
				// Do the trade
				Block newBlock = mBag.drawBlock();
				player.removeBlock(block);			// Don't change this order!
				player.addBlock(newBlock);			// !!
				mBag.returnBlock(block);			// !!
				return newBlock;
			} else {
				throw new NotYourBlockException();
			}
		} else {
			throw new NotYourTurnException();
		}
	}
	
	/**
	 * Removes a player from the game.
	 * @param player
	 */
	public void removePlayer(Player player) {
		mPlayers.remove(player);		
	}
	
	/**
	 * Returns the current amount of stones in the stone bag.
	 * @return The amount of stones left in the bag.
	 */
	public int getNumStonesInBag() {
		return mBag.blocks.size();
	}
}