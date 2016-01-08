package com.peterverzijl.softwaresystems.qwirkle;

/**
 * A node with child nodes that could be placed in the 
 * north, east, south and west positions.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Node {
	
	/**
	 * The amount of child nodes, for Qwirkle this is always 4.
	 */
	public static final int CHILD_NODES = 4;
	
	/**
	 * The parent node.
	 */
	private Node mParent;
	
	/**
	 * The child nodes for this node.
	 * These exits in the four wind directions.
	 * Are initialized to null.
	 */
	private Node[] mChildNodes;
	
	/**
	 * 
	 */
	public int mPosition_X; 
	public int mPosition_Y; 
	
	/**
	 * Node constructor
	 */
	public Node() {
		// Initialize all nodes to zero.
		mChildNodes = new Node[CHILD_NODES];
		mPosition_X = GameConstants.UNSET_NODE;
		mPosition_Y = GameConstants.UNSET_NODE;
	}
	
	/**
	 * Creates a node with a particular parent.
	 * @param parent The node that is the parent of this node.
	 */
	public Node(Node parent) {
		mParent = parent;
	}
		
	/**
	 * Sets the node of a given location index.
	 * 0 north, 1 east, 2 south, 3 west
	 * @param n The node to set as a child.
	 * @param i The index of the location to do so.
	 */
	public void setChildNode(Node n, int i) {
		mChildNodes[i] = n;
	}
	
	/**
	 * Sets the node of a given cardinal direction.
	 * @param n The node to set as a child.
	 * @param d The cardinal direction do so.
	 */
	public void setChildNode(Node n, Direction d) {
		switch (d) {
			case NORTH: 
				setChildNode(n, 0);
			case EAST: 
				setChildNode(n, 1);
			case SOUTH: 
				setChildNode(n, 2);
			case WEST: 
				setChildNode(n, 3);
			default:
				return;
		}
	}
	
	/**
	 * Returns a child node by index. The indexes are defined as:
	 * 0 north, 1 east, 2 south, 3 west
	 * Child can be null.
	 * @param i The index of the node.
	 * @return The node at the given index.
	 */
	public Node getChildNode(int i) {
		if (i > mChildNodes.length) {
			return null;
		}
		return mChildNodes[i];
	}
	
	/**
	 * Returns a node by cardinal direction.
	 * @param d The direction of the child node.
	 * @return The child node at the given direction if it exists.
	 */
	public Node getChildNode(Direction d) {
		switch (d) {
			case NORTH: 
				return getChildNode(0);
			case EAST: 
				return getChildNode(1);
			case SOUTH: 
				return getChildNode(2);
			case WEST: 
				return getChildNode(3);
			default:
				return null;
		}
	}
	
	/**
	 * Sets the parent of this node.
	 * @param n The node to set as parent.
	 */
	public void setParent(Node n) {
		mParent = n;
	}
	
	/**
	 * Returns the parent of this node.
	 * @return The parent of this node.
	 */
	public Node getParent() {
		return mParent;
	}
	
	/**
	 * Returns an array of four child nodes. 
	 * Beware that child nodes can be null.
	 * @return An array of all child nodes. Can be null.
	 */
	public Node[] getChildNodes() {
		return mChildNodes;
	}
}
