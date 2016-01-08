package com.peterverzijl.softwaresystems.qwirkle.collision;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Component;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;

/**
 * A collider that can collide with other colliders.
 * Gets updated by the physics engine.
 * @author Peter Verzijl
 * @version 1.0a
 */
public abstract class Collider extends Component {
	
	protected float mWidth;
	protected float mHeight;
	protected Vector2 mCenter;
		
	/**
	 * Sets the bounds of the collider.
	 * @param width The width collider.
	 * @param width The height collider.
	 */
	public void setBounds(float width, float height) {
		mWidth = width;
		mHeight = height;
	}
	
	/**
	 * Sets the bounds of the collider.
	 * @param bounds The width and height of the collider as x and y.
	 */
	public void setBounds(Vector2 bounds) {
		setBounds(bounds.getX(), bounds.getY());
	}
	
	
	/**
	 * Sets the center of the collider.
	 * @param center The center point of the collider.
	 */
	public void setCenter(Vector2 center) {
		mCenter = center;
	}
	
	/**
	 * Sets the center of the collider.
	 * @param x The x coordinate of the center.
	 * @param y The y coordinate of the center.
	 */
	public void setCenter(float x, float y) {
		setCenter(new Vector2(x, y));
	}
		
	/**
	 * Called when the mouse enters the collider.
	 */
	public abstract void OnMouseEnter();
	
	/**
	 * Called when the mouse exits the colllider.
	 */
	public abstract void OnMouseLeave();
	
	/**
	 * Called when a collider enters the collider.
	 * @param other
	 */
	public abstract void OnCollisionEnter(Collider other);
	
	/**
	 * Called when a collider exits the collider.
	 * @param other
	 */
	public abstract void OnCollisionExit(Collider other);
}
