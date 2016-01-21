package com.peterverzijl.softwaresystems.qwirkle.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * A handler thread that handles communication between a client and the server.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class ClientHandler implements Runnable {

	private String mUsername;
	private String[] mFeatures;

	private Server mServer;
	private Socket mSocket;
	private BufferedWriter out;
	private BufferedReader in;

	private boolean mRunning = false;

	public ClientHandler(Server server, Socket socket) {
		mServer = server;
		mSocket = socket;
		
		try {
			in = new BufferedReader(
					new InputStreamReader(
						mSocket.getInputStream(), 
						Charset.forName(Protocol.Server.Settings.ENCODING)));
		} catch (IOException e) {
			System.out.println("Error: could not create buffered reader from socket. Due to: " + e.getMessage());
		}
		
		try {
			out = new BufferedWriter(
					new OutputStreamWriter(
						mSocket.getOutputStream(), 
						Charset.forName(Protocol.Server.Settings.ENCODING)));
		} catch (IOException e) {
			System.out.println("Error: could not create buffered writer from socket. Due to: " + e.getMessage());
		}
	}

	@Override
	public void run() {		
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
			while(in.ready()) {
				mServer.sendMessage(in.readLine(), this);
			}
		} catch (IOException e) {
			System.out.println("Error: could not read message. Assuming client disconnected.");
			System.out.println(e.getMessage());
			shutdown();
		}
	}

	/**
	 * Sends a message to the client.
	 * @param string The message to send to the client.
	 */
	public void sendMessage(String message) {
		try {
			out.write(message + Protocol.Server.Settings.COMMAND_END);
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
			mSocket.close();
			in.close();
			out.close();
			mServer.removeHanlder(this);
		} catch (IOException e) {
			System.out.println("Error: failed to close read and wirte buffers and the socket. Due to: "
								+ e.getMessage());
		}
	}

	/**
	 * Sets the name of the client if that name is null.
	 * @param name The name to set the client user name to.
	 */
	public void setName(String name) {
		if (mUsername == null && name != null) {
			mUsername = name;
		}		
	}
	
	/**
	 * Returns the user name of the client.
	 * @return Returns the username.
	 */
	public String getName() {
		return mUsername;
	}
	
	/**
	 * Returns the supported features by the client.
	 * @return A list of supported features.
	 */
	public String[] getFeatures() {
		return mFeatures;
	}
	
	/**
	 * Sets the features of this client.
	 * @param features
	 */
	public void setFeatures(String[] features) {
		this.mFeatures = features;
	}
	
	/**
	 * Checks if the client supports a certain feature.
	 * @param feature The feature to check for compatibility.
	 * @return Weighter the feature is supported by this client.
	 */
	public boolean supportsFeature(String feature) {
		boolean result = false;
		for (String s : mFeatures) {
			if (s.equals(feature)) {
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * The server that this client handler reports to.
	 * @param server The server to send messages to.
	 */
	public void setServer(Server server) {
		mServer = server;
	}

	/**
	 * Gets the 
	 * @return The ip location of the server.
	 */
	public String getLocation() {
		return mSocket.getInetAddress().getHostAddress();
	}
}
