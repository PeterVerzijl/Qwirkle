package com.peterverzijl.softwaresystems.qwirkle;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.peterverzijl.softwaresystems.qwirkle.ui.Screen;

/**
 * The game engine component that can be added to the Canvas, it does rendering, game loop management and input gathering.
 * @author Peter Verzijl
 *
 */
public class GameEngineComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 160;
	private static final int HEIGHT = 120;
	private static final int SCALE = 4;

	private boolean running;
	private Thread thread;

	private Game game;
	private Screen screen;
	private BufferedImage img;
	private int[] pixels;

	public GameEngineComponent() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		game = new Game();
		screen = new Screen(WIDTH, HEIGHT);

		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
		
		// Call start on the game
		game.start();
	}
	
	/**
	 * Frames per second the game runs in
	 */
	public static final int FPS = 60;
	public static final int MS_PER_FRAME = (int) (1000.0f / FPS);

	/**
	 * The main thread and function that does the game loop
	 */
	@Override
	public void run() {
		int frameCount = 0;
		double last = System.currentTimeMillis();

		double lastTime = System.currentTimeMillis();
		double lag = 0.0;
		while (running) {
			if (System.currentTimeMillis() - last > 1000.0) {
				System.out.println(frameCount + " fps");
				last = System.currentTimeMillis();
				frameCount = 0;
			}

			double currentTime = System.currentTimeMillis();
			double elapsedTime = currentTime - lastTime;
			lastTime = currentTime;
			lag += elapsedTime;

			// processInput();

			while (lag >= MS_PER_FRAME) {
				tick();
				lag -= MS_PER_FRAME;
			}

			render();
			frameCount++;
		}
	}
	
	/**
	 * Renders the game to the canvas via a buffer strategy
	 */
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render(game);

		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
		bs.show();
	}
	
	/**
	 * Ticks the game logic
	 */
	public void tick() {
		game.tick();
	}

	/**
	 * Stops the game and exits from the game loop
	 */
	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		// Join the created threads
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Main point for java to hook into us
	 * @param args
	 */
	public static void main(String[] args) {
		GameEngineComponent game = new GameEngineComponent();
		JFrame frame = new JFrame("Game Engine");
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		game.start();
	}
}
