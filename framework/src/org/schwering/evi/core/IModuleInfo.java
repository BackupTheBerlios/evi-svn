package org.schwering.evi.core;

public interface IModuleInfo {
	public IModule newInstance();
	public Class getModuleClass();
	public String getName();
	public float getVersion();
	public String getInfoURL();
}
