/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio;

import org.schwering.evi.audio.conf.ConfigurationPanel;
import org.schwering.evi.audio.lang.Messages;
import org.schwering.evi.core.IButtonable;
import org.schwering.evi.core.IConfigurable;
import org.schwering.evi.core.IMenuable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.IParameterizable;

/**
 * Audio player module based on JLayer MP3 library.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class AudioPlayerInfo implements IModuleInfo, IButtonable, IMenuable, 
IParameterizable, IConfigurable {

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#getInfoURL()
	 */
	public String getInfoURL() {
		return Messages.getString("AudioPlayerInfo.INFO_HTML"); //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#getModuleClass()
	 */
	public Class<AudioPlayer> getModuleClass() {
		return AudioPlayer.class;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#getName()
	 */
	public String getName() {
		return Messages.getString("AudioPlayerInfo.MODULE_NAME"); //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#getVersion()
	 */
	public float getVersion() {
		return 1.2f;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#newInstance()
	 */
	public IModule newInstance() {
		return new AudioPlayer();
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IParameterizable#newInstance(java.lang.Object[])
	 */
	public IModule newInstance(Object[] args) {
		return new AudioPlayer(args);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IButtonable#isButtonable()
	 */
	public boolean isButtonable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IMenuable#isMenuable()
	 */
	public boolean isMenuable() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IConfigurable#getConfigPanel()
	 */
	public IPanel getConfigPanel() {
		return new ConfigurationPanel();
	}
}
