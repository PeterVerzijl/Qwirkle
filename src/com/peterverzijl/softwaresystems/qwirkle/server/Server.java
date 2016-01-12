package com.peterverzijl.softwaresystems.qwirkle.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * The server instance created by the server part of the java app.
 * @author Peter Verzijl
 * @version 0.1a
 */
public class Server implements Runnable {
	
	public static final int PORT = 4444;
	
	private boolean mRunning = false;
	private List<ClientHandler> mClients;
	private ServerSocket mServerSocket;
	
	public Server() {
		mClients = new ArrayList<ClientHandler>();
	}
	
	/**
	 * Sets up the server and enters the loop where we wait for clients to call us.
	 */
	public void run() {
		mRunning = true;
		try {
			mServerSocket = new ServerSocket(PORT);
			System.out.println("Starting server...");
			while(mRunning) {
				ClientHandler client = new ClientHandler(this, mServerSocket.accept());
				(new Thread(client)).start();
				mClients.add(client);
				
			}
		} catch (IOException e) {
			shutdown();
		}
	}
	
	/**
	 * Broadcast a message to all the clients.
	 * @param message The message to broadcast.
	 */
	public void broadcast(String message) {
		if (message != null) {
			for (ClientHandler client : mClients) {
				client.sendMessage(client.name + ": " + message);
			}
		}
	}
	
	/**
	 * Call this to shut down the server.
	 */
	public void shutdown() {
		mRunning = false;
		System.out.println("Closing server...");
		try {
			for (ClientHandler client : mClients) {
				client.shutdown();
			}
			if (mServerSocket != null) {
				mServerSocket.close();
			}
		} catch (IOException e) {
			System.out.println("Error: could not close down the server due to: " + e.getMessage());
 		}
	}

	/**
	 * Removes a client handler from the clients list.
	 * @param client The client handler to remove from the clients list.
	 */
	public void removeHanlder(ClientHandler client) {
		if (mClients.contains(client)) {
			mClients.remove(client);
		}
	}
}
