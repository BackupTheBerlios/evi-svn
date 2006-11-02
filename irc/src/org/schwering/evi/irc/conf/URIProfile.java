/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.conf;

import java.awt.Color;
import java.awt.Font;
import java.net.URI;

import org.schwering.evi.conf.MainConfiguration;

/**
 * Profile that takes its from a URI and default values.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class URIProfile implements Profile {
	private URI uri;
	private boolean ssl;
	private String server;
	private int port;
	private String nick;
	private String chan;
	
	public URIProfile(URI uri) {
		this.uri = uri;
		ssl = !uri.getScheme().equals("irc");
		server = uri.getHost();
		int defaultPort = ssl ? DefaultValues.DEFAULT_SSL_PORT : DefaultValues.DEFAULT_PORT;
		port = uri.getPort() != -1 ? uri.getPort() : defaultPort;
		nick = uri.getUserInfo();
		chan = "#"+ uri.getPath().substring(1);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getAcceptCerts()
	 */
	public boolean getAcceptCerts() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getBeepOnMention()
	 */
	public boolean getBeepOnMention() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getBeepOnQuery()
	 */
	public boolean getBeepOnQuery() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getBrowser()
	 */
	public String getBrowser() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getChannelFont()
	 */
	public Font getChannelFont() {
		return MainConfiguration.PROPS.getFont("font.secondary");
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getColorPalette()
	 */
	public Color[] getColorPalette() {
		return DefaultValues.DEFAULT_PALETTE;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getConsoleFont()
	 */
	public Font getConsoleFont() {
		return MainConfiguration.PROPS.getFont("font.primary");
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getEnableColors()
	 */
	public boolean getEnableColors() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getEnableLogging()
	 */
	public boolean getEnableLogging() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getLoggingDir()
	 */
	public String getLoggingDir() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getName()
	 */
	public String getName() {
		return uri.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getNeutralColor()
	 */
	public Color getNeutralColor() {
		return Color.black;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getNickname()
	 */
	public String getNickname() {
		return nick;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getOtherColor()
	 */
	public Color getOtherColor() {
		return Color.red;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getOwnColor()
	 */
	public Color getOwnColor() {
		return Color.blue;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getPartMessage()
	 */
	public String getPartMessage() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getPassword()
	 */
	public String getPassword() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getPerform()
	 */
	public String getPerform() {
		return "JOIN "+ chan;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getPort()
	 */
	
	public int getPort() {
		return port;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getQueryFont()
	 */
	public Font getQueryFont() {
		return MainConfiguration.PROPS.getFont("font.secondary");
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getEncoding()
	 */
	public String getEncoding() {
		return DefaultValues.DEFAULT_ENCODING;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getQuitMessage()
	 */
	
	public String getQuitMessage() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getRealname()
	 */
	public String getRealname() {
		return nick;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getReconnect()
	 */
	public boolean getReconnect() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getRejoinOnKick()
	 */
	public boolean getRejoinOnKick() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getServer()
	 */
	public String getServer() {
		return server;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getSSL()
	 */
	public boolean getSSL() {
		return ssl;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.conf.Profile#getUsername()
	 */
	public String getUsername() {
		return nick;
	}
}
