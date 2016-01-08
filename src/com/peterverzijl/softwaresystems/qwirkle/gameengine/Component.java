package com.peterverzijl.softwaresystems.qwirkle.gameengine;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic component class that can be added to a  
 * @author Peter Verzijl
 * @version 1.0a
 */
public abstract class Component extends EngineObject {
	
	public String tag;
	
	protected GameObject mGameObject;
	
	public Component() {
		super("New Component");
	}
	
	/**
	 * Sets the game object of the component.
	 * @param aGameObject The game object this component belongs to.
	 */
	public void setGameObject(GameObject aGameObject) {
		mGameObject = aGameObject;
		Start();
	}	
	
	/**
	 * Returns the mGameObject that this component belongs to.
	 * @return The belonging GameObject.
	 */
	public GameObject getGameObject() {
		return mGameObject;
	}
	
	/**
	 * Called on setting the game object of the component.
	 */
	public abstract void Start();
	
	/**
	 * Called every frame.
	 */
	public abstract void Update();
		
	/**
	 * Returns a component of type T.
	 * Returns the first component found.
	 * @param T The type of component to get.
	 * @return The component.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T>  type) {
		for (Component c : mGameObject.components) {
			if (c.getClass().isInstance(type)) {
				return (T) c;
			}
		}
		return null;
	}
	
	/**
	 * Returns an array of all components of type T.
	 * Returns the first component found.
	 * @param T The type of component to get.
	 * @return The component.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T[] getComponents(Class<T> type) {
		List<T> list = new ArrayList<T>();
		for (Component c : mGameObject.components) {
			if (c.getClass().isInstance(type)) {
				list.add((T) c);
			}
		}
		return (T[])list.toArray();
	}
	
	/**
	 * Returns a component of type T when searching in its children. 
	 * Returns the first component found.
	 * @param T The type of component to get.
	 * @return The component.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponentInChildren(Class<T> type) {
		for (GameObject child : mGameObject.children) {
			for (Component c : child.components) {
				if (c.getClass().isInstance(type)) {
					return (T) c;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns all components of type T when searching in all its children. 
	 * Returns the first component found.
	 * @param T The type of component to get.
	 * @return The component.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T[] getComponentsInChildren(Class<T> type) {
		List<T> list = new ArrayList<T>();
		for (GameObject child : mGameObject.children) {
			for (Component c : child.components) {
				if (c.getClass().isInstance(type)) {
					list.add((T) c);
				}
			}
		}
		return (T[])list.toArray();
	}
	
	/**
	 * Returns a component of type T when searching in the parent. 
	 * Returns the first component found.
	 * @param T The type of component to get.
	 * @return The component.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponentInParent(Class<T> type) {
		for (Component c : mGameObject.parent.components) {
			if (c.getClass().isInstance(type)) {
				return (T) c;
			}
		}
		return null;
	}
	
	/**
	 * Returns all components of type T when searching in all its parents. 
	 * Returns all components found.
	 * @param T The type of component to get.
	 * @return A list of all found components.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T[] getComponentsInParent(Class<T> type) {
		List<T> list = new ArrayList<T>();
		for (Component c : mGameObject.parent.components) {
			if (c.getClass().isInstance(type)) {
				list.add((T) c);
			}
		}
		return (T[])list.toArray();
	}
}
