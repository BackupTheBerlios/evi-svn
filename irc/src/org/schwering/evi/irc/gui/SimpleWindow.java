/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JScrollPane;

import org.schwering.evi.irc.conf.Profile;

/**
 * A simple window with chat field and input field.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public abstract class SimpleWindow extends AbstractWindow {

	public SimpleWindow(Profile profile) {
		super(profile);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#createHeaderComponent()
	 */
	protected Component createHeaderComponent() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#createTextComponent()
	 */
	protected Component createTextComponent() {
		JScrollPane sp = new JScrollPane(text);
		return sp;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#createFooterComponent()
	 */
	protected Component createFooterComponent() {
		input.addListener(new IInputListener() {
			public void inputFired(String str) {
				text.append(str);
				text.append("\n");
			}
		});
		return input;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#updateLayout()
	 */
	public abstract void updateLayout();
	
	/**
	 * Can be used by overridden <code>updateLayout</code> methods.
	 * Updates the font of the input field and of the text field.
	 * @param font The font.
	 * @param fg The foreground.
	 * @param bg The background.
	 */
	public void updateLayout(Font font, Color fg, Color bg) {
		input.modifyAttributes(font.getFamily(), font.getSize(), fg, bg, 
				font.isBold(), font.isItalic(), false);
		text.modifyAttributes(font.getFamily(), font.getSize(), fg, bg, 
				font.isBold(), font.isItalic(), false);
	}
}
