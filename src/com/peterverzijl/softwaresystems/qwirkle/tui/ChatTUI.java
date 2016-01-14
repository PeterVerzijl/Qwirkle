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
		System.out.println("Welcome to the chat!\n" +
							"Chat commands:\n" +
							" - \\exit \t exits the app.\n" +
							"Start saying something!");
		
		// Do the input wait loop
		
		mRunning = true;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
        	while(mRunning) {
        		String input = br.readLine();
        		if ("\\exit".equals(input)) {
        			System.out.println("Exiting the chat application.");
        			close();
        		} else {
        			mClient.sendChatMessage(input);
        		}
    		}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			// TODO (Peter) : Chat TUI should recieve a client as parameter
			/*
			mChatThread.interrupt();
			try {
				mChatThread.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			*/
			close();
		}
	}

	private void close() {
		mRunning = false;
	}

	@Override
	public void addMessage(String message) {
		System.out.println(message);
	}
}
