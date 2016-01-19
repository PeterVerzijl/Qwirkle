package com.peterverzijl.softwaresystems.qwirkle.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.peterverzijl.softwaresystems.qwirkle.gameengine.GameEngineComponent;
import com.peterverzijl.softwaresystems.qwirkle.gameengine.editor.GameObjectViewer;
import com.peterverzijl.softwaresystems.qwirkle.networking.Server;

/**
 * The start window that is shown when starting the game.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class MainWindow {
	
	public static ServerWindow serverWindow;
	public static ChatWindow chatWindow;
	public static JFrame gameWindow;
	
	/**
	 * Main point for java to hook into us.
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Get the native platform look and feel.
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		// Define the menu
		JMenuBar mMenuBar = new JMenuBar();
		
		// Chat menu
		JMenu chatMenu = new JMenu("Chat");
		chatMenu.setMnemonic(KeyEvent.VK_C);
		chatMenu.getAccessibleContext().setAccessibleDescription("Open the chat window.");
		// Menu items
		JMenuItem createChatItem = new JMenuItem(new CreateChatAction());
		createChatItem.setMnemonic(KeyEvent.VK_O);
		createChatItem.getAccessibleContext().setAccessibleDescription("Opens the chat window");
		chatMenu.add(createChatItem);
				
		// Server Menu
		JMenu serverMenu = new JMenu("Server");
		serverMenu.setMnemonic(KeyEvent.VK_S);
		serverMenu.getAccessibleContext().setAccessibleName("Create or connect to a server.");
		// Create server
		JMenuItem createServerItem = new JMenuItem(new CreateServerAction());
		createServerItem.setMnemonic(KeyEvent.VK_C);
		createServerItem.getAccessibleContext().setAccessibleDescription("Creates a server.");
		serverMenu.add(createServerItem);
		// Connect to server
		JMenuItem joinServerItem = new JMenuItem("Join server");
		joinServerItem.setMnemonic(KeyEvent.VK_J);
		joinServerItem.getAccessibleContext().setAccessibleDescription("Joins a server.");
		serverMenu.add(joinServerItem);
		
		// Game view
		// TODO (peter) : Remove this and make this appear when the client is connected!!!
		JMenu viewerMenu = new JMenu("Game");
		serverMenu.setMnemonic(KeyEvent.VK_G);
		serverMenu.getAccessibleContext().setAccessibleName("Creates a game view.");
		// Create game window
		JMenuItem createGameViewItem = new JMenuItem(new CreateGameView());
		createGameViewItem.setMnemonic(KeyEvent.VK_S);
		createGameViewItem.getAccessibleContext().setAccessibleDescription("Creates a game view.");
		viewerMenu.add(createGameViewItem);
		
		// Add all menus
		mMenuBar.add(chatMenu);
		mMenuBar.add(serverMenu);
		mMenuBar.add(viewerMenu);
		
		// Show view				
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Qwirkle Manager");
				frame.setJMenuBar(mMenuBar);
				frame.setSize(300, 300);
				frame.setVisible(true);
			}
		});
	}
}

/*
 * All the actions that can be performed by pressing buttons.
 */

@SuppressWarnings("serial")
class CreateChatAction extends AbstractAction {
	
	public CreateChatAction() {
		super("Open chat");
	}
	
	public void actionPerformed(ActionEvent e) {
		// Create chat window
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (MainWindow.chatWindow == null) {
					MainWindow.chatWindow = 
							new ChatWindow("Qwirkle Chat");
				}
			}
		});
	}
}

/**
 * Creates a new game view.
 * @author Peter Verzijl
 * @version 1.0a
 */
@SuppressWarnings("serial")
class CreateGameView extends AbstractAction {
	public CreateGameView() {
		super("Show game");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (MainWindow.gameWindow == null) {
			GameEngineComponent game = new GameEngineComponent();
			JFrame frame = new JFrame("Game Engine");
			frame.add(game);
			frame.pack();
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
			// Add window close listener
			frame.addWindowListener(new WindowAdapter() {
			    @Override
			    public void windowClosing(WindowEvent windowEvent) {
			        if (JOptionPane.showConfirmDialog(new JFrame(), 
			            "Are you sure to close this window?", "Really Closing?", 
			            JOptionPane.YES_NO_OPTION,
			            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			        	game.stop();
			            MainWindow.gameWindow = null;
			        }
			    }
			});
			// Set global game window
			MainWindow.gameWindow = frame;
			game.start();
			
			// Add game object viewer
			JFrame objectViewer = new GameObjectViewer(game);
		}
	}
} 

/**
 * Creates a server view with a server object.
 * @author Peter Verzijl
 * @version 1.0a
 */
@SuppressWarnings("serial")
class CreateServerAction extends AbstractAction {
	
	public CreateServerAction() {
		super("Create server");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Create chat window
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (MainWindow.serverWindow == null) {
					MainWindow.serverWindow = 
							new ServerWindow("Qwirkle Server");
				}
			}
		});
	}
}
