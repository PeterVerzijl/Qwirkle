package com.peterverzijl.softwaresystems.qwirkle.server;

/**
 * Based on marti's protocol implementation.
 * @author Peter Verzijl
 *
 */
public class Protocol {
	
	/**
	 * A list of all messages that the client can send to the server.
	 * @author Peter Verzijl
	 * @version 1.0
	 */
	public static class Client {
		
		/**
		 * Opening statement to connect with the server
		 * [syntax] <features> : <feature>*<feature>*...
		 * [syntax] HALLO_<playerName>_<features>\n\n
		 * features: chat, challenge, security, leader board
		 */
		public static final String HALLO = "HALLO";
		
		/**
		 * Disconnect from server.
		 */
		public static final String QUIT = "QUIT";

		/**
		 * To invite a player to play the game.
		 * [syntax] INVITE_<playerName>\n\n<
		 */
		public static final String INVITE = "INVITE";
		
		/**
		 * Accepts an invite by a player.
		 * [syntax] ACCEPTINVITE\n\n
		 */
		public static final String ACCEPTINVITE = "ACCEPTINVITE";
		
		/**
		 * Declines an invite by a player.
		 * [syntax] DECLINEINVITE\n\n
		 */
		public static final String DECLINEINVITE = "DECLINEINVITE";

		/*
		 * Makes a move on the board. Can do multiple moves at once.
		 * Move representation:
		 * Char | Color  | Shape
		 * ---- | ------ | ------
		 *  A   | RED    | CIRCLE
		 *  B   | ORANGE | CROSS
		 *  C   | YELLOW | DIAMOND
		 *  D   | GREEN  | SQUARE
		 *  E   | BLUE   | STAR
		 *  F   | PRUPLE | PLUS
		 * 
		 * 				  ColorShape*X*Y
		 * [Syntax] move: charchar*int*int\n\n
		 * [syntax] MAKEMOVE_<command>_BF*11*7\n\n
		 */
		public static final String MAKEMOVE = "MAKEMOVE";
		
		/**
		 * Send a chat message to the server
		 * [Syntax] CHAT_<string>\n\n
		 */
		public static final String CHAT = "CHAT";
		
		/**
		 * Request a game from the server with a number of players <int>.
		 * [Syntax] REQUESTGAME_<numPlayers>\n\n
		 */
		public static final String REQUESTGAME = "REQUESTGAME";
		
		/**
		 * Call the server to get new stones 
		 * [Syntax] CHANGESTONE_<steen>_<steen>\n\n
		 */
		public static final String CHANGESTONE = "CHANGESTONE";
		
		/**
		 * Request the leader board
		 * [Syntax] GETLEADERBOARD\n\n
		 */
		public static final String GETLEADERBOARD = "GETLEADERBOARD";
		
		/**
		 * Returns the number of stones in the bag
		 * [Syntax] GETSTONESINBAG\n\n
		 */
		public static final String GETSTONESINBAG = "GETSTONESINBAG";
		
		/**
		 * Sends an error to the server, no errors have been defined yet
		 * [Syntax] ERROR_<errorNo>\n\n
		 */
		public static final String ERROR = "ERROR";
	}

	/**
	 * Class of all messages that the server can send to the client.
	 * @author Peter Verzijl
	 * @version 1.0
	 */
	public static class Server {
		
		/**
		 * Responds to a hallo from the server to a client
		 * [Syntax] HALLO_<serverName>_<feature>_<feature>_..\n\n
		 */
		public static final String HALLO = "HALLO";
		
		/**
		 * All error messages that are send to the client
		 * Code | Definition
		 * ---- | -----------------
		 *   1  | notYourTurn
		 *   2  | notYourStone
		 *   3  | notEnoughStones
		 *   4  | nameExists
		 *   5  | notChallengable
		 *   6  | ChallengerRefused
		 *   7  | invalidMove
		 * 
		 * [Syntax] ERROR_<errorNo>\n\n 
		 */
		public static final String ERROR = "ERROR";
		
		/**
		 * Sends the amount of players we are waiting for
		 * [Syntax] OKWAITFOR_<numPlayers>\n\n
		 */
		public static final String OKWAITFOR = "OKWAITFOR";
		
		/**
		 * Message the clients that the game is starting 
		 * and who they are playing with.
		 * [Syntax] STARTGAME_<playerName>_<playerName>_..\n\n
		 */
		public static final String STARTGAME = "STARTGAME";
		
		/**
		 * Message the clients that the game has finished
		 * [Syntax] END\n\n
		 */
		public static final String GAME_END = "END";
		
		/**
		 * Message the clients that the game has finished
		 * 					ColorShape*X*Y
		 * [Syntax] <move> CharChar*int*int
		 * [Syntax] MOVE_<playerName>_<nextPlayerName>_<move>_<move>_..\n\n
		 */
		public static final String MOVE = "MOVE";
		
		/**
		 * Send a chat message to the clients.
		 * Server appends user name to the message
		 * [Syntax] CHAT_<message>\n\n
		 */
		public static final String CHAT = "CHAT";
		
		/**
		 * Server sends stones to the player hand.
		 * 					ColorShape
		 * [Syntax] <stone> CharChar
		 * [Syntax] ADDTOHAND_<stone>_<stone>_..\n\n
		 */
		public static final String ADDTOHAND = "ADDTOHAND";
		
		/**
		 * Server sends the amount of stones in the bag.
		 * [Syntax] STONESINBAG_<numStones>\n\n
		 */
		public static final String STONESINBAG = "STONESINBAG";

		/**
		 * Server sends the leader board to the clients.
		 * [Syntax] LEADERBOARD_<playerName>*<rank>_<playerName>*<rank>_..\n\n
		 */
		public static final String LEADERBOARD = "LEADERBOARD";
		
		/**
		 * The constants for the feature names.
		 * @author Peter Verzijl
		 * @version 1.0
		 */
		public static class Features {
			public static final String CHAT = "CHAT";
			public static final String LEADERBOARD = "LEADERBOARD";
			public static final String SECURITY = "SECURITY";
			public static final String CHALLENGE = "CHALLENGE"; // Deze functie wordt nog niet verwacht wordt dat SSLsocket gebruikt gaat worden
		}
		
		/**
		 * Command to invite a player
		 */
		public static final String INVITE = "INVITE";
		
		/**
		 * 
		 */
		public static final String DECLINEINVITE = "DECLINEINVITE";
		
		/**
		 * Settings of the server. 
		 * Contains delimiters, encodings, port, etc.
		 * @author Peter Verzijl
		 * @version 1.0
		 */
		public static class Settings {
			public static final String ENCODING = "UTF-16";
			public static final int TIMEOUTSECONDS = 15;
			public static final short DEFAULT_PORT = 4242;
			public static final char DELIMITER = '_';
			public static final char DELIMITER2 = '*';
			public static final String COMMAND_END = "\n\n";
		}
	}
}