package com.peterverzijl.softwaresystems.qwirkle;

import java.util.HashMap;

/**
 * Block printer prints out a block by its shape and color.
 * @author Peter Verzijl
 *
 */
public class BlockPrinter {
	/**
	 * The mapping between block shapes and unicode characters
	 */
	private static HashMap<Block.Shape, Character> shapeCharMap;
	static {
		shapeCharMap = new HashMap<Block.Shape, Character>();
		shapeCharMap.put(Block.Shape.CIRCLE, 'ഠ');
		shapeCharMap.put(Block.Shape.CROSS, '✖');
		shapeCharMap.put(Block.Shape.DIAMOND, '◆');
		shapeCharMap.put(Block.Shape.PLUS, '➕');
		shapeCharMap.put(Block.Shape.SQUARE, '■');
		shapeCharMap.put(Block.Shape.STAR, '✸');
	}
	
	/**
	 * Gets a char from a block shape
	 * @param block The block to get the shape from
	 * @return A character representing the block shape
	 */
	public static char getChar(Block block) {
		return shapeCharMap.get(block.getShape());
	}
	
	/**
	 * Prints out a block in unicode
	 * @param block The block to print out
	 */
	public static void print(Block block) {
		
		System.out.println(block.getColor().toString() + " " + getChar(block));
	}
}
