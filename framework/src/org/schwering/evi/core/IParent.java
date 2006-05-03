package org.schwering.evi.core;

/**
 * Interface for parent-modules.<br />
 * <br />
 * This interface must be implemented by modules offer an API for sub-modules
 * and thus might have children.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public interface IParent {
	/**
	 * Registers a new child module.
	 * @param module The new sub-module.
	 */
	public void registerModule(ModuleContainer module);
}
