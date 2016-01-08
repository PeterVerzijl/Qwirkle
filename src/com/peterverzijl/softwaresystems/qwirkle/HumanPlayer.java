package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Player class for the Qwirkle game, contains all the player information.
 * 
 * @author Peter Verzijl
 *
 */
public class HumanPlayer extends Player {

	public HumanPlayer(int aID) {
		super(aID);
	}

	public static void main(String[] args) {
		Player player = new HumanPlayer(1);
		player.initHand(new BlockBag(), 6);
		System.out.println("Start tekst");
		player.setMove();

	}

	public List<Block> determineMove() {
		List<Block> set = new ArrayList<Block>();
		List<Block> possibleMoves = this.getmHand();
		Scanner scanner = new Scanner(System.in);
		int hand;
		int move;
		boolean success = false;
		while (!success) {
			try {
				hand = scanner.nextInt();
				move = scanner.nextInt();
				scanner.nextLine();
				if (hand < possibleMoves.size() && !possibleMoves.isEmpty()) {
					System.out.printf("Input: \n\tHand: %d Move:%d is blockje %c %c \n", hand, move,possibleMoves.get(hand).getColor().toString().charAt(0),BlockPrinter.getChar(possibleMoves.get(hand)));
					set.add(possibleMoves.get(hand));
					// TODO DENNIS MOGELIJKE ZETTEN ZICHTBAAR MAKEN
					// set.get(set.size()-1).mTempPosition.set(frontier[move].posX,
					// frontier[move].posY);
					possibleMoves.remove(hand);
					// TODO DENNIS DIT OMZETTEN NAAR EEN TOSTRINGFUNCTIE
					for (Block b : possibleMoves) {
						System.out.println(b.getColor().toString().charAt(0) + " " + BlockPrinter.getChar(b) + ", ");
					}
				}
			} catch (java.util.InputMismatchException e) {
				String input = scanner.next();
				if (input.toLowerCase().equals("end")) {
					System.out.println("Zet was done");
					success = true;
					scanner.close();
				} else if (input.toLowerCase().equals("redo")) {
					set.clear();
					possibleMoves = this.getmHand();
					System.out.println("Je zet is gereset");
				} else {
					System.err.println("Input was not a valid number or command. Try again");
					hand = GameConstants.INALID_MOVE;
					move = GameConstants.INALID_MOVE;
				}
			}
		}
		return set;
	}
}
