package org.schwering.evi.irc.gui;

import org.schwering.evi.irc.IRC;
import org.schwering.evi.irc.conf.Profile;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.event.ConnectionAdapter;
import org.schwering.irc.manager.event.PrivateMessageAdapter;

public class ConnectionController {
	private IRC irc;
	private Profile profile;
	private Connection connection;
	private ConsoleWindow consoleWindow;
	
	public ConnectionController(IRC irc, Profile profile) {
		this.irc = irc;
		this.profile = profile;
		connection = new Connection(profile.getServer(), profile.getPort(),
				profile.getPort(), profile.getSSL(), profile.getPassword(), 
				profile.getNickname(), profile.getUsername(), 
				profile.getName());
		connection.setEncoding(profile.getEncoding());
		connection.setRequestModes(true);
		connection.addConnectionListener(new ConnectionListener());
		connection.addPrivateMessageListener(new PrivateMessageListener());
		consoleWindow = new ConsoleWindow(this);
		try {
			connection.connect();
		} catch (Exception exc) {
			// TODO better error handling
			exc.printStackTrace();
		}
	}
	
	public IRC getIRC() {
		return irc;
	}

	public Profile getProfile() {
		return profile;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	private class ConnectionListener extends ConnectionAdapter {
		
	}
	
	private class PrivateMessageListener extends PrivateMessageAdapter {
		
	}
}
