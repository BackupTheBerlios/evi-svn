package org.schwering.evi.irc.gui;

import javax.swing.JTabbedPane;

import org.schwering.evi.conf.Properties;
import org.schwering.evi.irc.conf.Configuration;

/**
 * The IRC tabbar.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class TabBar extends JTabbedPane {
	private Properties props;

	public TabBar() {
		setTabPlacement(Configuration.getTabPlacement());
	}
}
