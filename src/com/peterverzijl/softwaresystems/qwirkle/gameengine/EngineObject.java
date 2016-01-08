package com.peterverzijl.softwaresystems.qwirkle.gameengine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EngineObject {
	
	/**
	 * Name of the object.
	 */
	public String name;
	
	public EngineObject(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the unique ID for this object.
	 * @return The Objects ID.
	 */
	public int getInstaceId() {
		return System.identityHashCode(this);
	}
	
	/**
	 * Returns the class type and the name of the object.
	 * @return The type and name of the object.
	 */
	public String toString() {
		return this.getClass().toString() + " : " + name + " (" + getInstaceId() + ")";
	}
	
	/**
	 * Creates a new instance of the given object.
	 * @param o The object to create a new instance off.
	 * @return The instantiated object.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends EngineObject> T Instantiate(T o) {
		// TODO(Peter): Factory pattern
		try {
			Constructor<? extends EngineObject> con = o.getClass().getConstructor();
			try {
				T instance = (T)con.newInstance();
				GameEngineComponent.objects.add(instance);
				return instance;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns a new instance of 
	 * @param type
	 * @return
	 */
	public static <T extends EngineObject> T Instantiate(Class<T> type) {
		try {
			Constructor<? extends EngineObject> con = type.getConstructor();
			try {
				T instance = (T)con.newInstance();
				GameEngineComponent.objects.add(instance);
				return instance;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				Throwable t = e.getTargetException();
				t.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Removes an object from the game.
	 * @param o The object to remove.
	 */
	public static void destroy(EngineObject o) {
		GameEngineComponent.objects.remove(0);
		
	}
	
	/**
	 * Returns the first found object of type T.
	 * Returns null if no object is found.
	 * @param type The type of object to find.
	 * @return The found EngineObject.
	 */
	public static <T extends EngineObject> EngineObject findObjectOfType(Class<T> type) {
		for (EngineObject ob : GameEngineComponent.objects) {
			if (ob.getClass() == type) {
				return ob;
			}
		}
		return null;
	}
	
	/**
	 * Returns all objects found of type T.
	 * Returns an empty array when no objects are found.
	 * @param type The type of object to search for.
	 * @return The array of objects.
	 */
	public static <T extends EngineObject> List<T> findObjectsOfType(Class<T> type) {
		List<T> list = new ArrayList<T>();
		for (EngineObject ob : GameEngineComponent.objects) {
			if (ob.getClass() == type) {
				list.add((T)ob);
			}
		}
		return (List<T>) list;
	}
}
