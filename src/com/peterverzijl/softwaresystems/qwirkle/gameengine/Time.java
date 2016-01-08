package com.peterverzijl.softwaresystems.qwirkle.gameengine;

/**
 * Class that has the time and the delta time.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Time {
	
	private static double mDeltaTime = Float.MAX_VALUE;
	
	/**
	 * The current time elapsed during the program's life in seconds.
	 * @return The time elapsed in seconds.
	 */
	public static double getTime() {
		return System.currentTimeMillis();
	}
	
	/**
	 * The time elapsed during this frame in seconds.
	 * @return The elapsed time this frame in seconds.
	 */
	public static double getDeltaTime() {
		return mDeltaTime;
	}
	
	/**
	 * Sets the delta time in seconds.
	 * @param delta The elapsed time this frame.
	 */
	protected static void setDeltaTime(double delta) {
		mDeltaTime = delta / 1000.0;
	}
}
