package com.peterverzijl.softwaresystems.qwirkle;

/**
 * The block class that contains all the block's info
 * @author Peter Verzijl
 *
 */
public class Block {
	
	/**
	 * The shape that is on the block
	 */
	public static enum Shape {
		CIRCLE, CROSS, DIAMOND, SQUARE, STAR, PLUS
	}
	
	/**
	 * The current shape of the block
	 */
	private Shape mShape = null;
	
	/**
	 * The color that the shape has on the block
	 */
	public static enum Color {
		RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE
	}
	
	/**
	 * The current color of the block
	 */
	private Color mColor = null;
	
	/**
	 * Creates a new block
	 * @param shape The shape on the block
	 * @param color The color of the block
	 */
	public Block(Block.Shape shape, Block.Color color) {
		mShape = shape;
		mColor = color;
	}
	
	/* --- Getters and Setters --- */
	
	/**
	 * Gets the block shape
	 * @return Shape of the block
	 */
	public Shape getShape() { 
		return mShape; 
	}
	
	/**
	 * Gets the block color
	 * @return Color of the block
	 */
	public Color getColor() { 
		return mColor; 
	}
}
