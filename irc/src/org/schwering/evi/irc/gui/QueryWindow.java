package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Font;

import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.User;
import org.schwering.irc.manager.event.ConnectionAdapter;
import org.schwering.irc.manager.event.CtcpActionEvent;
import org.schwering.irc.manager.event.CtcpAdapter;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.NickEvent;
import org.schwering.irc.manager.event.PrivateMessageAdapter;
import org.schwering.irc.manager.event.UserParticipationEvent;

public class QueryWindow extends SimpleWindow {
	private static final long serialVersionUID = 1735723808145035225L;
	private final User user;
	
	public QueryWindow(ConnectionController controller, User user) {
		super(controller);
		this.user = user;
		setTitle(user.toString());
		controller.getConnection().addConnectionListener(new ConnectionListener());
		controller.getConnection().addPrivateMessageListener(new PrivateMessageListener());
		controller.getConnection().addCtcpListener(new CtcpListener());
		addToTabBar();
		select();
	}

	public void updateLayout() {
		Font font = controller.getProfile().getQueryFont();
		Color fg = controller.getProfile().getNeutralColor();
		Color bg = Color.white;
		updateLayout(font, fg, bg);
	}
	
	public Object getObject() {
		return user;
	}
	
	public void inputSubmitted(String str) {
		if (str.length() == 0) {
			return;
		}
		if (str.charAt(0) == '/') {
			str = str.substring(1);
			if (str.startsWith("me") || str.startsWith("ME")) {
				int index = str.indexOf(' ');
				if (index != -1) {
					str = str.substring(index + 1);
					if (str.length() > 0) {
						String nick = controller.getConnection().getNick();
						controller.getConnection().sendCtcpCommand(user.getNick(), "ACTION", str);
						appendText(nick +" is ", controller.getProfile().getOwnColor());
						appendMessage(str, controller.getProfile().getOwnColor());
					}
				}
			} else {
				appendLine("Sending to server: "+ str);
				controller.getConnection().send(str);
			}
		} else {
			String nick = controller.getConnection().getNick();
			controller.getConnection().sendPrivmsg(user.getNick(), str);
			appendText("<"+ nick +"> ", controller.getProfile().getOwnColor());
			appendMessage(str, controller.getProfile().getOwnColor());
			newLine();
		}
	}
	private class ConnectionListener extends ConnectionAdapter {
		public void nickChanged(NickEvent event) {
			if (user.isSame(event.getUser())) {
				setTitle(user.toString());
				appendLine(event.getOldNick() +" changed nickname to "+ user); 
			}
		}
		
		public void userLeft(UserParticipationEvent event) {
			if (user.isSame(event.getUser()) && event.isQuit()) {
				Message msg = event.getMessage();
				if (msg != null && !msg.isEmpty()) {
					appendLine(user +" quit (", msg, ")");
				} else {
					appendLine(user +" quit");
				}
			}
		}
	}
	
	private class PrivateMessageListener extends PrivateMessageAdapter {
		public void messageReceived(MessageEvent event) {
			if (user.isSame(event.getDestinationUser())) {
				appendLine("<"+ user +"> ", event.getMessage(),
						controller.getProfile().getOtherColor());
			}
		}

		public void noticeReceived(MessageEvent event) {
			if (user.isSame(event.getDestinationUser())) {
				appendLine("("+ user +") ", event.getMessage(), 
						controller.getProfile().getOtherColor());
			}
		}
	}
	
	private class CtcpListener extends CtcpAdapter {
		@Override
		public void actionReceived(CtcpActionEvent event) {
			if (event.getDestinationUser() != null
					&& event.getDestinationUser().isSame(user)) {
				appendLine(event.getSender().getNick() +" is ",
						event.getMessage(), 
						controller.getProfile().getOtherColor());
			}
		}
	}
}
