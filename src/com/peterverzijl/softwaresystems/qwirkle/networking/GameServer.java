package com.peterverzijl.softwaresystems.qwirkle.networking;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.peterverzijl.softwaresystems.qwirkle.Block;
import com.peterverzijl.softwaresystems.qwirkle.Board;
import com.peterverzijl.softwaresystems.qwirkle.Game;
import com.peterverzijl.softwaresystems.qwirkle.IllegalMoveException;
import com.peterverzijl.softwaresystems.qwirkle.Node;
import com.peterverzijl.softwaresystems.qwirkle.Player;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.AddPlayerToGameException;
import com.peterverzijl.softwaresystems.qwirkle.networking.exceptions.GameFullException;
import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourBlockException;
import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourTurnException;

/**
 * Handles a single game on the server, with a certain amount of players.
 * 
 * @author Peter Verzijl
 * @version 1.0a
 */
public class GameServer implements Server {

	private boolean hasGameStarted = false; // If the game has started
	private boolean isFirstMove = true;		// Check first move

	private int mTargetPlayerCount; // The amount of players wanted in this
									// game.
	
	private LobbyServer mMasterServer;
	
	private Game mGame; // The game played
	private List<ClientHandler> mClients; // The client handlers

	private Map<ClientHandler, Player> playerClientMap;
	
	private Map<Player, List<Node>> firstMoveMap = new HashMap<Player, List<Node>>();

	/**
	 * Constructor
	 * 
	 * @param targetPlayerCount
	 *            The amount of wanted players on this server.
	 */
	public GameServer(int targetPlayerCount, LobbyServer lobbyServer) {
		mTargetPlayerCount = targetPlayerCount;
		mMasterServer = lobbyServer;

		mClients = new ArrayList<ClientHandler>();
		playerClientMap = new HashMap<ClientHandler, Player>();
	}

	/**
	 * Adds a client to the client list of the GameServer
	 * 
	 * @param client
	 *            The client to add.
	 */
	public void addClient(ClientHandler client) {
		if (!mClients.contains(client)) {
			mClients.add(client);
		}
	}

	/**
	 * Returns the amount of players in this server game.
	 * 
	 * @return The amount of players in this server game.
	 */
	public int getNumPlayers() {
		return mClients.size();
	}

