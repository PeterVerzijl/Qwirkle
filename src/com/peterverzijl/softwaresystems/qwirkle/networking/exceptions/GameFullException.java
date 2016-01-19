package com.peterverzijl.softwaresystems.qwirkle.networking.exceptions;

/**
 * This exception is thrown when the game is full.
 * @author Peter Verzijl
 * @version 1.0a
 */
@SuppressWarnings("serial")
public class GameFullException extends AddPlayerToGameException {
	
	public GameFullException() {}
	
	public GameFullException(String msg) {
		super(msg);
	}
}
