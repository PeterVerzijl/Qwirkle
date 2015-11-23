package com.peterverzijl.softwaresystems.qwirkle.ui;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Rect;

/**
 * An effective area on a bitmap that is displayed on the screen.
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
	
	
}
