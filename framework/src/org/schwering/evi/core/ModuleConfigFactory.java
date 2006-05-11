package org.schwering.evi.core;

/**
 * Allows to instantiate the configuration panel of a configurable module.
 * <br />
 * <br />
 * <b>How to make a module configuration class</b><br />
 * A module configuration class must implement {@link IPanel}. The method
 * {@link IPanel#getPanelInstance()} must return the panel instance. Typically
 * a module configuration class looks like this:<pre>
 * public class MyModuleConfigClass 
 * 		extends javax.swing.JPanel 
 * 		implements org.schwering.evi.core.IPanel {
 * 	// The constructor <b>most not take any arguments</b>!
 *	public PongConfig() {
 *		super();
 *		// Add the gui components and so on:
 *		add(...)
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
 * Note that the constructor must not take any arguments!<br />
 * <br />
 * Do not forget to add the respective <b>entry in the manifest</b>! See
 * {@link ModuleLoader#ATTR_MODULE_CONFIG_CLASS}.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public final class ModuleConfigFactory {
	/**
	 * No instances allowed/useful.
	 */
	private ModuleConfigFactory() {
	}
	
	/**
	 * Instantiates the module's configuration panel. 
	 * This method doesn't do any special (no internal registering or so).
	 * There's nothing to consider :-).
	 * @param module The module that should be configurable.
	 * @return The <code>IPanel</code> which represents the module 
	 * configuration.
	 * @throws ModuleConfigInstantiationException If the module is not 
	 * configurable or if creating the instance fails.
	 */
	public static IPanel newInstance(ModuleContainer module) 
	throws ModuleConfigInstantiationException {
		if (!module.isConfigurable()) {
			throw new ModuleConfigInstantiationException("Module is not " +
					"configurable");
		}
		
		try {
			Class configClass = module.getConfigClass();
			Object o = configClass.newInstance();
			return (IPanel)o;
		} catch (Exception exc) {
			throw new ModuleConfigInstantiationException("Creating " +
					"configuration panel instance failed.", exc);
		}
	}

}
