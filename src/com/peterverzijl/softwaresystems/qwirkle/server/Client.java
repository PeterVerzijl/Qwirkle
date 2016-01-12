package com.peterverzijl.softwaresystems.qwirkle.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.peterverzijl.softwaresystems.qwirkle.gui.ChatWindow;

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
	
	private ChatWindow mGUI;
	
	private boolean mRunning = false;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client(String name, InetAddress host, int port, ChatWindow gui) throws IOException {
		clientName = name;
		sock = new Socket(host, port);
		mGUI = gui;
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
		while(mRunning) {
			recieveMessage();
		}
	}
	
	/**
	 * Get the message from the server and display it there.
	 */
	public void recieveMessage() {
		String message;
		try {
			message = in.readLine();
			if (message != null) {
				mGUI.addMessage(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * send a message to a ClientHandler.
	 */
	public void sendMessage(String msg) {
		if (!msg.trim().equals("exit")) {
			try {
				out.write(msg + System.lineSeparator());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				out.write(clientName + " left the chat.");
				shutdown();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 
	 * close the socket connection.
	 */
	public void shutdown() {
		mRunning = false;
		System.out.println("Closing socket connection...");
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * returns the client name 
	 */
	public String getClientName() {
		return clientName;
	}
}
