/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import org.schwering.evi.irc.conf.Profile;

/**
 * Base class for console, channels and queries.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public abstract class AbstractWindow extends JPanel {
	protected Profile profile;
	protected String title;
	
	public AbstractWindow(Profile p) {
		super(new BorderLayout());
		profile = p;
		
		Component c;
		c = createNorthComponent();
		if (c != null) {
			add(c, BorderLayout.NORTH);
		}
		c = createCenterComponent();
		if (c != null) {
			add(c, BorderLayout.CENTER);
		}
		c = createSouthComponent();
		if (c != null) {
			add(c, BorderLayout.SOUTH);
		}
		
		updateLayout();
	}
	
	/**
	 * Should create the new header component. The header can be <code>null</code> or 
	 * the the topic line, for example. The component is added to the panel with 
	 * <code>BorderLayout.NORTH</code>.
	 * @return The header or <code>null</code>.
	 */
	protected abstract Component createNorthComponent();
	
	/**
	 * Should create the new centered component. The center component is usually the 
	 * chat window. The component is added to the panel with 
	 * <code>BorderLayout.CENTER</code>.
	 * @return The text component or <code>null</code>.
	 */
	protected abstract Component createCenterComponent();
	
	/**
	 * Should create the new footer component. The footer is usually the input line.
	 * The component is added to the panel with <code>BorderLayout.NORTH</code>.
	 * @return The footer or <code>null</code>.
	 */
	protected abstract Component createSouthComponent();
	
	/**
	 * Should update the layout like fonts and colors.
	 */
	public abstract void updateLayout();
	
	/**
	 * Invokes when the tab is closed with <code>TabBar.removeTab</code>.
	 */
	public void dispose() {
	}
	
	/**
	 * Sets the title.
	 * @param title The new title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns the title of the window.
	 * @return The tab's title.
	 */
	public String getTitle() {
		return title;
	}
}
