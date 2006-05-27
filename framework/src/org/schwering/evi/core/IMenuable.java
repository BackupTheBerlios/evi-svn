package org.schwering.evi.core;

/**
 * ModuleInfoClass interface that adds a menu to the menubar.<br>
 * Implement this interface in your Module-Info-Class if EVI should 
 * generate a menu for this module in the menubar.
 * @see ModuleContainer#isMenuable()
 * @see ICustomMenuable
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IMenuable extends IModuleInfo {
	/**
	 * Indicates whether the menu should be really generated and added.
	 * @return <code>true</code> makes EVI create the menu, <code>false</code>
	 * leaves the menubar as it is.
	 */
	public boolean isMenuable();
}