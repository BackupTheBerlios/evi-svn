package org.schwering.evi.core;

/**
 * Basis interface for modules. <br />
 * This interface must be implemented by all modules. It forces them 
 * to provide the core functionalities: creating and destroying module
 * instances.
 * <br />
 * <br />
 * <b>Note #1:</b> Modules need to define a constructor for being instantiated.
 * This constructor must take no arguments.
 * An example constructor would be (for a class named <code>Constructor</code>:
 * <br />
 * <pre>	public Constructor() { ... }</pre>
 * <br />
 * <br />
 * <b>Note #2:</b> Modules might want to be parameterizable to be 
 * interactive with other modules. To achieve this, the constructor simply must
 * To achieve this, you need to define a second constructor that requires the 
 * respective arguments.<br />
 * To create an instance of a parameterizable module you need the 
 * {@link ModuleFactory#newInstance(ModuleContainer, Object[])} method.
 * An example constructor would be:<br />
 * <pre>	public Constructor(URL addr, Object[] blabla) { ... }</pre>
 * <br />
 * <br />
 * <b>Note #3:</b> This interface requires all implementing classes to define 
 * a method <code>dispose</code>. This method should never be invoked directly!
 * Instead, there is {@link ModuleFactory#disposeInstance(IModule)} which 
 * not only invokes this <code>dispose</code> method but also cares about some 
 * internal things.
 * @see org.schwering.evi.core.ModuleLoader
 * @see org.schwering.evi.core.ModuleContainer
 * @see org.schwering.evi.core.ModuleFactory
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IModule {
	/**
	 * Should clean up used resources when the module is shut down. For 
	 * example, this method should close opened streams and files.
	 * This method should never be invoked directly! Instead, there is 
	 * {@link ModuleFactory#disposeInstance(IModule)} which not only invokes 
	 * this <code>dispose</code> method but also cares about some internal 
	 * things.
	 * @see ModuleFactory#disposeInstance(IModule)
	 */
	public void dispose();
}
