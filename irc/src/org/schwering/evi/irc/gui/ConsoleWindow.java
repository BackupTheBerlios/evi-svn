/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import org.schwering.evi.irc.conf.Profile;

/**
 * A simple window with chat field and input field.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ConsoleWindow extends AbstractWindow {

	public ConsoleWindow(Profile profile) {
		super(profile);
		setTitle("Console ("+ profile.getName() +")");
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#getHeaderComponent()
	 */
	protected Component getHeaderComponent() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#getTextComponent()
	 */
	protected Component getTextComponent() {
		return text;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#updateLayout()
	 */
	public void updateLayout() {
		Font font = profile.getConsoleFont();
		Color fg = profile.getNeutralColor();
		Color bg = Color.white;
		input.setFont(font);
		input.setForeground(fg);
		input.setBackground(bg);
		text.modifyAttributes(font.getFamily(), font.getSize(), fg, bg, 
				font.isBold(), font.isItalic(), false);
	}
}
