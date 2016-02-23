package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;
import com.peterverzijl.softwaresystems.qwirkle.networking.Protocol;

/**
 * The playing field of the Qwirkle game.
 * 
 * @author Peter Verzijl en Dennis Vinke.
 * @version 1.0a
 */
public class Board {

	/**
	 * The block that is placed on the board first.
	 */
	private Node mRootBlock;

	/**
	 * A list to track the extreme positions of the board.
	 */
	public static int[] borders = {-1, 1, -1, 1};

	/**
	 * A list to keep track of all the empty neigbor spaces.
	 */
	private List<Node> mFrontier = new ArrayList<Node>();

	/**
	 * A list to keep track of all the stones that are placed on the board.
	 */
	private List<Node> mSetBlocks = new ArrayList<Node>(); // RAGERAGERAGERAGE


	/**
	 * Constructor for the board.
	 */
	public Board() {
		mRootBlock = new Node();
		mRootBlock.setPosition(0, 0);
		mFrontier.add(mRootBlock);
	}

	/**
	 * Reads a move string and converts this to a node.
	 * @param message The message to convert to a node.
	 * @return A node with the correct location and block assigned to it,
	 * 			based on the given string.\n
	 * 			It is used to convert MOVE server messages to moves the game understands.
	 */
	public static Node moveStringToNode(String message) {
		String[] move = message.split("" + '\\' + Protocol.Server.Settings.DELIMITER2);
		String stone = move[0];
		int x = (int) Float.parseFloat(move[1]);
		int y = (int) Float.parseFloat(move[2]);
		Node n = new Node();
		n.setPosition(x, y);
		n.setBlock(Block.getBlockFromCharPair(stone));
		return n;
	}

	/**
	 * Checks if a move a Player does is valid by checking if all blocks of a
	 * move are placed in the same row and if the color or the shape is the same 
	 * according to the rules.
	 * 
	 * Looks at the last stone of a set for the correctness of the nodes
	 * because of the fact that every stone is placed one for one on the board
	 * and every placed stone is checked. (If k+1 == true, k is also true).
	 * 
	 * @param aListOfMoves Checks a list of moves on validity.
	 * @return True if all the checks are passed and thus the move is valid.
	 */
	public boolean isValid(List<Node> aListOfMoves) {
		boolean isLegal = true;
		
		Node lastMove = aListOfMoves.get(aListOfMoves.size() - 1);
	
		if (isValidConnected(aListOfMoves)) {
			//looks in all possible directions
			for (int i = 0; i < Direction.values().length; i++) {
				if (!(isValidColor(lastMove, 
									Direction.values()[i])) && 
									!(isValidShape(lastMove, 
													Direction.values()[i]))) {
					isLegal = false;
				}
			}
		} else {
			isLegal = false;
		}
		//Calculate score
		if (isLegal && aListOfMoves.size() > 0) {
			calcScore(aListOfMoves.get(aListOfMoves.size() - 1));
		}
		return isLegal;
	}

	/**
	 * Creates an empty node in a certain direction used to expand the board.
	 * @param aDirection The direction in which to create an empty node.
	 * @param aBlock The node relative to the direction. 
	 * @return A new node placed at the direction of a node which both are defined as parameters. 
	 */
	// @require aDirection != null && aBlock != null
	// @ensure /result.getNeigborNode(aDirection) == aBlock &&
	//  /result.getPosition = aDirection.getPosition().add(aBlock.getPosition());
	public Node createEmptyNode(Direction aDirection, Node aBlock) {
		Node empty = new Node();
		empty.setPosition(aDirection.getX() + (int) (aBlock.getPosition().getX()),
						aDirection.getY() + (int) (aBlock.getPosition().getY()));
		empty.setNeighborNode(aBlock, 
								Direction.getDirection(aBlock.getPosition(), 
								empty.getPosition()));
		aBlock.setNeighborNode(empty, 
								Direction.getDirection(empty.getPosition(), 
								aBlock.getPosition()));
		return empty;
	}

	/**
	 * Searches the frontier list of empty nodes for a node with the same position.
	 * Placed nodes do not have to be checked because they are not legible to place a stone.
	 * @param aPosition		The x and y pair to find the duplicate stone off.
	 * @param aDirection	The direction to look from the given x, y coordinate.
	 * @return The duplicate node if it exists else it will return null
	 */
	// @require aPosition != null && aDirection != null
	// @ensure /result == null || duplicate.getPosition.equals(aPosition)
	public Node findDuplicateNode(Vector2 aPosition, Direction aDirection) {
		Node duplicate = null;
		Vector2 newNodePosition = new Vector2(aPosition.getX() + aDirection.getX(),
												aPosition.getY() + aDirection.getY());
		for (Node p : mFrontier) {
			if (p.getPosition().getX() == newNodePosition.getX() && 
							p.getPosition().getY() == newNodePosition.getY()) {
				duplicate = p;
			}
		}
		return duplicate;
	}

