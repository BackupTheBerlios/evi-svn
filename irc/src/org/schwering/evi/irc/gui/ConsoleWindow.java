/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Font;

import org.schwering.evi.irc.conf.Profile;

/**
 * Console window.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ConsoleWindow extends SimpleWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9050564952598388606L;

	public ConsoleWindow(Profile profile) {
		super(profile);
		setTitle("Console ("+ profile.getName() +")");
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#updateLayout()
	 */
	public void updateLayout() {
		Font font = profile.getConsoleFont();
		Color fg = profile.getNeutralColor();
		Color bg = Color.white;
		updateLayout(font, fg, bg);
	}
}
