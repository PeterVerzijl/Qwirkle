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

	/**
	 * Constructor for the board.
	 */

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
		Node mRootBlock =new Node();
		mFrontier.add(mRootBlock);
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
	public static boolean isValid(List<Node> aListOfMoves) {
		boolean isLegal = true;

		Node lastMove = aListOfMoves.get(aListOfMoves.size() - 1);
		boolean sameXPos = true;
		boolean sameYPos = true;
		// TODO Dennis: Dit omzetten naar neigbor check check left right if
		// stones==set.size()-2
		for (Node b : aListOfMoves) {
			if (!(b.getPosition().getX() == lastMove.getPosition().getX())) {
				sameXPos = false;
			}
			if (!(b.getPosition().getY() == lastMove.getPosition().getY())) {
				sameYPos = false;
			}
		}
		if (sameYPos || sameXPos) {
			// Dit voor alle units doen
			for (int i = 0; i < Direction.values().length; i++) {
				if (!(isValidColor(lastMove, Direction.values()[i]))
						&& !(isValidShape(lastMove, Direction.values()[i]))) {
					isLegal = false;
				}
			}
		} else {
			isLegal = false;
		}
		// System.out.println("Move is legal?: " + isLegal);
		return isLegal;
	}

	/**
	 * 
	 * @param aDirection
	 * @param aBlock
	 * @return
	 */
	public static Node createEmptyNode(Direction aDirection, Node aBlock) {
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
	public static Node findDuplicateNode(List<Node> aNodeList, Vector2 aPosition, Direction aDirection) {
		Node duplicate = null;
		Vector2 newNodePosition = new Vector2(aPosition.getX() + aDirection.getX(),
				aPosition.getY() + aDirection.getY());
		// System.out.println("");
		// System.out.println("newNodePosition: "+newNodePosition);
		for (Node p : aNodeList) {
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
	public static boolean isValidColor(Node lastMove, Direction aDirection) {
		boolean isLegal = true;
		Node block = lastMove;
		System.out.println("Direction: " + aDirection);
		while (block.getNeighborNode(aDirection) != null
				&&  block.getNeighborNode(aDirection).getBlock() != null) {
			if (lastMove.getBlock().getColor() != block.getNeighborNode(aDirection).getBlock().getColor()
					|| lastMove.getBlock().getShape() == block.getNeighborNode(aDirection).getBlock().getShape()) {
				System.out.println(
						block.getBlock().getColor().toString() + " aMove color: " + lastMove.getBlock().getColor().toString());
				isLegal = false;
				break;
			} else {
				System.out.println("Color check");
				System.out.println("Neigbor: " + block.getNeighborNode(aDirection).getBlock().getColor().toString() + " "
						+ block.getNeighborNode(aDirection).getBlock().getShape().toString() + " Move: "
						+ block.getBlock().getColor().toString());
				block = block.getNeighborNode(aDirection);
			}
		}

		System.out.println("Color is valid: " + isLegal);
		return isLegal;
	}

	public static boolean isValidShape(Node aMove, Direction aDirection) {
		boolean isLegal = true;

		Node block = aMove;
		System.out.println("Direction: " + aDirection);
		while (block.getNeighborNode(aDirection) != null
				&&  block.getNeighborNode(aDirection).getBlock() != null) {
			if (aMove.getBlock().getShape() !=  block.getNeighborNode(aDirection).getBlock().getShape()
					|| aMove.getBlock().getColor() ==  block.getNeighborNode(aDirection).getBlock().getColor()) {
				isLegal = false;
				break;
			} else {
				System.out.println("Shape check");
				System.out.println("Neigbor: " +  block.getNeighborNode(aDirection).getBlock().getColor().toString() + " "
						+ block.getNeighborNode(aDirection).getBlock().getShape().toString() + " Move: "
						+ block.getBlock().getColor().toString());
				block = block.getNeighborNode(aDirection);
			}
		}
		System.out.println("shape is valid: " + isLegal);
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
	public List<Node> getEmptySpaces() {
		return mFrontier;
	}

	public void addFrontier(Node aNode) {
		mFrontier.add(aNode);
	}

	public void removeFrontier(Block aBlock) {
		if (mFrontier.contains(aBlock)) {
			mFrontier.remove(aBlock);
		}
	}

	/**
	 * Prints a string representation of the current state of the board
	 * 
	 * @param aListOfPlacedBlocks
	 * @param aListOfPossibleMoves
	 */
	public static void boardToString(List<Node> aListOfPlacedBlocks, List<Node> aListOfPossibleMoves) {
		int x = (borders[1] - borders[0]);
		int y = (borders[3] - borders[2]);

		System.out.println("X: " + borders[1] + " - " + borders[0] + " " + x);

		System.out.println("Y: " + y);

		int midX = x / 2 + 1;// (Math.abs(borders[0])+borders[1])/2;
		int midY = y / 2 + 1;// (Math.abs(borders[2])+borders[3])/2;

		String boardToString[][] = new String[x + 1][y + 1];

		for (int i = 0; i < aListOfPlacedBlocks.size(); i++) {
			boardToString[(int) aListOfPlacedBlocks.get(i).getPosition().getX()
					+ midX][-(int) aListOfPlacedBlocks.get(i).getPosition().getY() + midY] = aListOfPlacedBlocks.get(i).getBlock().getColor().toString().charAt(0) + "" + BlockPrinter.getChar(aListOfPlacedBlocks.get(i).getBlock());
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
				System.out.print(boardToString[j][i] + "");
			}
			System.out.println("");
		}
	}
	
	public void setFrontier(Node aPlacedNode){
			addFrontiers(mFrontier,aPlacedNode);
			for(Node nodes:mFrontier){
				System.out.println(nodes.getPosition());
			}
	}
	/**
	 * 
	 */
	public static void addFrontiers(List<Node> aFrontierList, Node aNode) {
		Node[] neighbors = aNode.getNeighborNodes();
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] == null) {
				Node newEmpty = Board.findDuplicateNode(aFrontierList, aNode.getPosition(), Direction.values()[i]);
				if (newEmpty == null) {
					aFrontierList.add(Board.createEmptyNode(Direction.values()[i], aNode));
				} else {
					newEmpty.setNeighborNode(aNode, Direction.getDirection(aNode.getPosition(), newEmpty.getPosition()));
				}
			}
		}
	}
}
