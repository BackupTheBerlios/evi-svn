package org.schwering.evi.audio;

import org.schwering.evi.core.IButtonable;
import org.schwering.evi.core.IMenuable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;

/**
 * Audio player module based on JLayer MP3 library.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class AudioPlayerInfo implements IModuleInfo, IButtonable, IMenuable {

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#getInfoURL()
	 */
	public String getInfoURL() {
		return Messages.getString("AudioPlayerInfo.INFO_HTML"); //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#getModuleClass()
	 */
	public Class getModuleClass() {
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
		return 1.0f;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#newInstance()
	 */
	public IModule newInstance() {
		return new AudioPlayer();
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
}
