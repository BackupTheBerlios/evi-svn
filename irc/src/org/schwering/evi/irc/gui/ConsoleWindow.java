/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.schwering.irc.manager.Channel;
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
import org.schwering.irc.manager.event.StatsEvent;
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 5814642871030009539L;
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
		select();
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
					event.getIRCModeParser().getLine());
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
			appendLine("You ("+ event.getInvitedUser() +") got invited "+ 
					"by "+ event.getInvitingUser() +" to "+ event.getChannel());
		}

		public void invitationDeliveryReceived(InvitationEvent event) {
			appendLine("You ("+ event.getInvitingUser() +") invited "+ 
					event.getInvitedUser() +" to "+ event.getChannel());
		}

		public void messageReceived(MessageEvent event) {
			Object tgt = ((event.getDestinationChannel() != null)
					? (Object)event.getDestinationChannel()
					: (Object)event.getDestinationUser());
			appendLine("Received message to "+ tgt +": "+ event.getMessage());
		}

		@SuppressWarnings("unchecked")
		public void motdReceived(MotdEvent event) {
			appendLine("MOTD received:");
			for (String line : (Collection<String>)event.getText()) {
				appendText(line);
				newLine();
			}
		}

		@SuppressWarnings("unchecked")
		public void namesReceived(NamesEvent event) {
			appendLine("Names of "+ event.getChannel() +" received:");
			for (ChannelUser channelUser : (Collection<ChannelUser>)event.getChannelUsers()) {
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
					if (event.getChannelStatus(i) == Channel.OPERATOR) {
						appendText(" (operator)");
					}
					if (event.getChannelStatus(i) == Channel.VOICED) {
						appendText(" (operator)");
					}
					if (i+1 < event.getChannelCount()) {
						appendText(",");
					}
				}
				newLine();
			}
		}

		@SuppressWarnings("unchecked")
		public void infoReceived(InfoEvent event) {
			appendLine("Info received:");
			for (String line : (Collection<String>)event.getText()) {
				appendText(line);
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
		
		public void statsReceived(StatsEvent event) {
			appendLine("Stats received:");
			Map<String, Map<String, String>> map = new TreeMap<String, Map<String, String>>();
			
			Map<String, String> clineMap = new TreeMap<String, String>();
			clineMap.put("Host", event.getCLineHost());
			clineMap.put("Class", event.getCLineClass());
			clineMap.put("Name", event.getCLineName());
			map.put("C Line", clineMap);
			
			Map<String, String> hlineMap = new TreeMap<String, String>();
			hlineMap.put("Hostmask", event.getHLineHostMask());
			hlineMap.put("Servername", event.getHLineServerName());
			map.put("H Line", hlineMap);
			
			Map<String, String> klineMap = new TreeMap<String, String>();
			klineMap.put("Host", event.getKLineHost());
			klineMap.put("Class", event.getKLineClass());
			klineMap.put("Host", event.getKLineUsername() +":"+ event.getKLinePort());
			map.put("K Line", klineMap);
			
			Map<String, String> olineMap = new TreeMap<String, String>();
			olineMap.put("Hostmask", event.getOLineHostMask());
			olineMap.put("Name", event.getOLineName());
			map.put("O Line", olineMap);
			
			Map<String, String> ilineMap = new TreeMap<String, String>();
			ilineMap.put("Class", event.getILineClass());
			ilineMap.put("Host", event.getILineHost());
			ilineMap.put("Host 2", event.getILineHost2() +":"+ event.getILinePort());
			map.put("I Line", ilineMap);
			
			Map<String, String> ylineMap = new TreeMap<String, String>();
			ylineMap.put("Class", event.getYLineClass());
			ylineMap.put("Connection Frequency", String.valueOf(event.getYLineConnectionFrequency()));
			ylineMap.put("Max Queued Send-Data", String.valueOf(event.getYLineMaxQueuedSendData()));
			ylineMap.put("Ping Frequency", String.valueOf(event.getYLinePingFrequency()));
			map.put("Y Line", ylineMap);
			
			Map<String, String> nlineMap = new TreeMap<String, String>();
			nlineMap.put("Class", event.getNLineClass());
			nlineMap.put("Host", event.getNLineHost() +":"+ event.getNLinePort());
			nlineMap.put("Name", event.getNLineName());
			map.put("N Line", nlineMap);
			
			Map<String, String> llineMap = new TreeMap<String, String>();
			llineMap.put("Hostmask", event.getLLineHostmask());
			llineMap.put("Servername", event.getLLineServername());
			llineMap.put("Max Depth", String.valueOf(event.getLLineMaxDepth()));
			map.put("L Line", llineMap);
			
			appendLine("Linkname: "+ event.getLinkName());
			newLine();
			appendLine("Sent Data: "+ event.getSentBytes() +" bytes");
			newLine();
			appendLine("Sent Messages: "+ event.getSentMessages());
			newLine();
			appendLine("Queued Send-Data: "+ event.getQueuedSendData());
			newLine();
			appendLine("Received Data: "+ event.getReceivedBytes() +" bytes");
			newLine();
			appendLine("Received Messages: "+ event.getReceivedMessages());
			if (event.getOpenTime() != null) {
				appendLine("Open Time: "+ event.getOpenTime());
				
			}
			for (String key : map.keySet()) {
				appendLine(key +": ");
				for (Map.Entry<String, String> e : map.get(key).entrySet()) {
					System.out.println("\t"+ e.getKey() +": "+ e.getValue());
					newLine();
				}
			}
		}

		@SuppressWarnings("unchecked")
		public void listReceived(ListEvent event) {
			appendLine("List received:");
			long i = 1;
			Iterator<Topic> it1 = ((Collection<Topic>)event.getTopics()).iterator();
			Iterator<Integer> it2 = ((Collection<Integer>)event.getVisibleCounts()).iterator();
			while (it1.hasNext() && it2.hasNext()) {
				Topic topic = it1.next();
				int visibleCount = it2.next().intValue();
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
				appendText("connected to "+ server 
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
