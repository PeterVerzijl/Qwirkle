package com.peterverzijl.softwaresystems.qwirkle.tests;

import org.junit.Test;
import static org.junit.Assert.*;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector3;

public class Vector2Test {
	
	private static final double DELTA = 0.0001D;

	@Test
	public void constructorsTest() {
		Vector2 v = new Vector2(1, 1);
		assertTrue(v.equals(new Vector2(v)));	// Values are the same
		assertNotEquals(v, new Vector2(v));		// But the object is not
		v = new Vector2(new Vector3(1, 1, 1));
		assertTrue(v.equals(new Vector2(1, 1)));
	}
	
	@Test
	public void magnitudeTest() {
		Vector2 v = new Vector2(0, 0);
		assertTrue(v.magnitude() == 0);
		
		v = new Vector2(1, 1);
		assertEquals(v.magnitude(), Math.sqrt(2), DELTA);
	}
	
	@Test
	public void normalizeTest() {
		Vector2 v = new Vector2(1, 5);
		v.normalize();
		assertEquals(v.magnitude(), 1.0f, DELTA);
		
		v = new Vector2(-1, -5);
		assertEquals(v.normalized().magnitude(), 1.0f, DELTA);
	}
	
	@Test
	public void cloneTest() {
		Vector2 v1 = new Vector2(1, 1);
		Vector2 v2 = v1.clone();
		assertTrue(v1.equals(v2));		// Values are the same
		assertNotEquals(v1, v2);		// But the object is not
	}
	
	@Test
	public void toStringTest() {
		Vector2 v = new Vector2(1, 1);
		assertEquals(v.toString(), "(1.0, 1.0)");
	}
	
	@Test
	public void addTest() {
		Vector2 a = new Vector2(1, 2);
		Vector2 b = new Vector2(3, 2);
		Vector2 result = a.add(b);
		assertTrue(result.equals(new Vector2(4, 4)));
		
		a = new Vector2(1, 2);
		b = new Vector2(3, 2);
		Vector2 result2 = b.add(a);
		assertTrue(result.equals(result2));
		
		a = new Vector2(1, 2);
		result = a.add(-3, -2);
		assertTrue(result.equals(new Vector2(-2, 0)));
	}
	
	@Test
	public void subtractTest() {
		Vector2 a = new Vector2(1, 2);
		Vector2 b = new Vector2(3, 2);
		
		Vector2 result1 = a.subtract(b);
		assertTrue(result1.equals(new Vector2(-2, 0)));
		
		a = new Vector2(1, 2);	
		Vector2 result2 = a.subtract(3, 2);
		assertTrue(result2.equals(new Vector2(-2, 0)));		
	}
	
	@Test
	public void multiplyTest() {
		float scalar  = 1.8373f;
		Vector2 v = new Vector2(3.45f, 3.65f);
		float oldLength = v.magnitude();
		v.multiply(scalar);
		float newLength = v.magnitude();
		assertEquals(oldLength * scalar, newLength, DELTA);
	}
	
	@Test
	public void dotProductTest() {
		Vector2 v1 = new Vector2(1, 1);
		Vector2 v2 = new Vector2(1, 0);
		float dot = Vector2.dot(v1, v2);
		assertEquals(dot, 1.0f, DELTA);
		
		v1 = new Vector2(1.324f, 1.826f);
		v2 = new Vector2(8.860f, 11.37f);
		dot = Vector2.dot(v1, v2);
		assertEquals(dot, 32.49226f, DELTA);
	 }
	
	@Test
	public void lerpTest() {
		float fraction = 0.7f;
		Vector2 v1 = Vector2.ZERO();
		Vector2 v2 = Vector2.UNITY();
		Vector2 lerp = Vector2.lerp(v1, v2, fraction);
		Vector2 answer = new Vector2(fraction, fraction);
		assertTrue(lerp.equals(answer));
	}
	
	@Test
	public void slerpTest() {
		float fraction = 0.3f;
		Vector2 v1 = Vector2.ZERO();
		Vector2 v2 = Vector2.UNITY();
		Vector2 slerp = Vector2.slerp(v1, v2, fraction);
		
		// Normal geometric way
		float omega = Vector2.dot(v1, v2);
		if (omega < DELTA) {
			omega += DELTA * 1.0f;
		}
		float f1 = (float) (Math.sin((1-fraction)*omega) / Math.sin(omega));
		float f2 = (float) (Math.sin(fraction*omega) / Math.sin(omega));
		Vector2 result = (v1.multiply(f1)).add((v2.multiply(f2)));
		
		System.out.println(slerp.toString());
		System.out.println(result.toString());
		assertTrue(slerp.equals(result));
	}	
}
