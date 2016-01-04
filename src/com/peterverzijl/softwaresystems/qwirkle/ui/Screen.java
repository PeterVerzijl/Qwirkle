package com.peterverzijl.softwaresystems.qwirkle.ui;

import com.peterverzijl.softwaresystems.qwirkle.Game;
import com.peterverzijl.softwaresystems.qwirkle.rendering.Renderer;

/**
 * Screen class is a bitmap screen buffer that gets drawn with canvas.
 * @author Peter Verzijl
 *
 */
public class Screen extends Bitmap {
	
	/**
	 * The test bitmap to be drawn to the screen.
	 */
	public Bitmap backBuffer;
	Renderer mRenderer;

	/**
	 * Create a new screen with a certain mWidth and mHeight.
	 * @param mWidth Width in mPixels of the screen
	 * @param mHeight Height in mPixels of the screen
	 */
	public Screen(int width, int height) {
		super(width, height);
		
		mRenderer = new Renderer(this);
		
		backBuffer = new Bitmap(width, height);
		for (int i = 0; i < width * height; i++) {
			backBuffer.mPixels[i] = 0;
		}
	}
	
	/**
	 * Renders the game to the canvas.
	 * @param game Game to render
	 */
	public void render(Game game) {
		// Clear screen before rendering
		mRenderer.render();		
		draw(backBuffer, (mWidth - backBuffer.mWidth) / 2, 
							(mHeight - backBuffer.mHeight) / 2);
	}
	
	/**
	 * Clears the screen buffer to black.
	 */
	public void clear() {
		for (int i = 0; i <  mWidth * mHeight; i++) {
			backBuffer.mPixels[i] = 1;
		}
	}
}
