/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.schwering.evi.audio.AudioPlayer;
import org.schwering.evi.audio.conf.Configuration;
import org.schwering.evi.audio.lang.Messages;

/**
 * A very simple panel with control buttons.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 8290543586842163071L;
	
	public static final int PREV = 1;
	public static final int PLAY = 2;
	public static final int NEXT = 4;
	public static final int PLAYALL = 8;
	public static final int RANDOM = 16;
	
	private JButton prev;
	private JButton play;
	private JButton next;
	private JToggleButton playAll;
	private JToggleButton random;
	
	/**
	 * Creates a new control panel that displays all buttons.
	 * @param owner The owning AudioPlayer object which is controlled by this 
	 * panel.
	 */
	public ControlPanel(final AudioPlayer owner) {
		this(owner, PREV | PLAY | NEXT | PLAYALL | RANDOM);
	}
	
	/**
	 * Creates a new control panel.
	 * @param owner The owning AudioPlayer object which is controlled by this 
	 * panel.
	 * @param buttons A binary OR sum of buttons (PREV, PLAY, NEXT, RANDOM) 
	 * that should be displayed. For example <code>PREV | PLAY | NEXT</code>.
	 */
	public ControlPanel(final AudioPlayer owner, int buttons) {
		super();
		
		int buttonCount = 0;
		if ((buttons & PREV) != 0) buttonCount++;
		if ((buttons & PLAY) != 0) buttonCount++;
		if ((buttons & NEXT) != 0) buttonCount++;
		if ((buttons & PLAYALL) != 0) buttonCount++;
		if ((buttons & RANDOM) != 0) buttonCount++;
		
		setLayout(new GridLayout(0, buttonCount));

		if ((buttons & PREV) != 0) {
			prev = new JButton(String.valueOf((char)171)); //$NON-NLS-1$
			prev.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
					mainPanel.getPlaylist().previous();
				}
			});
			add(prev);
		}
		
		if ((buttons & PLAY) != 0) {
			play = new JButton(">"); //$NON-NLS-1$
			play.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
					if (mainPanel.getPlaylist().isPlaying()) {
						mainPanel.getPlaylist().stop();
					} else {
						mainPanel.getPlaylist().play();
					}
				}
			});
			add(play);
		}
			
		if ((buttons & NEXT) != 0) {
			next = new JButton(String.valueOf((char)187)); //$NON-NLS-1$
			next.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
					mainPanel.getPlaylist().next();
				}
			});
			add(next);
		}
		
		if ((buttons & PLAYALL) != 0) {
			playAll = new JToggleButton(Messages.getString("ControlPanel.PLAYALL"), //$NON-NLS-1$
					Configuration.isPlayAll());
			playAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
					mainPanel.getPlaylist().setPlayAll(playAll.isSelected());
					Configuration.setPlayAll(playAll.isSelected());
				}
			});
			add(playAll);
		}
		
		if ((buttons & RANDOM) != 0) {
			random = new JToggleButton(Messages.getString("ControlPanel.RANDOM"),  //$NON-NLS-1$
					Configuration.isRandom());
			random.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
					mainPanel.getPlaylist().setRandom(random.isSelected());
					Configuration.setRandom(random.isSelected());
				}
			});
			add(random);
		}
	}
	
	/**
	 * Sets a tooltip.
	 */
	public void setToolTipText(String s) {
		super.setToolTipText(s);
		AbstractButton[] arr = new AbstractButton[] { prev, play, next, playAll, random };
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null) {
				arr[i].setToolTipText(s);
			}
		}
	}
	
	/**
	 * Enables or disables the borders of the buttons.
	 * @param b If <code>true</code>, the buttons have borders; 
	 * if <code>false</code> the buttons don't.
	 */
	public void setBorderPainted(boolean b) {
		AbstractButton[] arr = new AbstractButton[] { prev, play, next, playAll, random };
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null) {
				arr[i].setBorderPainted(b);
			}
		}
	}
	
	/**
	 * Sets the backgrounds.
	 * @param color The color.
	 */
	public void setBackground(Color color) {
		super.setBackground(color);
		AbstractButton[] arr = new AbstractButton[] { prev, play, next, playAll, random };
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null) {
				arr[i].setBackground(color);
			}
		}
	}
	
	/**
	 * Enables or disables focus.
	 * @param b If <code>false</code>, the buttons will get no focus.
	 */
	public void setFocusable(boolean b) {
		super.setFocusable(false);
		AbstractButton[] arr = new AbstractButton[] { prev, play, next, playAll, random };
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null) {
				arr[i].setFocusable(b);
			}
		}
	}
}
