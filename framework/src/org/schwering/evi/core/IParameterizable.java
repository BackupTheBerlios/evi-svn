package org.schwering.evi.core;

/**
 * Module-Info-Class interface for parameterizable modules.<br>
 * A module is parameterizable if arguments can be passed to it when 
 * being instantiated.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IParameterizable extends IModuleInfo {
	/**
	 * Must create a new instance of the ModuleClass with given arguments.<br>
	 * <br>
	 * <b>Note:</b> Never instantiate modules directly via their constructor(s) or 
	 * via <code>IModuleInfo.newInstance</code>! <i>Instead</i> it is 
	 * <i>highly</i> recommended to use the {@link ModuleFactory} class to 
	 * <b>create and dispose module instances</b>.
	 * @see ModuleFactory#newInstance(ModuleContainer, Object[])
	 * @param args The arguments for the new instance.
	 * @return The newly created module instance as <code>IModule</code>.
	 */
	public IModule newInstance(Object[] args);
}
