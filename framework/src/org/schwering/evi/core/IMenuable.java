package org.schwering.evi.core;

/**
 * Interface for modules that have a menu.<br />
 * Implementing this interface has no effect, at least no positive (but an 
 * exception possibly). Instead, you might want to implement one of its 
 * subinterfaces: {@link IDefaultMenuable} and {@link ICustomMenuable}.
 * <br />
 * <br />
 * Generally speaking, there are three cases:
 * <ul>
 * <li> Your module should have <i>no</i> menu. Then just leave all of 
 * these interfaces out, you won't need one of them. </li>
 * <li> Your module should have the <i>default</i> menu. This menu offers 
 * some basic and common module options (such as creating a new instance).
 * Then have you need to implement {@link IDefaultMenuable}.</li>
 * <li> Your interface wants to have a <i>custom</i> menu to offer 
 * specific options. Then you need to implement {@link ICustomMenuable}.</li>
 * </ul>
 * @see ModuleMenuInvoker
 * @see ModuleContainer#isMenuable()
 * @see IDefaultMenuable
 * @see ICustomMenuable
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IMenuable extends IModule {
}