package org.schwering.evi.console;

import org.schwering.evi.core.IMenuable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;

/**
 * The IModuleInfo class for the console module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ConsoleInfo implements IModuleInfo, IMenuable {

	public String getInfoURL() {
		return null;
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
		return new Console();
	}

	public boolean isMenuable() {
		return true;
	}

}
