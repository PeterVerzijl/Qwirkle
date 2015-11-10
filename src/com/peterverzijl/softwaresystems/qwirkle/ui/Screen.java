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
	 * Create a new screen with a certain width and height
	 * @param width Width in pixels of the screen
	 * @param height Height in pixels of the screen
	 */
	public Screen(int width, int height) {
		super(width, height);

		Random random = new Random();
		testBitmap = new Bitmap(64, 64);
		for (int i = 0; i < 64 * 64; i++)
			testBitmap.pixels[i] = random.nextInt() * random.nextInt(2);
	}
	
	/**
	 * Renders the game to the canvas
	 * @param game Game to render
	 */
	public void render(Game game) {
		int xo = (int) (Math.sin(System.currentTimeMillis() % 2000.0 / 2000.0 * Math.PI * 2) * 100);
		int yo = (int) (Math.cos(System.currentTimeMillis() % 2000.0 / 2000.0 * Math.PI * 2) * 50);
		draw(testBitmap, (width - testBitmap.width) / 2 + xo, (height - testBitmap.height) / 2 + yo);
	}
}
