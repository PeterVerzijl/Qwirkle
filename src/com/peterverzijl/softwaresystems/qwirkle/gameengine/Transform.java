package com.peterverzijl.softwaresystems.qwirkle.gameengine;

public class Transform extends Component {
	
	// TODO(peter): Implement these!
	// public Vector2 up;
	// public Vector2 right;
	// public float angle;
	
	private Vector2 mPosition = Vector2.ZERO;
	
	/**
	 * Returns the current position of the transform.
	 * @return The current position.
	 */
	public Vector2 getPosition() {
		return mPosition;
	}
	
	/**
	 * Sets the current position of the transform.
	 * @param newPosition the position to set the transform to.
	 */
	public void setPosition(Vector2 newPosition) {
		mPosition = newPosition;
	}
	
	/**
	 * Translate the transform towards the vector.
	 * @param vector The vector to translate to.
	 */
	public void translate(Vector2 vector) {
		mPosition.add(vector);
	}
	
	/**
	 * Setting the position of the transform by x and y components.
	 * @param x The x offset from the top left corner.
	 * @param y The y offset from the top left corner.
	 */
	public void setPosition(int x, int y) {
		setPosition(new Vector2(x, y));	
	}
}
