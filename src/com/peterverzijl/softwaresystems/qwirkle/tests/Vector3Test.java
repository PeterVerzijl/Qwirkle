package com.peterverzijl.softwaresystems.qwirkle.tests;

import org.junit.Test;
import static org.junit.Assert.*;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector3;

public class Vector3Test {
	
	private static final double DELTA = 0.0001D;

	@Test
	public void constructorsTest() {
		Vector3 v = new Vector3(1, 1, 1);
		assertTrue(v.equals(new Vector3(v)));	// Values are the same
		assertNotEquals(v, new Vector3(v));		// But the object is not
		
		v = new Vector3(new Vector2(1, 1));
		assertTrue(v.getZ() == 0);
	}
	
	@Test
	public void magnitudeTest() {
		Vector3 v = new Vector3(0, 0, 0);
		assertTrue(v.magnitude() == 0);
		
		v = new Vector3(1, 1, 1);
		assertEquals(v.magnitude(), Math.sqrt(3), DELTA);
	}
	
	@Test
	public void normalizeTest() {
		Vector3 v = new Vector3(1, 5, 7);
		v.normalize();
		assertEquals(v.magnitude(), 1.0f, DELTA);
		
		v = new Vector3(-1, -5, -7);
		assertEquals(v.normalized().magnitude(), 1.0f, DELTA);
	}
	
	@Test
	public void cloneTest() {
		Vector3 v1 = new Vector3(1, 1, 1);
		Vector3 v2 = v1.clone();
		assertTrue(v1.equals(v2));		// Values are the same
		assertNotEquals(v1, v2);		// But the object is not
	}
	
	@Test
	public void toStringTest() {
		Vector3 v = new Vector3(1, 1, 1);
		assertEquals(v.toString(), "(1.0, 1.0, 1.0)");
	}
	
	@Test
	public void addTest() {
		Vector3 a = new Vector3(1, 2, 3);
		Vector3 b = new Vector3(3, 2, 1);
		Vector3 result = a.add(b);
		assertTrue(result.equals(new Vector3(4, 4, 4)));
		
		a = new Vector3(1, 2, 3);
		b = new Vector3(3, 2, 1);
		Vector3 result2 = b.add(a);
		assertTrue(result.equals(result2));
		
		a = new Vector3(1, 2, 3);
		result = a.add(-3, -2, -1);
		assertTrue(result.equals(new Vector3(-2, 0, 2)));
	}
	
	@Test
	public void subtractTest() {
		Vector3 a = new Vector3(1, 2, 3);
		Vector3 b = new Vector3(3, 2, 1);
		
		Vector3 result1 = a.subtract(b);
		assertTrue(result1.equals(new Vector3(-2, 0, 2)));
		
		a = new Vector3(1, 2, 3);	
		Vector3 result2 = a.subtract(3, 2, 1);
		assertTrue(result2.equals(new Vector3(-2, 0, 2)));		
	}
	
	@Test
	public void multiplyTest() {
		float scalar  = 1.8373f;
		Vector3 v = new Vector3(3.45f, 3.65f, 6.32f);
		float oldLength = v.magnitude();
		v.multiply(scalar);
		float newLength = v.magnitude();
		assertEquals(oldLength * scalar, newLength, DELTA);
	}
	
	@Test
	public void dotProductTest() {
		Vector3 v1 = new Vector3(1, 1, 0);
		Vector3 v2 = new Vector3(1, 0, 1);
		float dot = Vector3.dot(v1, v2);
		assertEquals(dot, 1.0f, DELTA);
		
		v1 = new Vector3(1.324f, 1.826f, 7.47f);
		v2 = new Vector3(8.860f, 11.37f, 8.32f);
		dot = Vector3.dot(v1, v2);
		assertEquals(dot, 94.64266f, DELTA);
	 }
	
	@Test
	public void crossProductTest() {
		assertTrue(Vector3.cross(Vector3.FORWARD(), Vector3.RIGHT()).equals(Vector3.UP()));
	}
	
	@Test
	public void lerpTest() {
		float fraction = 0.7f;
		Vector3 v1 = Vector3.ZERO();
		Vector3 v2 = Vector3.UNITY();
		Vector3 lerp = Vector3.lerp(v1, v2, fraction);
		Vector3 answer = new Vector3(fraction, fraction, fraction);
		assertTrue(lerp.equals(answer));
	}
	
	@Test
	public void slerpTest() {
		float fraction = 0.3f;
		Vector3 v1 = Vector3.ZERO();
		Vector3 v2 = Vector3.UNITY();
		Vector3 slerp = Vector3.slerp(v1, v2, fraction);
		
		// Normal geometric way
		float omega = Vector3.dot(v1, v2);
		if (omega < DELTA) {
			omega += DELTA * 1.0f;
		}
		float f1 = (float) (Math.sin((1-fraction)*omega) / Math.sin(omega));
		float f2 = (float) (Math.sin(fraction*omega) / Math.sin(omega));
		Vector3 result = (v1.multiply(f1)).add((v2.multiply(f2)));
		
		System.out.println(slerp.toString());
		System.out.println(result.toString());
		assertTrue(slerp.equals(result));
	}
	
	
}
