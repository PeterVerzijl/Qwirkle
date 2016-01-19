package com.peterverzijl.softwaresystems.qwirkle.gameengine;

import java.util.ArrayList;
import java.util.List;


public class GameObject extends EngineObject {
	
	/**
	 * The tag identifier that this gameObject belongs to.
	 */
	public String tag;
	
	public GameObject() {
		super("New GameObject");
	}
	
	public GameObject(String name) {
		super(name);
	}
	
	GameObject parent;
	List<GameObject> children = new ArrayList<GameObject>();
	List<Component> components = new ArrayList<Component>();
	
	/**
	 * Adds a new component to the game object. 
	 * @param type The type of component to add.
	 */
	public <T extends Component> T addComponent(Class<T> type) {
		T newComponent = Instantiate(type);
		newComponent.setGameObject(this);
		newComponent.tag = this.tag;
		components.add(newComponent);
		return newComponent;
	}
	
	/**
	 * Returns the first component of the given type.
	 * @param type The type of component to return.
	 * @return The first component of that type found. Else returns null.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> type) {
		for (Component comp : components) {
			if (comp.getClass() == type) {
				return (T) comp;
			}
		}
		return null;
	}
	
	/**
	 * Returns all the components of this game object.
	 * @return All the components.
	 */
	public Component[] getComponents() {
		return components.toArray(new Component[components.size()]);
	}
}
