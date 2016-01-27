package com.peterverzijl.softwaresystems.qwirkle;

import java.util.*;

/**
 * Testclass for the game
 * @author Dennis
 *
 */
public class LokalGame {
	
	public static void main(String[] args){
		List<Player> players = new ArrayList<Player>();
		players.add(new ComputerPlayer());
		players.add(new HumanTUIPlayer());
		Game newGame = new Game(players);
		//newGame.run();
	}
}
