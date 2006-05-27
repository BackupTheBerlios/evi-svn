package org.schwering.evi.core;

import javax.swing.JButton;

/**
 * ModuleInfoClass interface that allows to customize a module's button.<br>
 * Implement this interface in your Module-Info-Class if your module 
 * should display a specific (non-standard-generated) button in the toolbar. 
 * @see ModuleContainer#isCustomButtonable()
 * @see IButtonable 
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface ICustomButtonable extends IButtonable {
	/**
	 * Returns the customized button. The button should perform an action
	 * when it's clicked.
	 * @return A <code>javax.swing.JButton</code> which performs a 
	 * specific action when it's clicked.
	 */
	public JButton getCustomButton();
}
