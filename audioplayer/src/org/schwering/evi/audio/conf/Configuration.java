/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.conf;

import java.io.File;

import org.schwering.evi.audio.AudioPlayer;
import org.schwering.evi.conf.Properties;
import org.schwering.evi.core.ModuleContainer;

/**
 * Configuration wrapper.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class Configuration {
	private static Properties props;
	
	static {
		try {
			props = new Properties(ModuleContainer.getIdByClass(AudioPlayer.class));
			props.load();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public static void store() {
		if (props != null) {
			try {
				props.store();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	public static File getLastFile() {
		if (props != null) {
			String s = props.getString("last.file", null);
			return (s != null) ? new File(s) : null;
		} else {
			return null;
		}
	}
	
	public static void setLastFile(File f) {
		if (props != null && f != null) {
			props.setString("last.file", f.toString());
		}
	}

	public static File getLastDirectory() {
		if (props != null) {
			String s = props.getString("last.dir", null);
			return (s != null) ? new File(s) : null;
		} else {
			return null;
		}
	}
	
	public static void setLastDirectory(File f) {
		if (props != null && f != null) {
			props.setString("last.dir", f.toString());
		}
	}
	
	public static boolean isShuffle() {
		if (props != null) {
			return props.getBoolean("shuffle", false);
		} else {
			return false;
		}
	}
	
	public static void setShuffle(boolean b) {
		if (props != null) {
			props.setBoolean("shuffle", b);
		}
	}
	
	public static boolean isPlayAll() {
		if (props != null) {
			return props.getBoolean("playall", true);
		} else {
			return true;
		}
	}
	
	public static void setPlayAll(boolean b) {
		if (props != null) {
			props.setBoolean("playall", b);
		}
	}
	
	public static boolean isSearchDirectly() {
		if (props != null) {
			return props.getBoolean("searchdirectly", true);
		} else {
			return true;
		}
	}
	
	public static void setSearchDirectly(boolean b) {
		if (props != null) {
			props.setBoolean("searchdirectly", b);
		}
	}
	
	public static boolean isApplet() {
		if (props != null) {
			return props.getBoolean("applet", true);
		} else {
			return true;
		}
	}
	
	public static void setApplet(boolean b) {
		if (props != null) {
			props.setBoolean("applet", b);
		}
	}
}
