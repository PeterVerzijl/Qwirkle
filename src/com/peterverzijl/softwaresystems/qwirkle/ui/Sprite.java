package com.peterverzijl.softwaresystems.qwirkle.ui;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Rect;

/**
 * Marks a rectangle of pixels on a bitmap which could contain more then one sprite.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Sprite {
	
	private Bitmap mSource;
	private Rect mRect;
	
	/**
	 * Creates a new sprite on a given bitmap with a rectangle.
	 * @param source The source bitmap that the sprite is positioned on.
	 * @param position The rectangle on the source that the sprite represents.
	 */
	public Sprite(Bitmap source, Rect rect) {
		mSource = source;
		mRect = rect;
	}
	
	/**
	 * Returns all the pixels of the bitmap area.
	 * @return All the pixels of the bitmap area.
	 */
	public int[] getPixels() {
		return mSource.getRegionPixels(mRect);
	}
	
	/**
	 * Returns the sprite rectangle.
	 * @return Returns the sprite rectangle.
	 */
	public Rect getRect() {
		return mRect;
	}
}
