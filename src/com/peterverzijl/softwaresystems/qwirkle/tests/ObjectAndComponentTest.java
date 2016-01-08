package com.peterverzijl.softwaresystems.qwirkle.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.EngineObject;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameEngineComponent;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameObject;

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
		
		EngineObject go = EngineObject.Instantiate(testObject);
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
		
		EngineObject.Instantiate(testObject);
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
		
		GameObject go1 = (GameObject)EngineObject.Instantiate(testObject);
		GameObject go2 = (GameObject)EngineObject.Instantiate(testObject);
		assertNotNull(go1);
		assertNotNull(go2);
		
		List<GameObject> gos = GameObject.findObjectsOfType(GameObject.class);		
		assertTrue(gos.size() == GameEngineComponent.objects.size());
	}
	
	@Test
	public void testObjectRefID() {
		GameObject go = (GameObject)EngineObject.Instantiate(testObject);
		assertEquals(System.identityHashCode(go), go.getInstaceId());
		assertThat(go.toString(), CoreMatchers.containsString(Integer.toString(go.getInstaceId())));
	}
	
	
}
