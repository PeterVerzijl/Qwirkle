package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.peterverzijl.softwaresystems.qwirkle.exceptions.IllegalMoveException;
import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourBlockException;
import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourTurnException;

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

	/**
	 * constructor that creates a game based on the amount of players.
	 * @param aPlayerList The players who play the game.
	 */
	public Game(List<Player> aPlayerList) {
		mBag = new BlockBag();
		mBoard = new Board();

		mPlayers = aPlayerList;
		for (int i = 0; i < mPlayers.size(); i++) {
			mPlayers.get(i).resetHand();
			mPlayers.get(i).initHand(mBag, 6);
		}

		mCurrentPlayer = mPlayers.get(0);
	}

	/**
	 * Sets the first player and does the move that was worth the most points.
	 * @param firstSet The map of players, and their respective first moves.
	 */
	public void setFirstPlayer(Map<Player, List<Node>> firstSet) {
		HashMap.Entry<Player, List<Node>> entry = firstSet.entrySet().iterator().next();
		mCurrentPlayer = entry.getKey();
		try {
			doMove(entry.getValue());
			// mCurrentPlayer = nextPlayer();
		} catch (IllegalMoveException e) {
			System.err.println("How the fuck did you mess this up!");
		} catch (NotYourBlockException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Receives the first move of all the players and determine which player
	 * should start by calculation the scores of each move. The player with the
	 * highest scoring moves wins.
	 * @param aMapWithFirstMoves The map of players, and their respective first moves.
	 * @return map with the best move and the player that did the move
	 */
	public Map<Player, List<Node>> startingPlayer(Map<Player, List<Node>> aMapWithFirstMoves) {
		Map<Player, List<Node>> firstPlayerSet = new HashMap<Player, List<Node>>();
		Player playerHelper = mPlayers.get(0);
		int highestScore = 0;
		for (Map.Entry<Player, List<Node>> e : aMapWithFirstMoves.entrySet()) {
			mCurrentPlayer = e.getKey(); // Set the currently 'winning'player,
											// since the checkHand in doMove
											// uses this
			List<Block> handBackup = new ArrayList<Block>(mCurrentPlayer.getHand());
			try {
				doMove(e.getValue());
			} catch (NotYourBlockException | IllegalMoveException e1) {
				// This could happen, if so, just skip that person.
				// NOTE: This happens for example if you send and empty initial
				// node.
				continue;
			}
			int score = 0;
			for (int i = 0; i < e.getValue().size(); i++) {
				score += mBoard.calcScore(mBoard.getPlacedBlocks().get(i));
			}
			if (score > highestScore) {
				playerHelper = e.getKey();
				highestScore = score;
			}

			e.getKey().setHand(handBackup);
			mBoard = new Board(); // Clear the board for the next try or for
									// setting the move.

			System.out.println(mPlayers.indexOf(playerHelper) + " " + 
								aMapWithFirstMoves.get(playerHelper).size());
			firstPlayerSet.put(playerHelper, aMapWithFirstMoves.get(playerHelper));
			setFirstPlayer(firstPlayerSet);
		}

		return firstPlayerSet;

	}

	/**
	 * sets the next player.
	 * @return The next player.
	 */
	public Player nextPlayer() {
		mCurrentPlayer = mPlayers.get((mPlayers.indexOf(mCurrentPlayer) + 
										1) % mPlayers.size());
		return mCurrentPlayer;
	}

	/**
	 * If there are no more stones in the bag Loop through all players and their
	 * hand till it finds a valid move.
	 * @return True if a valid move is found or if there are stones in the bag
	 *         of stones.
	 */
	public boolean hasEnded() {
		if (!(getNumStonesInBag() > 0)) {
			for (Player p : mPlayers) {
				for (Block s : p.getHand()) {
					if ((mBoard.giveHint(mBoard.getEmptySpaces(), null, s)).size() > 0) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Checks if a similar block, exists in the hand of the player.
	 * @param player The player to check for.
	 * @param block The block to check.
	 * @return Weighter the given block also exists in the player's hand.
	 */
	public boolean checkHand(Player player, Block block) {
		boolean result = false;
		if (player == mCurrentPlayer) {
			for (Block b : mCurrentPlayer.getHand()) {
				if (b.equals(block)) {
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * Checks if all the given blocks are in the hand of the player.
	 * @param set The set of blocks to check.
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

	/**
	 * Checks if the list of blocks received are in the hand of the player.
	 * Checks if the player wants to trade checks with the board if the move is
	 * valid removes the blocks from the hand if the move is valid.
	 * @param aPlayerMove The move that the player wants to make.
	 * @throws IllegalMoveException If the move given does not comply with the game rules.
	 * @throws NotYourBlockException If the player tries to do a move with stones he/she
	 * 				Does not have in her/his hand.
	 */
	public void doMove(List<Node> aPlayerMove) throws IllegalMoveException, NotYourBlockException {
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
		if (checkHand(playersMove)) {
			System.out.println("Set in hand");
			mBoard.setStones(playersMove);
			for (int i = 0; i < playersMove.size(); i++) {
				if (!trade) {
				} else {
					tradeBlocks(playersMove.get(i).getBlock());
				}
				try {
					mCurrentPlayer.removeBlock(playersMove.get(i).getBlock());
				} catch (NotYourBlockException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new NotYourBlockException();
		}
	}

	/**
	 * Debugging method to see if board is updating at player side. Do not use when
	 * there are no human players.
	 * @param aValidMove The valid move to debug.
	 */
	void notifyPlayer(List<Node> aValidMove) {
		for (Player players : mPlayers) {
			if (!players.equals(mCurrentPlayer)) {
				((HumanTUIPlayer) players).setMove(aValidMove);
			}
		}
	}

	/**
	 * Returns the board frontier, aka all possible empty spaces where the player can 
	 * place his/her blocks.
	 * @return A list of empty nodes where blocks can be place.
	 */
	public List<Node> getFrontier() {
		List<Node> copy = new ArrayList<Node>();
		List<Node> original = mBoard.getEmptySpaces();
		for (Node v : original) {
			copy.add(v);
		}
		return copy;
	}

	/**
	 * Returns the players that are currently in the game.
	 * @return A list of players in this game.
	 */
	public List<Player> getPlayers() {
		return mPlayers;
	}

	/**
	 * Method that makes sure that the player gets enough stones from the
	 * gameBag.
	 * @param aPlayer The player to fill the hand for.
	 * @return A list of blocks given by the game to the player.
	 */
	public List<Block> addBlocks(Player aPlayer) {
		List<Block> newBlocks = new ArrayList<Block>();
		while (aPlayer.getHand().size() != 6 && 
				mBag.blocks.size() - (6 - aPlayer.getHand().size()) > -1) {
			Block blockFromBag = mBag.drawBlock();
			newBlocks.add(blockFromBag);
			aPlayer.addBlock(blockFromBag);
		}
		return newBlocks;
	}

	/**
	 * Gets the placed nodes of the unit.
	 * @return A list of placed blocks.
	 */
	public List<Node> getCopyBoard() {
		return mBoard.getPlacedBlocks();
	}

	/**
	 * Adds stones to the hand of the player.
	 * @param aAmount The amount of stones to add.
	 * @return A list of blocks added to the player hand.
	 */
	public List<Block> addStone(int aAmount) {
		List<Block> newBlocks = new ArrayList<Block>();
		for (int i = 0; i < aAmount; i++) {
			newBlocks.add(mBag.drawBlock());
		}
		return newBlocks;
	}

	/**
	 * Returns the blocks back to the bag.
	 * @param aBlock The block to return to the bag.
	 */
	public void tradeBlocks(Block aBlock) {
		mBag.blocks.add(aBlock);
	}

	/**
	 * Trades a block with the bag.
	 * 
	 * @param player The player who does the trade.
	 * @param block The block type the player wants to trade.
	 * @return The block that is the result of the trade.
	 * @throws NotYourTurnException Thrown if this function is called 
	 * 				when the player is not the current player.
	 * @throws NotYourBlockException Thrown if you try to trade a block 
	 * 				that is not yours.
	 */
	public Block tradeBlock(Player player, Block block) 
			throws NotYourTurnException, NotYourBlockException {
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
	
	/**
	 * Gets the current player from the game.
	 * @return The current player.
	 */
	public Player getCurrentPlayer() {
		return mCurrentPlayer;
	}

	/**
	 * Removes a player from the game.
	 * @param player The player to remove.
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