/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JScrollPane;

import org.schwering.evi.irc.conf.Profile;
import org.schwering.evi.util.TextPane;

/**
 * A simple window with chat field and input field.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public abstract class SimpleWindow extends AbstractWindow {
	protected InputField input;
	protected TextPane text;

	public SimpleWindow(Profile profile) {
		super(profile);
		requestFocus();
		input.requestFocus();
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#createNorthComponent()
	 */
	protected Component createNorthComponent() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#createCenterComponent()
	 */
	protected Component createCenterComponent() {
		text = new TextPane();
		text.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					input.requestFocus();
			}
		});
		text.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				System.out.println(e.getX() +" x "+ e.getY());
			}
		});
		JScrollPane sp = new JScrollPane(text);
		return sp;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#createSouthComponent()
	 */
	protected Component createSouthComponent() {
		input = new InputField();
		input.addListener(new IInputListener() {
			public void inputFired(String str) {
				if (profile.getEnableColors()) {
					ColorParser.appendColored(text, str, profile);
				} else {
					ColorParser.appendPlain(text, str);
				}
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
		input.setFont(font);
		text.modifyAttributes(font.getFamily(), font.getSize(), fg, bg, 
				font.isBold(), font.isItalic(), false);
	}
	
	/**
	 * Forwards the focus to the input line.
	 */
	public void requestFocus() {
		super.requestFocus();
		if (input != null) {
			input.requestFocus();
		}
	}
}
