package org.schwering.evi.test;

import org.schwering.evi.core.IButtonable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;

public class TestInfo implements IModuleInfo, IButtonable {

	public String getInfoURL() {
		return "about.html";
	}

	public Class getModuleClass() {
		return Test.class;
	}

	public String getName() {
		return "Test-Module";
	}

	public float getVersion() {
		return 0;
	}

	public IModule newInstance() {
		return new Test();
	}

	public boolean isButtonable() {
		return true;
	}
	
}
