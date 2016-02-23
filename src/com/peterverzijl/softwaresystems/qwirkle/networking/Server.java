package com.peterverzijl.softwaresystems.qwirkle.networking;

/**
 * The server instance created by the server part of the java app.
 * @author Peter Verzijl
 * @version 0.1a
 */
public interface Server {
	
	public static final String ADDRESS = "localhost";
	public static final int PORT = 4444;
	
	/**
	 * Processes all incoming messages.
	 * @param message The message to process.
	 * @param client The client handler that send the message.
	 */
	void sendMessage(String message, ClientHandler client);
	
	/**
	 * Broadcast a message to all the clients.
	 * @param message The message to broadcast.
	 */
	void broadcast(String message);
	
	/**
	 * Removes a client from the server.
	 * @param client The client handler to remove from the server.
	 */
	void removeHanlder(ClientHandler client);
}