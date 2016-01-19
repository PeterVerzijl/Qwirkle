package com.peterverzijl.softwaresystems.qwirkle.tests;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.peterverzijl.softwaresystems.qwirkle.networking.Client;
import com.peterverzijl.softwaresystems.qwirkle.networking.LobbyServer;

/**
 * Tests the working of the server and the matchmaking.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class ServerTest {
	
	public static final String ADDRESS = "localhost";
	public static final int PORT = 4444;
	
	public static final String PLAYER1_NAME = "Peter";
	public static final String PLAYER2_NAME = "John";
	
	public static Client client1;
	public static Client client2;
	
	public static LobbyServer server;

	public static void main(String[] args) {
		
		try {
			// Create a server thread
			server = new LobbyServer(
					InetAddress.getByName(ADDRESS), 
					PORT, null);
			(new Thread(server)).start();
			
			// Create a client thread
			client1 = new Client(
					InetAddress.getByName(ADDRESS), 
					PORT, null);
			(new Thread(client1)).start();
			
			// Create a client thread
			client2 = new Client(
					InetAddress.getByName(ADDRESS), 
					PORT, null);
			(new Thread(client2)).start();
			
			// Set the player name
			client1.setPlayerName(PLAYER1_NAME);
			client2.setPlayerName(PLAYER2_NAME);
			
			// Ask for a game
			client1.JoinGame(2);
			client2.JoinGame(2);
			
			
			//server.shutdown();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
