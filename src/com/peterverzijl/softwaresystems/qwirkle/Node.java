package com.peterverzijl.softwaresystems.qwirkle;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;

/**
 * A node with neighbor nodes that could be placed in the 
 * north, east, south and west positions.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class Node {
	
	/**
	 * The amount of neighbor nodes, for Qwirkle this is always 4.
	 */
	public static final int NEIGHBOR_NODES = 4;
	
	/**
	 * The neighbor nodes for this node.
	 * These exits in the four wind directions.
	 * Are initialized to null.
	 */
	private Node[] mNeighborNodes;
	
	/**
	 * 
	 */
	private Vector2 mPosition;
	
	/**
	 * The block that is positioned on this node.
	 */
	private Block mBlock;
	

	/**
	 * Node constructor
	 */
	public Node() {
		// Initialize all nodes to zero.
		mNeighborNodes = new Node[NEIGHBOR_NODES];
		mPosition = new Vector2(GameConstants.UNSET_NODE,GameConstants.UNSET_NODE);
	}
	
	/**
	 * Sets the node of a given location index.
	 * 0 north, 1 east, 2 south, 3 west
	 * @param n The node to set as a neighbor.
	 * @param i The index of the location to do so.
	 */
	public void setNeighborNode(Node n, int i) {
		mNeighborNodes[i] = n;
	}
	
	/**
	 * Sets the node of a given cardinal direction.
	 * @param n The node to set as a neighbor.
	 * @param d The cardinal direction do so.
	 */
	public void setNeighborNode(Node n, Direction d) {
		switch (d) {
			case NORTH: 
				setNeighborNode(n, 0);
			case EAST: 
				setNeighborNode(n, 1);
			case SOUTH: 
				setNeighborNode(n, 2);
			case WEST: 
				setNeighborNode(n, 3);
			default:
				return;
		}
	}
	
	/**
	 * Returns a neighbor node by index. The indexes are defined as:
	 * 0 north, 1 east, 2 south, 3 west
	 * Neighbor can be null.
	 * @param i The index of the node.
	 * @return The node at the given index.
	 */
	public Node getNeighborNode(int i) {
		if (i > mNeighborNodes.length) {
			return null;
		}
		return mNeighborNodes[i];
	}
	
	/**
	 * Returns a node by cardinal direction.
	 * @param d The direction of the neighbor node.
	 * @return The neighbor node at the given direction if it exists.
	 */
	public Node getNeighborNode(Direction d) {
		switch (d) {
			case NORTH: 
				return getNeighborNode(0);
			case EAST: 
				return getNeighborNode(1);
			case SOUTH: 
				return getNeighborNode(2);
			case WEST: 
				return getNeighborNode(3);
			default:
				return null;
		}
	}
	
		
	/**
	 * Returns an array of four neighbor nodes. 
	 * Beware that neighbor nodes can be null.
	 * @return An array of all neighbor nodes. Can be null.
	 */
	public Node[] getNeighborNodes() {
		return mNeighborNodes;
	}
	
	public void setPosition(int aX, int aY){
		mPosition.set(aX, aY);
	}
	
	public Vector2 getPosition(){
		return mPosition;
	}
	
	/**
	 * Sets the block on this node.
	 * @param aBlock The block to put on the node.
	 */
	public void setBlock(Block aBlock){
        mBlock = aBlock;
    }
    
	/**
	 * Returns the block on this node.
	 * @return The block on this node.
	 */
	public Block getBlock(){
        return mBlock;
    }
}