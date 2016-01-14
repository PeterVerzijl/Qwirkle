package com.peterverzijl.softwaresystems.qwirkle.tui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.peterverzijl.softwaresystems.qwirkle.networking.Server;
import com.peterverzijl.softwaresystems.qwirkle.ui.ServerView;

public class ServerTUI implements ServerView {
	
	private Server mServer;
	private InetAddress mAddress;
	private int mPort;
	private Thread mServerThread;
	
	private String messageBuffer;
	
	/**
	 * 
	 * @param address
	 * @param port
	 */
	public ServerTUI(InetAddress address, int port) throws IOException {
		mAddress = address;
		mPort = port;
		
		mServer = new Server(mAddress, mPort, this);			
		mServerThread = new Thread(mServer);
		mServerThread.start();
	}

	@Override
	public void sendMessage(String message) {
		messageBuffer += ("Server: " + message + "\n");		
	}
	
	/**
	 * Displays all server messages
	 */
	public void displayMessages() {
		System.out.println(messageBuffer);
	}
}
