package com.peterverzijl.softwaresystems.qwirkle.rendering;

import com.peterverzijl.softwaresystems.qwirkle.ui.Bitmap;
import com.peterverzijl.softwaresystems.qwirkle.ui.Screen;
import com.peterverzijl.softwaresystems.qwirkle.ui.Sprite;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.Block;
import com.peterverzijl.softwaresystems.qwirkle.Block.Color;
import com.peterverzijl.softwaresystems.qwirkle.Block.Shape;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameEngineComponent;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Input;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Rect;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Transform;

/**
 * Renders the screen in the game. Responsible for rendering the tiles and the 
 * @author Peter Verzijl
 *
 */
public class Renderer {
	
	private Screen mScreen;
	
	HashMap<Block, Sprite> spriteMap = new HashMap<Block, Sprite>();
	
	public Renderer(Screen screen) {
		mScreen = screen;
	}
	
	public void render() {
		mScreen.clear();
		
		// Convert list to array to create a copy we can loop trough. In this case, 
		// if any other thread tries to modify while looping, we will get that modification next frame.
		RenderComponent[] renderers = GameEngineComponent.renderers.toArray(new RenderComponent[GameEngineComponent.renderers.size()]);
		for (RenderComponent renderer : renderers) {
			Transform t = renderer.gameObject.getComponent(Transform.class);
			Sprite sprite = renderer.getSprite();
			if (sprite != null && t != null) {
				renderSprite(sprite, t.getPosition().getX(), t.getPosition().getY());
			}
		}		
	}
	
	/**
	 * Renders a sprite on the back buffer.
	 * @param sprite The sprite to render.
	 * @param xOffset The x offset on the screen.
	 * @param yOffset They y offset on the screen.
	 */
	private void renderSprite(Sprite sprite, float xOffset, float yOffset) {
		int spriteWidth = (int) sprite.getRect().getWidth();
		int spriteHeight = (int) sprite.getRect().getHeight();
		int[] spritePixels = sprite.getPixels();
		
		for (int x = 0; x < spriteWidth; x++) {
			for (int y = 0; y < spriteHeight; y++) {
				if (((int) (x + xOffset) >= 0) && 
					((int) (y + yOffset) >= 0) && 
					((int)(x + xOffset) < mScreen.mWidth) && 
					((int) (y + yOffset) < mScreen.mHeight) &&
					((int) (xOffset + x) + (int) ((y + yOffset) * mScreen.mWidth) < mScreen.backBuffer.mPixels.length)
					) {
					mScreen.backBuffer.mPixels[(int) (xOffset + x) + (int) ((y + yOffset) * mScreen.mWidth)] = 
						spritePixels[x + (y * spriteWidth)];
				}
			}
		}
	}
	
	public static Sprite getSpriteFromBlock(Block block) {
		Sprite result;
		
		int spriteSize = 16;
		int colorXOffset = 0;
		int colorYOffset = 0;
		
		switch (block.getColor()) {
			case RED:
				colorXOffset = 0;
				colorYOffset = 0;
				break;
			case ORANGE:
				colorXOffset = spriteSize * 3;
				colorYOffset = 0;
				break;
			case YELLOW:
				colorXOffset = 0;
				colorYOffset = spriteSize * 2;
				break;
			case GREEN:
				colorXOffset = spriteSize * 3;
				colorYOffset = spriteSize * 2;
				break;
			case BLUE:
				colorXOffset = 0;
				colorYOffset = spriteSize * 2 * 2;
				break;
			case PURPLE:
				colorXOffset = spriteSize * 3;
				colorYOffset = spriteSize * 2 * 2;
				break;			
		}
		
		int shapeXOffset = 0;
		int shapeYOffset = 0;
		switch (block.getShape()) {
			case PLUS:
				shapeXOffset = 0;
				shapeYOffset = 0;
				break;
			case CROSS:
				shapeXOffset = spriteSize;
				shapeYOffset = 0;
				break;
			case STAR:
				shapeXOffset = spriteSize * 2;
				shapeYOffset = 0;
				break;
			case CIRCLE:
				shapeXOffset = 0;
				shapeYOffset = spriteSize;
				break;
			case DIAMOND:
				shapeXOffset = spriteSize;
				shapeYOffset = spriteSize;
				break;
			case SQUARE:
				shapeXOffset = spriteSize * 2;
				shapeYOffset = spriteSize;
				break;
		}
		result = new Sprite(Bitmap.load("qwirkle-tiles.png"), 
				new Rect(colorXOffset + shapeXOffset, 
						colorYOffset + shapeYOffset, 
						spriteSize, 
						spriteSize));
		return result;
	}
}
