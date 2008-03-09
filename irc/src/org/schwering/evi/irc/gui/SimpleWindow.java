/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JScrollPane;

import org.schwering.evi.irc.conf.Profile;
import org.schwering.evi.util.RightClickMenu;
import org.schwering.evi.util.TextPane;
import org.schwering.irc.manager.Message;

/**
 * A simple window with chat field and input field.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public abstract class SimpleWindow extends AbstractWindow {
	protected InputField input;
	protected TextPane text;

	public SimpleWindow(ConnectionController controller) {
		super(controller);
		RightClickMenu.addRightClickMenu(text);
		requestFocus();
		input.requestFocus();
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#createNorthComponent()
	 */
	protected Component createNorthComponent() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#createCenterComponent()
	 */
	protected Component createCenterComponent() {
		text = new TextPane();
		text.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					input.requestFocus();
			}
		});
//		text.addMouseMotionListener(new MouseMotionAdapter() {
//			public void mouseMoved(MouseEvent e) {
//				System.out.println(e.getX() +" x "+ e.getY());
//			}
//		});
		JScrollPane sp = new JScrollPane(text);
		return sp;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#createSouthComponent()
	 */
	protected Component createSouthComponent() {
		input = new InputField();
		input.addListener(new IInputListener() {
			public void inputFired(String str) {
				inputSubmitted(str);
			}
		});
		return input;
	}
	
	/**
	 * Invoked when the user submits the input in the input line.
	 */
	public abstract void inputSubmitted(String str);
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.irc.gui.AbstractWindow#updateLayout()
	 */
	public abstract void updateLayout();
	
	/**
	 * Can be used by overridden <code>updateLayout</code> methods.
	 * Updates the font of the input field and of the text field.
	 * @param font The font.
	 * @param fg The foreground.
	 * @param bg The background.
	 */
	public void updateLayout(Font font, Color fg, Color bg) {
		input.modifyAttributes(font.getFamily(), font.getSize(), fg, bg, 
				font.isBold(), font.isItalic(), false);
		input.setFont(font);
		text.modifyAttributes(font.getFamily(), font.getSize(), fg, bg, 
				font.isBold(), font.isItalic(), false);
	}
	
	/**
	 * Forwards the focus to the input line.
	 */
	public void requestFocus() {
		super.requestFocus();
		if (input != null) {
			input.requestFocus();
		}
	}
	
	protected void appendDate() {
		appendDate(controller.getProfile().getNeutralColor());
	}
	
	private Date prevDate = null;
	
	protected void appendDate(Color color) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		Calendar prevCal = null;
		if (prevDate != null) {
			prevCal = Calendar.getInstance();
			prevCal.setTime(prevDate);
		}
		
		SimpleDateFormat sdf;
		if (prevDate == null || prevCal.get(Calendar.YEAR) < cal.get(Calendar.YEAR)) {
			sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ");
		} else if (prevCal.get(Calendar.DAY_OF_MONTH) < cal.get(Calendar.DAY_OF_MONTH)) {
			sdf = new SimpleDateFormat("[MM-dd HH:mm:ss] ");
		} else {
			sdf = new SimpleDateFormat("[HH:mm:ss] ");
		}
		String dateStr = sdf.format(date);
		ColorParser.appendPlain(text, dateStr, color);
		
		prevDate = date;
	}
	
	protected void appendText(String text) {
		appendText(text, controller.getProfile().getNeutralColor());
	}
	
	protected void appendText(String text, Color fg) {
		this.text.append(text, fg);
	}
	
	protected void appendMessage(Message msg) {
		appendMessage(msg.getText());
	}
	
	protected void appendMessage(String text) {
		appendMessage(text, controller.getProfile().getNeutralColor());
	}
	
	protected void appendMessage(Message msg, Color fg) {
		appendMessage(msg.getText(), fg);
	}
	
	protected void appendMessage(String text, Color fg) {
		Profile profile = controller.getProfile();
		if (profile.getEnableColors()) {
			ColorParser.appendColored(this.text, text, profile, fg);
		} else {
			ColorParser.appendPlain(this.text, text, fg);
		}
	}
	
	protected void newLine() {
		text.append("\n");
	}
	
	protected void appendLine(String text) {
		appendLine(text, controller.getProfile().getNeutralColor());
	}
	
	protected void appendLine(String text, Color fg) {
		appendDate(fg);
		appendText(text, fg);
		newLine();
	}
	
	protected void appendMessageLine(Message msg) {
		appendLine(msg, controller.getProfile().getNeutralColor());
	}
	
	protected void appendLine(Message msg, Color fg) {
		appendDate(fg);
		appendMessage(msg, fg);
		newLine();
	}
	
	protected void appendLine(String s1, Message m) {
		appendLine(s1, m, controller.getProfile().getNeutralColor());
	}
		
	protected void appendLine(String s1, Message m, Color fg) {
		appendDate(fg);
		appendText(s1, fg);
		appendMessage(m, fg);
		newLine();
	}
	
	protected void appendLine(String s1, Message m, String s2) {
		appendLine(s1, m, s2, controller.getProfile().getNeutralColor());
	}
		
	protected void appendLine(String s1, Message m, String s2, Color fg) {
		appendDate(fg);
		appendText(s1, fg);
		appendMessage(m, fg);
		appendText(s2);
		newLine();
	}
}
