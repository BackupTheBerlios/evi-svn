package org.schwering.evi.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
	private static final Class RETURN_TYPE = javax.swing.JMenu.class;

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
	public static javax.swing.JMenu invoke(ModuleContainer module) 
	throws ModuleInvocationException {
		if (!module.isMenuable()) {
			throw new ModuleInvocationException("Module is not " +
					"configurable");
		}
		
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
			return (javax.swing.JMenu)o;
		} catch (Exception exc) {
			throw new ModuleInvocationException("Could not "+
					"create module configuration panel", exc);
		}
	}
}
