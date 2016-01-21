package com.peterverzijl.softwaresystems.qwirkle;
//import com.peterverzijl.softwaresystems.qwirkle.collision.RectangleCollider;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.ui.Sprite;

import java.util.List;

import javax.swing.JFrame;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameEngineComponent;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameObject;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Rect;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Transform;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Bitmap;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Camera;
import com.peterverzijl.softwaresystems.qwirkle.graphics.SpriteRenderer;
import com.peterverzijl.softwaresystems.qwirkle.scripts.MoveOnMouse;

public class PlayerGUI {

	private Camera mMainCamera;
	private Game mGame;
	private Player mPlayer;
	private Bitmap mTilemap;
	private Sprite tileSprite;
	
	//TODO: PLAYER OOK ALS ARGUMENT MEEGEVEN
	PlayerGUI(Player aPlayer){
		mPlayer=aPlayer;
		mGame=mPlayer.getGame();
		
		GameEngineComponent game = new GameEngineComponent(this);
		JFrame frame = new JFrame("Game Engine");
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
	public void start() {
	 
	mMainCamera = new Camera();
	 System.out.println("Test");

		// Load the tile map
		if (mTilemap == null) {
			mTilemap = Bitmap.load("qwirkle-tiles.png");
			tileSprite = new Sprite(mTilemap, new Rect(6 * 16, 0, 16, 16));
		}
		System.out.println("Tilemap done");

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


		System.out.println("Building board done");
		
		// Show hand on screen
		List<Block> weHand = mPlayer.getmHand();
		System.out.println(mPlayer.getmHand().size());
		
		float blockPadding = 75;
		float xOffset = (Camera.getWidth() - blockPadding) / weHand.size();
		int blockCount = 0;
		for (Block b : weHand) {
			Sprite sprite = BlockSpriteMap.getSprite(b);
			GameObject guiBlock = new GameObject("GUI Block");
			SpriteRenderer r = guiBlock.addComponent(SpriteRenderer.class);
			r.setSprite(sprite);
			Transform t = guiBlock.addComponent(Transform.class);
			// RectangleCollider c =
			// guiBlock.addComponent(RectangleCollider.class);
			t.setPosition(blockPadding / 2 + xOffset * blockCount++, Camera.getHeight() - 8);
			System.out.println("hand block " + t.getPosition());
		}
	 }
	
	 Transform cameraTransform;
	 
	 public void tick(){
		 
	 }
	 	 
	 public Player getPlayer(){
		 return mPlayer;
	 }
}
