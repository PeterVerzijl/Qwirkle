package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.peterverzijl.softwaresystems.qwirkle.collision.RectangleCollider;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.ui.Sprite;
import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourBlockException;
import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourTurnException;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameObject;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Rect;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Transform;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Bitmap;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Camera;
import com.peterverzijl.softwaresystems.qwirkle.graphics.SpriteRenderer;
import com.peterverzijl.softwaresystems.qwirkle.scripts.MoveOnMouse;

/**
 * Master class for the game, this handles the setting up and running of the
 * game.
 * 
 * @author Peter Verzijl
 *
 */
public class Game {

	public static final int HAND_SIZE = 6;

	private BlockBag mBag;

	private List<Player> mPlayers = new ArrayList<Player>();

	private Player mCurrentPlayer;

	private Board mBoard;

	public GameObject currentBlock;

	public Game(List<Player> aPlayerList) {
		mBag = new BlockBag();
		mBoard = new Board();
		// TODO: DENNIS score resetten

		mPlayers = aPlayerList;
		for (int i = 0; i < mPlayers.size(); i++) {
			mPlayers.get(i).resetHand();
			mPlayers.get(i).initHand(mBag, 6);
		}

		mCurrentPlayer = mPlayers.get(0);
	}

	public void setFirstPlayer(HashMap<Player, List<Node>> firstSet) {
		HashMap.Entry<Player, List<Node>> entry = firstSet.entrySet().iterator().next();
		mCurrentPlayer = entry.getKey();
		try {
			doMove(entry.getValue());
		} catch (IllegalMoveException e) {
			System.err.println("How the fuck did you mess this up!");
		}

	}

	public Map<Player, List<Node>> startingPlayer(Map<Player, List<Node>> aMapWithFirstMoves) {
		Map<Player, List<Node>> firstPlayerSet = new HashMap<Player, List<Node>>();
		Player playerHelper = mPlayers.get(0);
		int highestScore = 0;
		for (Player player : mPlayers) {
			try {
				for (Map.Entry<Player, List<Node>> e : aMapWithFirstMoves.entrySet()) {
					doMove(e.getValue());
					int score = 0;
					for (Node n : e.getValue()) {
						score += mBoard.calcScore(n);

					}
					if (score > highestScore) {
						playerHelper = e.getKey();
						highestScore = score;
					}
				}

				mBoard = new Board();
				firstPlayerSet.put(playerHelper, aMapWithFirstMoves.get(playerHelper));
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}

		}
		return firstPlayerSet;

	}

	public Player nextPlayer() {
		mCurrentPlayer = mPlayers.get(((mPlayers.indexOf(mCurrentPlayer) + 1) % mPlayers.size()));
		return mCurrentPlayer;
	}

	// test unit
	public void run() {
		while (!hasEnded()) {
			try {
				System.out.println("Board in Game");
				System.out.println(mBoard.toString(mBoard.getPlacedBlocks(), mBoard.getEmptySpaces()));
				doMove(mCurrentPlayer.determineMove());
				addBlocks(mCurrentPlayer);
				mCurrentPlayer = mPlayers.get(((mPlayers.indexOf(mCurrentPlayer) + 1) % mPlayers.size()));
			} catch (IllegalMoveException e) {
				System.err.println("Er gaan dingen mis!!!");
			}
		}
		// doe iets als de game klaar is
	}

	/**
	 * If there are no more stones in the bag Loop through all players and their
	 * hand till it finds a vallid move
	 * 
	 * @return true if a valid move is found or if there are stones in the bag
	 *         of stones
	 */
	public boolean hasEnded() {
		if (!(getNumStonesInBag() > 0)) {
			for (Player p : mPlayers) {
				for (Block s : p.getHand()) {
					if ((mBoard.GiveHint(mBoard.getEmptySpaces(), null, s)).size() > 0) {
						return false;
					}

				}
			}
			return true;
		}
		return false;
	}

