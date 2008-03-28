/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Component;
import java.io.File;
import java.net.Socket;
import java.util.Date;

import javax.swing.JOptionPane;

import org.schwering.evi.gui.EVI;
import org.schwering.evi.irc.IRC;
import org.schwering.evi.irc.IRCInfo;
import org.schwering.evi.irc.conf.Profile;
import org.schwering.evi.util.ExceptionDialog;
import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.event.ConnectionAdapter;
import org.schwering.irc.manager.event.ConnectionEvent;
import org.schwering.irc.manager.event.CtcpActionEvent;
import org.schwering.irc.manager.event.CtcpClientinfoReplyEvent;
import org.schwering.irc.manager.event.CtcpClientinfoRequestEvent;
import org.schwering.irc.manager.event.CtcpDccChatEvent;
import org.schwering.irc.manager.event.CtcpDccSendEvent;
import org.schwering.irc.manager.event.CtcpErrmsgReplyEvent;
import org.schwering.irc.manager.event.CtcpErrmsgRequestEvent;
import org.schwering.irc.manager.event.CtcpFingerReplyEvent;
import org.schwering.irc.manager.event.CtcpFingerRequestEvent;
import org.schwering.irc.manager.event.CtcpPingReplyEvent;
import org.schwering.irc.manager.event.CtcpPingRequestEvent;
import org.schwering.irc.manager.event.CtcpSedEvent;
import org.schwering.irc.manager.event.CtcpSourceReplyEvent;
import org.schwering.irc.manager.event.CtcpSourceRequestEvent;
import org.schwering.irc.manager.event.CtcpTimeReplyEvent;
import org.schwering.irc.manager.event.CtcpTimeRequestEvent;
import org.schwering.irc.manager.event.CtcpUnknownReplyEvent;
import org.schwering.irc.manager.event.CtcpUnknownRequestEvent;
import org.schwering.irc.manager.event.CtcpUserinfoReplyEvent;
import org.schwering.irc.manager.event.CtcpUserinfoRequestEvent;
import org.schwering.irc.manager.event.CtcpVersionReplyEvent;
import org.schwering.irc.manager.event.CtcpVersionRequestEvent;
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
				w.appendLine(event.getSender().getNick() +" is ", 
						event.getMessage(), profile.getOtherColor());
			}
		}

		public void clientinfoReplyReceived(CtcpClientinfoReplyEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("Client info of "+ event.getSender() 
						+": "+ event.getClientinfo(), 
						profile.getNeutralColor());
			}
		}

		public void clientinfoRequestReceived(CtcpClientinfoRequestEvent event) {
			event.reply("IRClib 2.00 with EVI IRC");
		}

		public void dccChatReceived(CtcpDccChatEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("DCC Chat offered with "+ event.getSender() 
						+ " ("+ event.getAddress().getHostAddress() +":"+ event.getPort() +")",
						profile.getNeutralColor());
				String question = "Do you want to chat with\n"
					+ event.getSender() + " ("
					+ event.getAddress().getHostAddress() +":"+ event.getPort()
					+ ")?";
				int answer = JOptionPane.showConfirmDialog(null, question, 
						"DCC Send", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					try {
						Socket sock = new Socket(event.getAddress(), event.getPort());
						new ChatWindow(ptrToThis, event.getSender(), sock);
					} catch (Exception exc) {
						ExceptionDialog.show(exc);
					}
				}
			}
		}

		public void dccSendReceived(CtcpDccSendEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("DCC File: "+ event.getFile() +" from "
						+ event.getSender() 
						+ " ("+ event.getAddress().getHostAddress() +":"+ event.getPort() +") "+
						+ (event.getSize()/1024) +"k",
						profile.getNeutralColor());
				File file = new File(event.getFile());
				String question;
				if (file.exists()) {
					question = "Do you want to override\n"+ file +"?";
				} else {
					question = "Do you want to receive\n"+ file +"?";
				}
				int answer = JOptionPane.showConfirmDialog(null, question, 
						"DCC Send", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					try {
						event.accept(file);
					} catch (Exception exc) {
						ExceptionDialog.show(exc);
					}
				}
			}
		}

		public void errmsgReplyReceived(CtcpErrmsgReplyEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("Error message for "+ event.getQuery() +": "+
						event.getAnswer(),
						profile.getNeutralColor());
			}
		}

		public void errmsgRequestReceived(CtcpErrmsgRequestEvent event) {
			event.reply("I don't need no error messages");
		}

		public void fingerReplyReceived(CtcpFingerReplyEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("Finger of "+ event.getSender() +": "
						+ event.getInfo(),
						profile.getNeutralColor());
			}
		}

		public void fingerRequestReceived(CtcpFingerRequestEvent event) {
			event.reply("studying computer science at RWTH Aachen University");
		}

		public void pingReplyReceived(CtcpPingReplyEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("Ping of "+ event.getSender() +": "
						+ event.getTimestamp(),
						profile.getNeutralColor());
			}
		}

		public void pingRequestReceived(CtcpPingRequestEvent event) {
			event.reply();
		}

		public void sedReceived(CtcpSedEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("SED "+ event.getSender() +": "
						+ event.getArguments(),
						profile.getNeutralColor());
			}
		}

		public void sourceReplyReceived(CtcpSourceReplyEvent event) {
			if (event.isEndMarker()) {
				return;
			}
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				String tmp = event.getHost() +"/"+ event.getDirectory();
				for (String file : event.getFiles().split(" ")) {
					w.appendLine("Source of "+ event.getSender() +": "
							+ tmp +"/"+ file,
							profile.getNeutralColor());
				}
			}
		}

		public void sourceRequestReceived(CtcpSourceRequestEvent event) {
			event.reply("prdownloads.sourceforge.net", "moepii", "irclib-1.10.zip");
			event.reply();
		}

		public void timeReplyReceived(CtcpTimeReplyEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("Time of "+ event.getSender() +": "
						+ event.getTime(),
						profile.getNeutralColor());
			}
		}

		public void timeRequestReceived(CtcpTimeRequestEvent event) {
			event.reply(new Date().toString());
		}

		public void unknownReplyEventReceived(CtcpUnknownReplyEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("Unknown CTCP reply from "+ event.getSender() +": "
						+ event.getCommand() +": "+ event.getArguments(),
						profile.getNeutralColor());
			}
		}

		public void unknownRequestEventReceived(CtcpUnknownRequestEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("Unknown CTCP request from "+ event.getSender() +": "
						+ event.getCommand() +": "+ event.getArguments(),
						profile.getNeutralColor());
			}
		}

		public void userinfoReplyReceived(CtcpUserinfoReplyEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("User info of "+ event.getSender() 
						+": "+ event.getUserinfo(), 
						profile.getNeutralColor());
			}
		}

		public void userinfoRequestReceived(CtcpUserinfoRequestEvent event) {
			event.reply("CS-student from RWTH Aachen university (www.rwth-aachen.de)");
		}

		public void versionReplyReceived(CtcpVersionReplyEvent event) {
			Component c = irc.getTabBar().getSelectedComponent();
			if (c != null && c instanceof SimpleWindow) {
				SimpleWindow w = (SimpleWindow)c;
				w.appendLine("Version of "+ event.getSender() 
						+": "+ event.getVersion(), 
						profile.getNeutralColor());
			}
		}

		public void versionRequestReceived(CtcpVersionRequestEvent event) {
			event.reply("IRClib 2.0 on "+ EVI.TITLE +" "+ EVI.VERSION +" on "+
					new IRCInfo().getName() +" "+ new IRCInfo().getVersion());
		}
	}
}
