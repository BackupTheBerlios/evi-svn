package org.schwering.evi.core;

import java.net.URL;

public interface IURLHandler extends IModuleInfo {
	public IModule newInstance(URL url);
	public String[] getProtocols();
}