	/**
	 * Prints the list of empty nodes to the console.
	 * @param aNodeList The list of nodes to print.
	 */
	 // @pure 
	public static void printNodeList(List<Node> aNodeList) {
		System.out.println("Frontierlist:");
		for (Node p : aNodeList) {
			System.out.println(p.getPosition());
		}
	}

	/**
	 * Looks if the color requirement is fulfilled by checking all the neighbor node, 
	 * in all possible Directions.
	 * @param lastMove		The last node placed on the board.
	 * @param aDirection	The direction to check for color correctness.
	 * @return true if all the neighbors in aDirection are the same and no
	 *         duplicate char exists in row.
	 */
	 // @ensure lastMove != null && aDirection != null
	 // @ensure \result == true -> \forall int i=0; i<Direction.values.length && i >= 0; 
	 // 			block.getNeighborNode(Direction.values()[i]; 
	 // 				lastMove.getBlock().getColor() == 
	 // 					block.getNeighborNode(Direction.values()[i]).getBlock().getColor()
	 //				&& lastMove.getBlock().getShape() != 
	 //					block.getNeighborNode(Direction.values()[i]).getBlock().getShape();	
	public boolean isValidColor(Node lastMove, Direction aDirection) {
		boolean isLegal = true;
		Node block = lastMove;
		while (block.getNeighborNode(aDirection) != null && 
				block.getNeighborNode(aDirection).getBlock() != null) {
			if (lastMove.getBlock().getColor() != 
							block.getNeighborNode(aDirection).getBlock().getColor() || 
							lastMove.getBlock().getShape() == 
							block.getNeighborNode(aDirection).getBlock().getShape()) {
				isLegal = false;
				break;
			} else {
				block = block.getNeighborNode(aDirection);
			}
		}
		return isLegal;
	}

	/**
	 * Looks if the shape requirement is fulfilled by checking all the neighbor node,
	 * in all possible Directions. 
	 * @param aMove The last node placed on the board.
	 * @param aDirection The direction to check for shape correctness.
	 * @return true if all the neighbors in aDirection are the same and no
	 *         duplicate char exists in row
	 */
	// @ensure lastMove != null && aDirection != null
	// @ensure \result == true -> \forall int i=0; i<Direction.values.length && i >= 0; 
	// 			block.getNeighborNode(Direction.values()[i]; 
	// 					lastMove.getBlock().getColor() != 
	// 					block.getNeighborNode(Direction.values()[i]).getBlock().getColor()
	//					&& lastMove.getBlock().getShape() == 
	//					block.getNeighborNode(Direction.values()[i]).getBlock().getShape();
	public boolean isValidShape(Node aMove, Direction aDirection) {
		boolean isLegal = true;

		Node block = aMove;
		while (block.getNeighborNode(aDirection) != null && 
				block.getNeighborNode(aDirection).getBlock() != null) {
			if (aMove.getBlock().getShape() != 
							block.getNeighborNode(aDirection).getBlock().getShape() || 
							aMove.getBlock().getColor() == 
							block.getNeighborNode(aDirection).getBlock().getColor()) {
				isLegal = false;
				break;
			} else {

				block = block.getNeighborNode(aDirection);
			}
		}
		return isLegal;
	}

	/**
	 * Getter for all blocks placed on the board.
	 * @return A list of all placed blocks on the board.
	 */
	public List<Node> getPlacedBlocks() {
		return mSetBlocks;
	}

	/**
	 * Setter for empty spaces on the board.
	 * First clears the frontier, then adds all new empty spaces to the fronteir.
	 * @param aNewEmptySpaceList The new frontier.
	 */
	public void setEmptySpaces(List<Node> aNewEmptySpaceList) {
		mFrontier.clear();
		mFrontier.addAll(aNewEmptySpaceList);
	}

	/**
	 * Makes a copy of the empty spaces and returns the copy list.
	 * @return a copy of the empty nodes
	 * TODO(Dennis): Plz fx yo!
	 */
	// @ensure \result 
	public List<Node> getEmptySpaces() {
		List<Node> copyList = new ArrayList<Node>();
		for (Node n : mFrontier) {
			Node copyNode = new Node();
			copyNode.setPosition((int) n.getPosition().getX(), (int) n.getPosition().getY());
			for (int i = 0; i < n.getNeighborNodes().length; i++) {
				copyNode.setNeighborNode(n.getNeighborNode(i), i);
			}
			copyList.add(copyNode);
		}
		return copyList;
	}

