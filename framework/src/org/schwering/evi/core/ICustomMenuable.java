/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.core;

import javax.swing.JMenu;

/**
 * ModuleInfoClass interface that allows to customize a module's menu.<br>
 * Implement this interface in your Module-Info-Class if your module 
 * should display a specific (non-standard-generated) menu in the menubar. 
 * @see ModuleContainer#isCustomMenuable()
 * @see IMenuable
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface ICustomMenuable extends IMenuable {
	/**
	 * Returns the customized menu. The menu-items should perform respective 
	 * actions when they're clicked.
	 * @return A <code>javax.swing.JMenu</code> for the menubar.
	 */
	public JMenu getCustomMenu();
}