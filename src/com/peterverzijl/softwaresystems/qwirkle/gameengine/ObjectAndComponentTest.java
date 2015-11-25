package com.peterverzijl.softwaresystems.qwirkle.gameengine;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ObjectAndComponentTest {
	
	GameObject testObject;
	
	public static final String correctName = "John";
	public static final String incorrectName = "Nope";
	
	@Before
	public void setUp() throws Exception {
		
		testObject = new GameObject();
	}
	
	@Test
	public void testFindObjectOfType() {
		GameEngineComponent.objects.clear();
		assertTrue(GameEngineComponent.objects.size() == 0);
		
		GameObject go = new GameObject();
		GameEngineComponent.objects.add(go);
		assertTrue(GameEngineComponent.objects.size() == 1);
		GameObject found = (GameObject)GameObject.findObjectOfType(GameObject.class);
		assertEquals(go, found);
	}
	
	@Test
	public void testInstantiatedObjectFind() {
		GameEngineComponent.objects.clear();
		assertTrue(GameEngineComponent.objects.size() == 0);
		
		Object go = Object.Instantiate(testObject);
		assertNotNull(go);
		GameObject found = (GameObject)GameObject.findObjectOfType(GameObject.class);
		assertEquals(found, go);
	}
	
	@Test
	public void testGameObjectName() {		
		GameEngineComponent.objects.clear();
		assertTrue(GameEngineComponent.objects.size() == 0);
		
		GameObject go = new GameObject(correctName);
		GameEngineComponent.objects.add(go);
		assertTrue(GameEngineComponent.objects.size() == 1);
		GameObject found = (GameObject)GameObject.findObjectOfType(GameObject.class);
		assertEquals(go, found);
		assertTrue(go.name.equals(correctName));
	}
	
	@Test
	public void testDestroyGameObject() {
		GameEngineComponent.objects.clear();
		assertTrue(GameEngineComponent.objects.size() == 0);
		
		Object.Instantiate(testObject);
		assertTrue(GameEngineComponent.objects.size() == 1);
		GameObject found = (GameObject)GameObject.findObjectOfType(GameObject.class);
		assertNotNull(found);
		GameObject.destroy(found);
		assertTrue(GameEngineComponent.objects.size() == 0);
	}
	
	@Test
	public void testFindMultipleObjects() {
		GameEngineComponent.objects.clear();
		assertTrue(GameEngineComponent.objects.size() == 0);
		
		GameObject go1 = (GameObject)Object.Instantiate(testObject);
		GameObject go2 = (GameObject)Object.Instantiate(testObject);
		assertNotNull(go1);
		assertNotNull(go2);
		
		GameObject[] gos = (GameObject[])GameObject.findObjectsOfType(GameObject.class);		
		assertTrue(gos.length == GameEngineComponent.objects.size());
	}
}
