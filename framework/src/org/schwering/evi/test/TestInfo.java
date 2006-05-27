package org.schwering.evi.test;

import org.schwering.evi.core.IButtonable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;
import org.schwering.evi.core.IParameterizable;

public class TestInfo implements IModuleInfo, IButtonable, IParameterizable {

	public IModule newInstance() {
		return new Test();
	}
	
	public IModule newInstance(Object[] args) {
		return new Test((String)args[0]);
	}

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

	public boolean isButtonable() {
		return true;
	}
	
}
