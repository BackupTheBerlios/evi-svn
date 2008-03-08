/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc;

import org.schwering.evi.irc.conf.Profile;
import org.schwering.evi.irc.gui.ConnectionController;

/**
 * Kind of interface between IRC and GUI.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ConnectionManager {
	/**
	 * Connects a profile.
	 * @param irc
	 * @param profile
	 */
	public static void connect(IRC irc, Profile profile) {
		new ConnectionController(irc, profile);
//		TabBar tabBar = irc.getTabBar();
//		
//		ConsoleWindow console;
//		ConsoleWindow[] arr = (ConsoleWindow[])tabBar.getInstancesOf(ConsoleWindow.class);
//		if (arr.length == 1) {
//			console = arr[0];
//		} else {
//			console = new ConsoleWindow(profile);
//			tabBar.addTab(0, console);
//		}
	}
}
