package com.peterverzijl.softwaresystems.qwirkle.gameengine;

/**
 * A two dimensional vector point in space. Contains an x and a y floating point value.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Vector2 {
	
	private float x;
	private float y;
	
	public static final Vector2 ZERO = new Vector2(0, 0);
	public static final Vector2 UNITY = new Vector2(1, 1);
	public static final Vector2 RIGHT = new Vector2(1, 0);
	public static final Vector2 UP = new Vector2(0, 1);
	
	/**
	 * The constructor for a floating point two dimensional vector.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Vector2(float x, float y) {
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
		return (float) Math.sqrt(x * x + y * y);
	}
	
	/**
	 * Sets the vector to a given length.
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
		// Multiply by ratio
		float magnitude = magnitude();
		x = x / magnitude;
		y = y / magnitude;
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
	
	/**
	 * Prints the vector in the following format (x, y).
	 */
	public String toString() {
		return "(" + getX() + ", " + getY() + " )";
	}
	
	/**
	 * Adds one vector to the calling vector.
	 * @param vector The vector to add.
	 */
	public void add(Vector2 vector) {
		x += vector.getX();
		y += vector.getY();
	}

	/**
	 * Adds the two given components to the vector.
	 * @param x Amount of x to add.
	 * @param y Amount of y to add.
	 */
	public void add(float x, float y) {
		this.x += x;
		this.y += y;
	}
}
