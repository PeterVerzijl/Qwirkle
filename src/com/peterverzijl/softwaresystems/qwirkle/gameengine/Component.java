package com.peterverzijl.softwaresystems.qwirkle.gameengine;

import java.util.ArrayList;
import java.util.List;

public abstract class Component extends Object {
	
	public GameObject gameObject;
	public String tag = gameObject.tag;
	
	public Component(GameObject go) {
		gameObject = go;
	}
		
	/**
	 * Returns a component of type T.
	 * Returns the first component found.
	 * @param T The type of component to get.
	 * @return The component.
	 */
	public <T extends Component> T getComponent(Class<T>  type) {
		for (Component c : gameObject.components) {
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
	public <T extends Component> T[] getComponents(Class<T> type) {
		List<T> list = new ArrayList<T>();
		for (Component c : gameObject.components) {
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
	public <T extends Component> T getComponentInChildren(Class<T> type) {
		for (GameObject child : gameObject.children) {
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
	public <T extends Component> T[] getComponentsInChildren(Class<T> type) {
		List<T> list = new ArrayList<T>();
		for (GameObject child : gameObject.children) {
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
	public <T extends Component> T getComponentInParent(Class<T> type) {
		for (Component c : gameObject.parent.components) {
			if (c.getClass().isInstance(type)) {
				return (T) c;
			}
		}
		return null;
	}
	
	public <T extends Component> T[] getComponentsInParent(Class<T> type) {
		List<T> list = new ArrayList<T>();
		for (Component c : gameObject.parent.components) {
			if (c.getClass().isInstance(type)) {
				list.add((T) c);
			}
		}
		return (T[])list.toArray();
	}
}
