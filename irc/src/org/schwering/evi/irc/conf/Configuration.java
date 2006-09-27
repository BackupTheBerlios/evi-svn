/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.conf;

import javax.swing.JTabbedPane;

import org.schwering.evi.conf.Properties;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.irc.IRC;

/**
 * IRC configuration.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class Configuration {
	private static Properties props;
	
	static {
		try {
			props = new Properties(ModuleContainer.getIdByClass(IRC.class));
			props.load();
			props.setShutdownHook(true);
		} catch (Exception exc) {
			exc.printStackTrace();
			props = null;
		}
	}
	
	public static int getTabPlacement() {
		if (props != null) {
			return props.getInt("gui.tabs.placement", JTabbedPane.LEFT);
		} else {
			return JTabbedPane.LEFT;
		}
	}
	
	public static void setTabPlacement(int placement) {
		if (props != null) {
			props.setInt("gui.tabs.placement", placement);
		}
	}
	
	public static String getLastProfile() {
		if (props != null) {
			return props.getString("connect.lastprofile", null);
		} else {
			return null;
		}
	}
	
	public static void setLastProfile(String s) {
		if (props != null) {
			props.setString("connect.lastprofile", s);
		}
	}
	
	public static String getLastURI() {
		if (props != null) {
			return props.getString("connect.lasturi", "irc://nickname@server/channel");
		} else {
			return null;
		}
	}
	
	public static void setLastURI(String s) {
		if (props != null) {
			props.setString("connect.lasturi", s);
		}
	}
}
