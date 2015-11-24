package com.peterverzijl.softwaresystems.qwirkle.gameengine;

import java.util.HashMap;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements MouseListener, MouseMotionListener, KeyListener {
	
	public static Vector2 mousePosition = Vector2.zero;
	
	private static HashMap<Integer, Boolean> keysDown = new HashMap<Integer, Boolean>();
	private static HashMap<Integer, Boolean> keysUp = new HashMap<Integer, Boolean>();
	private static HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
	
	private static HashMap<Integer, Boolean> mouseButtonDown = new HashMap<Integer, Boolean>();
	private static HashMap<Integer, Boolean> mouseButtonUp = new HashMap<Integer, Boolean>();
	private static HashMap<Integer, Boolean> mouseButton = new HashMap<Integer, Boolean>();
	
	public Input() {}
	
	public void tick() {
		keysDown.clear();	
		keysUp.clear();
		mouseButtonDown.clear();
		mouseButtonUp.clear();
	}	
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = new Vector2(e.getX(), e.getY());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		if (mouseButton.containsKey(button) == false || mouseButton.get(button) == false) {
			mouseButtonDown.put(button, true);
		}
		mouseButton.put(button, true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
		if (mouseButton.containsKey(button) == false || mouseButton.get(button) == true) {
			mouseButtonUp.put(button, true);
		}
		mouseButton.put(button, false);
	}
	
	/**
	 * If the mouse button is currently down.
	 * @param button The button to check.
	 * @return True if the button is held down.
	 */
	public static boolean getMouseButton(int button) {
		return (mouseButton.containsKey(button) && mouseButton.get(button))? true : false;
	}
	/**
	 * If the mouse button has gone down.
	 * @param button The mouse button to check.
	 * @return If the button has gone down.
	 */
	public static boolean getMouseButtonDown(int button) {
		return (mouseButtonDown.containsKey(button) && mouseButtonDown.get(button))? true : false;
	}
	/**
	 * If the mouse button has gone up.
	 * @param button The mouse button to check.
	 * @return If the button has gone up.
	 */
	public static boolean getMouseButtonUp(int button) {
		return (mouseButtonUp.containsKey(button) && mouseButtonUp.get(button))? true : false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (keys.containsKey(key) == false || keys.get(key) == false) {
			keysDown.put(key, true);
		}
		keys.put(key, true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (keys.containsKey(key) == false || keys.get(key) == true) {
			keysUp.put(key, true);
		}
		keys.put(key, false);
	}

	
	@Override
	public void keyTyped(KeyEvent e) {	}
	
	/**
	 * If the key is currently down or not.
	 * @param key The key to check.
	 * @return True if the key is down now.
	 */
	public static boolean getKey(int key) {
		return (keys.containsKey(key) && keys.get(key))? true : false;
	}
	/**
	 * Returns true if the key came down this frame.
	 * @param key The key to check.
	 * @return If the key came down.
	 */
	public static boolean getKeyDown(int key) {
		return (keysDown.containsKey(key) && keysDown.get(key))? true : false;
	}
	/**
	 * Returns true if the key came up this frame.
	 * @param key The key to check.
	 * @return If the key came up.
	 */
	public static boolean getKeyUp(int key) {
		return (keysUp.containsKey(key) && keysUp.get(key))? true : false;
	}
}