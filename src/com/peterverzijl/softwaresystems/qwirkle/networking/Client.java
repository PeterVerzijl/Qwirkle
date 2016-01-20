package com.peterverzijl.softwaresystems.qwirkle.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import com.peterverzijl.softwaresystems.qwirkle.gui.ChatWindow;
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
	
	private ChatView mViewer;
	
	private boolean mRunning = false;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client(InetAddress host, int port, ChatView viewer) throws IOException {
		sock = new Socket(host, port);
		mViewer = viewer;
		
		try {
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
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
		String[] parameters = message.split("" + Protocol.Server.Settings.DELIMITER);
		String command = parameters[0];
		// Remove command from parameters
		parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
		switch(command) {
			case Protocol.Server.ADDTOHAND:
				
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
				// TODO (peter) : Get the move from the server and roll with it!
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
	
	private void endGame() {
		if (mViewer != null) {
			mViewer.displayMessage("The game has ended. " + 
									"You can disconnect now.");
			shutdown();
		}
	}

	private void handleError(int error) {
		// TODO Auto-generated method stub
		switch(error) {
			case 1:			// Not your turn
				break;
			case 2:			// Not your turn
				break;
			case 3:			// Not your turn
				break;
			case 4:			// Name Exists
				// Name was refused by the server
				MainTUI.askName();
				break;
			case 5:			// Not your turn
				// TODO (peter) : Implement challenging
				break;
			case 6:			// Challenger Refused
				// TODO (peter) : Implement challenging
				break;
			case 7:			// Invalid move
				break;
			case 8:			// General error ??
				break;
		}
	}

	/** 
	 * send a message to a ClientHandler.
	 * @param msg The message to send to the client handler.
	 */
	private void sendMessage(String msg) {
		try {
			out.write(msg + System.lineSeparator());
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
	}
}
