package org.schwering.evi.core;

/**
 * ModuleClass basis interface.<br>
 * <br>
 * Each ModuleClass must implement this interface. The ModuleClass is 
 * something like the main class of the module. When you would like to 
 * display a <code>JFrame</code>, the ModuleClass would extend 
 * <code>JFrame</code>.<br>
 * <br>
 * Every module needs a ModuleClass and a ModuleInfoClass. The latter 
 * must implement {@link IModuleInfo}. It provides all basical 
 * information of the menu like its name the version etc.<br>
 * <br>
 * <b>Note:</b> Never instantiate modules directly via their constructor(s) or 
 * via <code>IModuleInfo.newInstance</code>!! <i>Instead</i> it is 
 * <i>highly</i> recommended to use the {@link ModuleFactory} class to 
 * <b>create and dispose module instances</b>.
 * @see ModuleLoader
 * @see ModuleContainer
 * @see ModuleFactory
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IModule {
	/**
	 * Should clean up used resources when the module is shut down. For 
	 * example, this method should close opened streams and files.<br>
	 * <br>
	 * <b>Note:</b> Never invoke this method directly! Instead, there is 
	 * {@link ModuleFactory#disposeInstance(IModule)} which not only invokes 
	 * this <code>dispose</code> method but also cares about some internal 
	 * things.
	 * @see ModuleFactory#disposeInstance(IModule)
	 */
	public void dispose();
}
