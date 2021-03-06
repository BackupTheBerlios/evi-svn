/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;

import org.schwering.evi.audio.conf.Configuration;
import org.schwering.evi.audio.gui.ControlPanel;
import org.schwering.evi.audio.gui.MainPanel;
import org.schwering.evi.audio.lang.Messages;
import org.schwering.evi.core.IApplet;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.gui.main.MainFrame;
import org.schwering.evi.gui.main.ToolBar;

/**
 * Audio player module based on JLayer MP3 library.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class AudioPlayer implements IModule, IPanel, IApplet {
	private MainPanel mainPanel;
	private ControlPanel ctrlPanel;
	
	public AudioPlayer() {
		this(null);
	}
	
	public AudioPlayer(Object[] args) {
		mainPanel = new MainPanel(this);
		if (Configuration.isApplet()) {
			ctrlPanel = new ControlPanel(this, 
					ControlPanel.PREV | ControlPanel.PLAY | ControlPanel.NEXT);
			ctrlPanel.setBorderPainted(false);
			MainFrame mainFrame = MainFrame.getInstance();
			ToolBar toolBar = mainFrame.getMainToolBar();
			Color bg = toolBar.getBackground();
			bg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), bg.getAlpha());
			ctrlPanel.setBackground(bg);
			ctrlPanel.setFocusable(false);
		} else {
			ctrlPanel = null;
		}
		
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("play")) {
					mainPanel.getPlaylist().play();
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IApplet#dispose()
	 */
	public void dispose() {
		mainPanel.dispose();
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getIcon()
	 */
	public Icon getIcon() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getPanelInstance()
	 */
	public Component getPanelInstance() {
		return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return Messages.getString("AudioPlayer.MODULE_TITLE"); //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IApplet#getAppletInstance()
	 */
	public Component getAppletInstance() {
		return ctrlPanel;
	}
}
