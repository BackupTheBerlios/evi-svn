package org.schwering.evi.audio;

import org.schwering.evi.core.IButtonable;
import org.schwering.evi.core.IMenuable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;

/**
 * Audio player module based on JLayer MP3 library.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class AudioPlayerInfo implements IModuleInfo, IButtonable, IMenuable {

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#getInfoURL()
	 */
	public String getInfoURL() {
		return "info.html";
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
		return "AudioPlayer";
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#getVersion()
	 */
	public float getVersion() {
		return 0.1f;
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