	/**
	 * Searches for the node in the list with possible moves and removes it.
	 * @param aBlock Removes a block with the same position from the frontier.
	 */
	// @ensure \result mFrontier.getIndex(aBlock) == -1
	public void removeFrontier(Node aBlock) {
		for (Node n : mFrontier) {
			if (n.getPosition().equals(aBlock.getPosition())) {
				System.out.println(n.getPosition() + " aBlock? " + aBlock.getPosition());
				mFrontier.remove(n);
				break;
			}
		}
	}

	/**
	 * Prints a string representation of the current state of the board.
	 * @param aListOfPlacedBlocks The list of placed blocks to convert to a stirng.
	 * @param aListOfPossibleMoves The list of possible moves to convert to a stirng.
	 * @return A string of the whole board.
	 */
	public static String toString(List<Node> aListOfPlacedBlocks, List<Node> aListOfPossibleMoves) {
		String boardRep = "";
		int x = borders[1] - borders[0];
		int y = borders[3] - borders[2];

		int midX = (x / 2) + 1;	// (Math.abs(borders[0])+borders[1])/2;
		int midY = (y / 2) + 1;	// (Math.abs(borders[2])+borders[3])/2;

		String[][] boardToString = new String[x + 1][y + 1];

		for (int i = 0; i < aListOfPlacedBlocks.size(); i++) {
			boardToString[(int) aListOfPlacedBlocks.get(i).getPosition().getX() + midX]
						[-(int) aListOfPlacedBlocks.get(i).getPosition().getY() + midY] = 
							aListOfPlacedBlocks.get(i).getBlock().getColor().toString().charAt(0) + 
							"" + BlockPrinter.getChar(aListOfPlacedBlocks.get(i).getBlock());
		}

		if (aListOfPossibleMoves != null) {
			for (int i = 0; i < aListOfPossibleMoves.size(); i++) {
				boardToString[(int) aListOfPossibleMoves.get(i).getPosition().getX() + midX]
							[-(int) aListOfPossibleMoves.get(i).getPosition().getY() + midY] = 
							" " + i + ((i > 9) ? "" : " ");
			}
		}

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if (boardToString[j][i] == null) {
					boardToString[j][i] = "  ";
				}
				boardRep += boardToString[j][i] + "";
			}
			boardRep += "\n";
		}
		return boardRep;
	}

	/**
	 * Adds a node to the empty node list.
	 * @param aPlacedNode Adds the node to the frontier.
	 */
	public void setFrontier(Node aPlacedNode) {
		addFrontiers(aPlacedNode);
	}

	/**
	 * Checks if the node already has a neighbor node exists.
	 * If not it is checked if an empty node already exists
	 * and sets the neighbors accordingly
	 * Else create a new neighbor node.
	 * @param aNode The node to add to the frontier.
	 */
	public void addFrontiers(Node aNode) {
		Node[] neighbors = aNode.getNeighborNodes();
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] == null) {
				Node newEmpty = findDuplicateNode(aNode.getPosition(), Direction.values()[i]);
				if (newEmpty == null) {
					mFrontier.add(createEmptyNode(Direction.values()[i], aNode));
				} else {
					newEmpty.setNeighborNode(aNode,
									Direction.getDirection(aNode.getPosition(), 
															newEmpty.getPosition()));
				}
			}
		}
	}

	/**
	 * Sets a block if the position of the node that has to be added 
	 * is also the position of a node in the empty node list
	 * Else the move is invalid.
	 * @param node The node containing the block and the position to place the block at.
	 * @return False if the position of node does not exists in the empty nodes list
	 */
	// @ensure \result Node m, x; 
	// 					m = node && m -> emptyList.get(x).getPosition().equals(m.getPosition()); 
	// 					mFrontier.indexOf(x)==-1;
	public boolean setPlacedBlock(Node node) {
		for (Node n : mFrontier) {
			if (n.getPosition().equals(node.getPosition())) {
				n.setBlock(node.getBlock());
				mSetBlocks.add(n);
				mFrontier.remove(n);
				boardScale(n.getPosition());
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates the empty nodes list if the setPlacedBlock(node) == true.
	 * @param node The stone and position to set on the board.
	 * @return return true if a stone placed is placed on a valid spot
	 */
	public boolean setStone(Node node) {
		boolean set = setPlacedBlock(node);
		if (set) {
			setFrontier(mSetBlocks.get(mSetBlocks.size() - 1));
		}
		return set;
	}

	/**
	 * Makes a copy of the placedBlock list and empty spaces list.
	 * If the move was valid, changes to the placedBlock list and empty spaces lists are kept
	 * else the list is reverted back to the state when the copies are made
	 * @param aNodeList A list of moves that are to be placed on the board.
	 * @throws IllegalMoveException Thrown when an invalid move is done.
	 */
	// TODO(Dennis): Fix this sh*t yo!
	// @ensure \result 
	public void setStones(List<Node> aNodeList) throws IllegalMoveException {
		List<Node> copySetBlocks = new ArrayList<Node>();
		List<Node> copyFrontiers = new ArrayList<Node>();
		copySetBlocks.addAll(mSetBlocks);
		copyFrontiers = getEmptySpaces();
		for (Node aNode : aNodeList) {
			if (!setStone(aNode)) {
				throw new IllegalMoveException();
			}
		}
		List<Node> lastSet = new ArrayList<Node>();
		for (int i = aNodeList.size() - 1; i > -1; i--) {
			lastSet.add(mSetBlocks.get(mSetBlocks.size() - (i + 1)));
		}

		if (!isValid(lastSet)) {
			mSetBlocks.clear();
			mSetBlocks.addAll(copySetBlocks);
			mFrontier.clear();
			mFrontier.addAll(copyFrontiers);
		}
	}

	/**
	 * Fills in a block to find out which positions offer a valid move.
	 * @param possibleMoves	A list of all possible moves.
	 * @param currentSet	A list of all currently held blocks in the hand.
	 * @param aBlock		The block to fill in as a hint.
	 * @return Returns a list of valid moves the player could do.
	 */
	public List<Node> giveHint(List<Node> possibleMoves, List<Node> currentSet, Block aBlock) {
		List<Node> validMoves = new ArrayList<Node>();
		List<Node> testMove = new ArrayList<Node>();
		if (currentSet != null) {
			testMove.addAll(currentSet);
		}
		for (Node m : possibleMoves) {
			m.setBlock(aBlock);
			testMove.add(m);
			if (isValid(testMove)) {
				validMoves.add(m);
			}
			testMove.remove(m);
			m.setBlock(null);
		}
		return validMoves;
	}

	/**
	 * Calculates the score of the last stone placed. 
	 * This is done by calculation all the stones found in the horizontal,
	 * and vertical lines of this block
	 * @param aBlock The block to check the score of.
	 * @return The amount of blocks placed in a line.
	 */
	public int calcScore(Node aBlock) {
		int scoreX = 0;
		int scoreY = 0;
		Node block = aBlock;
		for (int i = 0; i < Direction.values().length; i++) {
			Direction aDirection = Direction.values()[i];
			while (block.getNeighborNode(aDirection) != null && 
					block.getNeighborNode(aDirection).getBlock() != null) {
				if (i % 2 == 0) {
					scoreY = ((scoreY += 1) == 6) ? scoreY = 12 : scoreY + 1;
				} else {
					scoreX = (scoreX + 1 == 6) ? scoreX = 12 : scoreX + 1;
				}
				block = block.getNeighborNode(aDirection);
			}
		}
		return scoreX + scoreY;
	}
	
	/**
	 * Checks if all stones placed are on the same row or column.
	 * @param aListOfBlock All stones placed in this turn.
	 * @return Weighter the connection is valid.
	 */
	public boolean isValidConnected(List<Node> aListOfBlock) {
		int blocksFoundX = 0; // found the starting block
		int blocksFoundY = 0;
		List<Node> copy = new ArrayList<Node>();
		copy.addAll(aListOfBlock);
		copy.remove(0);
		for (int i = 0; i < Direction.values().length; i++) {
			Node block = aListOfBlock.get(0);	// aListOfBlock.get(0);
			Direction aDirection = Direction.values()[i];
			while (block.getNeighborNode(aDirection) != null && 
					block.getNeighborNode(aDirection).getBlock() != null) {
				for (Node n : copy) {
					if (n.getBlock() != null && 
									n.getBlock().equals(
											block.getNeighborNode(aDirection).getBlock()
									)) {
						if (i % 2 == 0) {
							blocksFoundY++;
						} else {
							blocksFoundX++;
						}
						copy.remove(n);
						break;
					}
				}
				block = block.getNeighborNode(aDirection);
			}
		}
		return copy.size() == 0 && (blocksFoundY == 0 || blocksFoundX == 0);
	}
	
	/**
	 * Scales the board based on the extreme positions found during the game.
	 * @param aPosition Scales the board with a certain scale.
	 * 			X is width and y is height of the scaling.
	 */
	public void boardScale(Vector2 aPosition) {
		if (Board.borders[0] > aPosition.getX()) {
			Board.borders[0] = (int) aPosition.getX() - 7;
		}
		if (Board.borders[1] < aPosition.getX()) {
			Board.borders[1] = (int) aPosition.getX() + 7;
		}
		if (Board.borders[2] > aPosition.getY()) {
			Board.borders[2] = (int) aPosition.getY() - 7;
		}
		if (Board.borders[3] < aPosition.getY()) {
			Board.borders[3] = (int) aPosition.getY() + 7;
		}
	}
}