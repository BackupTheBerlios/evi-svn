/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.conf;

import java.awt.Font;
import java.awt.Color;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.irc.IRC;
import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.conf.Properties;

/**
 * Profile that stores the settings in a file.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class FullProfile implements Profile {
	private static final String PROFILE_PREFIX = ModuleContainer.getIdByClass(IRC.class) +"_profile_";
	private Properties props;
	private String name;
	
	private static Hashtable profileTable = new Hashtable();
	
	public static FullProfile[] getProfiles() {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(PROFILE_PREFIX);
			}
		};
		File[] files = MainConfiguration.CONFIG_DIR.listFiles(filter);
		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			name = name.substring(PROFILE_PREFIX.length());
			if (!profileTable.containsKey(name)) {
				try {
					FullProfile profile = new FullProfile(name);
					profileTable.put(name, profile);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		}
		Collection profileCollection = profileTable.values();
		FullProfile[] profiles = new FullProfile[profileCollection.size()];
		profileCollection.toArray(profiles);
		return profiles;
	}
	
	public FullProfile(String name) throws IOException {
		props = new Properties(PROFILE_PREFIX + name);
		props.setShutdownHook(true);
		props.load();
		this.name = name;
	}
	
	public void delete() {
		props.setShutdownHook(false);
		props.getFile().delete();
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public File getFile() {
		return props.getFile();
	}
	
	public void setServer(String s) {
		props.setString("conn.server", s);
	}
	
	public String getServer() {
		return props.getString("conn.server", "");
	}
	
	public void setPort(int port) {
		props.setInt("conn.port", port);
	}
	
	public int getPort() {
		return props.getInt("conn.port", DefaultValues.DEFAULT_PORT);
	}
	
	public void setSSL(boolean useSSL) {
		props.setBoolean("conn.ssl", useSSL);
	}
	
	public boolean getSSL() {
		return props.getBoolean("conn.ssl", false);
	}
	
	public void setPassword(String s) {
		props.setBase64String("conn.pswd", s);
	}
	
	public String getPassword() {
		return props.getBase64String("conn.pswd", "");
	}
	
	public void setNickname(String s) {
		props.setString("conn.nick", s);
	}
	
	public String getNickname() {
		return props.getString("conn.nick", "");
	}
	
	public void setUsername(String s) {
		props.setString("conn.user", s);
	}
	
	public String getUsername() {
		return props.getString("conn.user", "");
	}
	
	public void setRealname(String s) {
		props.setString("conn.real", s);
	}
	
	public String getRealname() {
		return props.getString("conn.real", "");
	}
	
	public void setQuitMessage(String s) {
		props.setString("conn.quitmsg", s);
	}
	
	public String getQuitMessage() {
		return props.getString("conn.quitmsg", "");
	}
	
	public void setPartMessage(String s) {
		props.setString("conn.partmsg", s);
	}
	
	public String getPartMessage() {
		return props.getString("conn.partmsg", "");
	}
	
	public void setConsoleFont(Font f) {
		props.setFont("font.console", f);
	}
	
	public Font getConsoleFont() {
		return props.getFont("font.console");
	}
	
	public void setChannelFont(Font f) {
		props.setFont("font.channel", f);
	}
	
	public Font getChannelFont() {
		return props.getFont("font.channel");
	}
	
	public void setQueryFont(Font f) {
		props.setFont("font.query", f);
	}
	
	public Font getQueryFont() {
		return props.getFont("font.query");
	}
	
	public void setOwnColor(Color c) {
		props.setColor("color.own", c);
	}
	
	public Color getOwnColor() {
		return props.getColor("color.own", Color.blue);
	}
	
	public void setOtherColor(Color c) {
		props.setColor("color.other", c);
	}
	
	public Color getOtherColor() {
		return props.getColor("color.other", Color.red);
	}
	
	public void setNeutralColor(Color c) {
		props.setColor("color.neutral", c);
	}
	
	public Color getNeutralColor() {
		return props.getColor("color.neutral", Color.black);
	}
	
	public void setEnableColors(boolean b) {
		props.setBoolean("color.enable", b);
	}
	
	public boolean getEnableColors() {
		return props.getBoolean("color.enable", true);
	}
	
	public void setColorPalette(Color[] palette) {
		if (palette.length != DefaultValues.PALETTE_SIZE) {
			throw new IllegalArgumentException("palette.length = "+ palette.length +", must be "+ DefaultValues.PALETTE_SIZE);
		}
		for (int i = 0; i < palette.length; i++) {
			props.setColor("color."+ i, palette[i]);
		}
	}
	
	public Color[] getColorPalette() {
		Color[] arr = new Color[DefaultValues.PALETTE_SIZE];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = props.getColor("color."+ i, null);
			if (arr[i] == null) {
				arr[i] = DefaultValues.DEFAULT_PALETTE[i];
			}
		}
		return arr;
	}
	
	public void setReconnect(boolean b) {
		props.setBoolean("misc.reconnect", b);
	}
	
	public boolean getReconnect() {
		return props.getBoolean("misc.reconnect", true);
	}
	
	public void setRejoinOnKick(boolean b) {
		props.setBoolean("misc.rejoin", b);
	}
	
	public boolean getRejoinOnKick() {
		return props.getBoolean("misc.rejoin", false);
	}
	
	public void setBeepOnMention(boolean b) {
		props.setBoolean("misc.beepmention", b);
	}
	
	public boolean getBeepOnMention() {
		return props.getBoolean("misc.beepmention");
	}
	
	public void setBeepOnQuery(boolean b) {
		props.setBoolean("misc.beepquery", b);
	}
	
	public boolean getBeepOnQuery() {
		return props.getBoolean("misc.beepquery");
	}
	
	public void setEnableLogging(boolean b) {
		props.setBoolean("misc.logging", b);
	}
	
	public boolean getEnableLogging() {
		return props.getBoolean("misc.logging");
	}
	
	public void setLoggingDir(String s) {
		props.setString("misc.loggingdir", s);
	}
	
	public String getLoggingDir() {
		return props.getString("misc.loggingdir", null);
	}
	
	public void setAcceptCerts(boolean b) {
		props.setBoolean("misc.acceptcerts", b);
	}
	
	public boolean getAcceptCerts() {
		return props.getBoolean("misc.acceptcerts");
	}
	
	public void setBrowser(String s) {
		props.setString("misc.browser", s);
	}
	
	public String getBrowser() {
		return props.getString("misc.browser", null);
	}
	
	public void setPerform(String s) {
		props.setString("perform", s);
	}
	
	public String getPerform() {
		return props.getString("perform");
	}
}
