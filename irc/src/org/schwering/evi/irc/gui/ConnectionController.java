package org.schwering.evi.irc.gui;

import java.awt.Component;

import org.schwering.evi.irc.IRC;
import org.schwering.evi.irc.conf.Profile;
import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.event.ConnectionAdapter;
import org.schwering.irc.manager.event.ConnectionEvent;
import org.schwering.irc.manager.event.CtcpActionEvent;
import org.schwering.irc.manager.event.CtcpClientinfoRequestEvent;
import org.schwering.irc.manager.event.CtcpClientinfoResponseEvent;
import org.schwering.irc.manager.event.CtcpDccChatEvent;
import org.schwering.irc.manager.event.CtcpDccSendEvent;
import org.schwering.irc.manager.event.CtcpErrmsgRequestEvent;
import org.schwering.irc.manager.event.CtcpErrmsgResponseEvent;
import org.schwering.irc.manager.event.CtcpFingerRequestEvent;
import org.schwering.irc.manager.event.CtcpFingerResponseEvent;
import org.schwering.irc.manager.event.CtcpPingRequestEvent;
import org.schwering.irc.manager.event.CtcpPingResponseEvent;
import org.schwering.irc.manager.event.CtcpSedEvent;
import org.schwering.irc.manager.event.CtcpSourceRequestEvent;
import org.schwering.irc.manager.event.CtcpSourceResponseEvent;
import org.schwering.irc.manager.event.CtcpTimeRequestEvent;
import org.schwering.irc.manager.event.CtcpTimeResponseEvent;
import org.schwering.irc.manager.event.CtcpUnknownRequestEvent;
import org.schwering.irc.manager.event.CtcpUnknownResponseEvent;
import org.schwering.irc.manager.event.CtcpUserinfoRequestEvent;
import org.schwering.irc.manager.event.CtcpUserinfoResponseEvent;
import org.schwering.irc.manager.event.CtcpVersionRequestEvent;
import org.schwering.irc.manager.event.CtcpVersionResponseEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;
import org.schwering.irc.manager.event.WhoisEvent;
import org.schwering.irc.manager.event.WhowasEvent;

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
				profile.getRealname());
		connection.setDebug(true);
		connection.setEncoding(profile.getEncoding());
		connection.setRequestModes(true);
		connection.addConnectionListener(new ConnectionListener());
		connection.addCtcpListener(new CtcpListener());
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

		public void whoisReceived(WhoisEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("Information about "+ event.getUser() +" received:");
				if (event.getUser().getNick() != null) {
					w.appendText("Nickname: "+ event.getUser().getNick());
					w.newLine();
				}
				if (event.getUser().getUsername() != null) {
					w.appendText("Username: "+ event.getUser().getUsername());
					w.newLine();
				}
				if (event.getUser().getHost() != null) {
					w.appendText("Host: "+ event.getUser().getHost());
					w.newLine();
				}
				if (event.getRealname() != null) {
					w.appendText("Real name: "+ event.getRealname());
					w.newLine();
				}
				if (event.getAuthname() != null) {
					w.appendText("Authed as: "+ event.getAuthname());
					w.newLine();
				}
				if (event.isIdle() && event.getAwayMessage() != null && event.getIdleMillis() != -1) {
					w.appendText("Is idle since "+ event.getIdleTime() +": "+ event.getAwayMessage());
					w.newLine();
				} else if (event.isIdle() && event.getIdleMillis() != -1) {
					w.appendText("Is idle since "+ event.getIdleTime());
					w.newLine();
				} else if (event.isIdle()) {
					w.appendText("Is idle.");
					w.newLine();
				}
				if (event.getServer() != null) {
					w.appendText("Is connected to: "+ event.getServer());
					if (event.getServerInfo() != null) {
						w.appendText(" ("+ event.getServerInfo() +")");
					}
					if (event.getSignonDate() != null) {
						w.appendText(" since "+ event.getSignonDate());
					}
					w.newLine();
				} else if (event.getSignonDate() != null) {
					w.appendText("Is connected since "+ event.getSignonDate());
					w.newLine();
				}
				if (event.getChannelCount() > 0) {
					w.appendText("Is on channels:");
					for (int i = 0; i < event.getChannelCount(); i++) {
						w.appendText(" "+ event.getChannel(i));
						if ((event.getChannelStatus(i) & Channel.OPERATOR) != 0){
							w.appendText(" (operator)");
						}
						if (event.getChannelStatus(i) == Channel.VOICED) {
							w.appendText(" (voiced)");
						}
						if (i+1 < event.getChannelCount()) {
							w.appendText(",");
						}
					}
					w.newLine();
				}
			}
		}

		public void whowasReceived(WhowasEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("Who was "+ event.getUser().getNick() +"?");
				w.appendText("Username: "+ event.getUser().getUsername());
				w.newLine();
				w.appendText("Realname: "+ event.getRealname());
				w.newLine();
				w.appendText("Host: "+ event.getUser().getHost());
				w.newLine();
			}
		}
	}
	
	private class CtcpListener 
	implements org.schwering.irc.manager.event.CtcpListener {
		public void actionReceived(CtcpActionEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
			}
		}

		public void clientinfoRequestReceived(CtcpClientinfoRequestEvent event) {
		}

		public void clientinfoResponseReceived(CtcpClientinfoResponseEvent event) {
		}

		public void dccChatReceived(CtcpDccChatEvent event) {
		}

		public void dccSendReceived(CtcpDccSendEvent event) {
		}

		public void errmsgRequestReceived(CtcpErrmsgRequestEvent event) {
		}

		public void errmsgResponseReceived(CtcpErrmsgResponseEvent event) {
		}

		public void fingerRequestReceived(CtcpFingerRequestEvent event) {
		}

		public void fingerResponseReceived(CtcpFingerResponseEvent event) {
		}

		public void pingRequestReceived(CtcpPingRequestEvent event) {
		}

		public void pingResponseReceived(CtcpPingResponseEvent event) {
		}

		public void sedReceived(CtcpSedEvent event) {
		}

		public void sourceRequestReceived(CtcpSourceRequestEvent event) {
		}

		public void sourceResponseReceived(CtcpSourceResponseEvent event) {
		}

		public void timeRequestReceived(CtcpTimeRequestEvent event) {
		}

		public void timeResponseReceived(CtcpTimeResponseEvent event) {
		}

		public void unknownRequestEventReceived(CtcpUnknownRequestEvent event) {
		}

		public void unknownResponseEventReceived(CtcpUnknownResponseEvent event) {
		}

		public void userinfoRequestReceived(CtcpUserinfoRequestEvent event) {
		}

		public void userinfoResponseReceived(CtcpUserinfoResponseEvent event) {
		}

		public void versionRequestReceived(CtcpVersionRequestEvent event) {
		}

		public void versionResponseReceived(CtcpVersionResponseEvent event) {
		}
	}
}
