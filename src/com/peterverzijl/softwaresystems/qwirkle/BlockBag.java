package com.peterverzijl.softwaresystems.qwirkle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The bag that contains the blocks we can draw from
 * 
 * @author Dennis en Peter
 *
 */
public class BlockBag {

	/**
	 * How many same blocks do we have of each type (default 3)
	 */
	private static final int blocksPerType = 3;

	/**
	 * All the blocks in the bag
	 */
	public List<Block> blocks = new ArrayList<Block>();

	/**
	 * Creates and initializes the bag with blocks
	 */
	public BlockBag() {
		InitBag();
	}

	/**
	 * Initializes the bag with the time as seed
	 */
	public void InitBag() {
		// Clear the bag
		blocks.clear();
		// Get the shapes and colors from the block
		Block.Shape[] blockShapes = Block.Shape.values();
		Block.Color[] blockColors = Block.Color.values();
		// Generate blocksPerType amount of each block combination
		for (int i = 0; i < blockShapes.length; i++) {
			for (int j = 0; j < blockColors.length; j++) {
				for (int k = 0; k < blocksPerType; k++) {
					Block b = new Block(blockShapes[i], blockColors[j]);
					blocks.add(b);
				}
			}
		}
	}

	/**
	 * Draws a random block from the bag of blocks
	 * 
	 * @return A random drawn block
	 */
	public Block drawBlock() {
		return drawBlock(new Random().nextLong());
	}

	/**
	 * Draws a seeded random block from the bag of blocks
	 * 
	 * @return A seeded random drawn block
	 */
	public Block drawBlock(long seed) {
		Random r = new Random(seed);
		ArrayList<Block> newList = new ArrayList<Block>(blocks);
		Collections.shuffle(newList, r);
		Block b = newList.get(r.nextInt(newList.size()));
		// Remove block from bag
		blocks.remove(b);
		return b;
	}

	/**
	 * Adds the block to the block bag.
	 * 
	 * @param block
	 *            The block to add to the bag.
	 */
	public void returnBlock(Block block) {
		blocks.add(block);
	}
}
