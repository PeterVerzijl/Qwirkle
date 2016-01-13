package com.peterverzijl.softwaresystems.qwirkle.tui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainTUI {
	
	private static boolean mRunning = false;
	
	public static void main(String[] args) {
		System.out.println("Welcome to the chat!");
		
		// Do the input wait loop
		mRunning = true;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Command: ");
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
		// Open command opens modules
		if (input.contains("OPEN: ")) {
			input.replace("OPEN: ", "");
			// Open the chat module
			if (input.contains("chat")) {
				openChat();
			// If no module found, give an error.
			} else {
				System.out.println("Can't open module " + input);
			}
		} else if (input.contains("EXIT")) {
			System.out.println("Exiting Qwirkle...");
			mRunning = false;
		} else {
			
		}
	}
	
	/**
	 * Opens the chat TUI
	 */
	private static void openChat() {
		String username = "Peter";
		InetAddress serverAddress = null;
		try {
			serverAddress = InetAddress.getLocalHost();
			int serverPort = 9999;
			try {
				System.out.println("Opening chat client...");
				ChatTUI chat = new ChatTUI(username, serverAddress, serverPort);
				chat.run();
			} catch(IOException e1) {
				System.out.println("Error: could not create server at address " + 
						serverAddress.toString() + ":" + serverPort + 
						". Due to " + e1.getMessage());
			}
		} catch (UnknownHostException e) {
			System.out.println("Error: Host " + serverAddress + " does not exist.");
			System.out.println(e.getMessage());
		}
	}
}
