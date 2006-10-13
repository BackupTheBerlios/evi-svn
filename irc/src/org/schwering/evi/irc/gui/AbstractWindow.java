/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import org.schwering.evi.irc.conf.Profile;
import org.schwering.evi.util.TextPane;

/**
 * Base class for console, channels and queries.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public abstract class AbstractWindow extends JPanel {
	protected Profile profile;
	protected String title;
	protected InputField input;
	protected TextPane text;
	
	public AbstractWindow(Profile p) {
		super(new BorderLayout());
		profile = p;
		text = new TextPane();
		input = new InputField();
		updateLayout();
		
		Component c;
		c = getHeaderComponent();
		if (c != null) {
			add(c, BorderLayout.CENTER);
		}
		c = getTextComponent();
		if (c != null) {
			add(c, BorderLayout.CENTER);
		}
		add(input, BorderLayout.SOUTH);
	}
	
	protected abstract Component getHeaderComponent();
	
	protected abstract Component getTextComponent();
	
	public abstract void updateLayout();
	
	public void dispose() {
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
}
