package com.peterverzijl.softwaresystems.qwirkle.ui;

/**
 * An interface for any chat view.
 * @author Peter Verzijl
 * @version 1.0a;
 */
public interface ChatView {
	
	/**
	 * Adds a message to the chat client.
	 * @param message The message to display in the viewer.
	 */
	public void displayMessage(String message);

	/**
	 * Ask the name of the user.
	 */
	public void askName();
}
