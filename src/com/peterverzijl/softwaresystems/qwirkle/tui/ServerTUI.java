package com.peterverzijl.softwaresystems.qwirkle.tui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.peterverzijl.softwaresystems.qwirkle.networking.LobbyServer;
import com.peterverzijl.softwaresystems.qwirkle.networking.Server;
import com.peterverzijl.softwaresystems.qwirkle.ui.ServerView;

public class ServerTUI implements ServerView {
	
	private LobbyServer mServer;
	private InetAddress mAddress;
	private int mPort;
	private Thread mServerThread;
	
	private String messageBuffer;
	
	/**
	 * Constructor creates a server at the given address and port.
	 * It creates a lobby server in the process.
	 * @param address The IP address of the server.
	 * @param port The port on the server where clients can connect to.
	 * @throws IOException when the lobby server could not be created.
	 */
	public ServerTUI(InetAddress address, int port) throws IOException {
		mAddress = address;
		mPort = port;
		
		mServer = new LobbyServer(mAddress, mPort, this);			
		mServerThread = new Thread(mServer);
		mServerThread.start();
	}

	@Override
	public void sendMessage(String message) {
		messageBuffer += "Server: " + message + "\n";		
	}
	
	/**
	 * Displays all server messages.
	 */
	public void displayMessages() {
		System.out.println(messageBuffer);
	}
}
