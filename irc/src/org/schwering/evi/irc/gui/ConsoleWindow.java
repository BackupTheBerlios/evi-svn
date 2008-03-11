/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.schwering.irc.manager.ChannelUser;
import org.schwering.irc.manager.Topic;
import org.schwering.irc.manager.User;
import org.schwering.irc.manager.event.BanlistEvent;
import org.schwering.irc.manager.event.ChannelModeEvent;
import org.schwering.irc.manager.event.ConnectionEvent;
import org.schwering.irc.manager.event.ErrorEvent;
import org.schwering.irc.manager.event.InfoEvent;
import org.schwering.irc.manager.event.InvitationEvent;
import org.schwering.irc.manager.event.LinksEvent;
import org.schwering.irc.manager.event.ListEvent;
import org.schwering.irc.manager.event.MotdEvent;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.NamesEvent;
import org.schwering.irc.manager.event.NickEvent;
import org.schwering.irc.manager.event.NumericEvent;
import org.schwering.irc.manager.event.PingEvent;
import org.schwering.irc.manager.event.TopicEvent;
import org.schwering.irc.manager.event.UnexpectedEvent;
import org.schwering.irc.manager.event.UnexpectedEventAdapter;
import org.schwering.irc.manager.event.UserModeEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;
import org.schwering.irc.manager.event.WhoEvent;
import org.schwering.irc.manager.event.WhoisEvent;
import org.schwering.irc.manager.event.WhowasEvent;

