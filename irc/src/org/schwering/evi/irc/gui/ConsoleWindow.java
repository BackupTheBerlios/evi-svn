/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Font;

import org.schwering.irc.manager.event.ConnectionAdapter;
import org.schwering.irc.manager.event.UnexpectedEventAdapter;

/**
 * Console window.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ConsoleWindow extends SimpleWindow {
	public ConsoleWindow(ConnectionController controller) {
		super(controller);
		setTitle("Console ("+ controller.getProfile().getName() +")");
		controller.getConnection().addConnectionListener(new ConnectionListener());
		controller.getConnection().addUnexpectedEventListener(new UnexpectedEventListener());
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#updateLayout()
	 */
	public void updateLayout() {
		Font font = controller.getProfile().getConsoleFont();
		Color fg = controller.getProfile().getNeutralColor();
		Color bg = Color.white;
		updateLayout(font, fg, bg);
	}
	
	private class ConnectionListener extends ConnectionAdapter {
		
	}
	
	private class UnexpectedEventListener extends UnexpectedEventAdapter {
		
	}
}
