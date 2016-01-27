package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;

public class HumanGUIPlayer extends Player{
	
	public HumanGUIPlayer(int aID){
		super();
		
	}
	//TODO: 
	public List<Node> determineMove(List<Node> aFrontier){
		return new ArrayList<Node>();
	}
	
	public void setGame(Game game){
		new PlayerGUI(this).start();
	}
	
	public static void main(String[] args){
		List<Player> players = new ArrayList<Player>();
		players.add(new HumanGUIPlayer(0));
		Game newGame = new Game(players);
	}
}
