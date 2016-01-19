package com.peterverzijl.softwaresystems.qwirkle.graphics;

import com.peterverzijl.softwaresystems.qwirkle.collision.Collider;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameEngineComponent;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.ui.Sprite;

/**
 * A class that contains the sprite that is due for rendering.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class SpriteRenderer extends RenderComponent {
	
	/**
	 * The sprite that gets rendered
	 */
	private Sprite mSprite;
	
	/**
	 * Sets the sprite for the rendering.
	 * @param sprite
	 */
	public void setSprite(Sprite sprite) {
		mSprite = sprite;
		GameEngineComponent.renderers.add(this);
	}
	
	@Override
	public Sprite getSprite() {
		return mSprite;
	}

	@Override
	public void Start() { }

	@Override
	public void Update() { }

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
