/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Component;
import java.lang.reflect.Array;
import java.util.Vector;

import javax.swing.JTabbedPane;

import org.schwering.evi.irc.conf.Configuration;

/**
 * The IRC tabbar.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class TabBar extends JTabbedPane {
	private static final long serialVersionUID = -417945783840186410L;

	/**
	 * Initializes the new TabBar.
	 */
	public TabBar() {
		setTabPlacement(Configuration.getTabPlacement());
	}
	
	/**
	 * Adds a <code>AbstractWindow</code> as last tab.
	 * @param tab The new tab.
	 */
	public void addTab(AbstractWindow tab) {
		addTab(getTabCount(), tab);
	}
	
	/**
	 * Adds a <code>AbstractWindow</code> at a given index.
	 * @param index The tab's index.
	 * @param tab The new tab.
	 */
	public void addTab(int index, AbstractWindow tab) {
		String title = tab.getTitle();
		String shortTitle = shortenTitle(title);
		insertTab(shortTitle, null, tab, title, index);
		tab.requestFocus();
	}
	
	/**
	 * Removes component at the given index.
	 * @param index The tab's index.
	 */
	public void removeTab(int index) {
		Component c = getComponentAt(index);
		if (c instanceof AbstractWindow) {
			removeTab((AbstractWindow)c);
		} else {
			remove(c);
		}
	}
	
	/**
	 * Removes an <code>AbstractWindow</code>.
	 * @param tab The tab.
	 */
	public void removeTab(AbstractWindow tab) {
		remove(tab);
		tab.dispose();
	}
	
	/**
	 * Returns all tabs that are instance of a given class.<br />
	 * <br />
	 * To get all instances of <code>AbstractWindow</code> in a <code>TabBar tb</code>
	 * invoke <code>(AbstractWindow[])tb.getInstancesOf(AbstractWindow.class)</code>.
	 * @param cls The class whose instances are searched.
	 * @return All tabs that are instances of <code>cls</code>.
	 */
	public Object[] getInstancesOf(Class cls) {
		if (cls == null) {
			cls = Object.class;
		}
		
		Vector list = new Vector();
		for (int i = 0; i < getTabCount(); i++) {
			if (cls.isInstance(getComponentAt(i))) {
				list.add(getComponentAt(i));
			}
		}
		return list.toArray((Object[])Array.newInstance(cls, list.size()));
	}
	
	/**
	 * Shortens a title if it's too long.
	 * @param title The original long title.
	 * @return A title where the middle part is replaced with "..." if it's too long.
	 */
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
