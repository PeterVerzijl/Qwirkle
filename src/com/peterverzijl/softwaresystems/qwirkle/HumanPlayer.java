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
		// while (true) {
		player.setMove();
		// }

	}

	public List<Block> determineMove() {
		List<Block> set = new ArrayList<Block>();
		Scanner scanner = new Scanner(System.in);
		int hand;
		int move;
		// String input=scanner.nextLine();
		while (scanner.hasNext()) {
			try {
				hand = scanner.nextInt();
				move = scanner.nextInt();
				System.out.printf("Hand: %d Move:%d \n", hand, move);
				set.add(this.getmHand().get(hand));
				break;
			} catch (java.util.InputMismatchException e) {
				// scanner.
				scanner.close();
				System.err.println("Input was not a number. Try again");
				return null;
			}
		}

		scanner.close();
		System.out.println(set.size()+"Deze?");
		return set;
	}
}