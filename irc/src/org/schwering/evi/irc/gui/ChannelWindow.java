package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.Topic;
import org.schwering.irc.manager.User;
import org.schwering.irc.manager.event.BanlistEvent;
import org.schwering.irc.manager.event.ChannelModeEvent;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.NamesEvent;
import org.schwering.irc.manager.event.NickEvent;
import org.schwering.irc.manager.event.TopicEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;
import org.schwering.irc.manager.event.UserStatusEvent;
import org.schwering.irc.manager.event.WhoEvent;

public class ChannelWindow extends SimpleWindow {
	private Channel channel;
	private NickListModel listModel;
	private JSplitPane splitPane;
	
	public ChannelWindow(ConnectionController controller, Channel channel) {
		super(controller);
		this.channel = channel;
		setTitle(channel.toString());
		listModel.sync(channel.getChannelUsers());
		channel.addChannelListener(new ChannelListener());
		addToTabBar();
		select();
		splitPane.setDividerLocation(0.8);
	}
	
	protected Component createCenterComponent() {
		Component textArea = super.createCenterComponent();
		listModel = new NickListModel();
		JList nickList = new JList(listModel);
		nickList.setCellRenderer(new NickRenderer());
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				textArea, nickList);
		splitPane.setDividerLocation(0.8);
		return splitPane;
	}

	protected Component createNorthComponent() {
		return super.createNorthComponent();
	}

	public void updateLayout() {
		Font font = controller.getProfile().getChannelFont();
		Color fg = controller.getProfile().getNeutralColor();
		Color bg = Color.white;
		updateLayout(font, fg, bg);
	}
	
	public Object getObject() {
		return channel;
	}
	
	public void inputSubmitted(String str) {
		str = str.trim();
		if (str.charAt(0) == '/') {
			str = str.substring(1);
			appendLine("Sending to server: "+ str);
			controller.getConnection().send(str);
		} else {
			String line = "PRIVMSG "+ channel.getName() +" :"+ str;
			String nick = controller.getConnection().getNick();
			controller.getConnection().send(line);
			appendLine("<"+ nick +"> ", new Message(str),
					controller.getProfile().getOwnColor());
		}
	}
	
	/**
	 * The model for the nick list. 
	 * TODO This uses an interator for random access. Change this to real
	 * random access.
	 */
	private class NickListModel implements ListModel {
		private List listeners = new LinkedList();
		private List list = new Vector();
		
		public int insert(User user) {
			if (list.contains(user)) { // debugging, can be outcommented
				throw new RuntimeException("user already in list");
			}
			
			int i = 0;
			while (i < list.size() && compare(user, (User)list.get(i)) > 0) {
				i++;
			}
			list.add(i, user);
			fireContentsAdded(i);
			System.out.println("inserted "+ user +" at #"+ i);
			return i;
		}
		
		public int insertIfNotYetContained(User user) {
			return (!list.contains(user)) ? insert(user) : -1;
		}
		
		public void reinsert(User user) {
			int i = list.indexOf(user);
			if (i == -1) {
				throw new RuntimeException("instance not found");
			}
			list.remove(i);
			fireContentsRemoved(i);
			i = insert(user);
			fireContentsAdded(i);
		}
		
		public void remove(int i) {
			list.remove(i);
		}
		
		public void sync(Collection userList) {
			System.out.println("sync() with "+ userList.size());
			for (Iterator it = userList.iterator(); it.hasNext(); ) {
				User user = (User)it.next();
				int i = insertIfNotYetContained(user);
				System.out.println("sync(): "+ user +" #"+ i);
			}
		}
		
		public Object getElementAt(int i) {
			return list.get(i);
		}

		public int getSize() {
			return (list != null) ? list.size() : 0;
		}
		
		private int compare(User u, User v) {
			int s = channel.getUserStatus(u);
			int t = channel.getUserStatus(v);
			if ((s & Channel.OPERATOR) != 0 && (t & Channel.OPERATOR) == 0){
				return -1;
			} else if ((s & Channel.OPERATOR) == 0 && (t & Channel.OPERATOR) != 0){
				return 1;
			} else if (s == Channel.VOICED && t == Channel.NONE){
				return -1;
			} else if (s == Channel.NONE && t == Channel.VOICED) {
				return 1;
			}
			return u.getNick().compareToIgnoreCase(v.getNick());
		}
		
		public void addListDataListener(ListDataListener l) {
			listeners.add(l);
		}

		public void removeListDataListener(ListDataListener l) {
			listeners.remove(l);
		}

		private void fireContentsAdded(int i) {
			fireContentsAdded(i, i);
		}
		
		private synchronized void fireContentsAdded(int i, int j) {
			ListDataEvent event = new ListDataEvent(this, 
					ListDataEvent.INTERVAL_ADDED, i, j);
			for (Iterator it = listeners.iterator(); it.hasNext(); ) {
				((ListDataListener)it.next()).intervalAdded(event);
			}
		}
		
		private void fireContentsRemoved(int i) {
			fireContentsRemoved(i, i);
		}
		
		private synchronized void fireContentsRemoved(int i, int j) {
			ListDataEvent event = new ListDataEvent(this, 
					ListDataEvent.INTERVAL_REMOVED, i, j);
			for (Iterator it = listeners.iterator(); it.hasNext(); ) {
				((ListDataListener)it.next()).intervalRemoved(event);
			}
		}
		
		private void fireContentsChanged(int i) {
			fireContentsChanged(i, i);
		}
		
		private synchronized void fireContentsChanged(int i, int j) {
			ListDataEvent event = new ListDataEvent(this, 
					ListDataEvent.CONTENTS_CHANGED, i, j);
			for (Iterator it = listeners.iterator(); it.hasNext(); ) {
				((ListDataListener)it.next()).contentsChanged(event);
			}
		}
	}
	
	private class NickRenderer implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			User user = (User)value;
			int status = channel.getUserStatus(user);
			String prefix = "";
			if (status == Channel.OPERATOR) {
				prefix = "@";
			} else if (status == Channel.VOICED) {
				prefix = "+";
			}
			JLabel lbl = new JLabel();
			lbl.setText(prefix + user.getNick());
			if (user.isAway()) {
				lbl.setForeground(Color.DARK_GRAY);
			}
			return lbl;
		}
	}
	
	private class ChannelListener implements org.schwering.irc.manager.event.ChannelListener {
		public void messageReceived(MessageEvent event) {
			appendLine("<"+ event.getUser() +"> ", event.getMessage(),
					controller.getProfile().getOtherColor());
		}

		public void noticeReceived(MessageEvent event) {
			appendLine("("+ event.getUser() +") ", event.getMessage(),
					controller.getProfile().getOtherColor());
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

		public void channelModeReceived(ChannelModeEvent event) {
			appendLine(event.getUser() +" changed channel mode to "+
					event.getIRCModeParser().getLine());
		}

		public void userStatusChanged(UserStatusEvent event) {
			listModel.reinsert(event.getUser());
		}

		public void namesReceived(NamesEvent event) {
			if (event.hasNewUsers()) {
				listModel.sync(event.getChannelUsers());
			}
		}
		
		public void whoReceived(WhoEvent event) {
			if (event.hasNewUsers()) {
				listModel.sync(event.getChannelUsers());
			}
		}

		public void nickChanged(NickEvent event) {
			appendLine(event.getOldNick() +" is now known as "+ 
					event.getUser().getNick());
			System.out.println("status(user) = status("+ event.getUser() +") = "+ channel.getUserStatus(event.getUser()));
			System.out.println("status(oldNick) = status("+ event.getOldNick() +") = "+ channel.getUserStatus(event.getOldNick()));
			listModel.reinsert(event.getUser());
		}

		public void userJoined(UserParticipationEvent event) {
			appendLine(event.getUser() +" joined");
		}

		public void userLeft(UserParticipationEvent event) {
			String method = "";
			String rest = "";
			if (event.isQuit()) {
				method = "quit";
			} else if (event.isPart()) {
				method = "left";
			} else if (event.isKick()) {
				method = "was kicked from";
				rest = " by "+ event.getKickingUser();
			}
			
			if (event.getMessage() != null) {
				appendLine(event.getUser() +" "+ method + rest +" (", 
						event.getMessage(), ")");
			} else {
				appendLine(event.getUser() +" "+ method + rest);
			}
		}
	}
}
