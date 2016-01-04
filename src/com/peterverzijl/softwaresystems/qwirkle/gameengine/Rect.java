package com.peterverzijl.softwaresystems.qwirkle.gameengine;

/**
 * Formal defninition of a rectangle that starts at the top left and has a mWidth and a mHeight
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Rect {
	
	private float x;
	private float y;
	private float w;
	private float h;
	
	/**
	 * Constructor for a rectangle.
	 * @param x Top left corner of the rectangle
	 * @param y Top right corner of the rectangle
	 * @param w Width of the rectangle
	 * @param h Height of the rectangle
	 */
	public Rect(float x, float y, float w, float h) {
		this.setX(x);
		this.setY(y);
		this.setWidth(w);
		this.setHeight(h);
	}
	
	/**
	 * Constructs a copy of the given rectangle.
	 * @param rect The rectangle to copy.
	 */
	public Rect(Rect rect) {
		assert rect != null : "Error: You cannot create a Rect that is null!";
		
		this.setX(rect.getX());
		this.setY(rect.getY());
		this.setWidth(rect.getWidth());
		this.setHeight(rect.getHeight());
	}
	
	/**
	 * Creates a rectangle with corner as its top left corner and size as its mWidth and mHeight.
	 * @param corner The top left corner.
	 * @param size The mWidth and mHeight of the rectangle.
	 */
	public Rect(Vector2 corner, Vector2 size) {
		assert corner != null : "Error: You cannot create a Vector2 that is null!";
		assert size != null : "Error: You cannot create a Vector2 that is null!";
		
		this.setX(corner.getX());
		this.setY(corner.getY());
		this.setWidth(size.getX());
		this.setHeight(size.getY());
	}
	
	/**
	 * Returns the center of the rectangle.
	 * @return The center
	 */
	public Vector2 center() {
		return new Vector2(x + w * 0.5f, y + h * 0.5f);
	}

	/**
	 * Gets the x position of the top left corner
	 * @return x coordinate of the top left corner
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the x value of the top left corner
	 * @param x The x coordinate of the top left corner
	 */
	public void setX(float x) {
		this.x = x;
	}
	
	/**
	 * Gets the y position of the top left corner
	 * @return Y The y coordinate of the top left corner
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Sets the y value of the top left position
	 * @param y The y coordinate of the top left corner
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * Gets the mWidth of the rectangle
	 * @return The mWidth
	 */
	public float getWidth() {
		return w;
	}

	/**
	 * Sets the mWidth of the rectangle.
	 * @param mWidth
	 */
	public void setWidth(float width) {
		this.w = width;
	}
	
	/**
	 * Gets the mHeight of the rectangle.
	 * @return The mHeight
	 */
	public float getHeight() {
		return h;
	}
	
	/**
	 * Sets the mHeight of the rectangle
	 * @param mHeight
	 */
	public void setHeight(float height) {
		this.h = height;
	}
		
}
