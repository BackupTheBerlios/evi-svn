package org.schwering.evi.irc.gui;

import org.schwering.evi.irc.IRC;
import org.schwering.evi.irc.conf.Profile;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.event.ConnectionAdapter;
import org.schwering.irc.manager.event.ConnectionEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;

public class ConnectionController {
	private ConnectionController ptrToThis = this;
	private IRC irc;
	private Profile profile;
	private Connection connection;
	
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
		new ConsoleWindow(this);
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
		public void connectionEstablished(ConnectionEvent event) {
			SimpleWindow[] windows = (SimpleWindow[])irc.getTabBar().getInstancesOf(SimpleWindow.class);
			for (int i = 0; i < windows.length; i++) {
				windows[i].appendLine("Connected.");
			}
		}

		public void connectionLost(ConnectionEvent event) {
			SimpleWindow[] windows = (SimpleWindow[])irc.getTabBar().getInstancesOf(SimpleWindow.class);
			for (int i = 0; i < windows.length; i++) {
				windows[i].appendLine("Disconnected.");
			}
		}
		
		public void channelJoined(UserParticipationEvent event) {
			new ChannelWindow(ptrToThis, event.getChannel());
		}

		public void channelLeft(UserParticipationEvent event) {
			irc.getTabBar().removeTab(event.getChannel());
		}
	}
}
