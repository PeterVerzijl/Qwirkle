package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayer extends Player {
	Board mBoard;

	public ComputerPlayer() {
		super();
		mBoard = new Board();
	}

	public List<Node> determineMove() {
		List<Node> randomSet = new ArrayList<Node>();
		Long seed = new Random().nextLong();
		Random r = new Random(seed);
		List<Block> possibleMoves = new ArrayList<Block>(getHand());
		List<Node> possiblePositions = new ArrayList<Node>(mBoard.getEmptySpaces());

		for (Node n : mBoard.GiveHint(possiblePositions, randomSet, possibleMoves.get(hand))) {
			System.out.println(n.getPosition());
		}
	}

	public List<Node> determineBestMove(List<Block> aPossibleMoves, List<Node> aPossiblePositions, List<Node> set) {
		for(Block block: aPossibleMoves){
			List<Node> possiblePositions = new ArrayList<Node>(mBoard.GiveHint(aPossiblePositions, set, block).size());
			
			if(possiblePositions.size()>0){
				for(Node n: possiblePositions){
				List<Block> possibleMoves=new ArrayList<Block>(aPossibleMoves);
				}
			}
		}
	}
}