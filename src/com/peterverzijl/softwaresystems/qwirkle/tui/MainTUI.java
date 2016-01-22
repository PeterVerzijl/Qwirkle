package com.peterverzijl.softwaresystems.qwirkle.tui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.networking.Client;

public class MainTUI {
	
	static class ServerSettings {
		public InetAddress address;
		public int port;
	}
	
	private static boolean mRunning = false;	
	
	// private static Thread mServerThread;
	private static ServerTUI mServerTUI;
	private static ServerSettings server;
	
	private static Client mClient;
	// private static Thread mClientThread;
	
	private static boolean mInGame = false;
	
	public static void main(String[] args) {
		System.out.println("Welcome to the Qwirkle TUI!");
		System.out.println("You can now start typing commands.");
		
		// Do the input wait loop
		mRunning = true;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
        	do {
        		handleCommand(br.readLine());
    		} while(mRunning);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Handles the execution of the give command.
	 * @param input The command to execute.
	 */
	private static void handleCommand(String input) {
		if (input.contains("help")) {
			System.out.println("Perhaps you need HELP.");
			return;
		}
		
		input = input.toUpperCase();
		
		// Show the help
		if (input.contains("HELP")) {
			showHelp();
		} else if (input.contains("CHAT")) {
			openChat();
		} else if (input.contains("SERVER CREATE")) {
			input = input.replace("SERVER CREATE ", "");
			createServer(input);
		} else if (input.contains("SERVER CONNECT")) {
			input = input.replace("SERVER CONNECT ", "");
			connectServer(input);
		} else if (input.contains("SERVER MESSAGES")) {
			if (mServerTUI != null) {
				mServerTUI.displayMessages();
			} else {
				System.out.println("No.");
			}
		} else if (input.contains("SERVER COMMAND")) {
			if (mClient != null) {
				input = input.replace("SERVER COMMAND ", "");
				mClient.sendCommand(input);
			}
		} else if (input.contains("GAME JOIN")) {
			if (mClient != null) {
				if (!mInGame) {
					tryJoinGame(input);
				} else {
					System.out.println("You are already in a game!");
				}
			} else {
				System.out.println("Go play with yourself somewhere else!");
			}
		} else if (input.contains("GAME STONE AMOUNT")) {
			if (mInGame && mClient != null) {
				mClient.getNumStones();
			} else {
				System.out.println("Deez nuts are in Da sack.");
			}
		} else if (input.contains("GAME HAND")) {
			if (mInGame && mClient != null) {
				System.out.println(mClient.getPlayerHand());
			} else {
				System.out.println("Put your hand in my pants and I bet you feel nutz!");
			}
		} else if (input.contains("GAME TRADE")) {
			input = input.replace("GAME TRADE ", "");
			if (mInGame && mClient != null) {
				String[] blocks = input.split(" ");
				List<Integer> indexList = new ArrayList<Integer>();
				for (int i = 0; i < blocks.length; i++) {
					indexList.add(Integer.parseInt(blocks[i]));
				}
				mClient.tradeBlocks(indexList);
			} else {
				System.out.println("You have nothing of value to me.");
			}
		} else if (input.contains("WHOAMI")) {
			if (mClient != null && mClient.getName() != null) {
				System.out.println(mClient.getName());
			} else {
				System.out.println("How am I supposed to know?!");
			}			
		} else if (input.contains("EXIT")) {
			System.out.println("Initiating RAGEQUIT...\nExiting Qwirkle.");
			mRunning = false;
			System.exit(0);
		} else {
			System.out.println("I don't speak retard. Command " + input + " does not exist.");
		}
	}

	private static void tryJoinGame(String input) {
		// Remove the command
		input = input.replaceAll("GAME JOIN ", "");
		String[] param = input.split(" ");
		
		// Try to get the number
		if (param.length != 1) {
			// Stupid
			System.out.println("");
			return;
		}
		try {
			int numPlayers = Integer.parseInt(param[0]);
			mClient.joinGame(numPlayers);
			System.out.println("Waiting for other players...");
		} catch (NumberFormatException e) {
			System.out.println("Do you even know what a number is?!");
			return;
		}
		mInGame = true;
	}

	/**
	 * Gets the settings from a string file.
	 * @param input The server settings in string.
	 * @return Either the previous server settings
	 * 			or the created server settings.
	 */
	private static ServerSettings getSettings(String input) {
		if (server == null) {
			server = new ServerSettings();
						
			String[] param = input.split(" ");		
			// Check if the correct amount of parameters is supplied
			if (param.length != 2) {
				System.out.println("RTFM!");
				return null;
			}
			
			// Check if the correct parameters are given.
			try {
				server.address = InetAddress.getByName(param[0]);
			} catch (UnknownHostException e) {
				System.out.println("Apparently typing in correct IP adresses is hard.");
				return null;
			}
			
			try {
				server.port = Integer.parseInt(param[1]);
			} catch (NumberFormatException e) {
				System.out.println("Do you even know what a number is?!");
				return null;
			}
		}
		return server;
	}
	
	/**
	 * Shows the help information
	 */
	private static void showHelp() {
		System.out.println("Commands:");
		System.out.println("CHAT \t opens chat");
		System.out.println("SERVER CONNECT <address> <port> \t connects to a server.");
		System.out.println("SERVER CREATE <address> <port> \t creates server.");
		System.out.println("SERVER MESSAGES \t shows all server messages.");
		System.out.println("GAME JOIN <number of opponents> \t tries to join a game with x opponents.");
		System.out.println("GAME STONE AMOUNT \t asks the game how many stones are left in the game bag.");
		System.out.println("GAME HAND \t asks the game to display the stones in the hand.");
		System.out.println("WHOAMI \t shows the name of the user.");
		System.out.println("EXIT \t exits the application.");
	}

	/**
	 * Fills out the server settings.
	 * @param input The input string with the settings.
	 */
	private static void connectServer(String input) {
		server = getSettings(input);
		if (mClient == null && server != null) {
			try {
				mClient = createClient();
				// Now fetch the name of the user
				System.out.println("Connecting to server...");
				// Ask the player for his name.
				askName();
			} catch (IOException e) {
				System.out.println("Failed to create a client. Due to: " + e.getMessage());
			}
		}
	}
	
	public static void askName() {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));
		try {
			String name = "";
			 do {
				 System.out.println("Enter your name: ");
				 name = br.readLine();
				 if (name.length() < 2) {
					 System.out.println("Enter a longer name!");
				 }
			 } while (name.length() < 2);
			 mClient.setPlayerName(name);
		} catch(IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	/**
	 * Tries to create a client;
	 */
	private static Client createClient() throws IOException {
		Client client = null;
		client = new Client(server.address, server.port, new ClientViewer());
		(new Thread(client)).start();
		return client;
	}

	/**
	 * Creates a server from the given input.
	 * [syntax] <serverAddress> <port>
	 * @param input A string containing the name and the port of the server.
	 */
	private static void createServer(String input) {
		server = getSettings(input);
		if (server != null) {
			try {
				mServerTUI = new ServerTUI(server.address, server.port);
				connectServer(input);
			} catch (IOException e) {
				System.out.println("Oeps, it seems you did something wrong. " + e.getMessage());
			}
		} else {
			System.out.println("Forgot to enter the rest of the command?");
		}
	}

	/**
	 * Opens the chat TUI
	 */
	private static void openChat() {
		// Are we even connected to a server?
		if (server == null || mClient == null) {
			System.out.println("Hey idiot, nobody is going to talk to someone who forgets to connect to a server!");
			return;
		}
		
		try {
			System.out.println("Opening chat client...");
			ChatTUI chat = new ChatTUI(mClient);
			chat.run();
		} catch(IOException e1) {
			System.out.println("Error: could not connect to server at address " + 
								server.address.toString() + ":" + server.port + 
								". Due to " + e1.getMessage());
			System.out.println("Are you stupid?! You can't start a chat at a server that doesn't exist!");
		}
	}
}
