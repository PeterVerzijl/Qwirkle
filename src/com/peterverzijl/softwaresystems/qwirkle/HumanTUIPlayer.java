package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Player class for the Qwirkle game, contains all the player information.
 * @author Peter Verzijl
 */
public class HumanTUIPlayer extends Player {
	
	private Board mBoard;
	private Scanner scanner;
	
	/**
	 * Constructor.
	 * Creates a player and initiates a game board on the client side.
	 */
	public HumanTUIPlayer() {
		super();
		mBoard = new Board();
	}
	
	/**
	 * Does a move on the board with a list of nodes.
	 * @param aNode The node that contains the move.
	 */
	public void setMove(List<Node> aNode) { 
		if (aNode.size() < 1) {
			return;
		}
		try {
			mBoard.setStones(aNode);
		} catch (IllegalMoveException e) {
			System.err.println("The Server send an INVALID MOVE");
		}
	}
	
	/**
	 * Returns a string representation of the board.
	 * @return The string representation of the board.
	 */
	public String boardToString() {
		return Board.toString(mBoard.getPlacedBlocks(), mBoard.getEmptySpaces());
	}

	/**
	 * Provides the user of the program with an input loop to play the game.
	 * This loop does all the related TUI actions.
	 * @return A list of the placed moves. This is to be send to the server.
	 */
	public List<Node> determineMove() {
		List<Node> set = new ArrayList<Node>();
		List<Block> possibleMoves = new ArrayList<Block>();
		List<Node> possiblePositions = new ArrayList<Node>();
		possibleMoves.addAll(this.getHand());
		possiblePositions.addAll(mBoard.getEmptySpaces());
		List<Node> copyBoard = new ArrayList<Node>();
		copyBoard.addAll(mBoard.getPlacedBlocks());

		scanner = new Scanner(System.in);
		int hand;
		int move;
		boolean success = false;
		while (!success) {
			try {
				//print current state of board
				copyBoard.addAll(set);
				System.out.println("Huidige spel");
				System.out.println(Board.toString(copyBoard, possiblePositions));
				System.out.println(Player.handToString(possibleMoves));
				System.out.println("Amount of free spaces: " + possiblePositions.size());
				System.out.println("[Stone index] [Space index]");
				
				// Gets input to choose stone from a players hand,
				// and shows at which positions is can be placed
				hand = scanner.nextInt();				
				move = scanner.nextInt();
				//empties rest of the input
				scanner.nextLine();

				// TODO: Make the player use the internal board instead of keeping 
				// the stones in player like it is done now.
				// If hand is not a negative number and smaller or equal to 
				// the amount of stones in hand.
				if (hand > -1 && hand < possibleMoves.size() && !possibleMoves.isEmpty()) {
					if (move < possiblePositions.size()) {
						for (Node n : mBoard.giveHint(possiblePositions, 
										set, possibleMoves.get(hand))) {
							System.out.println(n.getPosition());
						}
						System.out.printf("Input: \n\tHand: %d Move:%d is " + 
											"blockje %c %c op positie %s \n", 
											hand, move, 
											possibleMoves.get(hand).getColor().toString().charAt(0),
											BlockPrinter.getChar(possibleMoves.get(hand)),
											possiblePositions.get(move).getPosition());
						
						// Grabs a Node out of the possible moves list 
						// and block out of the input of the player 
						// and adds the move to a list containing all the to place nodes.
						Block newMove = possibleMoves.get(hand);
						Node currentNode = possiblePositions.get(move);

						Node[] neighbors = currentNode.getNeighborNodes();
						currentNode.setBlock(newMove);
						
						System.out.println("De positie van de huidige move " + 
											currentNode.getPosition());
						
						set.add(currentNode);
						// Checks if the added node makes a valid node
						// if not, remove the last node from the list
						// else remove the block and node out the copied list 
						// and set the neighbor nodes of the Node and it's neighbors.
						if (!mBoard.isValid(set)) {
							System.err.print("\t Deze zet mag niet!");
							currentNode.setBlock(null);
							set.remove(currentNode);
						} else {
							possiblePositions.remove(move);
							possibleMoves.remove(hand);
							for (int i = 0; i < neighbors.length; i++) {
								if (neighbors[i] == null) {
									Node newEmpty = mBoard.findDuplicateNode(
														currentNode.getPosition(), 
														Direction.values()[i]);
									if (newEmpty == null) {
										possiblePositions.add(mBoard.createEmptyNode(
														Direction.values()[i], currentNode));
									} else {
										newEmpty.setNeighborNode(currentNode, 
																Direction.getDirection(
																		currentNode.getPosition(), 
																		newEmpty.getPosition()));
									}
								}
							}
						}
					} else {
						System.out.println("Position " + move + " is not valid.");
					}
				} else {
					System.out.println("Stone " + hand + " does not exist. Not a valid move");
				}
				// If a letter or word was given as an input,
				// see if it matches with one of the predefined commands.
			} catch (java.util.InputMismatchException e) {
				String input = scanner.next();
				
				//command to finish a move
				if (input.toLowerCase().equals("end")) {
					System.out.println("Zet was done");
					success = true;
					scanner.reset();
					possibleMoves.clear();
					
					//comment to reset the moves
				} else if (input.toLowerCase().equals("reset")) {
					set.clear();
					possibleMoves.clear();
					possibleMoves.addAll(this.getHand());
					possiblePositions.clear();
					possiblePositions.addAll(mBoard.getEmptySpaces());
					
					//command to go into trade mode which resets the move upon entering
				} else if (input.toLowerCase().equals("trade")) {
					set.clear();
					possibleMoves.clear();
					possibleMoves.addAll(this.getHand());
					possiblePositions.clear();
					possiblePositions.addAll(mBoard.getEmptySpaces());
					boolean trading = true;
					System.out.println("Select stones to trade");
					List<Block> blocksToTrade = new ArrayList<Block>();
					
					// Trading works by setting a node with a block, 
					// and add it to a set without giving the node a position
					while (trading) {
						try {
							System.out.println("Blocks in hand: " + 
											Player.handToString(possibleMoves));
							System.out.println("Blocks pending for trade: " + 
											Player.handToString(blocksToTrade));
							hand = scanner.nextInt();
							if (hand > possibleMoves.size() - 1) {
								System.err.println("Deze steen zit niet in je hand");
							}
							Node emptyNode = new Node();
							emptyNode.setBlock(possibleMoves.get(hand));
							blocksToTrade.add(emptyNode.getBlock());
							set.add(emptyNode);
							possibleMoves.remove(possibleMoves.get(hand));
						} catch (java.util.InputMismatchException e1) {
							String input2 = scanner.next();
							// Trade will not be executed here, 
							// but this command will break the trade loop and make a move loop.
							if (input2.toLowerCase().equals("end")) {
								System.out.println("Now trading");
								trading = false;
								success = true;
							}
							if (input2.toLowerCase().equals("cancel")) {
								System.out.println("Trading canceled");
								trading = false;
								set.clear();
								possibleMoves.clear();
								possibleMoves.addAll(this.getHand());
								possiblePositions.clear();
								possiblePositions.addAll(mBoard.getEmptySpaces());
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
		for (Node n : setCopy) {
			Node freshNode = new Node();
			freshNode.setBlock(n.getBlock());
			freshNode.setPosition((int) n.getPosition().getX(), (int) n.getPosition().getY());	
			set.add(freshNode);
		}
		return set;
	}
	
	/**
	 * Translates the hand of the player to a string.
	 * @param aHand The hand to convert to string.
	 * @return The resulting string representation of the hand.
	 */
	public static String handToString(List<Block> aHand) {
		String hand = "";
		for (int i = 0; i < aHand.size(); i++) {
			hand += aHand.get(i).getColor().toString().charAt(0) + 
					BlockPrinter.getChar(aHand.get(i));
		}
		return hand;
	}

	/**
	 * Creates a new board.
	 */
	public void resetBoard() {
		mBoard = new Board();
	}
	
}
