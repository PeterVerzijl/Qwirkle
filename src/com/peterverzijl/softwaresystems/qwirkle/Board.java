package com.peterverzijl.softwaresystems.qwirkle;

import com.peterverzijl.softwaresystems.qwirkle.Node;
import com.peterverzijl.softwaresystems.qwirkle.networking.Protocol;

/**
 * The playing field of the Qwirkle game.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Board {
	
	/**
	 * The block that is placed on the board first.
	 */
	private Block rootBlock;
	
	/**
	 * Constructor for the board.
	 */
	public Board() {
		
	}
	
	public boolean placeBlock() {
		
		return false;
	}

	/**
	 * Reads a move string
	 * @return
	 */
	public static Node moveStringToNode(String s) {
		String[] move = s.split("" + Protocol.Server.Settings.DELIMITER2);
		String stone = move[0];
		int x = Integer.parseInt(move[1]);
		int y = Integer.parseInt(move[2]); 
		Node n = new Node();
		n.setPosition(x, y);
		n.setBlock(Block.getBlockFromCharPair(stone));
		return n;
	}
	
	
}
