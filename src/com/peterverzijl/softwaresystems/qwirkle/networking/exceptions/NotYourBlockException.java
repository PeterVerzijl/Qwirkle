package com.peterverzijl.softwaresystems.qwirkle.networking.exceptions;

/**
 * This exception is thrown when you try to place or trade a block that is not in your hand.
 * @author Peter Verzijl
 * @version 1.0a;
 */
@SuppressWarnings("serial")
public class NotYourBlockException extends Exception {
	
	public NotYourBlockException() { }
	
	public NotYourBlockException(String msg) {
		super(msg);
	}
}
