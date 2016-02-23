### Qwirkle README ###
# Version 1.0 : 27:01:2016
# Created by:
# - Dennis Vinke
# - Peter Verzijl
# For the University of Twente, Technical Computer Science, Module 2

# Setup:
1. Unzip the build.zip folder and place the jar and the folder in the same directory. 
2. Start a command prompt (windows cmd, unix terminal).
3. Navigate to the folder where you have put the jar file.
4. Run command: java -jar qwirkle.jar
5. You should no be able to enter commands.

# Playing a game:
1. Start a sever by entering the command : server create <address> <port>
	Where address could be localhost
2. You should now have entered the server.
3. Now join a game by entering the command : game join <players>
	Where players is the amount of opponents to wait for
4. Once you have a server running, you can connect other terminals
5. Redo the setup steps and enter the connect command: server connect <address> <port>
	Make sure the address and the port are the same of that of the server.
6. The game should start automatically when entering a game.

# Notes
- IF asked to press enter, press it a bunch of times before something happens. Due to an issue it doesn't always register.
- Enter the HELP command to see a list of commands.
- Type "end" to end your turn.
- Type "trade" to start trading, end trading by eighter "cancel" or "end"