	/**
	 * Processes all incoming messages for the game state of the server.
	 * 
	 * @param message
	 *            The message to process.
	 * @param client
	 *            The client handler that send the message.
	 */
	@Override
	public void sendMessage(String message, ClientHandler client) {
		if (message.length() < 1) {
			return;
		}

		String[] parameters = message.split("" + Protocol.Server.Settings.DELIMITER);
		String command = parameters[0];
		// Remove command from parameters
		parameters = Arrays.copyOfRange(parameters, 1, parameters.length);

		switch (command) {
			case Protocol.Client.CHANGESTONE:
				if (!hasGameStarted) {
					client.sendMessage(Protocol.Server.ERROR + 
							Protocol.Server.Settings.DELIMITER + 1);
					return;
				}
				// Get stones from command
				try {
					List<Block> tradedBlocks = new ArrayList<Block>();
					for (String stone : parameters) {
						// TODO (Peter) : Change the way we get a stone from the player hand.
						Block b = Block.getBlockFromCharPair(stone);
						
						for (Block handBlock : playerClientMap.get(client).getHand()) {
							if (b.equals(handBlock)) {
								b = handBlock;
							}
						}
						
						if (b != null) {
							// Check if the current player has this block.
							Block newBlock = mGame.tradeBlock(playerClientMap.get(client), b);
							tradedBlocks.add(newBlock);
						} else {
							client.sendMessage(Protocol.Server.ERROR + 
												Protocol.Server.Settings.DELIMITER + 2);
						}
					}
					sendBlocksClient(tradedBlocks, client);
				} catch (NotYourTurnException e) {
					client.sendMessage(Protocol.Server.ERROR + 
										Protocol.Server.Settings.DELIMITER + 1);
				} catch (NotYourBlockException e) {
					client.sendMessage(Protocol.Server.ERROR + 
										Protocol.Server.Settings.DELIMITER + 2);
				}
				break;
			case Protocol.Client.CHAT:
				broadcast(Protocol.Server.CHAT + 
							Protocol.Server.Settings.DELIMITER + 
							client.getName() + ": "
							+ parameters[0]); // The first parameter is the text.
				break;
			case Protocol.Client.GETSTONESINBAG:
				if (hasGameStarted) {
					client.sendMessage(Protocol.Server.CHAT + 
										Protocol.Server.Settings.DELIMITER + 
										mGame.getNumStonesInBag());
				} else {
					client.sendMessage(Protocol.Server.ERROR + 
										Protocol.Server.Settings.DELIMITER + 8);
				}
				break;
			case Protocol.Client.MAKEMOVE:
				// Do we have all the player's initial moves?
				if (hasGameStarted && isFirstMove) {
					Player player = playerClientMap.get(client);
					if (!firstMoveMap.containsKey(player)) {
						List<Node> moves = new ArrayList<Node>();
						for (String move : parameters) {
							moves.add(Board.moveStringToNode(move));
						}
						// Add this move to the moves list
						firstMoveMap.put(player, moves);
						
						// Check if everyone has done the first move
						if (firstMoveMap.keySet().size() == mClients.size()) {
							// Do the stuff!
							Map<Player, List<Node>> startingMove = mGame.startingPlayer(firstMoveMap);
							Map.Entry<Player, List<Node>> entry = startingMove.entrySet().iterator().next();
							ClientHandler curPlayer = getClientFromPlayer(entry.getKey());
							ClientHandler nextPlayer = getClientFromPlayer(mGame.nextPlayer());
							
							// Translate moves to text
							String[] newMoves = new String[entry.getValue().size()];
							int index = 0;
							for (Node n : entry.getValue()) {
								String s = "";
								s += Block.toChars(n.getBlock());
								s += Protocol.Server.Settings.DELIMITER2;
								s += n.getPosition().getX();
								s += Protocol.Server.Settings.DELIMITER2;
								s += n.getPosition().getY();
								newMoves[index++] = s;
							}
							
							// Set the first move done
							isFirstMove = false;
							// Broadcast the new move
							broadcast(getMoveMessage(curPlayer.getName(), 
													nextPlayer.getName(), 
													newMoves));
						}
					} else {
						// ignore move
						// NOTE: Not your turn!
						client.sendMessage(Protocol.Server.ERROR + 
								Protocol.Server.Settings.DELIMITER + 1);
					}					
				} else if (hasGameStarted) {
					// Is it our turn?
					Player clientPlayer = playerClientMap.get(client);
					if (clientPlayer == mGame.getCurrentPlayer()) {
						if (parameters.length > 0) {
							// Get moves
							List<Node> moves = new ArrayList<Node>();
							for (String move : parameters) {
								moves.add(Board.moveStringToNode(move));
							}
							// Do moves
							try {
								mGame.doMove(moves);
								
								// Add stones to our hand
								List<Block> newBlocks = mGame.addBlocks(clientPlayer);
								// Send new blocks
								sendBlocksClient(newBlocks, client);
								
								// Check if the game has ended
								if (!mGame.hasEnded()) {
									// Give turn to next player
									Player p = mGame.nextPlayer();
									ClientHandler ch = getClientFromPlayer(p);
									// Now broadcast to next player a do move thing...
									broadcast(getMoveMessage(client.getName(), ch.getName(), parameters));
								} else {
									stopGame();
								}
							} catch (IllegalMoveException e) {
								// NOTE: Invalid move
								client.sendMessage(Protocol.Server.ERROR + 
										Protocol.Server.Settings.DELIMITER + 7);
							}
						} else {
							// NOTE: Invalid move (can't move 0 stones!)
							client.sendMessage(Protocol.Server.ERROR + 
									Protocol.Server.Settings.DELIMITER + 7);
						}
					} else {
						// NOTE: Not your turn!
						client.sendMessage(Protocol.Server.ERROR + 
								Protocol.Server.Settings.DELIMITER + 1);
					}
				} else {
					client.sendMessage(Protocol.Server.ERROR + 
							Protocol.Server.Settings.DELIMITER + 1);
				}
				break;
			case Protocol.Client.QUIT:
				// Ok doei lol
				mClients.remove(client);
				mGame.removePlayer(playerClientMap.get(client));
				playerClientMap.remove(client);
				client.shutdown();
				// TODO (peter) : Check if this player had the turn, if so, pass on.
				break;
			default:
				client.sendMessage(Protocol.Server.ERROR + 
									Protocol.Server.Settings.DELIMITER + 8);
				break;
		}
	}
	
