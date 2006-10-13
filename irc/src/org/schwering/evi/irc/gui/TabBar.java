/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Component;

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
	
	public void addTab(AbstractWindow tab) {
		addTab(getTabCount(), tab);
	}
	
	public void addTab(int index, AbstractWindow tab) {
		String title = tab.getTitle();
		String shortTitle = shortenTitle(title);
		insertTab(shortTitle, null, tab, title, index);
	}
	
	public void removeTab(int index) {
		Component c = getComponentAt(index);
		if (c instanceof AbstractWindow) {
			removeTab((AbstractWindow)c);
		} else {
			remove(c);
		}
	}
	
	public void removeTab(AbstractWindow tab) {
		remove(tab);
		tab.dispose();
	}
	
	public boolean hasConsoleWindow() {
		for (int i = 0; i < getTabCount(); i++) {
			if (getComponentAt(i) instanceof ConsoleWindow) {
				return true;
			}
		}
		return false;
	}
	
	public ConsoleWindow getConsoleWindow() {
		for (int i = 0; i < getTabCount(); i++) {
			if (getComponentAt(i) instanceof ConsoleWindow) {
				return (ConsoleWindow)getComponentAt(i);
			}
		}
		return null;
	}
	
	private static String shortenTitle(String title) {
		if (title == null || title.length() < 18) {
			return title;
		} else {
			String first = title.substring(0, 10);
			String last = title.substring(title.length() - 4);
			return first +"..."+ last;
		}
	}
}
