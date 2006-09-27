/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import javax.swing.JTabbedPane;

import org.schwering.evi.irc.conf.Configuration;

/**
 * The IRC tabbar.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class TabBar extends JTabbedPane {
	private static final long serialVersionUID = -417945783840186410L;

	public TabBar() {
		setTabPlacement(Configuration.getTabPlacement());
	}
}
