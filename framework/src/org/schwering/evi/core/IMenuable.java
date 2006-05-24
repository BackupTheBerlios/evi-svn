package org.schwering.evi.core;

/**
 * Interface for modules that have menu.<br />
 * Implementing this interface has no effect, at least no positive (but an 
 * exception possibly). Instead, you might want to implement one of its 
 * subinterfaces: {@link IDefaultMenuable} and {@link ICustomMenuable}.
 * @see ModuleMenuInvoker
 * @see ModuleContainer#isMenuable()
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IMenuable extends IModule {
}