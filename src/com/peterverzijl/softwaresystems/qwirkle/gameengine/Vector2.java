package com.peterverzijl.softwaresystems.qwirkle.gameengine;

/**
 * A two dimensional vector point in space. Contains an x and a y floating point value.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Vector2 {
	
	private float x;
	private float y;
	
	public static final Vector2 zero = new Vector2(0, 0);
	public static final Vector2 unity = new Vector2(1, 1);
	public static final Vector2 right = new Vector2(1, 0);
	public static final Vector2 up = new Vector2(0, 1);
	
	/**
	 * The constructor for a floating point two dimensional vector.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Vector2(float x , float y) {
		this.setX(x);
		this.setY(y);
	}
	
	/**
	 * Creates a copy of the given vector.
	 * @param vector The vector to create a copy of.
	 */
	public Vector2(Vector2 vector) {
		assert vector != null : "Error: You cannot create a Vector2 that is null!";
		
		this.setX(vector.getX());
		this.setY(vector.getY());
	}
	
	/**
	 * Returns the magnitude or length of the vector.
	 * @return The magniude of the vector.
	 */
	public float magnitude() {
		
		// TODO(Peter): Calculate the magnitude of the vector.
		return 0;
	}
	
	/**
	 * Sets the vector to a given length
	 * @param length The length of the vector
	 */
	public void setMagnitude(float length) {
		assert length > 0 : "Error: The length of a vector cannot be negative!";
		// TODO(Peter): Sets the magnitude of the vector.
		// TODO(Peter): Maybe take the absolute value of the length and generate an error?
	}
	
	/**
	 * Normalizes the vector.
	 */
	public void normalize() {
		// TODO(Peter): Something is super wrong here...
		float ax = Math.abs(x);
		float ay = Math.abs(y);
		// Ratio
		float ratio = 1 / Math.max(ax, ay);
		ratio = ratio * (1.29289f - (ax + ay) * ratio * 0.29289f);
		// Multiply by ratio
		x = x * ratio;
		y = y * ratio;
	}	
	
	/**
	 * Returns a normalized copy of the vector.
	 * @return The normalized copy.
	 */
	public Vector2 normalized() {
		Vector2 result = this.clone();
		result.normalize();
		return result;
	}
	
	/**
	 * Returns a copy of the current vector.
	 * @return A copy of the current vector.
	 */
	@Override
	public Vector2 clone() {
		return new Vector2(x, y);
	}
	
	/**
	 * Returns the x coordinate of the vector.
	 * @return The y coordinate
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the x coordinate of the vector.
	 * @param x
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the y coordinate of the vector.
	 * @return The y coordinate
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Sets the y coordinate of the vector.
	 * @param y
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	public String toString() {
		return "(" + getX() + ", " + getY() + " )";
	}
}
