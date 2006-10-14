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
		text = new TextPane() {
			public void requestFocus() {
				input.requestFocus();
			}
		};
		input = new InputField();
		updateLayout();
		
		Component c;
		c = createHeaderComponent();
		if (c != null) {
			add(c, BorderLayout.NORTH);
		}
		c = createTextComponent();
		if (c != null) {
			add(c, BorderLayout.CENTER);
		}
		c = createFooterComponent();
		if (c != null) {
			add(c, BorderLayout.SOUTH);
		}
	}
	
	protected abstract Component createHeaderComponent();
	
	protected abstract Component createTextComponent();
	
	protected abstract Component createFooterComponent();
	
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
