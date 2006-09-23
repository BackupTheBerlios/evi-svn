package org.schwering.evi.irc.gui;

import javax.swing.JTabbedPane;

import org.schwering.evi.conf.Properties;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.irc.IRC;

/**
 * The IRC tabbar.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class TabBar extends JTabbedPane {
	private Properties props;

	public TabBar() {
		int placement;
		try {
			props = new Properties(ModuleContainer.getIdByClass(IRC.class));
			placement = props.getInt("gui.tabs.placement", JTabbedPane.LEFT);
		} catch (Exception exc) {
			placement = JTabbedPane.LEFT;
			exc.printStackTrace();
		}
		
		setTabPlacement(placement);
	}
}