	/**
	 * Returns the first found client handler that is mapped to this player.
	 * @param player The player to get the client from.
	 * @return The client found that belongs to the given player.
	 */
	private ClientHandler getClientFromPlayer(Player player) {
		ClientHandler result = null;
		for (ClientHandler ch : playerClientMap.keySet()) {
			if (playerClientMap.get(ch) == player) {
				result = ch;
			}
		}
		return result;
	}

	/**
	 * Sends an ADDTOHAND message to the client.
	 * @param blocks The blocks to send to the client.
	 * @param client The receiver of the blocks.
	 */
	private void sendBlocksClient(List<Block> blocks, ClientHandler client) {
		String message = "";
		message += Protocol.Server.ADDTOHAND;
		for (Block b : blocks) {
			message += Protocol.Server.Settings.DELIMITER;
			message += Block.toChars(b);
		}
		client.sendMessage(message);
	}

	/**
	 * Broadcast a message to all the clients.
	 * 
	 * @param message
	 *            The message to broadcast.
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
	 * 
	 * @param client
	 *            The client to add to the game.
	 * @throws AddPlayerToGameException
	 */
	public void addPlayer(ClientHandler client) throws GameFullException {
		if (!hasGameStarted && mClients.size() <= mTargetPlayerCount) {
			addClient(client);
			client.setServer(this);
			// Check if we can now start the game
			if (mClients.size() == mTargetPlayerCount) {
				startGame();
			} else {
				broadcast(Protocol.Server.OKWAITFOR + Protocol.Server.Settings.DELIMITER
						+ (mTargetPlayerCount - mClients.size()));
			}
		} else {
			throw new GameFullException();
		}
	}

	/**
	 * Start the game.
	 */
	private void startGame() {
		// Create a new player for every client.
		List<Player> players = new ArrayList<Player>();
		for (ClientHandler client : mClients) {
			Player p = new Player();
			players.add(p);
			playerClientMap.put(client, p);
		}
		mGame = new Game(players);
		
		// Send blocks to the clients
		for (ClientHandler client : mClients) {
			sendBlocksClient(playerClientMap.get(client).getHand(), client);
		}
		
		// Send start game message
		String message = Protocol.Server.STARTGAME;
		for (ClientHandler client : mClients) {
			message += Protocol.Server.Settings.DELIMITER;
			message += client.getName();
		}
		broadcast(message);
		hasGameStarted = true;
		isFirstMove = true;
		
		// Ask all players to do a move
		for (ClientHandler client : mClients) {
			client.sendMessage(getMoveMessage(client.getName(), 
											client.getName(), 
											null));
		}
		
		// Init the first move...
	}
	
	/**
	 * Called by the game. This shuts down everything.
	 */
	public void stopGame() {
		// Send all players back to the lobby server
		broadcast(Protocol.Server.GAME_END);
		for (ClientHandler c : mClients) {
			c.setServer(mMasterServer);
		}
		
		// Set everyting to null
		this.mClients = null;
		this.playerClientMap = null;
		this.mGame = null;
		this.hasGameStarted = false;
		// Remove reference to object.
		mMasterServer.removeGame(this);
	}
	
	/*
	 * Sends the move to the clients.
	 */
	private String getMoveMessage(String playerName, String nextPlayerName, String[] moves) {
		String message = "";
		message += Protocol.Server.MOVE;
		message += Protocol.Server.Settings.DELIMITER;
		message += playerName;
		message += Protocol.Server.Settings.DELIMITER;
		message += nextPlayerName;
		if (moves != null) {
			for (String move : moves) {
				message += Protocol.Server.Settings.DELIMITER;
				message += move;
			}
		}
		return message;
	}

	/**
	 * Removes a client from our list of clients.
	 * 
	 * @param client
	 *            The client that wants to disconnect.
	 */
	@Override
	public void removeHanlder(ClientHandler client) {
		mClients.remove(client);
	}
}