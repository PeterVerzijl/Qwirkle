package com.peterverzijl.softwaresystems.qwirkle.scripts;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.EngineBehaviour;

public class MoveOnMouse extends EngineBehaviour {
	
	@Override
	public void OnMouseEnter() {
		System.out.println("Mouse is on stone! With object: " + mGameObject.name);
	}
}
