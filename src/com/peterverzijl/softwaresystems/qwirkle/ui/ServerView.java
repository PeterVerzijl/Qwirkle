package com.peterverzijl.softwaresystems.qwirkle.ui;

/**
 * An interface representation of a server view.
 * @author Peter Verzijl
 * @version 1.0a
 */
public interface ServerView {
	
	/**
	 * Sends a message to the server.
	 * @param message The message to send to the server.
	 */
	public void sendMessage(String message);
}
