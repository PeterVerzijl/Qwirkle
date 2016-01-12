package com.peterverzijl.softwaresystems.qwirkle.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * A handler thread that handles communication between a client and the server.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class ClientHandler implements Runnable {

	public String name;
	
	private Server mServer;
	private Socket mSocket;
	private BufferedWriter out;
	private BufferedReader in;

	private boolean mRunning = false;

	public ClientHandler(Server server, Socket socket) {
		mServer = server;
		mSocket = socket;
	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(
					new InputStreamReader(
						mSocket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Error: could not create buffered reader from socket. Due to: " + e.getMessage());
		}
		
		try {
			out = new BufferedWriter(
					new OutputStreamWriter(
						mSocket.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Error: could not create buffered writer from socket. Due to: " + e.getMessage());
		}
		
		try {
			name = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mRunning  = true;
		while (mRunning) {
			processMessages();
		}
	}

	/**
	 * Processes messages gotten from the client.
	 */
	private void processMessages() {
		try {
			mServer.broadcast(in.readLine());
		} catch (IOException e) {
			System.out.println("Error: could not read message. Assuming client disconnected.");
			shutdown();
		}
	}

	/**
	 * Sends a message to the client.
	 * @param string The message to send to the client.
	 */
	public void sendMessage(String message) {
		try {
			out.write(message + System.lineSeparator());
			out.flush();
		} catch (IOException e) {
			System.out.println("Error: could not read message. Assuming client disconnected.");
			shutdown();
		}
	}
	
	/**
	 * Closes the client handler and all its buffers.
	 */
	public void shutdown() {
		mRunning = false;
		try {
			in.close();
			out.close();
			mSocket.close();
			mServer.removeHanlder(this);
		} catch (IOException e) {
			System.out.println("Error: failed to close read and wirte buffers and the socket. Due to: "
								+ e.getMessage());
		}
	}

}
