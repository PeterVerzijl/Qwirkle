package com.peterverzijl.softwaresystems.qwirkle.graphics;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameEngineComponent;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameObject;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Transform;

/**
 * The offset of the drawing.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Camera extends GameObject {
	
	public Transform transform;
	
	public Camera() { 
		transform = addComponent(Transform.class);
		GameEngineComponent.setCamera(this);
	}
	/**
	 * Returns the screen height.
	 * @return The screen height.
	 */
	public static int getHeight() {
		return GameEngineComponent.HEIGHT;
	}
	/**
	 * Returns the screen width.
	 * @return The screen width.
	 */
	public static int getWidth() {
		return GameEngineComponent.WIDTH;
	}
}
