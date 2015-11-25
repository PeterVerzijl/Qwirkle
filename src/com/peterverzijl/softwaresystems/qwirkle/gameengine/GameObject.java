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
	 * Adds a new component to 
	 * @param type
	 */
	public <T extends Component> void addComponent(T type) {
		components.add(type);
	}
}
