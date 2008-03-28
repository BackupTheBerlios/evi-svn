/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.schwering.evi.util.Util;
import org.schwering.irc.manager.User;

public class ChatWindow extends SimpleWindow implements Runnable {
	private static final long serialVersionUID = -5009381070021052829L;

	private Socket sock;
	private BufferedReader in;
	private PrintWriter out;
	
	public ChatWindow(ConnectionController controller, User user, Socket sock) 
	throws IOException {
		super(controller);
		this.sock = sock;
		
		this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		this.out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
		setTitle("DCC "+ user.getNick());
		addToTabBar();
		select();
		new Thread(this).start();
	}

	public void updateLayout() {
		Font font = controller.getProfile().getQueryFont();
		Color fg = controller.getProfile().getNeutralColor();
		Color bg = Color.white;
		updateLayout(font, fg, bg);
	}
	
	public Object getObject() {
		return sock;
	}
	
	public void inputSubmitted(String str) {
		appendText("<me> ", controller.getProfile().getOwnColor());
		appendMessage(str, controller.getProfile().getOwnColor());
		newLine();
		try {
			out.println(str);
			out.flush();
		} catch (Exception exc) {
			exc.printStackTrace();
			String excStr = Util.exceptionToString(exc);
			appendText("Exception: "+ excStr, 
					controller.getProfile().getNeutralColor());
		}
	}

	public void run() {
		try {
			String str;
			while ((str = in.readLine()) != null) {
				appendText("<jolie> ", controller.getProfile().getOwnColor());
				appendMessage(str, controller.getProfile().getOwnColor());
				newLine();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			String excStr = Util.exceptionToString(exc);
			appendText("Exception: "+ excStr, 
					controller.getProfile().getNeutralColor());
		}
	}
}
