package com.peterverzijl.softwaresystems.qwirkle.collision;

/**
 * A collision between two objects.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Collision {
	
	private Collider mOther;
	private Collider mSelf;
	
	public Collision(Collider self, Collider other) {
		mSelf = self;
		mOther = other;
	}
	
	/**
	 * Gets the collider that is colliding with us.
	 * @return The other collider.
	 */
	public Collider other() {
		return mOther;
	}
}
