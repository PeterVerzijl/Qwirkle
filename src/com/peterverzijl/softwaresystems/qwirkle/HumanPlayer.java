package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;

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
		System.out.println("Index of Direction.NORTH: " + Direction.valueOf(Direction.NORTH.toString()));
		System.out.println(Direction.values()[getDirection(new Vector2(0, 0), new Vector2(0, 1))]);
		System.out.println(Direction.values()[getDirection(new Vector2(0, 0), new Vector2(0, -1))]);
		System.out.println(Direction.values()[getDirection(new Vector2(0, 0), new Vector2(1, 0))]);
		System.out.println(Direction.values()[getDirection(new Vector2(0, 0), new Vector2(-1, 0))]);

		Player player = new HumanPlayer(1);
		player.initHand(new BlockBag(), 6);
		System.out.println("Start tekst");
		List<Block> frontierTest = new ArrayList<Block>();
		frontierTest.add(new Block(null, null));
		frontierTest.get(0).setPosition(0, 0);
		/*frontierTest.add(new Block(null, null));
		frontierTest.get(1).setPosition(1, 0);
		frontierTest.add(new Block(null, null));
		frontierTest.get(2).setPosition(2, 0);
		frontierTest.add(new Block(null, null));
		frontierTest.get(3).setPosition(3, 0);
		frontierTest.add(new Block(null, null));
		frontierTest.get(4).setPosition(4, 0);
		*/
		player.setMove(frontierTest);

	}

	public List<Block> determineMove(List<Block> aFrontier) {
		List<Block> set = new ArrayList<Block>();
		List<Block> possibleMoves = this.getmHand();
		List<Block> possiblePositions = new ArrayList<Block>();
		possiblePositions.addAll(aFrontier);
		Scanner scanner = new Scanner(System.in);
		int hand;
		int move;
		boolean success = false;
		while (!success) {
			try {
				if (possiblePositions.size() > 0) {
					for (int i = 0; i < possiblePositions.size(); i++) {
						System.out.println("Move: " + i + " Position" + possiblePositions.get(i).getPosition());
					}
				}
				hand = scanner.nextInt();
				move = scanner.nextInt();
				scanner.nextLine();

				if (hand < possibleMoves.size() && !possibleMoves.isEmpty()) {
					if (move < possiblePositions.size()) {
						System.out.printf("Input: \n\tHand: %d Move:%d is blockje %c %c op positie %s \n", hand, move,
								possibleMoves.get(hand).getColor().toString().charAt(0),
								BlockPrinter.getChar(possibleMoves.get(hand)),
								possiblePositions.get(move).getPosition());
						set.add(possibleMoves.get(hand));
						Node currentNode = possiblePositions.get(move);
						Node[] neighbors = currentNode.getChildNodes();
						for (int i = 0; i < neighbors.length; i++) {
							if (neighbors[i] == null) {
								Block newEmpty = findDuplicateNode(possiblePositions, currentNode.getPosition(),
										Direction.values()[i]);
								if (newEmpty == null) {
									possiblePositions.add(createEmptyNode(Direction.values()[i],currentNode));
								} else {
									 newEmpty.setChildNode(currentNode,getDirection(currentNode.getPosition(),newEmpty.getPosition()));
								}
							}
						}
						possiblePositions.remove(move);

						// TODO DENNIS MOGELIJKE ZETTEN ZICHTBAAR MAKEN
						// set.get(set.size()-1).mTempPosition.set(frontier[move].posX,
						// frontier[move].posY);
						possibleMoves.remove(hand);

						// TODO DENNIS DIT OMZETTEN NAAR EEN TOSTRINGFUNCTIE
						for (Block b : possibleMoves) {
							System.out
									.println(b.getColor().toString().charAt(0) + " " + BlockPrinter.getChar(b) + ", ");
						}
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
					possiblePositions.clear();
					possiblePositions.addAll(aFrontier);
					System.out.println("Je zet is gereset");
					System.out.println(
							"PossiblePositions: " + possiblePositions.size() + " aFrontier: " + aFrontier.size());
				} else {
					System.err.println("Input was not a valid number or command. Try again");
					hand = GameConstants.INALID_MOVE;
					move = GameConstants.INALID_MOVE;
				}
			}
		}
		return set;
	}

	// TODO DENNIS deze twee in player zetten
	public Block createEmptyNode(Direction aDirection, Node aBlock) {
		Block empty = new Block(null, null);
		empty.setPosition(aDirection.getX() + (int) (aBlock.getPosition().getX()),
				aDirection.getY() + (int) (aBlock.getPosition().getY()));
		empty.setParent(aBlock);
		empty.setChildNode(aBlock, Direction.getIndex(aDirection));
		return empty;
	}

	public Block findDuplicateNode(List<Block> aNodeList, Vector2 aPosition, Direction aDirection) {
		Block duplicate = null;
		Vector2 newNodePosition = new Vector2(aPosition.getX() + aDirection.getX(),
				aPosition.getY() + aDirection.getY());
		for (Block p : aNodeList) {
			if (p.getPosition() == newNodePosition) {
				duplicate = p;
			}
		}
		return duplicate;
	}

	// @ensure possiblePositions.contains(aNode) &&
	// anotherNode==findDuplicateNode()
	// @ ensure xDiff==1 || xDiff==-1
	public static int getDirection(Vector2 aNode, Vector2 anotherNode) {
		int direction = -1;
		int xDiff = (int) (anotherNode.getX() - aNode.getX());
		int yDiff = (int) (anotherNode.getY() - aNode.getY());

		if (xDiff == 1) {
			direction = 1;
		}
		if (xDiff == -1) {
			direction = 3;
		}
		if (yDiff == 1) {
			direction = 0;
		}
		if (yDiff == -1) {
			direction = 2;
		}
		return direction;
	}
}
