package com.peterverzijl.softwaresystems.qwirkle.tests;

import java.util.ArrayList;
import java.util.List;

import com.peterverzijl.softwaresystems.qwirkle.Block;
import com.peterverzijl.softwaresystems.qwirkle.BlockBag;
import com.peterverzijl.softwaresystems.qwirkle.Game;
import com.peterverzijl.softwaresystems.qwirkle.HumanTUIPlayer;
import com.peterverzijl.softwaresystems.qwirkle.Player;

public class GameTUITest {
	
	public static void main(String[] args) {

		/**
		 * DEZE INCLUDEN IN EEN TESTCLASSE
		 */
		/*
		 * System.out.println(Direction.values()[getDirection(new Vector2(0, 0),
		 * new Vector2(0, 1))]);
		 * System.out.println(Direction.values()[getDirection(new Vector2(0, 0),
		 * new Vector2(0, -1))]);
		 * System.out.println(Direction.values()[getDirection(new Vector2(0, 0),
		 * new Vector2(1, 0))]);
		 * System.out.println(Direction.values()[getDirection(new Vector2(0, 0),
		 * new Vector2(-1, 0))]);
		 */
		Game game = new Game(null);
		Player player = new HumanTUIPlayer();
		player.initHand(new BlockBag(), 6);
		Player player2 = new HumanTUIPlayer();
		player2.initHand(new BlockBag(), 6);
		// System.out.println("Start zet");
		List<Block> frontierTest = new ArrayList<Block>();
		// mGame.getFrontier().add(new Node(null, null));
		// mGame.getFrontier().get(0).setPosition(0, 0);
		// System.out.println("game." + game.mFrontier.size());

		/*
		 * frontierTest.add(new Block(null, null));
		 * frontierTest.get(1).setPosition(1, 0); frontierTest.add(new
		 * Block(null, null)); frontierTest.get(2).setPosition(2, 0);
		 * frontierTest.add(new Block(null, null));
		 * frontierTest.get(3).setPosition(3, 0); frontierTest.add(new
		 * Block(null, null)); frontierTest.get(4).setPosition(4, 0);
		 */

		while (true) {
			// Game.boardToString(Game.setBlocks);
			// player.setMove(mGame.getFrontier());
			// Game.boardToString(Game.setBlocks);
			// player2.setMove(mGame.getFrontier());
		}
	}
}
