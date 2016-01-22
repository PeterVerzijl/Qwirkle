package com.peterverzijl.softwaresystems.qwirkle.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.peterverzijl.softwaresystems.qwirkle.Block;
import com.peterverzijl.softwaresystems.qwirkle.Board;
import com.peterverzijl.softwaresystems.qwirkle.HumanTUIPlayer;
import com.peterverzijl.softwaresystems.qwirkle.Node;
import com.peterverzijl.softwaresystems.qwirkle.Player;
import com.peterverzijl.softwaresystems.qwirkle.tui.MainTUI;
import com.peterverzijl.softwaresystems.qwirkle.ui.ChatView;

/**
 * Client class for the chatting feature in the Qwirkle game.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Client implements Runnable {
	
	private String username;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	
	private Map<String, Player> mPlayerNameMap;
	
	private ChatView mViewer;
	
	// Game stuff
	private Board mBoard;
	private Player mPlayer;
	private HumanTUIPlayer gameTUI;
	
	private boolean mRunning = false;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client(InetAddress host, int port, ChatView viewer) throws IOException {
		sock = new Socket(host, port);
		mViewer = viewer;
		
		try {
			out = new BufferedWriter(
					new OutputStreamWriter(
							sock.getOutputStream(), 
							Charset.forName(Protocol.Server.Settings.ENCODING)));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		try {
			in = new BufferedReader(
					new InputStreamReader(
							sock.getInputStream(), 
							Charset.forName(Protocol.Server.Settings.ENCODING)));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Reads the messages in the socket connection. Each message will be
	 * forwarded to the MessageUI
	 */
	@Override
	public void run() {
		mRunning = true;
		recieveMessage();
	}
	
	/**
	 * Get the message from the server and display it there.
	 */
	private void recieveMessage() {
		try {
			while(mRunning) {
				while(in.ready()) {
					handleMessage(in.readLine());
				}
			}
		} catch (IOException e) {
			// Safe to close the socket.
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Handles the messages
	 * @param message
	 */
	private void handleMessage(String message) {
		if (message.length() < 1) { return; }
		
		String[] parameters = message.split("" + Protocol.Server.Settings.DELIMITER);
		String command = parameters[0];
		// Remove command from parameters
		parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
		switch(command) {
			case Protocol.Server.ADDTOHAND:
				// Gets the hand from the server
				for (String blockString : parameters) {
					Block b = Block.getBlockFromCharPair(blockString);
					mPlayer.addBlock(b);
				}
				break;
			case Protocol.Server.CHAT:
				if(mViewer != null) {
					// Remove the header thing
					mViewer.displayMessage(parameters[0]);
				}
				break;
			case Protocol.Server.DECLINEINVITE:
				// TODO (peter) : Implement inviting.
				break;
			case Protocol.Server.ERROR:
				try {
					int error = Integer.parseInt(parameters[0]);
					handleError(error);
				} catch (NumberFormatException e) {
					// TODO (peter) : Error logging in a file?
				}
				break;
			case Protocol.Server.GAME_END:
				endGame();
				break;
			case Protocol.Server.HALLO:
				// Print server name and featues
				for (int i = 0; i < parameters.length; i++) {
					if (i == 0) {
						System.out.println("Connected to server: " + parameters[i]);
						System.out.println("Supported features: ");
					} else {
						System.out.println(" - " + parameters[i]);
					}
				}
				break;
			case Protocol.Server.INVITE:
				// TODO (peter) : Implement challenging
				break;
			case Protocol.Server.LEADERBOARD:
				// TODO (peter) : Implement leader board
				break;
			case Protocol.Server.MOVE:
				// MOVE_<playerName>_<nextPlayerName>_<move>_<move>_..\n\n
				String movingPlayer = parameters[0];
				String nextPlayer = parameters[1];
				String[] moves = Arrays.copyOfRange(parameters, 2, parameters.length);
				for (String move : moves) {
					Node n = Board.moveStringToNode(move);
					//mBoard.setPlacedBlock(n);
				}
				if (!movingPlayer.equals(username)) {
					// Message the viewer that a move has been done
					if (mViewer != null) {
						mViewer.displayMessage(movingPlayer + " has made a move.");
						// Draw updated board
						mViewer.displayMessage(nextPlayer + " now has the turn.");
						if (moves.length > 0) {
							//mViewer.displayMessage(Board.boardToString(mBoard.getPlacedBlocks(), null);
						}
					}
				}
				// Is it our turn?
				if (nextPlayer.equals(username)) {
					// TODO (peter) : Contact PlayerTUI
					gameTUI.determineMove(mBoard);
				}
				break;
			case Protocol.Server.OKWAITFOR:
				try {
					int numPlayers = Integer.parseInt(parameters[0]);
					if (mViewer != null) {
						mViewer.displayMessage("Waiting for " + numPlayers + " other players...");
					}
				} catch (NumberFormatException e) {
					// TODO (peter) : Error logging in a file?
				}
				break;
			case Protocol.Server.STARTGAME:
				if (mViewer != null) {
					mViewer.displayMessage("Game is starting!!");
					mViewer.displayMessage("Opponents:");
					for (String name : parameters) {
						mViewer.displayMessage(name);
					}					
				}
				
				///mGame = new Game();
				break;
			case Protocol.Server.STONESINBAG:
				try {
					int numStones = Integer.parseInt(parameters[0]);
					if (mViewer != null) {
						mViewer.displayMessage("There are " + numStones + 
												" stones left in the bag.");
					}
				} catch (NumberFormatException e) {
					// TODO (peter) : Error logging in a file?
				}
				break;
			default:
				if (mViewer != null) {
					mViewer.displayMessage(message);
				}
				break;
		}
	}
	
	/**
	 * Show the viewer that the game has stopped.
	 */
	private void endGame() {
		if (mViewer != null) {
			mViewer.displayMessage("The game has ended. " + 
									"You can disconnect now.");
			shutdown();
		}
	}
	
	/**
	 * Handles an error message gotten from the server.
	 * Code | Definition
	 * ---- | -----------------
	 *   1  | notYourTurn
	 *   2  | notYourStone
	 *   3  | notEnoughStones
	 *   4  | nameExists
	 *   5  | notChallengable
	 *   6  | ChallengerRefused
	 *   7  | invalidMove
	 * @param error The error code.
	 */
	private void handleError(int error) {
		// TODO Auto-generated method stub
		switch(error) {
			case 1:			// Not your turn
				if (mViewer != null) {
					mViewer.displayMessage("Nope.");
				}
				break;
			case 2:			// Not your stone
				if (mViewer != null) {
					mViewer.displayMessage("Not your stone.");
				}
				break;
			case 3:			// Not enough stones
				if (mViewer != null) {
					mViewer.displayMessage("Oeps, it seems the server has run out of stones.");
				}
				break;
			case 4:			// Name Exists
				if (mViewer != null) {
					mViewer.displayMessage("Name already taken.");
				}
				MainTUI.askName();
				break;
			case 5:			// Not your turn
				// TODO (peter) : Implement challenging
				break;
			case 6:			// Challenger Refused
				// TODO (peter) : Implement challenging
				break;
			case 7:			// Invalid move
				if (mViewer != null) {
					mViewer.displayMessage("Invalid move.");
				}
				break;
			case 8:			// General error ??
				if (mViewer != null) {
					mViewer.displayMessage("Nope.");
				}
				break;
		}
	}

	/** 
	 * send a message to a ClientHandler.
	 * @param msg The message to send to the client handler.
	 */
	private void sendMessage(String msg) {
		try {
			out.write(msg + Protocol.Server.Settings.COMMAND_END);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a chat message to the server.
	 * @param message The chat message.
	 */
	public void sendChatMessage(String message) {
		sendMessage(Protocol.Client.CHAT + 
					Protocol.Server.Settings.DELIMITER + 
					message);
	}
	
	/** 
	 * close the socket connection.
	 */
	public void shutdown() {
		mRunning = false;
		System.out.println("Closing socket connection...");
		sendMessage(username + " left the chat.");
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			if(mViewer != null) mViewer.displayMessage("Left chat.");
		}
	}
	
	/** 
	 * returns the client name 
	 */
	public String getName() {
		return username;
	}
	
	/**
	 * Sets the viewer of this client.
	 * @param view The viewer of the client.
	 */
	public void setViewer(ChatView view) {
		mViewer = view;
	}
	
	/**
	 * Send to the server that we are ready to connect.
	 * @param name The client name.
	 */
	public void setPlayerName(String name) {
		username = name;
		sendMessage(Protocol.Client.HALLO + 
					Protocol.Server.Settings.DELIMITER + 
					username);
	}

	/**
	 * Request a game with a certain amount of opponents.
	 * @param numPlayers The ammount of opponents to play with.
	 */
	public void joinGame(int numPlayers) {
		sendMessage(Protocol.Client.REQUESTGAME + 
				Protocol.Server.Settings.DELIMITER + 
				numPlayers);
		// Init game variables here in case we get blocks before a game start.
		mPlayer = new Player();
		mBoard = new Board();
		gameTUI = new HumanTUIPlayer();
	}

	/**
	 * Asks the server to push the amount of stones in the bag.
	 */
	public void getNumStones() {
		sendMessage(Protocol.Client.GETSTONESINBAG);
	}

	/**
	 * Returns the player associated with the client.
	 * @return The player associated with the client.
	 */
	public Player getPlayer() {
		return mPlayer;
	}
	
	/**
	 * Returns the string form of the player hand.
	 * @return The player hand in string form.
	 */
	public String getPlayerHand() {
		return Player.handToString(mPlayer.getHand());
	}
	
	/**
	 * Trades stones with the server given that we think we have them.
	 * @param indexList indexes of blocks to trade.
	 */
	public void tradeBlocks(List<Integer> indexList) {
		// Reverse order such that we don't run into index problems
		Collections.sort(indexList, Collections.reverseOrder());
		// Get the blocks
		if (indexList.size() <= mPlayer.getHand().size()) {
			List<Block> blocks = new ArrayList<Block>();
			for (int i : indexList) {
				blocks.add(mPlayer.getHand().get(i));
				// Remove old blocks
				mPlayer.getHand().remove(i);
			}
			// Get new blocks
			tradeBlocksServer(blocks);
		} else {
			if (mViewer != null) {
				mViewer.displayMessage("You do not have a stone at that index!");
			}
		}
	}
	
	/**
	 * Trades stones with the server given that we think we have them.
	 * @param blocks The blocks to trade.
	 */
	void tradeBlocksServer(List<Block> blocks) {
		String message = Protocol.Client.CHANGESTONE;
		for (Block b : blocks) {
			message += Protocol.Server.Settings.DELIMITER;
			message += Block.toChars(b);
		}
		sendMessage(message);		
	}
	
	/**
	 * Sends a user input command to the server.
	 * @param input Command
	 */
	public void sendCommand(String input) {
		sendMessage(input);	
	}
}
