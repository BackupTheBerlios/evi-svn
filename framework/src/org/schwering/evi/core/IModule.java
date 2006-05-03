package org.schwering.evi.core;

/**
 * Basis interface for modules. <br />
 * This interface must be implemented by all modules. It forces them 
 * to provide the core functionalities: creating and destroying module
 * instances.
 * <br />
 * <br />
 * <b>Note #1:</b> Modules need to define a constructor for being instantiated.
 * This constructor must take one argument whose type is <code>IParent</code>.
 * An example constructor would be:<br />
 * <pre>	public Constructor(IParent owner) { ... }</pre>
 * <br />
 * <br />
 * <b>Note #2:</b> Modules might want to be parameterizable for being 
 * interactive with other modules. To achieve this, you need to define a 
 * second constructor which takes two arguments: first the <code>IParent</code>
 * and second a <code>Object[]</code>. This array is intended to store the 
 * arguments. To create a parameterized module you should take a look at 
 * {@link org.schwering.evi.core.ModuleFactory#newInstance(ModuleContainer, IParent, Object[])}.
 * An example constructor would be:<br />
 * <pre>	public Constructor(IParent owner, Object[] arguments) { ... }</pre>
 * <br />
 * <br />
 * <b>Note #3:</b> Unfortunately, at the moment the type of the first argument 
 * of the constructor(s) must really be <code>IParent</code>. It must <b>not</b>
 * be a class implementing <code>IParent</code>. It must explicitely be 
 * <code>IParent</code>. However, you might cast it to the type of your 
 * choice later in the constructor, of course.
 * <br />
 * <br />
 * <br />
 * <b>Note #4:</b> This interface requires all implementing classes to define 
 * a method <code>dispose</code>. This method should never be invoked directly!
 * Instead, there is {@link ModuleFactory#disposeInstance(IModule)} which 
 * not only invokes this <code>dispose</code> method but also cares about some 
 * internal things.
 * @see org.schwering.evi.core.ModuleLoader
 * @see org.schwering.evi.core.ModuleContainer
 * @see org.schwering.evi.core.ModuleFactory
 * @author Christoph Schwering (mailto:schwering@gmail.com)
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
