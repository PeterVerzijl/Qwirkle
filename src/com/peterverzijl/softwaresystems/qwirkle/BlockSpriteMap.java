package com.peterverzijl.softwaresystems.qwirkle;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.ui.Sprite;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Rect;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Bitmap;

public class BlockSpriteMap {
	
	public static Sprite getSprite(Block block) {
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
