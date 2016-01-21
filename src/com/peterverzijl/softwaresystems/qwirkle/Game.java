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
	private Bitmap mTilemap;
	private Sprite tileSprite;

	private Camera mMainCamera;

	private int mCurrentPlayer = 0;

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
			mPlayers.get(i).setGame(this);
		}
		// Add first possible move
		}
	//test unit
	public void run() {
		while(!hasEnded()) {
			try {
				doMove(mPlayers.get(mCurrentPlayer).determineMove(mBoard.getEmptySpaces()));
				addBlocks(mPlayers.get(mCurrentPlayer));
				mCurrentPlayer = ((mCurrentPlayer + 1) % mPlayers.size());
			} catch (IllegalMoveException e) {
				System.err.println("Er gaan dingen mis!!!");
			}
			
		}
		//doe iets als de game klaar is
	}

	public boolean hasEnded() {
		return false;
	}
	

	public boolean checkHand(Block block) {
		boolean result = false;
		List<Block> playerHand= mPlayers.get(mCurrentPlayer).getmHand();
		for (Block b : playerHand) {
			if (b.equals(block)) {
				result = true;
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
			if (!checkHand(n.getBlock())) {
				result = false;
			}
		}
		return result;
	}

	public void doMove(List<Node> aPlayerMove) throws IllegalMoveException{
		List<Node> playersMove = aPlayerMove;
		boolean trade = false;
		if (playersMove.size() > 0 && playersMove.get(0).getPosition().getX()==GameConstants.UNSET_NODE) {
			playersMove.remove(playersMove.size() - 1);
			trade = true;
		}
		System.out.println("checking hand!");
		if (checkHand(playersMove)) {
			System.out.println("Set in hand");
			System.out.println(playersMove.size());
			for (int i = 0; i < playersMove.size(); i++) {
				if (!trade) {
				//	boardScale(playersMove.get(i).getPosition());
					//if (Board.isValid(playersMove)) {
						System.out.println("if isValid");
						mBoard.setFrontier(playersMove.get(i));
						mBoard.getPlacedBlocks().add(playersMove.get(i));
					//}
				} else {
					System.out.println("Now trading");
					tradeBlocks(playersMove.get(i).getBlock());
				}
				mPlayers.get(mCurrentPlayer).removeBlock(playersMove.get(i).getBlock());
			}
		} else {System.out.println("checkHand was false");}
		System.out.println("De zet is gezet");
	}

	public List<Node> getFrontier() {
		List<Node> copy = new ArrayList<Node>();
		List<Node> original = mBoard.getEmptySpaces();
		for(Node v : original){
			copy.add(v);
		}
		return copy;
	}

		public List<Player> getPlayers(){
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

	public void addBlocks(Player aPlayer) {
		while (aPlayer.getmHand().size() != 6 && mBag.blocks.size() - (6 - aPlayer.getmHand().size()) > -1) {
			aPlayer.addBlock(mBag.drawBlock());
		}
	}

	public List<Node> getCopyBoard(){
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
}