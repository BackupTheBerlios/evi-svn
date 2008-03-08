package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Font;

import org.schwering.irc.manager.User;
import org.schwering.irc.manager.event.ConnectionAdapter;
import org.schwering.irc.manager.event.MessageEvent;
import org.schwering.irc.manager.event.NickEvent;
import org.schwering.irc.manager.event.PrivateMessageAdapter;
import org.schwering.irc.manager.event.UserParticipationEvent;

public class QueryWindow extends SimpleWindow {
	private User user;
	
	public QueryWindow(ConnectionController controller, User user) {
		super(controller);
		this.user = user;
		setTitle(user.toString());
		controller.getConnection().addConnectionListener(new ConnectionListener());
		controller.getConnection().addPrivateMessageListener(new PrivateMessageListener());
	}

	public void updateLayout() {
		Font font = controller.getProfile().getQueryFont();
		Color fg = controller.getProfile().getNeutralColor();
		Color bg = Color.white;
		updateLayout(font, fg, bg);
	}
	
	private class ConnectionListener extends ConnectionAdapter {
		public void nickChanged(NickEvent event) {
		}

		public void userLeft(UserParticipationEvent event) {
		}
	}
	
	private class PrivateMessageListener extends PrivateMessageAdapter {
		public void messageReceived(MessageEvent event) {
		}

		public void noticeReceived(MessageEvent event) {
		}
	}
}
