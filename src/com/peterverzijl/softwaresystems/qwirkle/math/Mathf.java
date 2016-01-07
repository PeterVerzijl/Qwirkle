package com.peterverzijl.softwaresystems.qwirkle.math;

public class Mathf {
	
	/**
	 * Calculates the inverse square root the quake 3 way.
	 * @param x The value to take the fast square root.
	 * @return The fast inverse square root 
	 */
	public static float invSqrt(float x) {
	    float xhalf = 0.5f * x;
	    int i = Float.floatToIntBits(x);
	    i = 0x5f3759df - (i >> 1);
	    x = Float.intBitsToFloat(i);
	    x = x * (1.5f - xhalf * x * x);		// Newton Raphson
	    return x;
	}
	
	/**
	 * Calculates the clamped value of two floats.
	 * @param val The value to clamp.
	 * @param min The minimum value to clamp to.
	 * @param max The maximum value to clamp to.
	 * @return The clamped value.
	 */
	public static float clamp(float val, float min, float max) {
	    return Math.max(min, Math.min(max, val));
	}
}
