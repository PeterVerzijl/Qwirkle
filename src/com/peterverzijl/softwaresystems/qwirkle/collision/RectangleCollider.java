package com.peterverzijl.softwaresystems.qwirkle.collision;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameEngineComponent;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Transform;
import com.peterverzijl.softwaresystems.qwirkle.graphics.SpriteRenderer;

public class RectangleCollider extends Collider {

	@Override
	public void Start() {
		// Try to get the sprite renderer's dimensions
		Transform t = mGameObject.getComponent(Transform.class);
		mCenter = t.getPosition();
		SpriteRenderer sp = mGameObject.getComponent(SpriteRenderer.class);
		if (sp.getSprite() != null) {
			mWidth = sp.getSprite().getRect().getWidth();
			mHeight = sp.getSprite().getRect().getWidth();
		}
		GameEngineComponent.colliders.add(this);
	}
	
	@Override
	public void Update() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void OnMouseEnter() {
		// TODO Auto-generated method stub
		System.out.println("Mouse enters!");
	}

	@Override
	public void OnMouseLeave() {
		// TODO Auto-generated method stub
		System.out.println("Mouse leaves!");
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
