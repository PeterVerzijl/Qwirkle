package com.peterverzijl.softwaresystems.qwirkle;

public class IllegalMoveException extends Exception {

	public IllegalMoveException() {
		super("Deze move mag niet!");
	}
}
