package com.peterverzijl.softwaresystems.qwirkle.gameengine;

import com.peterverzijl.softwaresystems.qwirkle.math.Mathf;

/**
 * A two dimensional vector point in space. Contains an x and a y floating point value.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Vector3 {
	
	private float x;
	private float y;
	private float z;
	
	public static Vector3 ZERO() 	{ return new Vector3(0.0f, 0.0f, 0.0f); }
	public static Vector3 UNITY() 	{ return new Vector3(1.0f, 1.0f, 1.0f); }
	public static Vector3 RIGHT() 	{ return new Vector3(1.0f, 0.0f, 0.0f); }
	public static Vector3 UP() 		{ return new Vector3(0.0f, 1.0f, 0.0f); }
	public static Vector3 FORWARD() { return new Vector3(0.0f, 0.0f, 1.0f); }
	
	/**
	 * The constructor for a floating point two dimensional vector.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Vector3(float x, float y, float z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	
	/**
	 * Creates a copy of the given vector.
	 * @param vector The vector to create a copy of.
	 */
	public Vector3(Vector3 vector) {
		assert vector != null : "Error: You cannot create a Vector3 that is null!";
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
	}
	
	/**
	 * Creates a copy of the given Vector2.
	 * @param vector The Vector2 to create a copy of.
	 */
	public Vector3(Vector2 vector) {
		assert vector != null : "Error: You cannot create a Vector2 that is null!";
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = 0;
	}
	
	/**
	 * Sets the position of the vector.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Sets the position of the vector by another vector.
	 * @param vector The vector to copy the compontents from.
	 */
	public void set(Vector3 vector) {
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
	}
	
	/**
	 * Returns the magnitude or length of the vector.
	 * @return The magniude of the vector.
	 */
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y + z * z);
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
		z = z / magnitude;
	}	
	
	/**
	 * Returns a normalized copy of the vector.
	 * @return The normalized copy.
	 */
	public Vector3 normalized() {
		Vector3 result = this.clone();
		result.normalize();
		return result;
	}
	
	/**
	 * Returns a copy of the current vector.
	 * @return A copy of the current vector.
	 */
	@Override
	public Vector3 clone() {
		return new Vector3(x, y, z);
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
	 * Returns the y coordinate of the vector.
	 * @return The y coordinate
	 */
	public float getZ() {
		return z;
	}
	
	/**
	 * Sets the y coordinate of the vector.
	 * @param y
	 */
	public void setZ(float z) {
		this.z = z;
	}
	
	/**
	 * Prints the vector in the following format (x, y).
	 */
	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
	}
	
	/**
	 * Adds one vector to the calling vector.
	 * @param vector The vector to add.
	 * @return this.
	 */
	public Vector3 add(Vector3 vector) {
		x += vector.getX();
		y += vector.getY();
		z += vector.getZ();
		return this;
	}

	/**
	 * Adds the two given components to the vector.
	 * @param x Amount of x to add.
	 * @param y Amount of y to add.
	 * @return this.
	 */
	public Vector3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	/**
	 * Subtracts one vector to the calling vector.
	 * @param vector The vector to subtract.
	 * @return this.
	 */
	public Vector3 subtract(Vector3 vector) {
		x -= vector.getX();
		y -= vector.getY();
		z -= vector.getZ();
		return this;
	}

	/**
	 * Subtracts the two given components to the vector.
	 * @param x Amount of x to subtract.
	 * @param y Amount of y to subtract.
	 * @param z Amount of z to subtract.
	 * @return this.
	 */
	public Vector3 subtract(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	
	/**
	 * Multiplies the vector by a scalar.
	 * @param scalar The scalar to multiply the vector by.
	 * @param Returns the resulting vector.
	 * @return this.
	 */
	public Vector3 multiply(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}
	
	/**
	 * Returns the dot product of the two vectors.
	 * Following the formula: a · b = ax × bx + ay × by + az × bz
	 * @param v1 The first Vector3
	 * @param v2 The second Vector3
	 * @return The resulting length of the dot product.
	 */
	public static float dot(Vector3 v1, Vector3 v2) {
		return v1.x * v2.x + v1.y * v2.y  + v1.z * v2.z;
	}
	
	/**
	 * Calculates and returns the cross product of the two vectors.
	 * Following the formula: 
	 * 	cx = aybz − azby
	 * 	cy = azbx − axbz
	 * 	cz = axby − aybx
	 * @param v1 The first vector.
	 * @param v2 The second vector.
	 * @return The resulting Vector3 of the cross product.
	 */
	public static Vector3 cross(Vector3 v1, Vector3 v2) {
		float x = v1.y * v2.z - v1.z * v2.y;
		float y = v1.z * v2.x - v1.x * v2.z;
		float z = v1.x * v2.y - v1.y * v2.x;
		return new Vector3(x, y, z);
	}
	
	/**
	 * Interpolates linearly between two points.
	 * Uses a precise method which guarantees v = v1 at t = 1.
	 * @param v1 Vector a
	 * @param v2 Vector b
	 * @param t The fraction between the two vectors.
	 * @return The linearly interpolated vector between v1 and v2 at fraction t.
	 */
	public static Vector3 lerp(Vector3 v1, Vector3 v2, float t) {
		float x = (1.0f - t) * v1.getX() + t * v2.getX();
		float y = (1.0f - t) * v1.getY() + t * v2.getY();
		float z = (1.0f - t) * v1.getZ() + t * v2.getZ();
		return new Vector3(x, y, z);
	}
	
	/**
	 * Returns a spherically interpolated vector at point t.
	 * @param v1 The starting vector.
	 * @param v2 The ending vector.
	 * @param t The fraction between 1 and 0.
	 * @return The resulting spherically interpolated vector.
	 */
	public static Vector3 slerp(Vector3 v1, Vector3 v2, float t) {
		// Make a copy of the references
		Vector3 a = v1.clone();
		Vector3 b = v2.clone();
		
		// Get the cosine of the angle between v1 and v2
		float dot = Vector3.dot(a, b);
		// Clap the range
		dot = Mathf.clamp(dot, -1.0f, 1.0f);
		// Acos(dot) returns the angle between the two vectors
		float theta = (float) (Math.acos(dot) * t);
		Vector3 relativeVector = b.subtract(a.multiply(dot));
		relativeVector.normalize();
		return ((a.multiply((float) Math.cos(theta))).
				add(relativeVector.multiply(
						(float) Math.sin(theta))));
	}
	
	/**
	 * Checks if two vectors are equal to another.
	 * @param vector The vector to check if equal.
	 * @return Weighter the two vectors are equal.
	 */
	public boolean equals(Vector3 vector) {
		boolean result = false;
		if (x == vector.getX() &&
			y == vector.getY() &&
			z == vector.getZ()) {
			result = true;
		}
		return result;
	}
	
	/**
	 * Converts the Vector3 to a Vector2
	 * @return The new Vector2.
	 */
	public Vector2 toVector2() {
		return new Vector2(x, y);
	}
}
