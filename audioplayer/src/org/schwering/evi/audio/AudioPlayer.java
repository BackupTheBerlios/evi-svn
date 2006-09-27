package org.schwering.evi.audio;

import java.awt.Component;

import javax.swing.Icon;

import org.schwering.evi.core.IApplet;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IPanel;

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
		ctrlPanel = new ControlPanel(this, 
				ControlPanel.PREV | ControlPanel.PLAY | ControlPanel.NEXT);
		ctrlPanel.setBordersPainted(false);
		
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
