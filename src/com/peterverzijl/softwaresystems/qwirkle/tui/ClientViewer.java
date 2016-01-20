package com.peterverzijl.softwaresystems.qwirkle.tui;

import com.peterverzijl.softwaresystems.qwirkle.ui.ChatView;

/**
 * A very simple client viewer that prints out the unhandled messages.
 * @author Peter Verzijl
 * @version 1.0a
 */
public class ClientViewer implements ChatView {

	@Override
	public void displayMessage(String message) {
		System.out.println("Server: " + message);

	}

	@Override
	public void askName() {
		MainTUI.askName();
	}

}
