/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.conf;

import java.awt.Font;
import java.awt.Color;

/**
 * Interface for different profile types, file (= full) and URI profile.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface Profile {
	public String getName();
	
	public String getServer();
	
	public int getPort();
	
	public boolean getSSL();
	
	public String getPassword();
	
	public String getNickname();
	
	public String getUsername();
	
	public String getRealname();
	
	public String getEncoding();
	
	public String getQuitMessage();
	
	public String getPartMessage();
	
	public Font getConsoleFont();
	
	public Font getChannelFont();
	
	public Font getQueryFont();
	
	public Color getOwnColor();
	
	public Color getOtherColor();
	
	public Color getNeutralColor();
	
	public boolean getEnableColors();
	
	public Color[] getColorPalette();
	
	public boolean getReconnect();
	
	public boolean getRejoinOnKick();
		
	public boolean getBeepOnMention();
	
	public boolean getBeepOnQuery();
	
	public boolean getEnableLogging();
	
	public String getLoggingDir();
	
	public boolean getAcceptCerts();
	
	public String getBrowser();
	
	public String getPerform();
}
