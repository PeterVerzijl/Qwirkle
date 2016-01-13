package com.peterverzijl.softwaresystems.qwirkle.tui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import com.peterverzijl.softwaresystems.qwirkle.server.Client;
import com.peterverzijl.softwaresystems.qwirkle.ui.ChatView;

public class ChatTUI implements ChatView {
	
	private boolean mRunning = false;
	private Client mClient;
	private Thread mChatThread;
	
	public ChatTUI(String username, InetAddress serverAddress, int serverPort) throws IOException {
		mClient = new Client(username, serverAddress, serverPort, this);
		mChatThread = new Thread(mClient);
		mChatThread.start();
	}
	
	/**
	 * Starts the chat TUI
	 */
	public void run() {
		System.out.println("Welcome to the chat!");
		System.out.println("Start saying something!");
		
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
        			mClient.sendMessage(input);
        		}
    		}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			mChatThread.interrupt();
			try {
				mChatThread.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
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
