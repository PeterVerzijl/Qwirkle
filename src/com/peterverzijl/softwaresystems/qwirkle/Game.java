package com.peterverzijl.softwaresystems.qwirkle;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.Block.Color;
import com.peterverzijl.softwaresystems.qwirkle.Block.Shape;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameObject;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Input;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Rect;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Transform;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;
import com.peterverzijl.softwaresystems.qwirkle.rendering.Renderer;
import com.peterverzijl.softwaresystems.qwirkle.rendering.SpriteRenderer;
import com.peterverzijl.softwaresystems.qwirkle.ui.Bitmap;
import com.peterverzijl.softwaresystems.qwirkle.ui.Camera;
import com.peterverzijl.softwaresystems.qwirkle.ui.Sprite;

/**
 * Master class for the game, this handles the setting up and running of the game.
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
	
	public GameObject currentBlock;
	
	public void start() {
		
		System.out.println("The game has started, aw yeah!");
		System.out.println("Shuffeling mBag of blocks...");
		mBag = new BlockBag();
		
		// Create a bunch of mPlayers
		int playerCount = 4;
		for (int i = 0; i < playerCount; i++) {
			Player p = new Player();
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
		
		mMainCamera = new Camera();
		
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
		
		currentBlock = new GameObject("Current Block");
		currentBlock.addComponent(Transform.class);
		Sprite block = Renderer.getSpriteFromBlock(new Block(Shape.CIRCLE, Color.BLUE));
		SpriteRenderer renderer = currentBlock.addComponent(SpriteRenderer.class);
		renderer.setSprite(block);
		
		
		// Print amount of left over blocks after mPlayers have drawn
		System.out.println(mBag.blocks.size() + 
							" blocks left in the mBag.");		
	}

	/**
	 * Function gets called every frame.
	 */
	public void tick() {
		float speed = 0.1f;
		
		Vector2 camPos = mMainCamera.transform.getPosition();
		if (Input.getKey(KeyEvent.VK_UP)) {
			camPos.add(0, speed);
		} else if (Input.getKey(KeyEvent.VK_DOWN)) {
			camPos.add(0, -speed);
		}
		if (Input.getKey(KeyEvent.VK_RIGHT)) {
			camPos.add(-speed, 0);
		} else if (Input.getKey(KeyEvent.VK_LEFT)) {
			camPos.add(speed, 0);
		}
		
		Transform transform = currentBlock.getComponent(Transform.class);
		transform.setPosition(Input.mousePosition);		
	}
}
