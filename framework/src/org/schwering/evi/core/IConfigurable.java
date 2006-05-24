package org.schwering.evi.core;

/**
 * Interface for configurable modules.<br />
 * <br />
 * This interface should be implemented by modules which offer a 
 * graphical configuration frontend.<br />
 * <br />
 * <b>Note #1:</b> Modules that implement this interface <b>must define the 
 * following static method</b>:<pre>
 * 	public static IPanel getConfigPanel() {
 * 		...
 * 	}
 * </pre>
 * <br />
 * <b>Note #2:</b> How to make a module configuration class<br />
 * A module configuration class must implement {@link IPanel}. The method
 * {@link IPanel#getPanelInstance()} must return the panel instance. Typically
 * a module configuration class looks like this:<pre>
 * public class MyModuleConfig
 * 		extends javax.swing.JPanel 
 * 		implements org.schwering.evi.core.IPanel {
 * 
 * 	// The constructor <b>most not take any arguments</b>!
 *	public MyModuleConfig() {
 *		super();
 *		// Add the gui components and so on:
 *		add(...);
 *	}
 *	
 *	// See {@link IPanel#dispose()}
 *	public void dispose() {
 *		// It makes sense to write the configuration to a file here,
 *		// because this method is invoked when the config panel is closed,
 *		// even when it's closed via right-click -> close on the tabbar.
 *		//
 *		// By the way, it also makes sense to put the configuration
 *		// into {@link org.schwering.evi.conf.MainConfiguration#CONFIG_DIR}
 *	}
 *
 *	// See {@link IPanel#getIcon()}
 *	public Icon getIcon() {
 *		return null;
 *	}
 *
 *	// See {@link IPanel#getPanelInstance()}
 *	public Component getPanelInstance() {
 *		return this;
 *	}
 *
 *	// See {@link IPanel#getTitle()}
 *	public String getTitle() {
 *		return "MyModule Configuration";
 *	}
 * }
 * </pre>
 * @see ModuleConfigurationInvoker
 * @see ModuleContainer#isConfigurable()
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IConfigurable extends IModule {
}