package com.peterverzijl.softwaresystems.qwirkle.ui;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The reference to an array of mPixels. Represents an image asset.
 * @author Peter Verzijl
 * @version 1.0b
 */
public class Bitmap {
	
	public int mWidth;
	public int mHeight;
	public int[] mPixels;

	/**
	 * Constructs an empty bitmap with a particular mWidth and mHeight.
	 * @param mWidth The mWidth of the bitmap in mPixels.
	 * @param mHeight The mHeight of the bitmap in mPixels.
	 */
	public Bitmap(int width, int height) {
		this.mWidth = width;
		this.mHeight = height;
		mPixels = new int[width * height];
	}
	
	/**
	 * Draws the bitmap on a position on the screen.
	 * @param bitmap The bitmap to draw.
	 * @param xOffs The x offset on the screen.
	 * @param yOffs The y offset on the screen.
	 */
	public void draw(Bitmap bitmap, int xOffs, int yOffs) {
		for (int y = 0; y < bitmap.mHeight; y++) {
			int yPix = y + yOffs;
			if (yPix < 0 || yPix >= mHeight)
				continue;

			for (int x = 0; x < bitmap.mWidth; x++) {
				int xPix = x + xOffs;
				if (xPix < 0 || xPix >= mWidth)
					continue;

				int src = bitmap.mPixels[x + y * bitmap.mWidth];
				if (src > 0 || src < 0)
					mPixels[xPix + yPix * mWidth] = src;
			}
		}
	}
	
	/**
	 * Loads the mPixels of a bitmap image at the given path.
	 * @param path The path to the image.
	 */
	public void load(String path) {
		// Get the image from io
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Get the pixel data
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		mWidth = img.getWidth();
		mHeight = img.getHeight();
		
		// Conversion to int
		int index = 0;
		int[] result = new int[mWidth * mHeight];
		if (img.getAlphaRaster() != null) {
			int pixelLength = 4;
			for (int pixel = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += (((int)pixels[pixel] & 0xff) << 24);		// alpha
				argb += (((int)pixels[pixel + 1] & 0xff));			// b
				argb += (((int)pixels[pixel + 2] & 0xff) << 8);		// g
				argb += (((int)pixels[pixel + 3] & 0xff) << 16);	// r
				result[index++] = argb;
			}
		} else {
			int pixelLength = 3;
			for (int pixel = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += -16777216;									// 255 alpha
				argb += (((int)pixels[pixel] & 0xff));				// b
				argb += (((int)pixels[pixel + 1] & 0xff) << 8);		// g
				argb += (((int)pixels[pixel + 2] & 0xff) << 16);	// r
				result[index++] = argb;
			}
		}
		
		// Store result
		mPixels = result;
	}
}
