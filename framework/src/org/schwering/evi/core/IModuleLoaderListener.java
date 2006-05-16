package org.schwering.evi.core;

/**
 * Listens for module load/unload events.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IModuleLoaderListener {
	/**
	 * Fired when a module is loaded.
	 * The module is already registered internally when this event is fired.
	 * This means it is found by the <code>ModuleLoader.getLoaded*</code>
	 * methods.
	 */
	public void loaded(ModuleContainer loadedModule);
	
	/**
	 * Fired when a module is unloaded. 
	 * The module is already unregistered internally when this event is fired.
	 * This means it is no longer found by the 
	 * <code>ModuleLoader.getLoaded*</code> methods!
	 * @param unloadedModule The module which is unloaded.
	 */
	public void unloaded(ModuleContainer unloadedModule);
}