/**
 * Console window.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ConsoleWindow extends SimpleWindow {
	private ConnectionListener connectionListener;
	private UnexpectedEventListener unexpectedEventListener;
	
	public ConsoleWindow(ConnectionController controller) {
		super(controller);
		setTitle("Console ("+ controller.getProfile().getName() +")");
		connectionListener = new ConnectionListener();
		controller.getConnection().addConnectionListener(connectionListener);
		unexpectedEventListener = new UnexpectedEventListener();
		controller.getConnection().addUnexpectedEventListener(unexpectedEventListener);
		addToTabBar();
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#updateLayout()
	 */
	public void updateLayout() {
		Font font = controller.getProfile().getConsoleFont();
		Color fg = controller.getProfile().getNeutralColor();
		Color bg = Color.white;
		updateLayout(font, fg, bg);
	}
	
	public void dispose() {
		controller.getConnection().removeConnectionListener(connectionListener);
		controller.getConnection().removeUnexpectedEventListener(unexpectedEventListener);
	}

	public Object getObject() {
		return controller;
	}
	
	public void inputSubmitted(String str) {
		str = str.trim();
		if (str.length() > 0 && str.charAt(0) == '/') {
			str = str.substring(1);
		}
		if (str.length() > 0) {
			appendLine("Sending to server: "+ str);
			controller.getConnection().send(str);
		}
	}
	
	private class ConnectionListener implements org.schwering.irc.manager.event.ConnectionListener {
		public void banlistReceived(BanlistEvent event) {
			appendLine("Banlist for "+ event.getChannel());
			for (int i = 0; i < event.getCount(); i++) {
				String id = event.getBanID(i);
				User user = event.getUser(i);
				Date date = event.getDate(i);
				String text = (i+1)+".: "+ id;
				if (user != null) {
					text += " by "+ user;
				}
				if (date != null) {
					text += " ("+date+")";
				}
				appendText(text);
				newLine();
			}
		}

		public void channelJoined(UserParticipationEvent event) {
			appendLine("Joined channel "+ event.getChannel());
		}

		public void channelLeft(UserParticipationEvent event) {
			String text;
			if (event.isKick()) {
				text = "left "+ event.getChannel() +" by being kicked by "+ event.getKickingUser();
			} else if (event.isPart()) {
				text = "left "+ event.getChannel() +" by parting";
			} else if (event.isQuit()) {
				text = "quit";
			} else {
				text = "left (this indicates an error in IRClib)";
			}
			appendLine(event.getUser() + text);
		}

		public void channelModeReceived(ChannelModeEvent event) {
			appendLine("Channel mode for "+ event.getChannel() +" received: "+
					event.getIrcModeParser().getLine());
		}

		public void connectionEstablished(ConnectionEvent event) {
			String perform = controller.getProfile().getPerform();
			StringTokenizer tokenizer = new StringTokenizer(perform, "\n");
			appendLine("Executing perform:");
			while (tokenizer.hasMoreTokens()) {
				String tok = (String)tokenizer.nextToken();
				tok = tok.trim();
				if (tok.length() > 0 && tok.charAt(0) == '/') {
					tok = tok.substring(1);
				}
				if (tok.length() > 0) {
					appendLine("Sending to server: "+ tok);
					controller.getConnection().send(tok);
				}
			}
		}

		public void connectionLost(ConnectionEvent event) {
			appendLine("Disconnected.");
		}

		public void errorReceived(ErrorEvent event) {
			appendLine("An error occurred: "+ event.getMessage());
		}

		public void invitationReceived(InvitationEvent event) {
			appendLine("You ("+ event.getInvitedUser() +" got invited "+ 
					"by "+ event.getInvitingUser() +" to "+ event.getChannel());
		}

		public void messageReceived(MessageEvent event) {
			Object tgt = ((event.getDestinationChannel() != null)
					? (Object)event.getDestinationChannel()
					: (Object)event.getDestinationUser());
			appendLine("Received message to "+ tgt +": "+ event.getMessage());
		}

		public void motdReceived(MotdEvent event) {
			appendLine("MOTD received:");
			for (Iterator it = event.getText().iterator(); it.hasNext(); ) {
				appendText(it.next().toString());
				newLine();
			}
		}

		public void namesReceived(NamesEvent event) {
			appendLine("Names of "+ event.getChannel() +" received:");
			for (Iterator it = event.getChannelUsers().iterator(); it.hasNext(); ) {
				ChannelUser channelUser = (ChannelUser)it.next();
				String away = channelUser.isAway() ? "is gone." : "is here.";
				if (channelUser.isOperator()) {
					appendText(channelUser +" is operator and "+ away);
				} else if (channelUser.isVoiced()) {
					appendText(channelUser +" is voiced and "+ away);
				} else {
					appendText(channelUser +" "+ away);
				}
				newLine();
			}
		}

		public void nickChanged(NickEvent event) {
			appendLine(event.getOldNick() +" changed his nickname to "+ 
					event.getUser());
		}

		public void noticeReceived(MessageEvent event) {
			Object tgt = ((event.getDestinationChannel() != null)
					? (Object)event.getDestinationChannel()
					: (Object)event.getDestinationUser());
			appendLine("Received notice to "+ tgt +": "+ event.getMessage());
		}

		public void numericErrorReceived(NumericEvent event) {
			appendLine("Received numeric error #"+ event.getNumber() +": "+
					event.getValue() +" "+ event.getMessage());
		}

		public void numericReplyReceived(NumericEvent event) {
			appendLine("Received numeric reply #"+ event.getNumber() +": "+
					event.getValue() +" "+ event.getMessage());
		}

		public void pingReceived(PingEvent event) {
			appendLine("PING? PONG!");
		}

		public void topicReceived(TopicEvent event) {
			Topic topic = event.getTopic();
			appendLine("Received topic of "+ topic.getChannel());
			if (topic.getDate() != null && topic.getUser() != null) {
				appendText("It was set on "+ topic.getDate() +" by "+ topic.getUser());
				newLine();
			} else if (topic.getDate() != null) {
				appendText("It was set on "+ topic.getDate());
				newLine();
			} else if (topic.getUser() != null) {
				appendText("It was set by "+ topic.getUser());
				newLine();
			}
			if (topic.getMessage() != null && !topic.getMessage().isEmpty()) {
				appendText("The topic is: ");
				appendMessage(topic.getMessage());
				newLine();
			} else {
				appendText("The topic was removed");
				newLine();
			}
		}

		public void userJoined(UserParticipationEvent event) {
			appendLine("The user "+ event.getUser() +" joined "+ 
					event.getChannel());
		}

		public void userLeft(UserParticipationEvent event) {
			appendLine("The user "+ event.getUser() +" left "+ 
					event.getChannel());
		}

		public void userModeReceived(UserModeEvent event) {
			appendLine("The user "+ event.getActiveUser() +" changed "+ 
					event.getPassiveUser() +"'s mode to "+ event.getMode());
		}

		public void whoisReceived(WhoisEvent event) {
			appendLine("Information about "+ event.getUser() +" received:");
			if (event.getUser().getNick() != null) {
				appendText("Nickname: "+ event.getUser().getNick());
				newLine();
			}
			if (event.getUser().getUsername() != null) {
				appendText("Username: "+ event.getUser().getUsername());
				newLine();
			}
			if (event.getUser().getHost() != null) {
				appendText("Host: "+ event.getUser().getHost());
				newLine();
			}
			if (event.getRealname() != null) {
				appendText("Real name: "+ event.getRealname());
				newLine();
			}
			if (event.getAuthname() != null) {
				appendText("Authed as: "+ event.getAuthname());
				newLine();
			}
			if (event.isIdle() && event.getAwayMessage() != null && event.getIdleMillis() != -1) {
				appendText("Is idle since "+ event.getIdleTime() +": "+ event.getAwayMessage());
				newLine();
			} else if (event.isIdle() && event.getIdleMillis() != -1) {
				appendText("Is idle since "+ event.getIdleTime());
				newLine();
			} else if (event.isIdle()) {
				appendText("Is idle.");
				newLine();
			}
			if (event.getServer() != null) {
				appendText("Is connected to: "+ event.getServer());
				if (event.getServerInfo() != null) {
					appendText(" ("+ event.getServerInfo() +")");
				}
				if (event.getSignonDate() != null) {
					appendText(" since "+ event.getSignonDate());
				}
				newLine();
			} else if (event.getSignonDate() != null) {
				appendText("Is connected since "+ event.getSignonDate());
				newLine();
			}
			if (event.getChannelCount() > 0) {
				appendText("Is on channels:");
				for (int i = 0; i < event.getChannelCount(); i++) {
					appendText(" "+ event.getChannel(i));
					if (event.getChannelStatus(i) == ChannelUser.OPERATOR) {
						appendText(" (operator)");
					}
					if (event.getChannelStatus(i) == ChannelUser.VOICED) {
						appendText(" (operator)");
					}
					if (i+1 < event.getChannelCount()) {
						appendText(",");
					}
				}
				newLine();
			}
		}

		public void infoReceived(InfoEvent event) {
			appendLine("Info received:");
			for (Iterator it = event.getText().iterator(); it.hasNext(); ) {
				appendText(it.next().toString());
				newLine();
			}
		}

		public void linksReceived(LinksEvent event) {
			appendLine("Links for mask "+ event.getMask() +" received:");
			for (int i = 0; i < event.getCount(); i++) {
				String server = event.getServer(i);
				String serverInfo = event.getServerInfo(i);
				String hopCount = event.getHopCount(i);
				appendText(server +" ("+ serverInfo +", hop count "+ hopCount +")");
				newLine();
			}
		}

		public void listReceived(ListEvent event) {
			appendLine("List received:");
			long i = 1;
			for (Iterator it1 = event.getTopics().iterator(), 
					it2 = event.getVisibleCounts().iterator();
					it1.hasNext() && it2.hasNext(); ) {
				Topic topic = (Topic)it1.next();
				int visibleCount = ((Integer)it2.next()).intValue();
				String channel = topic.getChannel().getName();
				if (topic.getMessage() != null) {
					appendText(i +".: "+ channel +" ("+ visibleCount +" visible users): ");
					appendMessage(topic.getMessage());
				} else {
					appendText(i +".: "+ channel +" ("+ visibleCount +" visible users) has no topic");
				}
				newLine();
				i++;
			}
		}

		public void whoReceived(WhoEvent event) {
			appendLine("Who of "+ event.getChannel() +" received:");
			for (int i = 0; i < event.getCount(); i++) {
				ChannelUser channelUser = event.getChannelUser(i);
				String realName = event.getRealname(i);
				String hopCount = event.getHopCount(i);
				String server = event.getServer(i);
				
				String descr = channelUser.getNick() +" ("+ realName +")";
				String away = channelUser.isAway() ? "is gone, " : "is here, ";
				if (channelUser.isOperator()) {
					appendText(descr +" is operator and "+ away);
				} else if (channelUser.isVoiced()) {
					appendText(descr +" is voiced and "+ away);
				} else {
					appendText(descr +" "+ away);
				}
				appendText(", connected to "+ server 
						+" (hop count "+ hopCount +")");
				newLine();
			}
		}

		public void whowasReceived(WhowasEvent event) {
			appendLine("Who was "+ event.getUser().getNick() +"?");
			appendText("Username: "+ event.getUser().getUsername());
			newLine();
			appendText("Realname: "+ event.getRealname());
			newLine();
			appendText("Host: "+ event.getUser().getHost());
			newLine();
		}
	}
	
	private class UnexpectedEventListener extends UnexpectedEventAdapter {
		public void unexpectedEventReceived(UnexpectedEvent event) {
			appendLine("Unexpected event '"+ event.getEventName() +"' received:");
			appendText("Arguments: ");
			Object[] args = event.getArguments();
			for (int i = 0; i < args.length; i++) {
				appendText(args[i] +" ");
			}
			newLine();
		}
	}
}
