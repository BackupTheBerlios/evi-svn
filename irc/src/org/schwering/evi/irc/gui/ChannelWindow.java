package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.ChannelUser;
import org.schwering.irc.manager.Topic;
import org.schwering.irc.manager.User;
import org.schwering.irc.manager.event.BanlistEvent;
import org.schwering.irc.manager.event.ChannelModeEvent;
import org.schwering.irc.manager.event.CtcpActionEvent;
import org.schwering.irc.manager.event.CtcpAdapter;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.NamesEvent;
import org.schwering.irc.manager.event.NickEvent;
import org.schwering.irc.manager.event.TopicEvent;
import org.schwering.irc.manager.event.UserParticipationEvent;
import org.schwering.irc.manager.event.UserStatusEvent;
import org.schwering.irc.manager.event.WhoEvent;

public class ChannelWindow extends SimpleWindow {
	private static final long serialVersionUID = -1641623088498545249L;
	private Channel channel;
	private NickListModel listModel;
	
	@SuppressWarnings("unchecked")
	public ChannelWindow(ConnectionController controller, Channel channel) {
		super(controller);
		this.channel = channel;
		listModel.sync((Collection<ChannelUser>)channel.getChannelUsers());
		channel.addChannelListener(new ChannelListener());
		channel.addCtcpListener(new CtcpListener());
		input.setCompleter(new NickCompleter());
		setTitle(channel.toString());
		addToTabBar();
		select();
	}
	
	protected Component createCenterComponent() {
		Component textArea = super.createCenterComponent();
		listModel = new NickListModel();
		JList nickList = new JList(listModel);
		nickList.setCellRenderer(new NickRenderer());
		JScrollPane nickListScrollPane = new JScrollPane(nickList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				textArea, nickListScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(0.8);
		splitPane.setResizeWeight(0.8);
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
		if (str.charAt(0) == '/') {
			str = str.substring(1);
			appendLine("Sending to server: "+ str);
			controller.getConnection().send(str);
		} else {
			String line = "PRIVMSG "+ channel.getName() +" :"+ str;
			String nick = controller.getConnection().getNick();
			controller.getConnection().send(line);
			appendText("<"+ nick +"> ", controller.getProfile().getOwnColor());
			appendMessage(str, controller.getProfile().getOwnColor());
			newLine();
		}
	}
	
	/**
	 * The model for the nick list. 
	 */
	private class NickListModel implements ListModel {
		private List<ListDataListener> listeners = new LinkedList<ListDataListener>();
		private List<User> list = new Vector<User>();
		
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
		
		public void remove(User user) {
			int removes = 0;
			for (int i = list.indexOf(user); i != -1; i = list.indexOf(user)) {
				list.remove(i);
				fireContentsRemoved(i);
				removes++;
			}
			if (removes != 1) {
				throw new RuntimeException("removed user "+ user +" "+ removes +" times from nicklist");
			}
		}
		
		public void sync(Collection<? extends User> userList) {
			System.out.println("sync() with "+ userList.size());
			for (User user : userList) {
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
			for (ListDataListener listener : listeners) {
				listener.intervalAdded(event);
			}
		}
		
		private void fireContentsRemoved(int i) {
			fireContentsRemoved(i, i);
		}
		
		private synchronized void fireContentsRemoved(int i, int j) {
			ListDataEvent event = new ListDataEvent(this, 
					ListDataEvent.INTERVAL_REMOVED, i, j);
			for (ListDataListener listener : listeners) {
				listener.intervalRemoved(event);
			}
		}
	}
	
	private class NickRenderer implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			User user = (User)value;
			int status = channel.getUserStatus(user);
			String prefix = "";
			if ((status & Channel.OPERATOR) != 0) {
				prefix = "@";
			} else if ((status & Channel.VOICED) != 0) {
				prefix = "+";
			}
			JLabel lbl = new JLabel();
			lbl.setText(prefix + user.getNick());
			if (user.isAway()) {
				lbl.setForeground(Color.DARK_GRAY);
			}
			String toolTip = "Username: "+ user.getUsername()
							+"\nHost: "+ user.getHost();
			if (user.isAway()) {
				toolTip += "\n"+ user.getNick() +" is away";
			}
			lbl.setToolTipText(toolTip);
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
			if (event.getUser() == null) {
				appendLine("Channel mode is "+
						event.getIRCModeParser().getLine());
			} else {
				appendLine(event.getUser() +" changed channel mode to "+
						event.getIRCModeParser().getLine());
			}
		}

		public void userStatusChanged(UserStatusEvent event) {
			listModel.reinsert(event.getUser());
		}

		@SuppressWarnings("unchecked")
		public void namesReceived(NamesEvent event) {
			if (event.hasNewUsers()) {
				listModel.sync((Collection<ChannelUser>)event.getChannelUsers());
			}
		}
		
		@SuppressWarnings("unchecked")
		public void whoReceived(WhoEvent event) {
			if (event.hasNewUsers()) {
				listModel.sync((Collection<ChannelUser>)event.getChannelUsers());
			}
		}

		public void nickChanged(NickEvent event) {
			appendLine(event.getOldNick() +" is now known as "+ 
					event.getUser().getNick());
			listModel.reinsert(event.getUser());
		}

		public void userJoined(UserParticipationEvent event) {
			appendLine(event.getUser() +" joined");
			listModel.insert(event.getUser());
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
			listModel.remove(event.getUser());
		}
	}
	
	private class CtcpListener extends CtcpAdapter {
		@Override
		public void actionReceived(CtcpActionEvent event) {
			appendLine(event.getDestinationUser() +" ", event.getArguments(),
					controller.getProfile().getOtherColor());
		}
	}
	
	private class NickCompleter implements Completer {
		@SuppressWarnings("unchecked")
		public String complete(String str) {
			str = str.toLowerCase();
			List<String> found = new LinkedList<String>();
			for (User other : (Collection<User>)channel.getChannelUsers()) {
				if (other.getNick().toLowerCase().startsWith(str)) {
					found.add(other.getNick());
				}
			}
			String commonPrefix = null;
			for (String nick : found) {
				if (commonPrefix == null) {
					commonPrefix = nick;
				} else {
					int i = getCommonPrefixLength(commonPrefix, nick);
					commonPrefix = commonPrefix.substring(0, i);
				}
			}
			return commonPrefix;
		}
		
		private int getCommonPrefixLength(String s, String t) {
			int len = (s.length() < t.length()) ? s.length() : t.length();
			for (int i = 0; i < len; i++) {
				if (Character.toLowerCase(s.charAt(i))
						!= Character.toLowerCase(t.charAt(i))) {
					return i;
				}
			}
			return len;
		}
	}
}
