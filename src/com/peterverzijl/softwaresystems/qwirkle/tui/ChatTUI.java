package com.peterverzijl.softwaresystems.qwirkle.tui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import com.peterverzijl.softwaresystems.qwirkle.networking.Client;
import com.peterverzijl.softwaresystems.qwirkle.ui.ChatView;

public class ChatTUI implements ChatView {
	
	private boolean mRunning = false;
	private Client mClient;
	
	public ChatTUI(Client client) throws IOException {
		mClient = client;
		mClient.setViewer(this);
	}
	
	/**
	 * Starts the chat TUI
	 */
	public void run() {
		System.out.println("------------------------------------------\n" +
							"| Welcome to the chat!                   |\n" +
							"| Commands:                              |\n" +
							"| - \\exit    exits the app.              |\n" +
							"|                                        |\n" +
							"| Start typing to say something!         |\n" +
							"------------------------------------------");
		
		// Do the input wait loop
		
		mRunning = true;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
        	while(mRunning) {
        		if (br.ready()) {
	        		String input = br.readLine();
	        		if ("\\exit".equals(input)) {
	        			System.out.println("Exiting the chat application.\n" + 
	        								"------------------------------------------");
	        			close();
	        		} else {
	        			mClient.sendChatMessage(input);
	        		}
        		}
    		}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			close();
		}
	}

	private void close() {
		mRunning = false;
	}

	@Override
	public void displayMessage(String message) {
		System.out.println(message);
	}

	@Override
	public void askName() {
		MainTUI.askName();		
	}
}
