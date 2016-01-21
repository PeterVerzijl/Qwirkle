package com.peterverzijl.softwaresystems.qwirkle;

import java.util.*;

public class LokalGame {
	
	public static void main(String[] args){
		List<Player> players = new ArrayList<Player>();
		players.add(new HumanTUIPlayer(0));
		players.add(new HumanTUIPlayer(1));
		Game newGame = new Game(players);
		newGame.run();
	}
}
