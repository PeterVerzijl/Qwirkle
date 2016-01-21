package com.peterverzijl.softwaresystems.qwirkle;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.Vector2;

/**
 * The four cardinal directions.
 * 
 * @author Peter Verzijl
 * @version 1.0a
 */
public enum Direction {
	NORTH(0, 1), EAST(1, 0), SOUTH(0, -1), WEST(-1, 0);

	private final int x;
	private final int y;

	Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static int getIndex(Direction aDirection) {
		int index = -1;
		switch (aDirection) {
		case NORTH:
			index = 0;
			break;
		case EAST:
			index = 1;
			break;
		case SOUTH:
			index = 2;
			break;
		case WEST:
			index = 3;
			break;
		}
		return index;
	}
	// TODO DENNIS add substraction to Vector
		// TODO DENNS IN DIRECTION STOPPEN
		// @ensure possiblePositions.contains(aNode) &&
		// anotherNode==findDuplicateNode()
		// @ ensure xDiff==1 || xDiff==-1

	public static int getDirection(Vector2 aNode, Vector2 anotherNode) {
		int direction = -1;
		int xDiff = (int) (aNode.getX() - anotherNode.getX());
		int yDiff = (int) (aNode.getY() - anotherNode.getY());

		if (xDiff == 1) {
			direction = 1;
		}
		if (xDiff == -1) {
			direction = 3;
		}
		if (yDiff == 1) {
			direction = 0;
		}
		if (yDiff == -1) {
			direction = 2;
		}
		return direction;
	}
}
