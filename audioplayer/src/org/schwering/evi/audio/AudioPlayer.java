package org.schwering.evi.audio;

import java.awt.Component;

import javax.swing.Icon;

import org.schwering.evi.core.IApplet;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IPanel;

/**
 * Audio player module based on JLayer MP3 library.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class AudioPlayer implements IModule, IPanel, IApplet {
	private MainPanel mainPanel = new MainPanel(this);
	private ControlPanel ctrlPanel = new ControlPanel(this);
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IApplet#dispose()
	 */
	public void dispose() {
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
		return "AudioPlayer";
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IApplet#getAppletInstance()
	 */
	public Component getAppletInstance() {
		return null;//ctrlPanel;
	}
}
