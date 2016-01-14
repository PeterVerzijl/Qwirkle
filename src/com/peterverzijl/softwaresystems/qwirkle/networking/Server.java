package com.peterverzijl.softwaresystems.qwirkle.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Arrays;
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
		mViewer.sendMessage("Starting server...");
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
				mViewer.sendMessage("Error, could not connect to client due to: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Processes all incoming messages.
	 * @param message The message to process.
	 * @param client The client handler that send the message.
	 */
	void sendMessage(String message, ClientHandler client) {
		String[] parameters = message.split("" + Protocol.Server.Settings.DELIMITER);
		String command = parameters[0];
		// Remove command from parameters
		parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
		
		switch(command) {
			case Protocol.Client.ACCEPTINVITE:
				// TODO (peter) : Implement challenging
				break;
			case Protocol.Client.CHANGESTONE:
				
				break;
			case Protocol.Client.CHAT:
				broadcast(Protocol.Server.CHAT + 
						Protocol.Server.Settings.DELIMITER + 
						client.getName() + ": " + parameters[0]);	// The first parameter is the text.
				break;
			case Protocol.Client.DECLINEINVITE:
				// TODO (peter) : Implement challenging
				break;
			case Protocol.Client.ERROR:
				// TODO (everyone) : No messages from the client supported in the protocol yet.
				break;
			case Protocol.Client.GETLEADERBOARD:
				// TODO (peter) : Implement leader board
				break;
			case Protocol.Client.GETSTONESINBAG:
				
				break;
			case Protocol.Client.HALLO:
				String name = parameters[0];
				if (!isNameUsed(name)) {
					client.setName(name);
					client.setFeatures(Arrays.copyOfRange(parameters, 1, parameters.length));
				} else {
					client.sendMessage(Protocol.Server.ERROR + 
										Protocol.Server.Settings.DELIMITER + 4);
				}
				break;
			case Protocol.Client.INVITE:
				// TODO (everyone) : No messages from the client supported in the protocol yet.
				break;
			case Protocol.Client.MAKEMOVE:
				
				break;
			case Protocol.Client.QUIT:
				
				break;
			case Protocol.Client.REQUESTGAME:
				
				break;
			default:
				client.sendMessage(Protocol.Server.ERROR + Protocol.Server.Settings.DELIMITER + -1);
				break;
		}
	}
	
	/**
	 * Loops the client handlers to see if this name is used yet.
	 * @param name The name to check.
	 */
	private boolean isNameUsed(String name) {
		boolean result = false;
		for (ClientHandler client : mClients) {
			if (client.getName().equals(name)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Broadcast a message to all the clients.
	 * @param message The message to broadcast.
	 */
	private void broadcast(String message) {
		if (message != null) {
			for (ClientHandler client : mClients) {
				client.sendMessage(message);
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
			mViewer.sendMessage("Client " + client.getName() + " connected.");
		}
	}
	
	/**
	 * Removes a client handler from the clients list.
	 * @param client The client handler to remove from the clients list.
	 */
	public void removeHanlder(ClientHandler client) {
		if (client != null && mClients.contains(client)) {
			mClients.remove(client);
			mViewer.sendMessage("Client " + client.getName() + " disconnected.");
		}
	}

	/**
	 * Call this to shut down the server.
	 */
	public void shutdown() {
		mRunning = false;
		mViewer.sendMessage("Closing server...");
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
			mViewer.sendMessage("Error: could not close down the server due to: " + e.getMessage());
 		}
	}
}
