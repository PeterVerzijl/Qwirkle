package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.collision.RectangleCollider;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameObject;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Rect;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Transform;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Bitmap;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Camera;
import com.peterverzijl.softwaresystems.qwirkle.graphics.SpriteRenderer;
import com.peterverzijl.softwaresystems.qwirkle.ui.Sprite;

/**
 * Master class for the game, this handles the setting up and running of the game.
 * @author Peter Verzijl
 *
 */
public class Game {
	
	public static final int HAND_SIZE = 6;
	
	private BlockBag mBag;
	
	private List<HumanPlayer> mPlayers = new ArrayList<HumanPlayer>();
	
	private Bitmap mTilemap;
	private Sprite tileSprite;
	
	private Camera mMainCamera;
	
	public GameObject currentBlock;
	
	public void start() {
		
		mMainCamera = new Camera();
		
		System.out.println("The game has started, aw yeah!");
		System.out.println("Shuffeling mBag of blocks...");
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
			for (int i = 0; i < mPlayers.get(0).getmHand().size(); i++) {
				Block b = mPlayers.get(j).getmHand().get(i);
				System.out.print(b.getColor().toString().charAt(0) + 
								" " + BlockPrinter.getChar(b) + ", ");
			}
			System.out.println();			
		}
				
		// Load the tile map
		if (mTilemap == null) {
			mTilemap = Bitmap.load("qwirkle-tiles.png");
			tileSprite = new Sprite(mTilemap, new Rect(6 * 16, 0, 16, 16));
		}
		
		// Build the board
		int spriteSize = 16;
		for (int x = 0; x < spriteSize; x++)  {
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
			Sprite sprite = BlockSpriteMap.getSprite(b);
			GameObject guiBlock = new GameObject("GUI Block");
			SpriteRenderer r = guiBlock.addComponent(SpriteRenderer.class);
			r.setSprite(sprite);
			Transform t = guiBlock.addComponent(Transform.class);
			RectangleCollider c = guiBlock.addComponent(RectangleCollider.class);
			t.setPosition(blockPadding/2 + xOffset * blockCount++, Camera.getHeight() - 8);
			System.out.println("hand block " + t.getPosition());
		}
		
		// Print amount of left over blocks after mPlayers have drawn
		System.out.println(mBag.blocks.size() + 
							" blocks left in the mBag.");		
	}
	
	Transform cameraTransform;
	
	/**
	 * Function gets called every frame.
	 */
	public void tick() {
		
	}
}
