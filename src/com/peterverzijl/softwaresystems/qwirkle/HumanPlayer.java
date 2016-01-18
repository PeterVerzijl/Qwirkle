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

	public HumanPlayer(Game aGame,int aID) {
		super(aGame, aID);
	}

	public static void main(String[] args) {

		/**
		 * DEZE INCLUDEN IN EEN TESTCLASSE
		 */
		/*
		 * System.out.println(Direction.values()[getDirection(new Vector2(0, 0),
		 * new Vector2(0, 1))]);
		 * System.out.println(Direction.values()[getDirection(new Vector2(0, 0),
		 * new Vector2(0, -1))]);
		 * System.out.println(Direction.values()[getDirection(new Vector2(0, 0),
		 * new Vector2(1, 0))]);
		 * System.out.println(Direction.values()[getDirection(new Vector2(0, 0),
		 * new Vector2(-1, 0))]);
		 */
		Game game = new Game();
		Player player = new HumanPlayer(game,1);
		player.initHand(new BlockBag(), 6);
		Player player2 = new HumanPlayer(game,2);
		player2.initHand(new BlockBag(), 6);
		// System.out.println("Start zet");
		List<Block> frontierTest = new ArrayList<Block>();
		mGame.getFrontier().add(new Block(null, null));
		mGame.getFrontier().get(0).setPosition(0, 0);
		// System.out.println("game." + game.mFrontier.size());

		/*
		 * frontierTest.add(new Block(null, null));
		 * frontierTest.get(1).setPosition(1, 0); frontierTest.add(new
		 * Block(null, null)); frontierTest.get(2).setPosition(2, 0);
		 * frontierTest.add(new Block(null, null));
		 * frontierTest.get(3).setPosition(3, 0); frontierTest.add(new
		 * Block(null, null)); frontierTest.get(4).setPosition(4, 0);
		 */
		
		while (true) {
			// Game.boardToString(Game.setBlocks);
			player.setMove(mGame.getFrontier());
			// Game.boardToString(Game.setBlocks);
			player2.setMove(mGame.getFrontier());
		}
	}

	public List<Block> determineMove(List<Block> aFrontier) {
		List<Block> set = new ArrayList<Block>();
		List<Block> possibleMoves = new ArrayList<Block>();
		List<Block> possiblePositions = new ArrayList<Block>();
		possibleMoves.addAll(this.getmHand());
		possiblePositions.addAll(aFrontier);
		Scanner scanner = new Scanner(System.in);
		int hand;
		int move;
		boolean success = false;
		while (!success) {
			try {
				List<Block> copyBoard = new ArrayList<Block>();
				copyBoard.addAll(mGame.getSetStones());
				copyBoard.addAll(set);
				System.out.println("Huidige spel");
				Game.boardToString(copyBoard,possiblePositions);
				System.out.println(Player.handToString(possibleMoves));
				System.out.println("Amount of free spaces: " + possiblePositions.size());
				//printNodeList(possiblePositions);

				if (possiblePositions.size() > 0) {
					for (int i = 0; i < possiblePositions.size(); i++) {
						// System.out.println("Move: " + i + " Position" +
						// possiblePositions.get(i).getPosition());
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
						Block newMove = possibleMoves.get(hand);
						Node currentNode = possiblePositions.get(move);
						newMove.setPosition((int) currentNode.getPosition().getX(),
								(int) currentNode.getPosition().getY());
						set.add(newMove);
						Node[] neighbors = currentNode.getNeighborNodes();
						((Block) currentNode).setBlock(set.get(set.size() - 1).getShape(),
								set.get(set.size() - 1).getColor());

						possiblePositions.remove(move);
						possibleMoves.remove(hand);
						for (int i = 0; i < neighbors.length; i++) {
							// System.out.println(Direction.values()[i]);
							if (neighbors[i] == null) {
								Block newEmpty = findDuplicateNode(possiblePositions, currentNode.getPosition(),
										Direction.values()[i]);
								// System.out.println(newEmpty==null);
								if (newEmpty == null) {
									possiblePositions.add(createEmptyNode(Direction.values()[i], currentNode));
								} else {
									newEmpty.setNeighborNode(currentNode,
											getDirection(currentNode.getPosition(), newEmpty.getPosition()));
								}
							}
						}
					}
				} else {
					System.out.println("This was not a valid move");
				}

			} catch (java.util.InputMismatchException e) {
				String input = scanner.next();
				if (input.toLowerCase().equals("end")) {
					System.out.println("Zet was done");
					success = true;
					scanner.reset();
					// scanner.close();
					//Deze sowieso niet hier laten zetten
					mGame.getFrontier().clear();
					mGame.getFrontier().addAll(possiblePositions);
					possibleMoves.clear();
				} else if (input.toLowerCase().equals("reset")) {
					set.clear();
					possibleMoves.clear();
					possibleMoves.addAll(this.getmHand());
					possiblePositions.clear();
					possiblePositions.addAll(aFrontier);
					System.out.println("Je zet is gereset");
					System.out.println(
							"PossiblePositions: " + possiblePositions.size() + " aFrontier: " + aFrontier.size());
				} else if (input.toLowerCase().equals("trade")) {
					set.clear();
					possibleMoves.clear();
					possibleMoves.addAll(this.getmHand());
					possiblePositions.clear();
					possiblePositions.addAll(aFrontier);
					boolean trading = true;
					System.out.println("Select stones to trade");
					while (trading) {
						try {
							System.out.println("Blocks in hand: " + Player.handToString(possibleMoves));
							System.out.println("Blocks pending for trade: " + Player.handToString(set));
							hand = scanner.nextInt();
						//	scanner.nextLine();
							set.add(possibleMoves.get(hand));
							possibleMoves.remove(possibleMoves.get(hand));
						} catch (java.util.InputMismatchException e1) {
							String input2 = scanner.next();
							if (input2.toLowerCase().equals("end")) {
								set.add(null);
								System.out.println("Now trading");
								trading = false;
								success = true;
							}
							if (input2.toLowerCase().equals("cancel")) {
								System.out.println("Trading canceled");
								trading = false;
							}
						}
					}
				} else if (input.toLowerCase().equals("player")) {
					System.out.println(this.getmID());
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
	public static Block createEmptyNode(Direction aDirection, Node aBlock) {
		Block empty = new Block(null, null);
		empty.setPosition(aDirection.getX() + (int) (aBlock.getPosition().getX()),
				aDirection.getY() + (int) (aBlock.getPosition().getY()));
		empty.setParent(aBlock);
		empty.setNeighborNode(aBlock, getDirection(aBlock.getPosition(), empty.getPosition()));
		// TODO: DEZE iets netter omzetten
		aBlock.setNeighborNode(empty, getDirection(empty.getPosition(), aBlock.getPosition()));
		Direction d = Direction.values()[getDirection(empty.getPosition(), aBlock.getPosition())];
		// System.out.println("aBlock: " + d.toString()+ " empty: " +
		// aDirection.toString());
		return empty;
	}

	public static Block findDuplicateNode(List<Block> aNodeList, Vector2 aPosition, Direction aDirection) {
		Block duplicate = null;
		Vector2 newNodePosition = new Vector2(aPosition.getX() + aDirection.getX(),
				aPosition.getY() + aDirection.getY());
		// System.out.println("");
		// System.out.println("newNodePosition: "+newNodePosition);
		for (Block p : aNodeList) {
			if (p.getPosition().getX() == newNodePosition.getX() && p.getPosition().getY() == newNodePosition.getY()) {
				// System.out.println("Deze zet bestond al");
				duplicate = p;
			}
		}
		return duplicate;
	}

	public static void printNodeList(List<Block> aNodeList) {
		System.out.println("Frontierlist:");
		for (Block p : aNodeList) {
			System.out.println(p.getPosition());
		}
	}

	// TODO DENNIS add substraction to Vector
	// @ensure possiblePositions.contains(aNode) &&
	// anotherNode==findDuplicateNode()
	// @ ensure xDiff==1 || xDiff==-1
	public static int getDirection(Vector2 aNode, Vector2 anotherNode) {
		int direction = -1;
		int xDiff = (int) (aNode.getX() - anotherNode.getX());
		int yDiff = (int) (aNode.getY() - anotherNode.getY());

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

	public static String handToString(List<Block> aHand) {
		String hand = "";
		for (int i = 0; i < aHand.size(); i++) {
			hand += aHand.get(i).getColor().toString().charAt(0) + BlockPrinter.getChar(aHand.get(i));
		}
		return hand;
	}
}
