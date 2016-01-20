package com.peterverzijl.softwaresystems.qwirkle.networking;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.peterverzijl.softwaresystems.qwirkle.Block;
import com.peterverzijl.softwaresystems.qwirkle.Game;
import com.peterverzijl.softwaresystems.qwirkle.HumanTUIPlayer;
import com.peterverzijl.softwaresystems.qwirkle.Player;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.AddPlayerToGameException;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.GameFullException;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.NotYourBlockException;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.NotYourTurnException;

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
	
	private Map<ClientHandler, Player> playerClientMap;
	
	/**
	 * Constructor
	 * @param targetPlayerCount The amount of wanted players on this server.
	 */
	public GameServer(int targetPlayerCount) {
		mTargetPlayerCount = targetPlayerCount;
		
		mClients = new ArrayList<ClientHandler>();
		playerClientMap = new HashMap<ClientHandler, Player>();
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
		if (message.length() < 1) { return; }
		
		String[] parameters = message.split("" + Protocol.Server.Settings.DELIMITER);
		String command = parameters[0];
		// Remove command from parameters
		parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
		
		switch(command) {
			case Protocol.Client.CHANGESTONE:
				// Get stones from command
				for (String stone : parameters) {
					Block b = Block.getBlockFromCharPair(stone);
					if (b != null) {
						// Check if the current player has this block.
						try {
							mGame.tradeBlock(playerClientMap.get(client), b);
						} catch (NotYourTurnException e) {
							client.sendMessage(Protocol.Server.ERROR + 
									Protocol.Server.Settings.DELIMITER + 
									1);
						} catch (NotYourBlockException e) {
							client.sendMessage(Protocol.Server.ERROR + 
									Protocol.Server.Settings.DELIMITER + 
									2);
						}
					} else {
						client.sendMessage(Protocol.Server.ERROR + 
								Protocol.Server.Settings.DELIMITER + 
								2);
					}
				}
				break;
			case Protocol.Client.CHAT:
				broadcast(Protocol.Server.CHAT + 
						Protocol.Server.Settings.DELIMITER + 
						client.getName() + ": " + parameters[0]);	// The first parameter is the text.
				break;
			case Protocol.Client.GETSTONESINBAG:
				if (hasGameStarted) {
					client.sendMessage(Protocol.Server.CHAT + 
								Protocol.Server.Settings.DELIMITER + 
								mGame.getNumStonesInBag());
				} else {
					client.sendMessage(Protocol.Server.ERROR + 
							Protocol.Server.Settings.DELIMITER + 
							8);
				}
				break;
			case Protocol.Client.MAKEMOVE:
				// TODO (peter) : 
				break;
			case Protocol.Client.QUIT:
				// Ok doei lol
				mClients.remove(client);
				mGame.removePlayer(playerClientMap.get(client));
				client.shutdown();
				break;
			default:
				client.sendMessage(Protocol.Server.ERROR + 
									Protocol.Server.Settings.DELIMITER + 
									8);
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
			addClient(client);
			client.setServer(this);
			// Check if we can now start the game
			if (mClients.size() == mTargetPlayerCount) {
				startGame();
			} else {
				broadcast(Protocol.Server.OKWAITFOR + 
							Protocol.Server.Settings.DELIMITER + 
							(mTargetPlayerCount - mClients.size()));
			}
		} else {
			throw new GameFullException();
		}
	}
	
	/**
	 * Start the game.
	 */
	private void startGame() {
		// Send start game message
		String message = Protocol.Server.STARTGAME;
		for (ClientHandler client : mClients) {
			message += Protocol.Server.Settings.DELIMITER;
			message += client.getName();
		}
		broadcast(message);
		
		hasGameStarted = true;
		
		// TODO (peter) : make new game
		// Create a new player for every client.
		List<Player> players = new ArrayList<Player>();
		for (ClientHandler client : mClients) {
			Player p = new Player();
			players.add(p);
			playerClientMap.put(client, p);
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