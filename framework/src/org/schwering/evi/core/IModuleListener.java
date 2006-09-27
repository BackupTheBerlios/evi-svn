/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.core;

/**
 * Listens to instantiation/disposal of a module. Each listener is connected 
 * with exactly one <code>ModuleContainer</code>. The events 
 * <code>instantiated</code> and <code>disposed</code> are fired when the 
 * respective methods in <code>ModuleFactory</code> are invoked. 
 * @see ModuleContainer#addListener(IModuleListener)
 * @see ModuleFactory#newInstance(ModuleContainer)
 * @see ModuleFactory#newInstance(ModuleContainer, Object[])
 * @see ModuleFactory#disposeInstance(IModule)
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IModuleListener {
	/**
	 * Fired when a module is instantiated via 
	 * {@link ModuleFactory#newInstance(ModuleContainer)}.
	 * <br>
	 * When a module is instantiated, the following things happen in this 
	 * order:
	 * <ol>
	 * <li> The instance object is created.
	 * <li> The instance is registrated at its <code>ModuleContainer</code> 
	 * 		and thus accessible via {@link ModuleContainer#getInstances()}.
	 * <li> This event is fired. 
	 * </ol>
	 * @param newInstance The newly created instance of the 
	 * module.
	 * @see ModuleFactory#newInstance(ModuleContainer)
	 * @see ModuleFactory#newInstance(ModuleContainer, Object[])
	 */
	public void instantiated(IModule newInstance);
	
	/**
	 * Fired when a module is disposed via 
	 * {@link ModuleFactory#disposeInstance(IModule)}.
	 * <br>
	 * When a module is disposed, the following things happen in this order:
	 * <ol>
	 * <li> This event is fired. 
	 * <li> The instance is unregistrated at its <code>ModuleContainer</code> 
	 * 		and no longer returned by {@link ModuleContainer#getInstances()}.
	 * <li> The instance object's {@link IModule#dispose()} is invoked.
	 * </ol>
	 * @param disposedInstance The instance which is disposed.
	 * @see ModuleFactory#disposeInstance(IModule)
	 */
	public void disposed(IModule disposedInstance);
}
