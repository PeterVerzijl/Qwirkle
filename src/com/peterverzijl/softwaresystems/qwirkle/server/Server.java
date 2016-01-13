package com.peterverzijl.softwaresystems.qwirkle.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.peterverzijl.softwaresystems.qwirkle.tui.ServerTUI;
import com.peterverzijl.softwaresystems.qwirkle.ui.ServerView;

/**
 * The server instance created by the server part of the java app.
 * @author Peter Verzijl
 * @version 0.1a
 */
public class Server implements Runnable {
	
	public static final int PORT = 4444;
	
	private boolean mRunning = false;
	private ServerSocket mServerSocket;
	private ServerView mViewer;
	
	private List<ClientHandler> mClients;
	
	public Server(InetAddress serverAddress, int port, ServerView viewer) throws IOException {
		mViewer = viewer;
		
		mClients = new CopyOnWriteArrayList<ClientHandler>();
		mServerSocket = new ServerSocket(port, 100, serverAddress);
		System.out.println("Starting server...");
	}
	
	/**
	 * Sets up the server and enters the loop where we wait for clients to call us.
	 */
	public void run() {
		mRunning = true;
		while(mRunning) {
			try {
				ClientHandler client;
				client = new ClientHandler(this, mServerSocket.accept());
				(new Thread(client)).start();
				addHandler(client);
			} catch (IOException e) {
				System.out.println("Error, could not connect to client due to: " + e.getMessage());
			}
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
	 * Adds a client to the client handler list.
	 * @param client The client to add.
	 */
	public void addHandler(ClientHandler client) {
		if (client != null && !mClients.contains(client)) {
			mClients.add(client);
			mViewer.sendMessage("Client " + client.name + " connected.");
		}
	}
	
	/**
	 * Removes a client handler from the clients list.
	 * @param client The client handler to remove from the clients list.
	 */
	public void removeHanlder(ClientHandler client) {
		if (client != null && mClients.contains(client)) {
			mClients.remove(client);
			mViewer.sendMessage("Client " + client.name + " disconnected.");
		}
	}

	/**
	 * Call this to shut down the server.
	 */
	public void shutdown() {
		mRunning = false;
		System.out.println("Closing server...");
		try {
			Iterator<ClientHandler> it = mClients.iterator();
			while (it.hasNext()) {
				ClientHandler client = it.next();
				client.shutdown();
			}
			if (mServerSocket != null) {
				mServerSocket.close();
			}
		} catch (IOException e) {
			System.out.println("Error: could not close down the server due to: " + e.getMessage());
 		}
	}
}
