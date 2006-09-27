/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc;

import java.net.URI;

import org.schwering.evi.core.IButtonable;
import org.schwering.evi.core.IConfigurable;
import org.schwering.evi.core.IMenuable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.IParameterizable;
import org.schwering.evi.core.IURIHandler;
import org.schwering.evi.irc.gui.conf.Configuration;

/**
 * The IModuleInfo class of the IRC module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class IRCInfo implements IModuleInfo, IButtonable, IConfigurable, IMenuable, 
IParameterizable, IURIHandler {

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#newInstance()
	 */
	public IModule newInstance() {
		return new IRC();
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IParameterizable#newInstance(java.lang.Object[])
	 */
	public IModule newInstance(Object[] args) {
		return new IRC(args);
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IURIHandler#newInstance(java.net.URI)
	 */
	public IModule newInstance(URI uri) {
		return new IRC(uri);
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IButtonable#isButtonable()
	 */
	public boolean isButtonable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IURIHandler#getProtocols()
	 */
	public String[] getProtocols() {
		return new String[] { "irc" };
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IMenuable#isMenuable()
	 */
	public boolean isMenuable() {
		return true;
	}

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
		return IRC.class;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#getName()
	 */
	public String getName() {
		return "IRC";
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleInfo#getVersion()
	 */
	public float getVersion() {
		return 0.1f;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IConfigurable#getConfigPanel()
	 */
	public IPanel getConfigPanel() {
		return Configuration.getInstance();
	}
}
