package com.peterverzijl.softwaresystems.qwirkle.rendering;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Component;
import com.peterverzijl.softwaresystems.qwirkle.ui.Sprite;

/**
 * An interface that indicates that the class is renderable
 * @author Peter Verzijl
 * @version 1.0a
 */
public abstract class RenderComponent extends Component {
	
	/**
	 * Get the sprite that gets rendered.
	 * @return The sprite to render.
	 */
	public abstract Sprite getSprite();
}
