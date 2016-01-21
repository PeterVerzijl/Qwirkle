package com.peterverzijl.softwaresystems.qwirkle.exceptions;

/**
 * This exception is thrown when you try to add act on the game when it is not your turn.
 * @author Peter Verzijl
 * @version 1.0a;
 */
@SuppressWarnings("serial")
public class NotYourTurnException extends Exception {
	
	public NotYourTurnException() { }
	
	public NotYourTurnException(String msg) {
		super(msg);
	}
}
