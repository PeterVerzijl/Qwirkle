package com.peterverzijl.softwaresystems.qwirkle.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.peterverzijl.softwaresystems.qwirkle.networking.Server;
import com.peterverzijl.softwaresystems.qwirkle.ui.ServerView;

/**
 * Creats a new server window that displays server debug info.
 * @author Peter Verzijl
 * @version 1.0a
 */
@SuppressWarnings("serial")
public class ServerWindow extends JFrame implements ServerView {
	
	private Server mServer;
	private InetAddress mAddress;
	private Thread mServerThread;
	private JTextArea mAddressField;	// Has the address and port of the server
	private JTextArea mMessageField;	// Container for all the server messages.
	
	public ServerWindow(String windowName) {
		super(windowName);
		
		try {
			mAddress = InetAddress.getByName("localhost");
			mServer = new Server(mAddress, Server.PORT, this);			
			mServerThread = new Thread(mServer);
			mServerThread.start();
		} catch (UnknownHostException e) {
			System.out.println("Error, couldn't create a server at the port and address. Due to: " + e.getMessage());
			return;
		} catch (IOException e) {
			System.out.println("Error, couldn't create a server at the port and address. Due to: " + e.getMessage());
			return;
		}
		
		setLayout(new BorderLayout());
		
		// Create a layout for the server address message thing
		JPanel bottomPanel = new JPanel();
		// Set height of address panel
		{
			Dimension d = bottomPanel.getPreferredSize();
			d.height = 20;
			bottomPanel.setPreferredSize(d);
		}
		
		// Create the 
		mMessageField = new JTextArea();
		mMessageField.setEditable(false);
		mMessageField.setFont(new Font("Serif", Font.PLAIN, 15));
		mMessageField.setLineWrap(true);
		
		this.add(new JScrollPane(mMessageField), BorderLayout.CENTER);
		
		// Create a south panel
		mAddressField = new JTextArea();
		mAddressField.setText(mAddress.toString() + " : " + Server.PORT);
		mAddressField.setEditable(false);
		mAddressField.setFont(new Font("Serif", Font.PLAIN, 15));
		mAddressField.setLineWrap(true);
		
		bottomPanel.add(mAddressField);
		
		// Add to south component
		this.add(mAddressField, BorderLayout.SOUTH);
		
		setSize(400, 200);
		setVisible(true);
		
		sendMessage("Server started!");
		
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(new JFrame(), 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        		try {
		        			mServer.shutdown();
							mServerThread.join();
							MainWindow.serverWindow = null;
						} catch (InterruptedException e) {
							sendMessage("Error: could not join server thread due to: " + e.getMessage());
						}
		        	}
		    }
		});
	}
	
	/**
	 * Adds a message to the server message panel.
	 * @param message The message to append.
	 */
	public void sendMessage(String message) {
		mMessageField.append(message + "\n");
	}
}
