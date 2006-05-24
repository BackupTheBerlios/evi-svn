package org.schwering.evi.core;

/**
 * Interface for modules that have a custom menu.<br />
 * <br />
 * This interface should be implemented by modules which provide a custom
 * menu for the menubar.<br />
 * <br />
 * <b>Note:</b> Modules that implement this interface <b>must define the 
 * following static method</b>:<pre>
 * 	public javax.swing.JMenu getMenu() {
 * 		...
 * 	}
 * </pre>
 * @see ModuleMenuInvoker
 * @see ModuleContainer#isCustomMenuable()
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface ICustomMenuable extends IMenuable {
}