package com.peterverzijl.softwaresystems.qwirkle.gameengine;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.peterverzijl.softwaresystems.qwirkle.Game;
import com.peterverzijl.softwaresystems.qwirkle.collision.Collider;
//import com.peterverzijl.softwaresystems.qwirkle.collision.Collider;
//import com.peterverzijl.softwaresystems.qwirkle.collision.PhysicsEngine;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Camera;
import com.peterverzijl.softwaresystems.qwirkle.graphics.RenderComponent;
import com.peterverzijl.softwaresystems.qwirkle.graphics.Screen;

/**
 * The mGame engine component that can be added to the Canvas, 
 * it does rendering, mGame loop management and input gathering.
 * @author Peter Verzijl
 *
 */
public class GameEngineComponent extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 160;
	public static final int HEIGHT = 120;
	private static final int SCALE = 5;

	private boolean running;
	private Thread thread;

	private Game mGame;
	private Screen mScreen;
	private static Input input;
	private BufferedImage img;
	private int[] pixels;
	
	// TODO (peter) : Make this better!!!!
	// FIXME (peter) : HACK !!!! 
	static Camera mCamera;
	
	/**
	 * A list of all objects in the mGame.
	 */
	public static List<EngineObject> objects = new ArrayList<EngineObject>();
	public static List<RenderComponent> renderers = new ArrayList<RenderComponent>();
	public static List<Collider> colliders = new ArrayList<Collider>();

	public GameEngineComponent() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		//mGame = new Game();
		mScreen = new Screen(WIDTH, HEIGHT);
		input = new Input(SCALE);
		
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}

	public synchronized void start() {
		if (running) {
			return;
		}
		
		// Call start on the mGame
		//mGame.start();
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Frames per second the mGame runs in.
	 */
	public static final int FPS = 60;
	public static final int MS_PER_FRAME = (int) (1000.0f / FPS);

	/**
	 * The main thread and function that does the mGame loop.
	 */
	@Override
	public void run() {
		int frameCount = 0;
		double last = System.currentTimeMillis();

		double lastTime = System.currentTimeMillis();
		double lag = 0.0;
		while (running) {
			if (System.currentTimeMillis() - last > 1000.0) {
				//System.out.println(frameCount + " fps");
				last = System.currentTimeMillis();
				frameCount = 0;
			}

			double currentTime = System.currentTimeMillis();
			double elapsedTime = currentTime - lastTime;
			Time.setDeltaTime(elapsedTime);
			lastTime = currentTime;
			lag += elapsedTime;

			// processInput();

			while (lag >= MS_PER_FRAME) {
				tick();
				lag -= MS_PER_FRAME;
			}
			
			updatePhysics();
			render();
			frameCount++;
		}
	}
	
	/**
	 * Updates all the colliders
	 */
	public void updatePhysics() {
		//PhysicsEngine.update();
	}
	
	/**
	 * Renders the mGame to the canvas via a buffer strategy.
	 */
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		mScreen.render(mGame);

		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = mScreen.mPixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
		bs.show();
	}
	
	/**
	 * Ticks the mGame logic.
	 */
	public void tick() {
		// mGame.tick();
		input.tick();
	}

	/**
	 * Stops the mGame and exits from the mGame loop.
	 */
	public synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;
		// Join the created threads
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the main camera of the game.
	 * @param camera The camera to set as the main camera.
	 */
	public static void setCamera(Camera camera) {
		mCamera = camera;
	}

	/**
	 * Get camera from the game engine component.
	 * @return The main camera.
	 */
	public static Camera getCamera() {
		return mCamera;
	}
}
