package com.peterverzijl.softwaresystems.qwirkle.collision;

import java.util.ArrayList;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Component;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameEngineComponent;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameObject;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Input;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;

public class PhysicsEngine {

	private static List<Collider> mMouseCollisions = new ArrayList<Collider>();
	
	// private static List<Collision> mCollisions = new ArrayList<Collision>(); 
	
	/**
	 * Updates all collisions.
	 * @param aColliders The colliders to check.
	 */
	public static void update() {
		List<Collider> colliders = GameEngineComponent.colliders;
		updateMouseCollision(colliders);	
	}

	/**
	 * Loops through all colliders to check if they collide with the mouse.
	 * @param aColliders Colliders to check.
	 */
	private static void updateMouseCollision(List<Collider> aColliders) {
		// Make a new temporary list to check later if some have exited collision.
		Collider[] colliders = aColliders.toArray(new Collider[aColliders.size()]);
		for (Collider c : colliders) {
			boolean isColliding = isMouseColliding(c);
			if (isColliding) {
				if (!mMouseCollisions.contains(c)) {
					mMouseCollisions.add(c);
					GameObject go = c.getGameObject();
					Component[] components = go.getComponents();
					for (Component co : components) {
						co.OnMouseEnter();
					}
				}
			} else {
				if (mMouseCollisions.contains(c)) {
					GameObject go = c.getGameObject();
					Component[] components = go.getComponents();
					for (Component co : components) {
						co.OnMouseLeave();
					}
					mMouseCollisions.remove(c);
				}
			}
		}	
	}
	
	/**
	 * Checks if the mouse is in the bounds of the collider.
	 * @param aCollider The collider to check.
	 * @return Weighter the mouse is collinding with the collider.
	 */
	private static boolean isMouseColliding(Collider aCollider) {
		boolean result = false;
		Vector2 mousePos = Input.getMousePosition();
		if (!(mousePos.getX() > aCollider.mCenter.getX() + (aCollider.mWidth * 0.5f) || 
			mousePos.getX() < aCollider.mCenter.getX() - (aCollider.mWidth * 0.5f) ||
			mousePos.getY() > aCollider.mCenter.getY() + (aCollider.mHeight * 0.5f) || 
			mousePos.getY() < aCollider.mCenter.getY() - (aCollider.mHeight * 0.5f))) {
			result = true;
		}
		
		return result;
	}
}
