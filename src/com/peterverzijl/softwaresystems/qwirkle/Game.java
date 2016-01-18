package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;

//import com.peterverzijl.softwaresystems.qwirkle.collision.RectangleCollider;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.ui.Sprite;
import com.peterverzijl.softwaresystems.qwirkle.collision.RectangleCollider;
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

	private List<HumanPlayer> mPlayers = new ArrayList<HumanPlayer>();
	public static List<Block> mFrontier = new ArrayList<Block>();
	public static List<Block> setBlocks = new ArrayList<Block>(); // RAGERAGERAGERAGE
	private Bitmap mTilemap;
	private Sprite tileSprite;

	private Camera mMainCamera;

	private int mCurrentPlayer = 0;

	private Node mStartNode;

	public GameObject currentBlock;

	public static int[] borders = { 0, 0, 0, 0 };

	public void start() {

		mMainCamera = new Camera();

		System.out.println("The game has started, aw yeah!");
		System.out.println("Shuffeling mBag of blocks...");

		// Make first move
		// mFrontier = new ArrayList<Block>();
		mFrontier.add(new Block(null, null));
		mFrontier.get(0).setPosition(0, 0);

		mBag = new BlockBag();

		// Create a bunch of mPlayers
		int playerCount = 4;
		for (int i = 0; i < playerCount; i++) {
			HumanPlayer p = new HumanPlayer(i);
			p.initHand(mBag, 6);
			mPlayers.add(p);
		}

		// Print all mPlayers their blocks
		for (int j = 0; j < mPlayers.size(); j++) {
			System.out.print("Player " + j + "'s hand: ");
			System.out.println(Player.handToString(mPlayers.get(j).getmHand()));
		}

		// Load the tile map
		if (mTilemap == null) {
			mTilemap = Bitmap.load("qwirkle-tiles.png");
			tileSprite = new Sprite(mTilemap, new Rect(6 * 16, 0, 16, 16));
		}

		// Build the board
		int spriteSize = 16;
		for (int x = 0; x < spriteSize; x++) {
			for (int y = 0; y < spriteSize; y++) {
				GameObject gridCell = new GameObject("Grid Cell");
				SpriteRenderer renderer = gridCell.addComponent(SpriteRenderer.class);
				renderer.setSprite(tileSprite);

				Transform transform = gridCell.addComponent(Transform.class);
				transform.setPosition((x * spriteSize) + (int) mMainCamera.transform.getPosition().getX(),
						(y * spriteSize) + (int) mMainCamera.transform.getPosition().getY());
			}
		}

		// Show hand on screen
		HumanPlayer wePlayer = mPlayers.get(0);
		List<Block> weHand = wePlayer.getmHand();
		float blockPadding = 75;
		float xOffset = (Camera.getWidth() - blockPadding) / weHand.size();
		int blockCount = 0;
		for (Block b : weHand) {

			GameObject guiBlock = new GameObject("GUI Block");
			Transform t = guiBlock.addComponent(Transform.class);
			
			SpriteRenderer r = guiBlock.addComponent(SpriteRenderer.class);
			r.setSprite(BlockSpriteMap.getSprite(b));
			
			RectangleCollider c = guiBlock.addComponent(RectangleCollider.class);
			
			guiBlock.addComponent(MoveOnMouse.class);	// Adds the move on mouse scipt
			
			t.setPosition(blockPadding / 2 + xOffset * blockCount++, Camera.getHeight() - 8);
			System.out.println("hand block " + t.getPosition());
		}

		// Print amount of left over blocks after mPlayers have drawn
		System.out.println(mBag.blocks.size() + " blocks left in the mBag.");
	}

	Transform cameraTransform;

	public void renderBlocks() {
		for (Block b : Game.setBlocks) {
			Sprite sprite = BlockSpriteMap.getSprite(b);
			GameObject guiBlock = new GameObject("GUI Block");
			SpriteRenderer r = guiBlock.addComponent(SpriteRenderer.class);
			r.setSprite(sprite);
			Transform t = guiBlock.addComponent(Transform.class);
			// RectangleCollider c =
			// guiBlock.addComponent(RectangleCollider.class);
			float blockPadding = 75;
			float xOffset = (Camera.getWidth() - blockPadding) / Game.setBlocks.size();
			int blockCount = 0;
			t.setPosition(blockPadding / 2 + xOffset * blockCount++, Camera.getHeight() - 8);
			System.out.println("hand block " + t.getPosition());
		}
	}

	public List<Block> getFrontier() {
		return mFrontier;
	}

	public void addFrontier(Block aBlock) {
		mFrontier.add(aBlock);
	}

	public void removeFrontier(Block aBlock) {
		if (mFrontier.contains(aBlock)) {
			mFrontier.remove(aBlock);
		}
	}

	/**
	 * Function gets called every frame.
	 */
	public void tick() {
		mPlayers.get(mCurrentPlayer).determineMove(mFrontier);
		mCurrentPlayer = (mCurrentPlayer + 1) % mPlayers.size();
		renderBlocks();
	}

	public List<Block> addStone(int aAmount){
		List<Block> newBlocks = new ArrayList<Block>();
		for(int i = 0; i < aAmount;i++ ){
			newBlocks.add(mBag.drawBlock());
		}
		return newBlocks;
	}

	// voor in het verslag Time Complexity: Since every node is visited at most
	// twice, the time complexity is O(n) where n is the number of nodes in
	// given linked list.
	// @ensure (aBlock instanceof Block) ==true.
	public static void boardToString(List<Block> aBlockList) {
		int x = 10;// borders[1] - borders[0];
		int y = 10;// borders[3] - borders[2];
		String boardToString[][] = new String[x][y];

		// Block currentBlock = aBlock;

		/*
		 * int t = 0; do { System.out.println(++t); boardToString[(int)
		 * aBlock.getPosition().getX()][(int) aBlock.getPosition().getY()] =
		 * aBlock.getShape() .toString(); currentBlock = (Block)
		 * currentBlock.getParent(); } while (currentBlock != null);
		 */

		int t = 0;
		for (int i = 0; i < aBlockList.size(); i++) {
			System.out.println(++t);
			boardToString[(int) aBlockList.get(i).getPosition().getX() + 5][(int) aBlockList.get(i).getPosition().getY()
					+ 5] = aBlockList.get(i).getColor().toString().charAt(0) + ""
							+ BlockPrinter.getChar(aBlockList.get(i));
		}

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < x; j++) {
				if (boardToString[i][j] == null) {
					boardToString[i][j] = "  ";
				}
				System.out.print(boardToString[i][j] + " ");
			}
			System.out.println("");
		}
	}
}
