/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.console;

import org.schwering.evi.core.IConfigurable;
import org.schwering.evi.core.IMenuable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;
import org.schwering.evi.core.IPanel;

/**
 * The IModuleInfo class for the console module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ConsoleInfo implements IModuleInfo, IMenuable, IConfigurable {

	public String getInfoURL() {
		return Messages.getString("ConsoleInfo.INFO_HTML"); //$NON-NLS-1$
	}

	public Class getModuleClass() {
		return Console.class;
	}

	public String getName() {
		return Messages.getString("ConsoleInfo.CONSOLE_NAME"); //$NON-NLS-1$
	}

	public float getVersion() {
		return 1.0f;
	}

	public IModule newInstance() {
		return Console.getInstance();
	}

	public boolean isMenuable() {
		return true;
	}

	public IPanel getConfigPanel() {
		return ConsoleConfiguration.getInstance();
	}
}
