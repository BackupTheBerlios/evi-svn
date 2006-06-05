package org.schwering.evi.test;

import java.net.URI;
import java.net.URL;

import org.schwering.evi.core.IButtonable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;
import org.schwering.evi.core.IParameterizable;
import org.schwering.evi.core.IURIHandler;

public class TestInfo implements IModuleInfo, IButtonable, IParameterizable, 
IURIHandler {

	public IModule newInstance() {
		return new Test();
	}
	
	public IModule newInstance(Object[] args) {
		return new Test((String)args[0]);
	}

	public IModule newInstance(URI uri) {
		try {
			URL url = uri.toURL();
			return new Test(url);
		} catch (Exception exc) {
			return null;
		}
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

	public String[] getProtocols() {
		return new String[] { "http", "https", "test" };
	}
}
