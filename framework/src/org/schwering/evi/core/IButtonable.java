/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.core;

/**
 * ModuleInfoClass interface that let's a button appear in the toolbar.<br>
 * Implement this interface in your Module-Info-Class if a 
 * button should be added to the toolbar.
 * @see ModuleContainer#isButtonable()
 * @see ICustomButtonable 
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IButtonable extends IModuleInfo {
	/**
	 * Decides whether a button is really added. If this method does not return 
	 * <code>true</code>, no button is added.
	 * @return <code>true</code> to display a button, <code>false</code> to 
	 * do not.
	 */
	public boolean isButtonable();
}