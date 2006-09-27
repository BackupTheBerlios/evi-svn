/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.core;

/**
 * ModuleInfoClass interface for configurable modules.<br>
 * Implement this interface in your Module-Info-Class if your module 
 * provides a configuration.
 * @see ModuleContainer#isConfigurable()
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IConfigurable extends IModuleInfo {
	/**
	 * Should return the config panel as <code>IPanel</code>.
	 * @return The config panel which actually provides the opportunity 
	 * to configure the module.
	 */
	public IPanel getConfigPanel();
}