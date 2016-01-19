package com.peterverzijl.softwaresystems.qwirkle.networking;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.peterverzijl.softwaresystems.qwirkle.Game;
import com.peterverzijl.softwaresystems.qwirkle.Player;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.AddPlayerToGameException;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.GameFullException;

/**
 * Handles a single game on the server, 
 * with a certain amount of players.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class GameServer implements Server, Runnable {
	
	private boolean hasGameStarted = false;	// If the game has started
	
	private int mTargetPlayerCount;			// The amount of players wanted in this game.
	private Game mGame;						// The game played
	private List<ClientHandler> mClients;	// The client handlers
	
	private Map<Player, ClientHandler> playerClientMap;
	
	/**
	 * Constructor
	 * @param targetPlayerCount The amount of wanted players on this server.
	 */
	public GameServer(int targetPlayerCount) {
		mTargetPlayerCount = targetPlayerCount;
		
		mClients = new ArrayList<ClientHandler>();
		playerClientMap = new HashMap<Player, ClientHandler>();
	}
	
	/**
	 * Adds a client to the client list of the GameServer
	 * @param client The client to add.
	 */
	public void addClient(ClientHandler client) {
		if (!mClients.contains(client)) {
			mClients.add(client);
		}
	}
	
	/**
	 * Returns the amount of players in this server game.
	 * @return The amount of players in this server game.
	 */
	public int getNumPlayers() {
		return mClients.size();
	}

	@Override
	public void run() {
		
	}
	
	/**
	 * Processes all incoming messages for the game state of the server.
	 * @param message The message to process.
	 * @param client The client handler that send the message.
	 */
	@Override
	public void sendMessage(String message, ClientHandler client) {
		String[] parameters = message.split("" + Protocol.Server.Settings.DELIMITER);
		String command = parameters[0];
		// Remove command from parameters
		parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
		
		switch(command) {
			case Protocol.Client.CHANGESTONE:
				
				break;
			case Protocol.Client.CHAT:
				broadcast(Protocol.Server.CHAT + 
						Protocol.Server.Settings.DELIMITER + 
						client.getName() + ": " + parameters[0]);	// The first parameter is the text.
				break;
			case Protocol.Client.GETSTONESINBAG:
				
				break;
			case Protocol.Client.MAKEMOVE:
				
				break;
			case Protocol.Client.QUIT:
				
				break;
			default:
				client.sendMessage(Protocol.Server.ERROR + Protocol.Server.Settings.DELIMITER + -1);
				break;
		}
	}
	
	/**
	 * Broadcast a message to all the clients.
	 * @param message The message to broadcast.
	 */
	@Override
	public void broadcast(String message) {
		if (message != null) {
			for (ClientHandler client : mClients) {
				client.sendMessage(message);
			}
		}
	}

	/**
	 * @return Returns the target amount of players in this game.
	 */
	public int getTargetPlayerCount() {
		return mTargetPlayerCount;
	}
	
	/**
	 * Adds a client to the game.
	 * @param client The client to add to the game.
	 * @throws AddPlayerToGameException
	 */
	public void addPlayer(ClientHandler client) 
			throws GameFullException {
		if (!hasGameStarted && mClients.size() <= mTargetPlayerCount) {
			mClients.add(client);
			client.setServer(this);
			// Check if we can now start the game
			if (mClients.size() == mTargetPlayerCount) {
				startGame();
			}
		} else {
			throw new GameFullException();
		}
	}
	
	/**
	 * Start the game.
	 */
	private void startGame() {
		hasGameStarted = true;
		
		System.out.println("Game is starting!!");
		
		// TODO (peter) : make new game
		// Create a new player for every client.
		List<Player> players = new ArrayList<Player>();
		for (ClientHandler client : mClients) {
			Player p = new Player(5);
			players.add(p);
			playerClientMap.put(p, client);
		}
		mGame = new Game(players);
		
		// Init the first move...
		
	}

	/**
	 * Removes a client from our list of clients.
	 * @param client The client that wants to disconnect.
	 */
	@Override
	public void removeHanlder(ClientHandler client) {
		mClients.remove(client);
	}
}