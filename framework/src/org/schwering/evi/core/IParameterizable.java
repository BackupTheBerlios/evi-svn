package org.schwering.evi.core;

public interface IParameterizable extends IModuleInfo {
	public IModule newInstance(Object[] args);
}
