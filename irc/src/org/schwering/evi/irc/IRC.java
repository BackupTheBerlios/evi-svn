/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc;

import java.awt.Component;
import java.io.IOException;
import java.net.URI;

import javax.swing.Icon;

import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.gui.main.MainFrame;
import org.schwering.evi.irc.conf.FullProfile;
import org.schwering.evi.irc.conf.Profile;
import org.schwering.evi.irc.conf.URIProfile;
import org.schwering.evi.irc.gui.ConnectPanel;
import org.schwering.evi.irc.gui.TabBar;

/**
 * The IModule class of the IRC module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class IRC implements IModule, IPanel {
	private TabBar tabs = new TabBar();

	public IRC() {
		tabs.addTab("Connect", new ConnectPanel(this));
	}
	
	public IRC(Object[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof Profile) {
				ConnectionManager.connect(this, (Profile)args[i]);
			} else if (args[i] instanceof String) {
				try {
					ConnectionManager.connect(this, new FullProfile((String)args[i]));
				} catch (IOException exc) {
					exc.printStackTrace();
				}
			} else {
				System.err.println("Parameter "+ args[i] +" instance of "+ args[i].getClass());
			}
		}
	}
	
	public IRC(URI uri) {
		ConnectionManager.connect(this, new URIProfile(uri));
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getIcon()
	 */
	public Icon getIcon() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getPanelInstance()
	 */
	public Component getPanelInstance() {
		return tabs;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return "IRC";
	}
	
	public TabBar getTabBar() {
		return tabs;
	}

	public void updateTitle(String newTitle) {
		MainFrame.getInstance().getMainTabBar().setTitle(this, newTitle);
	}
	
	public void reset() {
		MainFrame.getInstance().getMainTabBar().setDefaultBackground(this);
		MainFrame.getInstance().getMainTabBar().setDefaultForeground(this);
	}
	
	public void highlight() {
		MainFrame.getInstance().getMainTabBar().setHighlightForeground(this);
		MainFrame.getInstance().getMainTabBar().setHighlightBackground(this);
	}
}
