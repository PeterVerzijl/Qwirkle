package com.peterverzijl.softwaresystems.qwirkle.gameengine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Object {
	
	/**
	 * Name of the object.
	 */
	public String name;
	
	public Object(String name) {
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
		return this.getClass().toString() + " : " + name;
	}
	
	/**
	 * Creates a new instance of the given object.
	 * @param o The object to create a new instance off.
	 * @return The instantiated object.
	 */
	public static Object Instantiate(Object o) {
		try {
			Constructor<? extends Object> con = o.getClass().getConstructor();
			try {
				Object instance = con.newInstance();
				GameEngineComponent.objects.add(instance);
				return instance;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Removes an object from the game.
	 * @param o The object to remove.
	 */
	public static void destroy(Object o) {
		GameEngineComponent.objects.remove(0);
		
	}
	
	/**
	 * Returns the first found object of type T.
	 * Returns null if no object is found.
	 * @param type The type of object to find.
	 * @return The found Object.
	 */
	public static <T extends Object> Object findObjectOfType(Class<T> type) {
		for (Object ob : GameEngineComponent.objects) {
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
	public static <T extends Object> Object[] findObjectsOfType(Class<T> type) {
		List<Object> list = new ArrayList<Object>();
		for (Object ob : GameEngineComponent.objects) {
			if (ob.getClass() == type) {
				list.add(ob);
			}
		}
		return (Object[]) list.toArray();
	}
	
}
