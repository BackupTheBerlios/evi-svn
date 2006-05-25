package org.schwering.evi.core;

/**
 * Interface for modules that should have the default menu generated by EVI.<br />
 * <br />
 * Classes that implement this interface do not have to define any interfaces.
 * EVI checks whether a module implements this interface and then creates the 
 * menu.
 * @see ModuleMenuInvoker
 * @see ModuleContainer#isDefaultMenuable()
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IDefaultMenuable extends IMenuable {
}