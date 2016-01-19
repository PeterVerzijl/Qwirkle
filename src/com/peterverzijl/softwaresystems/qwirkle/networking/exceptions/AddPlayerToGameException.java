package com.peterverzijl.softwaresystems.qwirkle.networking.exceptions;

/**
 * This exception is thrown when you try to add a player to a game and it fails.
 * @author Peter Verzijl
 * @version 1.0a;
 */
@SuppressWarnings("serial")
public class AddPlayerToGameException extends Exception {
	
	public AddPlayerToGameException() { }
	
	public AddPlayerToGameException(String msg) {
		super(msg);
	}
}

