package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Font;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Message;
import org.schwering.irc.manager.event.ChannelAdapter;

public class ChannelWindow extends SimpleWindow {
	private Channel channel;
	
	public ChannelWindow(ConnectionController controller, Channel channel) {
		super(controller);
		this.channel = channel;
		setTitle(channel.toString());
		channel.addChannelListener(new ChannelListener());
		addToTabBar();
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
	
	private class ChannelListener extends ChannelAdapter {
		
	}
}
