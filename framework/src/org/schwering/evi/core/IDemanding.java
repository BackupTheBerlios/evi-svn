/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.core;

/**
 * ModuleInfoClass interface used to specify requirements of other modules.<br>
 * Implement this interface in 
 * Implement this interface in your Module-Info-Class if your module 
 * requires one or more other modules to run appropriately. 
 * @see ModuleContainer#isDemanding()
 * @see Requirement
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IDemanding extends IModuleInfo {
	/**
	 * Determines which modules in which specific versions are required by 
	 * this module.
	 * @return An array of requirements or simply <code>null</code>.
	 */
	public Requirement[] getRequirements();
}
