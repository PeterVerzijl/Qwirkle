package com.peterverzijl.softwaresystems.qwirkle.exceptions;

/**
 * This exception is thrown when you try to do a move that is not legal on the
 * current board.
 * @author Peter Verzijl
 * @version 1.0a;
 */
public class IllegalMoveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6440865807357446886L;

	public IllegalMoveException() {
		super("Deze move mag niet!");
	}
}
