package com.peterverzijl.softwaresystems.qwirkle.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.peterverzijl.softwaresystems.qwirkle.Game;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.GameFullException;
import com.peterverzijl.softwaresystems.qwirkle.ui.ServerView;

public class LobbyServer implements Server, Runnable {

	private String mServerName = "Peter's Server";
	private String[] mFeatures = new String[] {
		//Protocol.Server.Features.CHALLENGE,
		Protocol.Server.Features.CHAT,
		//Protocol.Server.Features.LEADERBOARD,
		//Protocol.Server.Features.SECURITY
	};
	
	private boolean mRunning = false;
	private ServerSocket mServerSocket;
	private ServerView mViewer;
	
	/**
	 * All clients connected to this server.
	 */
	private List<ClientHandler> mClients;
	
	/**
	 * All server games that are started.
	 */
	private List<GameServer> mGameServers;
	
	/**
	 * Constructor
	 * @param serverAddress
	 * @param port
	 * @param viewer
	 * @throws IOException
	 */
	public LobbyServer(InetAddress serverAddress, int port, ServerView viewer) throws IOException {
		mViewer = viewer;
		
		mClients = new CopyOnWriteArrayList<ClientHandler>();
		mServerSocket = new ServerSocket(port, 100, serverAddress);
		mGameServers = new ArrayList<GameServer>();
		
		if (mViewer != null) {
			mViewer.sendMessage("Starting server...");
		}
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
				if (mViewer != null) {
					mViewer.sendMessage("Error, could not connect to client due to: " + e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Processes all incoming messages.
	 * @param message The message to process.
	 * @param client The client handler that send the message.
	 */
	public synchronized void sendMessage(String message, ClientHandler client) {
		String[] parameters = message.split("" + Protocol.Server.Settings.DELIMITER);
		String command = parameters[0];
		// Remove command from parameters
		parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
		
		switch(command) {
			case Protocol.Client.ACCEPTINVITE:
				// TODO (peter) : Implement challenging
				break;
			case Protocol.Client.CHAT:
				broadcast(Protocol.Server.CHAT + 
						Protocol.Server.Settings.DELIMITER + 
						client.getName() + ": " + parameters[0]);	// The first parameter is the text.
				break;
			case Protocol.Client.DECLINEINVITE:
				// TODO (peter) : Implement challenging
				break;
			case Protocol.Client.GETLEADERBOARD:
				// TODO (peter) : Implement leader board
				break;
			case Protocol.Client.HALLO:
				String name = parameters[0];
				if (!isNameUsed(name)) {
					sendHallo(client);			// Reply with hallo
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
			case Protocol.Client.REQUESTGAME:
				try {
					int numPlayers = Integer.parseInt(parameters[0]);
					if (numPlayers < 0) {
						throw new NumberFormatException();
					}
					assignPlayerToGame(numPlayers, client);
				} catch (NumberFormatException e) {
					// Errors! this is not a number
					client.sendMessage(Protocol.Server.ERROR + 
							Protocol.Server.Settings.DELIMITER + 8);
				}
				break;
			default:
				client.sendMessage(Protocol.Server.ERROR + Protocol.Server.Settings.DELIMITER + 8);
				break;
		}
	}
	
	/**
	 * Assigns a player to a game, or creates a new game.
	 * @param numPlayers
	 * @param client
	 */
	private void assignPlayerToGame(int numPlayers, ClientHandler client) {
		// Search for a server game with this amount of players
		for (GameServer game : mGameServers) {
			if (numPlayers == 0 || game.getTargetPlayerCount() == numPlayers) {
				try {
					game.addPlayer(client);
					return;
				} catch (GameFullException e) {
					// Game is full.
					break;
				}
			}
		}
		// Make new game with same target players
		GameServer newGame = new GameServer((numPlayers == 0)?2:numPlayers);
		try {
			newGame.addPlayer(client);
			(new Thread(newGame)).start();
			mGameServers.add(newGame);
		} catch (GameFullException e) {
			// A game cannot be full after creating a new one.
		}
	}

	/**
	 * Sends an hallo to the client with all the supported features.
	 * @param client
	 */
	private void sendHallo(ClientHandler client) {
		String message = Protocol.Server.HALLO;			// Command
		message += Protocol.Server.Settings.DELIMITER + mServerName;
		for (String s : mFeatures) {					// Features
			message += Protocol.Server.Settings.DELIMITER;
			message += s;
		}
		// Message the final message.
		client.sendMessage(message);
	}

	/**
	 * Loops the client handlers to see if this name is used yet.
	 * @param name The name to check.
	 */
	public boolean isNameUsed(String name) {
		boolean result = false;
		for (ClientHandler client : mClients) {
			if (client.getName() != null && client.getName().equals(name)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Broadcast a message to all the clients.
	 * @param message The message to broadcast.
	 */
	public void broadcast(String message) {
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
			if (mViewer != null) {
				mViewer.sendMessage("Client " + client.getName() + " connected.");
			}
		}
	}
	
	/**
	 * Removes a client handler from the clients list.
	 * @param client The client handler to remove from the clients list.
	 */
	public void removeHanlder(ClientHandler client) {
		if (client != null && mClients.contains(client)) {
			mClients.remove(client);
			if (mViewer != null) {
				mViewer.sendMessage("Client " + client.getName() + " disconnected.");
			}
		}
	}

	public void shutdown() {
		for (ClientHandler client : mClients) {
			client.shutdown();
		}
		try {
			mServerSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the amount of connected clients.
	 * @return The number of connected clients.
	 */
	public int getNumClients() {
		return mClients.size();
	}
}
