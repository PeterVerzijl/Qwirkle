package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;
import com.peterverzijl.softwaresystems.qwirkle.networking.Protocol;

/**
 * The playing field of the Qwirkle game.
 * 
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Board {

	/**
	 * The block that is placed on the board first.
	 */
	private Node mRootBlock;

	private List<Node> lastSet = new ArrayList<Node>();

	private List<Node> frontierCopy = new ArrayList<Node>();
	/**
	 * Constructor for the board.
	 */

	private List<Node> mSetBlocksCopy = new ArrayList<Node>();

	/**
	 * A list to track the extreme positions of the board
	 */
	public static int[] borders = { -7, 7, -7, 7 };

	/**
	 * A list to keep track of all the empty neigbor spaces
	 */
	private List<Node> mFrontier = new ArrayList<Node>();

	/**
	 * A list to keep track of all the stones that are placed on the board
	 */
	private List<Node> mSetBlocks = new ArrayList<Node>(); // RAGERAGERAGERAGE

	public Board() {

		mRootBlock = new Node();
		mRootBlock.setPosition(0, 0);
		mFrontier.add(mRootBlock);
	}

	/**
	 * Reads a move string
	 * 
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

	public boolean placeBlock() {
		return false;
	}

	public boolean validMove() {
		return false;
	}

	/**
	 * Checks if a move a Player does is valid by checking if all blocks of a
	 * move are placed in the same row if
	 * 
	 * @param aMoveList
	 * @return
	 */
	public boolean isValid(Node aPlacedBlock) {
		boolean isLegal = true;

		Node lastMove = aPlacedBlock;

		/*
		 * boolean sameXPos = true; boolean sameYPos = true; for (Node b :
		 * aListOfMoves) { if (!(b.getPosition().getX() ==
		 * lastMove.getPosition().getX())) { sameXPos = false; } if
		 * (!(b.getPosition().getY() == lastMove.getPosition().getY())) {
		 * sameYPos = false; } } if (sameYPos || sameXPos) {
		 */
		/*
		 * if (isValidConnected(aPlacedBlock)) { // Dit voor alle units doen
		 */
		for (int i = 0; i < Direction.values().length; i++) {
			if (!(isValidColor(lastMove, Direction.values()[i])) && !(isValidShape(lastMove, Direction.values()[i]))) {
				isLegal = false;
			}
		}
		/*
		 * } else { System.err.println("Check is fout"); isLegal = false; } if
		 * (isLegal && aPlacedBlock.size() > 0)
		 * calcScore(aPlacedBlock.get(aPlacedBlock.size() - 1));
		 */ System.out.println("Move is legal?: " + isLegal);
		return isLegal;
	}

	/**
	 * 
	 * @param aDirection
	 * @param aBlock
	 * @return
	 */
	public Node createEmptyNode(Direction aDirection, Node aBlock) {
		Node empty = new Node();
		empty.setPosition(aDirection.getX() + (int) (aBlock.getPosition().getX()),
				aDirection.getY() + (int) (aBlock.getPosition().getY()));
		empty.setNeighborNode(aBlock, Direction.getDirection(aBlock.getPosition(), empty.getPosition()));
		aBlock.setNeighborNode(empty, Direction.getDirection(empty.getPosition(), aBlock.getPosition()));
		return empty;
	}

	/**
	 * 
	 * @param aNodeList
	 * @param aPosition
	 * @param aDirection
	 * @return
	 */
	public Node findDuplicateNode(Vector2 aPosition, Direction aDirection) {
		Node duplicate = null;
		Vector2 newNodePosition = new Vector2(aPosition.getX() + aDirection.getX(),
				aPosition.getY() + aDirection.getY());
		// System.out.println("");
		// System.out.println("newNodePosition: "+newNodePosition);
		for (Node p : frontierCopy) {
			if (p.getPosition().getX() == newNodePosition.getX() && p.getPosition().getY() == newNodePosition.getY()) {
				// System.out.println("Deze zet bestond al");
				duplicate = p;
			}
		}
		return duplicate;
	}

	public static void printNodeList(List<Node> aNodeList) {
		System.out.println("Frontierlist:");
		for (Node p : aNodeList) {
			System.out.println(p.getPosition());
		}
	}

	/**
	 * Looks
	 * 
	 * @param lastMove
	 * @param aDirection
	 * @return true if all the neigbors in aDirection are the same && no
	 *         duplicate char excists in row
	 */
	public boolean isValidColor(Node lastMove, Direction aDirection) {
		boolean isLegal = true;
		Node block = lastMove;
		// System.out.println("Direction: " + aDirection);
		while (block.getNeighborNode(aDirection) != null && block.getNeighborNode(aDirection).getBlock() != null) {
			if (lastMove.getBlock().getColor() != block.getNeighborNode(aDirection).getBlock().getColor()
					|| lastMove.getBlock().getShape() == block.getNeighborNode(aDirection).getBlock().getShape()) {
				// System.out.println(
				// block.getBlock().getColor().toString() + " aMove color: " +
				// lastMove.getBlock().getColor().toString());
				isLegal = false;
				break;
			} else {
				// System.out.println("Color check");
				// System.out.println("Neigbor: " +
				// block.getNeighborNode(aDirection).getBlock().getColor().toString()
				// + " "
				// +
				// block.getNeighborNode(aDirection).getBlock().getShape().toString()
				// + " Move: "
				// + block.getBlock().getColor().toString());
				block = block.getNeighborNode(aDirection);
			}
		}

		// System.out.println("Color is valid: " + isLegal);
		return isLegal;
	}

	public boolean isValidShape(Node aMove, Direction aDirection) {
		boolean isLegal = true;

		Node block = aMove;
		// System.out.println("Direction: " + aDirection);
		while (block.getNeighborNode(aDirection) != null && block.getNeighborNode(aDirection).getBlock() != null) {
			if (aMove.getBlock().getShape() != block.getNeighborNode(aDirection).getBlock().getShape()
					|| aMove.getBlock().getColor() == block.getNeighborNode(aDirection).getBlock().getColor()) {
				isLegal = false;
				break;
			} else {
				// System.out.println("Shape check");
				// System.out.println("Neigbor: " +
				// block.getNeighborNode(aDirection).getBlock().getColor().toString()
				// + " "
				// +
				// block.getNeighborNode(aDirection).getBlock().getShape().toString()
				// + " Move: "
				// + block.getBlock().getColor().toString());
				block = block.getNeighborNode(aDirection);
			}
		}
		// System.out.println("shape is valid: " + isLegal);
		return isLegal;
	}

	/**
	 * 
	 */
	public List<Node> getPlacedBlocks() {
		return mSetBlocks;
	}

	/**
	 * 
	 */
	public void setEmptySpaces(List<Node> aNewEmptySpaceList) {
		mFrontier.clear();
		mFrontier.addAll(aNewEmptySpaceList);
	}

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

	public void addFrontier(Node aNode) {
		mFrontier.add(aNode);
	}

	/**
	 * Searches for the node in the list with possible moves and removes it.
	 * 
	 * @param aBlock
	 */
	public void removeFrontier(Node aBlock) {
		// List<Node> removeList = new ArrayList<Node>();
		for (Node n : frontierCopy) {
			if (n.getPosition().equals(aBlock.getPosition())) {
				System.out.println(n.getPosition() + " aBlock? " + aBlock.getPosition());
				mFrontier.remove(n);
				break;
			}
		}
	}

	/**
	 * Prints a string representation of the current state of the board
	 * 
	 * @param aListOfPlacedBlocks
	 * @param aListOfPossibleMoves
	 */
	public static String toString(List<Node> aListOfPlacedBlocks, List<Node> aListOfPossibleMoves) {
		String boardRep = "";
		int x = (borders[1] - borders[0]);
		int y = (borders[3] - borders[2]);

		int midX = x / 2 + 1;// (Math.abs(borders[0])+borders[1])/2;
		int midY = y / 2 + 1;// (Math.abs(borders[2])+borders[3])/2;

		String boardToString[][] = new String[x + 1][y + 1];

		for (int i = 0; i < aListOfPlacedBlocks.size(); i++) {
			boardToString[(int) aListOfPlacedBlocks.get(i).getPosition().getX()
					+ midX][-(int) aListOfPlacedBlocks.get(i).getPosition().getY() + midY] = aListOfPlacedBlocks.get(i)
							.getBlock().getColor().toString().charAt(0) + ""
							+ BlockPrinter.getChar(aListOfPlacedBlocks.get(i).getBlock());
		}

		for (int i = 0; i < aListOfPossibleMoves.size(); i++) {
			boardToString[(int) aListOfPossibleMoves.get(i).getPosition().getX()
					+ midX][-(int) aListOfPossibleMoves.get(i).getPosition().getY() + midY] = " " + i + " ";
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

	public void setFrontier(Node aPlacedNode) {
		addFrontiers(aPlacedNode);
		// StackTraceElement[] elem = Thread.currentThread().getStackTrace();
		// for (Node nodes : mFrontier) {
		// System.out.println(elem[1] + ": " + "In setFrontier:" +
		// nodes.getPosition());
		// }
	}

	/**
	 * 
	 */
	public void addFrontiers(Node aNode) {
		Node[] neighbors = aNode.getNeighborNodes();
		for (int i = 0; i < neighbors.length; i++) {

			if (neighbors[i] == null) {
				Node newEmpty = findDuplicateNode(aNode.getPosition(), Direction.values()[i]);
				if (newEmpty == null) {
					frontierCopy.add(createEmptyNode(Direction.values()[i], aNode));
					// addFrontier(createEmptyNode(Direction.values()[i],
					// aNode));
				} else {
					newEmpty.setNeighborNode(aNode,
							Direction.getDirection(aNode.getPosition(), newEmpty.getPosition()));
				}
			}
		}
	}

	public void setPlacedBlock(Node node) {
		for (Node n : frontierCopy) {
			if (n.getPosition().equals(node.getPosition())) {
				if (isValid(n)) {
					n.setBlock(node.getBlock());
					mSetBlocksCopy.add(n);
					frontierCopy.remove(n);
					break;
				}
			}
		}

		System.out.println("Amount of stones placed on board: " + mSetBlocksCopy.size());
	}

	public void setStone(Node node) {
		setPlacedBlock(node);
		setFrontier(mSetBlocksCopy.get(mSetBlocksCopy.size() - 1));
	}

	/**
	 * Looks if it possible to get to all the blocks in the set by visiting the
	 * neighbor nodes that are in one line with the first block of the set.
	 * 
	 * @param aListOfBlock
	 * @return true if all the blocks are found in one direction (NORTH + SOUTH
	 *         xor EAST + WEST);
	 */
	public boolean isValidConnected() {// List<Node> aListOfBlock) {
		int blocksFoundX = 0; // found the starting block
		int blocksFoundY = 0;
		List<Node> copy = new ArrayList<Node>();
		copy.addAll(frontierCopy);// aListOfBlock);
		copy.remove(0);
		for (int i = 0; i < Direction.values().length; i++) {
			Node block = frontierCopy.get(0);// aListOfBlock.get(0);
			Direction aDirection = Direction.values()[i];
			while (block.getNeighborNode(aDirection) != null && block.getNeighborNode(aDirection).getBlock() != null) {
				for (Node n : copy) {
					if (n.getBlock() != null && n.getBlock().equals(block.getNeighborNode(aDirection).getBlock())) {
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
		return (copy.size() == 0 && (blocksFoundY == 0 || blocksFoundX == 0));

	}

	public void doMove() {
		if (isValidConnected()) {
			setEmptySpaces(frontierCopy);
			mSetBlocks.clear();
			mSetBlocks.addAll(mSetBlocksCopy);
		} else
			newSet(); // maak lijst leeg van de set
	}

	// TODO LastScore gets bonuspoints
	public int calcScore(Node aBlock) {
		int scoreX = 0;
		int scoreY = 0;
		Node block = aBlock;
		for (int i = 0; i < Direction.values().length; i++) {
			Direction aDirection = Direction.values()[i];
			while (block.getNeighborNode(aDirection) != null && block.getNeighborNode(aDirection).getBlock() != null) {
				if (i % 2 == 0) {
					scoreY = ((scoreY += 1) == 6) ? scoreY = 12 : scoreY + 1;

				} else {
					scoreX = (scoreX + 1 == 6) ? scoreX = 12 : scoreX + 1;
				}
				block = block.getNeighborNode(aDirection);
			}
			System.out.printf("scoreX: %d scoreY: %d \n", scoreX, scoreY);
		}
		System.out.println("Score of zet:" + (scoreX + scoreY));
		return scoreX + scoreY;
	}

	public void newSet() {
		lastSet.clear();
		frontierCopy.clear();
		frontierCopy = getEmptySpaces();
		mSetBlocksCopy.clear();
		mSetBlocksCopy.addAll(mSetBlocks);

	}
}
public class BackUpBoard {

}
