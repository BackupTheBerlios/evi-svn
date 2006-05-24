package org.schwering.evi.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.swing.JMenu;

import org.schwering.evi.gui.main.DefaultModuleMenu;

/**
 * Allows to instantiate the menu of a "menuable" module.
 * <br /><br />
 * This class is used by the EVI framework to create menus.
 * @see IMenuable
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public final class ModuleMenuInvoker {
	/**
	 * The name of the required static method. 
	 */
	private static final String METHOD_NAME = "getMenu";	
	
	/**
	 * The required return type.
	 */
	private static final Class RETURN_TYPE = JMenu.class;

	/**
	 * No instances allowed/useful.
	 */
	private ModuleMenuInvoker() {
	}
	
	/**
	 * Instantiates the module's menu. 
	 * This method doesn't do any special (no internal registering or so).<br />
	 * It just looks for a static method, invokes it and returns the returned 
	 * object. Have a look at {@link IMenuable} to learn how to define 
	 * this static method.
	 * @param module The module that should be configurable.
	 * @return The <code>IPanel</code> which represents the module 
	 * configuration.
	 * @throws ModuleInvocationException If the module is not menuable or 
	 * if invoking the method fails.
	 * @throws NullPointerException If <code>module</code> is 
	 * <code>null</code>.
	 */
	public static JMenu invoke(ModuleContainer module) 
	throws ModuleInvocationException {
		if (module.isDefaultMenuable()) {
			return getDefaultMenu(module);
		} else if (module.isCustomMenuable()) {
			return getCustomMenu(module);
		} else if (module.isMenuable()) {
			throw new ModuleInvocationException("Module neither implements "+
					"IDefaultMenuable nor ICustomMenuable.");
		} else {
			throw new ModuleInvocationException("Module is not " +
					"menuable");
		}
	}
	
	/**
	 * Invokes the static method <code>getMenu</code> which must be defined 
	 * in the module's main class.
	 * @param module The module whose menu should be created.
	 * @return The module's custom <code>JMenu</code>.
	 * @throws ModuleInvocationException If the module's main class does 
	 * not define the method or does not define it in a correct way or 
	 * if invoking the method fails for any other reason.
	 */
	private static JMenu getCustomMenu(ModuleContainer module) 
	throws ModuleInvocationException {
		Class cls = module.getModuleClass();
		Method method;
		try {
			method = cls.getMethod(METHOD_NAME, null);
		} catch (Exception exc) {
			throw new ModuleInvocationException("Could not " +
					"create module configuration panel", exc);
		}
		
		Class returnType = method.getReturnType();
		if (returnType == null) {
			throw new ModuleInvocationException(METHOD_NAME +
			" is void!");
		}
		if (!RETURN_TYPE.isAssignableFrom(returnType)) {
			throw new ModuleInvocationException(METHOD_NAME +
				" does not return "+ RETURN_TYPE);
		}
		
		int mod = method.getModifiers();
		if (Modifier.isAbstract(mod)) {
			throw new ModuleInvocationException(METHOD_NAME +
					" is abstract!");
		}
		if (!Modifier.isPublic(mod)) {
			throw new ModuleInvocationException(METHOD_NAME +
					" is not public!");
		}
		if (!Modifier.isStatic(mod)) {
			throw new ModuleInvocationException(METHOD_NAME +
					" is not static!");
		}
		
		try {
			Object o = method.invoke(null, null);
			return (JMenu)o;
		} catch (Exception exc) {
			throw new ModuleInvocationException("Could not "+
					"create custom module menu", exc);
		}
	}
	
	/**
	 * Returns the default module menu.
	 * @param module The module for which the module should be generated.
	 * @return The default module menu.
	 * @throws ModuleInvocationException If anyhting fails.
	 * @see org.schwering.evi.gui.main.DefaultModuleMenu
	 */
	private static JMenu getDefaultMenu(ModuleContainer module) 
	throws ModuleInvocationException {
		try {
			return new DefaultModuleMenu(module);
		} catch (Exception exc) {
			throw new ModuleInvocationException("Could not "+
					"create default module menu", exc);
		}
	}
}
