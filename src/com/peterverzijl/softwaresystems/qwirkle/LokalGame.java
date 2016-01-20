package com.peterverzijl.softwaresystems.qwirkle;

import java.util.*;

public class LokalGame {
	
	public static void main(String[] args){
		List<Player> players = new ArrayList<Player>();
		players.add(new HumanTUIPlayer());
		players.add(new HumanTUIPlayer());
		Game newGame = new Game(players);
		new Thread(newGame).start();
	}
}
