package com.peterverzijl.softwaresystems.qwirkle.gameengine;

/**
 * A two dimensional vector point in space. Contains an x and a y floating point value.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Vector2 {
	
	private float x;
	private float y;
	
	public static final Vector2 ZERO() 	{ return new Vector2(0, 0); }
	public static final Vector2 UNITY() { return new Vector2(1, 1); }
	public static final Vector2 RIGHT() { return new Vector2(1, 0); }
	public static final Vector2 UP()	{ return new Vector2(0, 1); }
	
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
	 * Creates a copy of the given vector.
	 * @param vector The vector to create a copy of.
	 */
	public Vector2(Vector3 vector) {
		this.setX(vector.getX());
		this.setY(vector.getY());
	}
	
	/**
	 * Sets the position of the vector.
	 * @param x
	 * @param y
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the position of the old vector to the new one.
	 * @param newPosition The new position.
	 */
	public void set(Vector2 newPosition) {
		this.x = newPosition.getX();
		this.y = newPosition.getY();
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
	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}
	
	/**
	 * Adds one vector to the calling vector.
	 * @param vector The vector to add.
	 * @return this.
	 */
	public Vector2 add(Vector2 vector) {
		x += vector.getX();
		y += vector.getY();
		return this;
	}

	/**
	 * Adds the two given components to the vector.
	 * @param x Amount of x to add.
	 * @param y Amount of y to add.
	 * @return this.
	 */
	public Vector2 add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**
	 * Subtracts one vector to the calling vector.
	 * @param vector The vector to subtract.
	 * @return this.
	 */
	public Vector2 subtract(Vector2 vector) {
		x -= vector.getX();
		y -= vector.getY();
		return this;
	}
	
	/**
	 * Subtracts the two given components to the vector.
	 * @param x Amount of x to subtract.
	 * @param y Amount of y to subtract.
	 * @return this.
	 */
	public Vector2 subtract(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	/**
	 * Multiplies the vector by a scalar.
	 * @param scalar The scalar to multiply the vector by.
	 * @param Returns the resulting vector.
	 * @return this.
	 */
	public Vector2 multiply(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}
	
	/**
	 * Returns the dot product of two vectors.
	 * Following the formula: a · b = ax × bx + ay × by
	 * @param v1 The first Vector3
	 * @param v2 The second Vector3
	 * @return The resulting lenth of the dot product.
	 */
	public static float dot(Vector2 v1, Vector2 v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}
	
	/**
	 * Interpolates linearly between two points.
	 * Uses a precise method which guarantees v = v1 at t = 1.
	 * @param v1 Vector a
	 * @param v2 Vector b
	 * @param t The fraction between the two vectors.
	 * @return The linearly interpolated vector between v1 and v2 at fraction t.
	 */
	public static Vector2 lerp(Vector2 v1, Vector2 v2, float t) {
		float x = (1-t)*v1.getX() + t*v2.getX();
		float y = (1-t)*v1.getY() + t*v2.getY();
		return new Vector2(x, y);
	}
	
	/**
	 * Returns a spherically interpolated vector at point t.
	 * @param v1 The starting vector.
	 * @param v2 The ending vector.
	 * @param t The fraction between 1 and 0.
	 * @return The resulting spherically interpolated vector.
	 */
	public static Vector2 slerp(Vector2 v1, Vector2 v2, float t) {
		return Vector3.slerp(v1.toVector3(), v2.toVector3(), t).toVector2();
	}
	
	/**
	 * Checks if two vectors are equal to another.
	 * @param vector The vector to check if equal.
	 * @return Weighter the two vectors are equal.
	 */
	public boolean equals(Vector2 vector) {
		boolean result = false;
		if (x == vector.getX() &&
			y == vector.getY()) {
			result = true;
		}
		return result;
	}
	
	/**
	 * Converts the Vector3 to a Vector2
	 * @return The new Vector2.
	 */
	public Vector3 toVector3() {
		return new Vector3(x, y, 0);
	}
}
