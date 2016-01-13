package com.peterverzijl.softwaresystems.qwirkle.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.peterverzijl.softwaresystems.qwirkle.gui.ChatWindow;
import com.peterverzijl.softwaresystems.qwirkle.ui.ChatView;

/**
 * Client class for the chatting feature in the Qwirkle game.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Client extends Thread {
	
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	
	private ChatView mViewer;
	
	private boolean mRunning = false;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client(String name, InetAddress host, int port, ChatView viewer) throws IOException {
		clientName = name;
		sock = new Socket(host, port);
		mViewer = viewer;
	}

	/**
	 * Reads the messages in the socket connection. Each message will be
	 * forwarded to the MessageUI
	 */
	@Override
	public void run() {
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
		
		// Send name
		sendMessage(clientName);
		
		mRunning = true;
		recieveMessage();
	}
	
	/**
	 * Get the message from the server and display it there.
	 */
	public void recieveMessage() {
		String message;
		try {
			message = in.readLine();
			while(message != null && mRunning) {
				if (message != null) {
					mViewer.addMessage(message);
					message = in.readLine();
				}
			}
		} catch (IOException e) {
			// Close socket
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * send a message to a ClientHandler.
	 */
	public void sendMessage(String msg) {
		try {
			out.write(msg + System.lineSeparator());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * close the socket connection.
	 */
	public void shutdown() {
		mRunning = false;
		System.out.println("Closing socket connection...");
		sendMessage(clientName + " left the chat.");
		try {
			sock.close();
			in.close();
			out.close();
		} catch (IOException e) {
			mViewer.addMessage("Left chat.");
		}
	}
	
	/** 
	 * returns the client name 
	 */
	public String getClientName() {
		return clientName;
	}
}