	public boolean checkHand(Player player, Block block) {
		boolean result = false;
		if (player == mCurrentPlayer) {
			List<Block> playerHand = mCurrentPlayer.getHand();
			for (Block b : playerHand) {
				if (b.equals(block)) {
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * Checks if all the given blocks are in the hand of the player.
	 * 
	 * @param set
	 *            The set of blocks to check.
	 * @return Weighter all the blocks are in the hand of the player.
	 */
	public boolean checkHand(List<Node> set) {
		boolean result = true;
		for (Node n : set) {
			if (!checkHand(mCurrentPlayer, n.getBlock())) {
				result = false;
			}
		}
		return result;
	}

	// public setScore();
	public void doMove(List<Node> aPlayerMove) throws IllegalMoveException {
		List<Node> playersMove = aPlayerMove;
		boolean trade = false;

		if (!(playersMove.size() > 0)) {
			if (mBag.blocks.size() > 0) {
				throw new IllegalMoveException();
			} else {
				return;
			}
		}

		if (playersMove.get(0).getPosition().getX() == GameConstants.UNSET_NODE) {
			trade = true;
		}
		// System.out.println("checking hand!");
		// System.out.println("Trade = " + trade);
		if (checkHand(playersMove)) {
			System.out.println("Set in hand");
			// System.out.println(playersMove.size());
			// mBoard.newSet();
			mBoard.setStones(playersMove);
			for (int i = 0; i < playersMove.size(); i++) {
				if (!trade) {
					// boardScale(playersMove.get(i).getPosition());
					/*
					 * System.out.println(playersMove.get(i).getBlock().getShape
					 * () + " " + playersMove.get(i).getBlock().getColor() +
					 * ""); mBoard.setStone(playersMove.get(i));
					 */
				} else {
					// System.out.println("Now trading");
					tradeBlocks(playersMove.get(i).getBlock());
				}
				try {
					mCurrentPlayer.removeBlock(playersMove.get(i).getBlock());
				} catch (NotYourBlockException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (true) {
				// mBoard.doMove();
				notifyPlayer(playersMove);
			}
		} else {
			// System.out.println("checkHand was false");
		}
		System.out.println("De zet is gezet");
	}

	/**
	 * Debugin method to see if board is updating at player side Do not use when
	 * there are no human players
	 * 
	 * @param aValidMove
	 */
	void notifyPlayer(List<Node> aValidMove) {
		for (Player players : mPlayers) {
			// for (Node n : aValidMove) {
			if (!players.equals(mCurrentPlayer)) {
				if (players instanceof HumanTUIPlayer)
					((HumanTUIPlayer) players).setMove(aValidMove);
				else
					((ComputerPlayer) players).setMove(aValidMove);
				// }
			}
		}
	}

	public List<Node> getFrontier() {
		List<Node> copy = new ArrayList<Node>();
		List<Node> original = mBoard.getEmptySpaces();
		for (Node v : original) {
			copy.add(v);
		}
		return copy;
	}

	public List<Player> getPlayers() {
		return mPlayers;
	}

	/**
	 * Function gets called every frame.
	 */
	public void tick() {
		/*
		 * renderHand(mPlayers.get(mCurrentPlayer));
		 * mPlayers.get(mCurrentPlayer).setMove(mFrontier); mCurrentPlayer =
		 * (mCurrentPlayer + 1) % mPlayers.size();
		 * addBlocks(mPlayers.get(mCurrentPlayer)); System.out.println(
		 * "Now the new board will get rendered"); renderBlocks();
		 */
	}

	/**
	 * Method that makes sure that the player gets enough stones from the
	 * gameBag
	 * 
	 * @param aPlayer
	 * @return
	 */
	public List<Block> addBlocks(Player aPlayer) {
		List<Block> newBlocks = new ArrayList<Block>();
		while (aPlayer.getHand().size() != 6 && mBag.blocks.size() - (6 - aPlayer.getHand().size()) > -1) {
			Block blockFromBag = mBag.drawBlock();
			newBlocks.add(blockFromBag);
			aPlayer.addBlock(blockFromBag);
		}
		return newBlocks;
	}

	public List<Node> getCopyBoard() {
		return mBoard.getPlacedBlocks();
	}

	public List<Block> addStone(int aAmount) {
		List<Block> newBlocks = new ArrayList<Block>();
		for (int i = 0; i < aAmount; i++) {
			newBlocks.add(mBag.drawBlock());
		}
		return newBlocks;
	}

	public void tradeBlocks(Block aBlock) {
		mBag.blocks.add(aBlock);
	}

	/**
	 * Trades a block with the bag.
	 * 
	 * @param player
	 *            The player who does the trade.
	 * @param block
	 *            The block type the player wants to trade.
	 * @throws NotYourTurnException
	 *             Thrown if this function is called when the player is not the
	 *             current player.
	 * @throws NotYourBlockException
	 *             Thrown if you try to trade a block that is not yours.
	 */
	public Block tradeBlock(Player player, Block block) throws NotYourTurnException, NotYourBlockException {
		// Look if we are the current player
		if (player == mCurrentPlayer) {
			if (checkHand(player, block)) {
				// Do the trade
				Block newBlock = mBag.drawBlock();
				player.removeBlock(block); // Don't change this order!
				player.addBlock(newBlock); // !!
				mBag.returnBlock(block); // !!
				return newBlock;
			} else {
				throw new NotYourBlockException();
			}
		} else {
			throw new NotYourTurnException();
		}
	}

	public Player getCurrentPlayer() {
		return mCurrentPlayer;
	}

	/**
	 * Removes a player from the game.
	 * 
	 * @param player
	 */
	public void removePlayer(Player player) {
		mPlayers.remove(player);
	}

	/**
	 * Returns the current amount of stones in the stone bag.
	 * 
	 * @return The amount of stones left in the bag.
	 */
	public int getNumStonesInBag() {
		return mBag.blocks.size();
	}
}