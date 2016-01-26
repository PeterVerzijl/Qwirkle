package com.peterverzijl.softwaresystems.qwirkle;

import java.util.*;

public class LokalGame {
	
	public static void main(String[] args){
		List<Player> players = new ArrayList<Player>();
		players.add(new HumanTUIPlayer());
		players.add(new ComputerPlayer());
		Game newGame = new Game(players);
		newGame.run();
	}
}
