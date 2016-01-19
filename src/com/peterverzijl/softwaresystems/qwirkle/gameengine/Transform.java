package com.peterverzijl.softwaresystems.qwirkle.gameengine;

import com.peterverzijl.softwaresystems.qwirkle.collision.Collider;

public class Transform extends Component {
	
	// TODO(peter): Implement these!
	// public Vector2 up;
	// public Vector2 right;
	// public float angle;
	
	private Vector2 mPosition = Vector2.ZERO();
	
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
		mPosition.set(newPosition);
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
	public void setPosition(float x, float y) {
		setPosition(new Vector2(x, y));	
	}

	@Override
	public void Start() { }

	@Override
	public void Update() { }

// -------- These functions are not used by the transform.
	@Override
	public void OnMouseEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMouseLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnCollisionEnter(Collider other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnCollisionExit(Collider other) {
		// TODO Auto-generated method stub
		
	}
}
