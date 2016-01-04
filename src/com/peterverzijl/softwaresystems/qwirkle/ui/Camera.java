package com.peterverzijl.softwaresystems.qwirkle.ui;

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
	}
}
