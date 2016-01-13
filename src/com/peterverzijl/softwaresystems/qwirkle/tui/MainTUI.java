package com.peterverzijl.softwaresystems.qwirkle.tui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainTUI {
	
	static class ServerSettings {
		public InetAddress address;
		public int port;
	}
	
	private static boolean mRunning = false;	
	private static ServerTUI mServerTUI;
	private static ServerSettings server;
	
	public static void main(String[] args) {
		System.out.println("Welcome to the Qwirkle TUI!");
		
		// Do the input wait loop
		mRunning = true;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
        	do {
        		System.out.print("Command: ");
        		handleCommand(br.readLine());
    		} while(mRunning);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Handles the execution of the give command.
	 * @param input The command to execute.
	 */
	private static void handleCommand(String input) {
		
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
		} else if (input.contains("EXIT")) {
			System.out.println("Initiating RAGEQUIT...\nExiting Qwirkle.");
			mRunning = false;
			System.exit(0);
		} else {
			System.out.println("I don't speak retard. Command " + input + " does not exist.");
		}
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
			}
			
			try {
				server.port = Integer.parseInt(param[1]);
			} catch (NumberFormatException e) {
				System.out.println("Do you even know what a number is?!");
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
		System.out.println("EXIT \t exits the application.");
	}

	/**
	 * Fills out the server settings.
	 * @param input The input string with the settings.
	 */
	private static void connectServer(String input) {
		server = getSettings(input);
	}

	/**
	 * Creates a server from the given input.
	 * [syntax] <serverAddress> <port>
	 * @param input A string containing the name and the port of the server.
	 */
	private static void createServer(String input) {
		server = getSettings(input);
		try {
			mServerTUI = new ServerTUI(server.address, server.port);
		} catch (IOException e) {
			System.out.println("Oeps, it seems you did something wrong. " + e.getMessage());
		}		
	}

	/**
	 * Opens the chat TUI
	 */
	private static void openChat() {
		String username = "Peter";
		
		// Are we even connected to a server?
		if (server == null) {
			System.out.println("Hey idiot, nobody is going to talk to someone who forgets to connect to a server!");
			return;
		}
		
		try {
			System.out.println("Opening chat client...");
			ChatTUI chat = new ChatTUI(username, server.address, server.port);
			chat.run();
		} catch(IOException e1) {
			System.out.println("Error: could not connect to server at address " + 
								server.address.toString() + ":" + server.port + 
								". Due to " + e1.getMessage());
			System.out.println("Are you stupid?! You can't start a chat at a server that doesn't exist!");
		}
	}
}
