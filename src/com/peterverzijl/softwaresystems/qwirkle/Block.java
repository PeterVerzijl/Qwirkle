package com.peterverzijl.softwaresystems.qwirkle;

/**
 * The block class that contains all the block's info.
 * @author Peter Verzijl
 *
 */
public class Block extends Node {
	/**
	 * The shape that is on the block.
	 */
	public static enum Shape {
		CIRCLE, CROSS, DIAMOND, SQUARE, STAR, PLUS
	}
	
	/**
	 * The current shape of the block.
	 */
	private Shape mShape = null;
	
	/**
	 * The color that the shape has on the block.
	 */
	public static enum Color {
		RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE
	}
	
	/**
	 * The current color of the block.
	 */
	private Color mColor = null;
	
	/**
	 * Creates a new block.
	 * @param shape The shape on the block.
	 * @param color The color of the block.
	 */
	public Block(Block.Shape shape, Block.Color color) {
		mShape = shape;
		mColor = color;
	}
	
	/* --- Getters and Setters --- */
	
	/**
	 * Gets the block shape.
	 * @return Shape of the block
	 */
	public Shape getShape() { 
		return mShape; 
	}
	
	/**
	 * Gets the block color.
	 * @return Color of the block
	 */
	public Color getColor() { 
		return mColor; 
	}
	
	/**
	 * Checks if two blocks have the same shape and color;\.
	 * @param other The block to to compare with the this block.
	 * @return Weighter the shape and the color are the same.
	 */
	public boolean equals(Block other) {
		boolean result = false;
		if (other.getColor() == getColor() && 
						other.getShape() == getShape()) {
			result = true;
		}
		return result;
	}
	
	public void setBlock(Block.Shape shape, Block.Color color){
		mShape = shape;
		mColor = color;
	}
	
	/**
	 * Gets a new block by a two character string AA - FF
	 * @param stone The string representing the stone.
	 * @return A new stone if the string is valid.
	 */
	public static Block getBlockFromCharPair(String stone) {
		Color c = null;
		Shape s = null;
		if (stone.length() != 2) {
			return null;
		}
		// Get color
		char color = stone.charAt(0);
		switch (color) {
			case 'A':
				c = Color.RED;
				break;
			case 'B':
				c = Color.ORANGE;
				break;
			case 'C':
				c = Color.YELLOW;
				break;
			case 'D':
				c = Color.GREEN;
				break;
			case 'E':
				c = Color.BLUE;
				break;
			case 'F':
				c = Color.PURPLE;
				break;
			default:
				return null;
		}
		
		// Get shape
		char shape = stone.charAt(1);
		switch (shape) {
			case 'A':
				s = Shape.CIRCLE;
				break;
			case 'B':
				s = Shape.CROSS;
				break;
			case 'C':
				s = Shape.DIAMOND;
				break;
			case 'D':
				s = Shape.SQUARE;
				break;
			case 'E':
				s = Shape.STAR;
				break;
			case 'F':
				s = Shape.PLUS;
				break;
			default:
				return null;
		}
		// Return the correct block type
		return new Block(s, c);
	}
}
