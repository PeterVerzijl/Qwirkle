package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.peterverzijl.softwaresystems.qwirkle.exceptions.NotYourBlockException;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;

/**
 * Player class for the Qwirkle game, contains all the player information.
 * 
 * @author Peter Verzijl
 *
 */
public class HumanTUIPlayer extends Player {
	private Board mBoard;

	public HumanTUIPlayer() {
		super();
		mBoard = new Board();
	}

	public void setMove(List<Node> aNode) {
		// System.out.println("Hallo alles spelers");
		if (aNode.size() < 1) {
			return;
		}
		try {
			mBoard.setStones(aNode);
		} catch (IllegalMoveException e) {
			System.err.println("The Server send an INVALID MOVE");
		}
	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * /** DEZE INCLUDEN IN EEN TESTCLASSE
	 */
	/*
	 * System.out.println(Direction.values()[getDirection(new Vector2(0, 0), new
	 * Vector2(0, 1))]); System.out.println(Direction.values()[getDirection(new
	 * Vector2(0, 0), new Vector2(0, -1))]);
	 * System.out.println(Direction.values()[getDirection(new Vector2(0, 0), new
	 * Vector2(1, 0))]); System.out.println(Direction.values()[getDirection(new
	 * Vector2(0, 0), new Vector2(-1, 0))]);
	 * 
	 * Game game = new Game(null); Player player = new HumanTUIPlayer();
	 * player.initHand(new BlockBag(), 6); Player player2 = new
	 * HumanTUIPlayer(); player2.initHand(new BlockBag(), 6); //
	 * System.out.println("Start zet"); List<Block> frontierTest = new
	 * ArrayList<Block>(); // mGame.getFrontier().add(new Node(null, null)); //
	 * mGame.getFrontier().get(0).setPosition(0, 0); //
	 * System.out.println("game." + game.mFrontier.size());
	 * 
	 * 
	 * frontierTest.add(new Block(null, null));
	 * frontierTest.get(1).setPosition(1, 0); frontierTest.add(new Block(null,
	 * null)); frontierTest.get(2).setPosition(2, 0); frontierTest.add(new
	 * Block(null, null)); frontierTest.get(3).setPosition(3, 0);
	 * frontierTest.add(new Block(null, null));
	 * frontierTest.get(4).setPosition(4, 0);
	 * 
	 * 
	 * while (true) { // Game.boardToString(Game.setBlocks); //
	 * player.setMove(mGame.getFrontier()); //
	 * Game.boardToString(Game.setBlocks); //
	 * player2.setMove(mGame.getFrontier()); } }
	 */

	public String boardToString() {
		return mBoard.toString(mBoard.getPlacedBlocks(), mBoard.getEmptySpaces());
	}

	/**
	 * Let a HumanPlayer do a move based on an input and output
	 * 
	 * @param aListOfEmptySpaces
	 * @return
	 */
	public List<Node> determineMove() {
		List<Node> set = new ArrayList<Node>();
		List<Block> possibleMoves = new ArrayList<Block>();
		List<Node> possiblePositions = new ArrayList<Node>();
		possibleMoves.addAll(this.getHand());
		possiblePositions.addAll(mBoard.getEmptySpaces());
		List<Node> copyBoard = new ArrayList<Node>();
		copyBoard.addAll(mBoard.getPlacedBlocks());

		Scanner scanner = new Scanner(System.in);
		int hand;
		int move;
		boolean success = false;
		while (!success) {
			try {
				copyBoard.addAll(set);
				System.out.println("Huidige spel");
				System.out.println(mBoard.toString(copyBoard, possiblePositions));
				System.out.println(Player.handToString(possibleMoves));
				System.out.println("Amount of free spaces: " + possiblePositions.size());
				// printNodeList(possiblePositions);

				if (possiblePositions.size() > 0) {
					for (int i = 0; i < possiblePositions.size(); i++) {
						// System.out.println("Move: " + i + " Position" +
						// possiblePositions.get(i).getPosition());
					}
				}
				hand = scanner.nextInt();
				for (Node n : mBoard.GiveHint(possiblePositions, set, possibleMoves.get(hand))) {
					System.out.println(n.getPosition());
				}
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

						Node[] neighbors = currentNode.getNeighborNodes();
						currentNode.setBlock(newMove);
						System.out.println("De positie van de huidige move " + currentNode.getPosition());
						System.out.println(
								"De positie van de huidige move in lijst" + possiblePositions.get(move).getPosition());

						set.add(currentNode);
						System.out.println("Size: " + set.size());
						if (!mBoard.isValid(set)) {// .get(set.size()-1))) {
							System.err.print("\t Deze zet m" + "ag niet!");
							currentNode.setBlock(null);
							set.remove(currentNode);
						} else {
							possiblePositions.remove(move);
							possibleMoves.remove(hand);
							for (int i = 0; i < neighbors.length; i++) {
								// System.out.println(Direction.values()[i]);
								if (neighbors[i] == null) {
									Node newEmpty = mBoard.findDuplicateNode(/* possiblePositions, */
											currentNode.getPosition(), Direction.values()[i]);
									// System.out.println(newEmpty==null);
									if (newEmpty == null) {
										possiblePositions
												.add(mBoard.createEmptyNode(Direction.values()[i], currentNode));
									} else {
										newEmpty.setNeighborNode(currentNode, Direction
												.getDirection(currentNode.getPosition(), newEmpty.getPosition()));
									}
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
					// Deze sowieso niet hier laten zetten
					// TODO Game->Board
					// TODO STACK OF BOARD
					// mBoard.getEmptySpaces().clear();
					// mBoard.getEmptySpaces().addAll(possiblePositions);
					possibleMoves.clear();

				} else if (input.toLowerCase().equals("reset")) {
					set.clear();
					possibleMoves.clear();
					possibleMoves.addAll(this.getHand());
					possiblePositions.clear();
					possiblePositions.addAll(mBoard.getEmptySpaces());
					System.out.println("Je zet is gereset");
					// TODO CHECK IF HAND < current hand.size()
				} else if (input.toLowerCase().equals("trade")) {
					set.clear();
					possibleMoves.clear();
					possibleMoves.addAll(this.getHand());
					possiblePositions.clear();
					possiblePositions.addAll(mBoard.getEmptySpaces());
					boolean trading = true;
					System.out.println("Select stones to trade");
					List<Block> blocksToTrade = new ArrayList<Block>();
					while (trading) {
						try {
							System.out.println("Blocks in hand: " + Player.handToString(possibleMoves));
							System.out.println("Blocks pending for trade: " + Player.handToString(blocksToTrade));
							hand = scanner.nextInt();
							if (hand > possibleMoves.size() - 1) {
								System.err.println("Deze steen zit niet in je hand");
							}
							// scanner.nextLine();
							Node emptyNode = new Node();
							emptyNode.setBlock(possibleMoves.get(hand));
							blocksToTrade.add(emptyNode.getBlock());
							set.add(emptyNode);
							possibleMoves.remove(possibleMoves.get(hand));
						} catch (java.util.InputMismatchException e1) {
							String input2 = scanner.next();
							if (input2.toLowerCase().equals("end")) {
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
				} else {
					System.err.println("Input was not a valid number or command. Try again");
					hand = GameConstants.INALID_MOVE;
					move = GameConstants.INALID_MOVE;
				}
			}
		}

		// added freshNode to make sure copy is empty
		// TODO find out if still needed
		List<Node> setCopy = new ArrayList<Node>();
		setCopy.addAll(set);
		set.clear();
		// mBoard.newSet();
		for (Node n : setCopy) {
			Node freshNode = new Node();
			freshNode.setBlock(n.getBlock());
			freshNode.setPosition((int) n.getPosition().getX(), (int) n.getPosition().getY());
			/*
			 * for(int i = 0; i < n.getNeighborNodes().length;i++){
			 * 
			 * if(n.getNeighborNode(i).getBlock()!=null){
			 * freshNode.setNeighborNode(n.getNeighborNode(i), i); } }
			 */
			try {
				removeBlock(freshNode.getBlock());
			} catch (NotYourBlockException e) {
				System.err.println("The stone was not your");
			}
			set.add(freshNode);
		}
		if (set.get(0).getBlock() != null)
			setMove(set);
		return set;
	}

	public static String handToString(List<Block> aHand) {
		String hand = "";
		for (int i = 0; i < aHand.size(); i++) {
			hand += aHand.get(i).getColor().toString().charAt(0) + BlockPrinter.getChar(aHand.get(i));
		}
		return hand;
	}

	public void resetBoard() {
		mBoard = new Board();
	}
	/*
	 * public static void addFrontiers(List<Node> aFrontierList, Node aNode) {
	 * Node[] neighbors = aNode.getNeighborNodes(); for (int i = 0; i <
	 * neighbors.length; i++) { if (neighbors[i] == null) { Node newEmpty =
	 * findDuplicateNode(aFrontierList, aNode.getPosition(),
	 * Direction.values()[i]); // System.out.println(newEmpty==null); if
	 * (newEmpty == null) {
	 * aFrontierList.add(createEmptyNode(Direction.values()[i], aNode)); } else
	 * { newEmpty.setNeighborNode(aNode,
	 * Direction.getDirection(aNode.getPosition(), newEmpty.getPosition())); } }
	 * } }
	 */
}
