package com.peterverzijl.softwaresystems.qwirkle.tui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.peterverzijl.softwaresystems.qwirkle.server.Server;
import com.peterverzijl.softwaresystems.qwirkle.ui.ServerView;

public class ServerTUI implements ServerView {
	
	private Server mServer;
	private InetAddress mAddress;
	private Thread mServerThread;
	
	public ServerTUI() {
		try {
			mAddress = InetAddress.getByName("localhost");
			mServer = new Server(mAddress, Server.PORT, this);			
			mServerThread = new Thread(mServer);
			mServerThread.start();
		} catch (UnknownHostException e) {
			System.out.println("Error, couldn't create a server at the port and address. Due to: " + e.getMessage());
			return;
		} catch (IOException e) {
			System.out.println("Error, couldn't create a server at the port and address. Due to: " + e.getMessage());
			return;
		}
	}

	@Override
	public void sendMessage(String message) {
		// TODO Auto-generated method stub
		
	}
}
