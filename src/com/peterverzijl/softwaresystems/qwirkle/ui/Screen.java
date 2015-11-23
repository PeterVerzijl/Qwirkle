package com.peterverzijl.softwaresystems.qwirkle.ui;

import java.util.Random;

import com.peterverzijl.softwaresystems.qwirkle.Game;

/**
 * Screen class is a bitmap screen buffer that gets drawn with canvas.
 * @author Peter Verzijl
 *
 */
public class Screen extends Bitmap {
	
	/**
	 * The test bitmap to be drawn to the screen
	 */
	private Bitmap testBitmap;

	/**
	 * Create a new screen with a certain mWidth and mHeight
	 * @param mWidth Width in mPixels of the screen
	 * @param mHeight Height in mPixels of the screen
	 */
	public Screen(int width, int height) {
		super(width, height);

		Random random = new Random();
		testBitmap = new Bitmap(64, 64);
		for (int i = 0; i < 64 * 64; i++)
			testBitmap.mPixels[i] = random.nextInt() * random.nextInt(2);
	}
	
	/**
	 * Renders the game to the canvas
	 * @param game Game to render
	 */
	public void render(Game game) {
		int xo = (int) (Math.sin(System.currentTimeMillis() % 2000.0 / 2000.0 * Math.PI * 2) * 100);
		int yo = (int) (Math.cos(System.currentTimeMillis() % 2000.0 / 2000.0 * Math.PI * 2) * 50);
		testBitmap.load("./dat/Test.png");
		draw(testBitmap, (mWidth - testBitmap.mWidth) / 2 + xo, (mHeight - testBitmap.mHeight) / 2 + yo);
	}
}
