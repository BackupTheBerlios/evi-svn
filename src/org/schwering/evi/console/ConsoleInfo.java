package org.schwering.evi.console;

import org.schwering.evi.core.IMenuable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;

public class ConsoleInfo implements IModuleInfo, IMenuable {

	public String getInfoURL() {
		return null;
	}

	public Class getModuleClass() {
		return Console.class;
	}

	public String getName() {
		return "Console";
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
