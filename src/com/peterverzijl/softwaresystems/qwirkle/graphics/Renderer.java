package com.peterverzijl.softwaresystems.qwirkle.graphics;


import java.util.HashMap;

import com.peterverzijl.softwaresystems.qwirkle.Block;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.ui.Sprite;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameEngineComponent;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Transform;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector3;

/**
 * Renders the screen in the game. 
 * Responsible for rendering all GameObjects that have a SpriteRenderer component.
 * Also translates the buffer according to a given camera if it exists.
 * @author Peter Verzijl
 *
 */
public class Renderer {
	
	public static Camera mCamera;
	private Screen mScreen;
	
	HashMap<Block, Sprite> spriteMap = new HashMap<Block, Sprite>();
	
	public Renderer(Screen screen) {
		mScreen = screen;
	}
		
	public void render() {
		
		if (mCamera == null) {
			// try get camera
			mCamera = GameEngineComponent.getCamera();
		}
		
		mScreen.clear();
		
		// Convert list to array to create a copy we can loop trough. In this case, 
		// if any other thread tries to modify while looping, we will get that modification next frame.
		RenderComponent[] renderers = GameEngineComponent.renderers.toArray(new RenderComponent[GameEngineComponent.renderers.size()]);
		for (RenderComponent renderer : renderers) {
			Transform t = renderer.getGameObject().getComponent(Transform.class);
			Sprite sprite = renderer.getSprite();
			if (sprite != null && t != null) {
				renderSprite(sprite, t.getPosition().toVector3());
			}
		}		
	}
	
	/**
	 * Renders a sprite on the back buffer.
	 * @param sprite The sprite to render.
	 * @param xOffset The x offset on the screen.
	 * @param yOffset They y offset on the screen.
	 */
	private void renderSprite(Sprite sprite, Vector3 position) {
		float xOffsetF = position.getX();
		float yOffsetF = position.getY();
		if (mCamera != null) {
			xOffsetF += mCamera.transform.getPosition().getX();
			yOffsetF += mCamera.transform.getPosition().getY();
		}
		int xOffset = (int) xOffsetF;
		int yOffset = (int) yOffsetF;
		
		int spriteWidth = (int) sprite.getRect().getWidth();
		int spriteHeight = (int) sprite.getRect().getHeight();
		int[] spritePixels = sprite.getPixels();
		
		for (int x = 0; x < spriteWidth; x++) {
			for (int y = 0; y < spriteHeight; y++) {
				if (((int) (x + xOffset) >= 0) && 
					((int) (y + yOffset) >= 0) && 
					((int) (x + xOffset) < mScreen.mWidth) && 
					((int) (y + yOffset) < mScreen.mHeight) &&
					((int) (xOffset + x) + (int) ((y + yOffset) * mScreen.mWidth) < mScreen.backBuffer.mPixels.length)
					) {
					mScreen.backBuffer.mPixels[(int) (xOffset + x) + (int) ((y + yOffset) * mScreen.mWidth)] = 
						spritePixels[x + (y * spriteWidth)];
				}
			}
		}
	}
}
