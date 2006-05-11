package org.schwering.evi.core;

import java.lang.reflect.Constructor;

/**
 * Provides methods to instantiate and dispose a module.<br />
 * When a module is instantiated, this new instance is registered in the 
 * module's <code>ModuleContainer</code>. And the <code>newInstance</code> / 
 * <code>disposeInstance</code> methods cause <code>IModuleListener</code> 
 * events to be fired. Hence, it is necessary to <b>use this class's methods 
 * to instantiate and dispose a module</b> to keep the internal mechanisms 
 * faultless!! 
 * <br />
 * <br />
 * By the way, this class adds a shutdownhook that invokes 
 * <code>disposeInstance</code> for each module instance when the client is 
 * shut down. (This shutdownhook is added in a <code>static</code> block; 
 * this means it is added automatically when this class is loaded.)
 * @see ModuleLoader
 * @see ModuleContainer
 * @see IModuleListener
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public final class ModuleFactory {
	/*
	 * Adds a shutdownhook that shuts down all open modules.
	 */
	static {
		Runtime r = Runtime.getRuntime();
		r.addShutdownHook(new Thread() {
			public void run() {
				ModuleContainer[] containers = ModuleLoader.getLoadedModules();
				for (int i = 0; i < containers.length; i++) {
					IModule[] instances = containers[i].getInstances();
					for (int j = 0; j < instances.length; j++) {
						disposeInstance(instances[j]);
					}
				}
			}
		});
	}
	
	/**
	 * No instances allowed/useful.
	 */
	private ModuleFactory() {
	}
	
	/**
	 * Creates a new instance of the given module, registers it internally and
	 * fires the {@link IModuleListener#instantiated(IModule)} event. 
	 * @param module The module.
	 * @return The module's instance.
	 * @throws ModuleInstantiationException If something fails (e.g. no 
	 * constructor).
	 * @see #newInstance(ModuleContainer, Object[])
	 * @see #disposeInstance(IModule)
	 */
	public static synchronized IModule newInstance(ModuleContainer module) 
	throws ModuleInstantiationException {
		return newInstance(module, null);
	}
	
	/**
	 * Creates a new instance of the given module with arguments, registers it
	 * internally and fires the {@link IModuleListener#instantiated(IModule)}
	 * event. 
	 * @param module The module.
	 * @param args The constructors arguments.
	 * @return The module's instance.
	 * @throws ModuleInstantiationException If something fails (e.g. no 
	 * constructor).
	 * @see #newInstance(ModuleContainer)
	 * @see #disposeInstance(IModule)
	 */
	public static synchronized IModule newInstance(ModuleContainer module, 
			Object[] args) throws ModuleInstantiationException {
		try {
			int len = (args != null) ? args.length : 0;
			Class[] wantedTypes = new Class[len];
			for (int i = 0; i < len; i++) {
				wantedTypes[i] = args[i].getClass();
			}
			
			Class moduleClass = module.getModuleClass();
			Constructor[] cons = moduleClass.getConstructors();
			for (int i = 0; i < cons.length; i++) {
				Class[] argList = cons[i].getParameterTypes();
				if (argListMatches(argList, wantedTypes)) {
					Constructor c = cons[i];
					IModule o = (IModule)c.newInstance(args);
					module.registerInstance(o);
					module.fireInstantiated(o);
					return o;
				}
			}
			throw new ModuleInstantiationException("No matching constructor.");
		} catch (Exception exc) {
			throw new ModuleInstantiationException("Creating instance failed.",
					exc);
		}
	}
	
	/**
	 * Fires the {@link IModuleListener#disposed(IModule)} event, unregisters
	 * the module internally and invokes {@link IModule#dispose()}.
	 * <br />
	 * Why should you call <code>ModuleFactorydisposeInstance(module)</code> 
	 * instead of calling <code>module.dispose()</code> directly?<br />
	 * Because (as told two lines above) this method does more than just 
	 * calling <code>dispose()</code>: it also unregisters the instance 
	 * internally and fires the respective event.
	 * @param o The module.
	 * @return <code>true</code> if the module is unregistered successfully.
	 */
	public synchronized static boolean disposeInstance(IModule o) {
		if (o == null) {
			return false;
		}
		String id = ModuleContainer.getIdByClass(o.getClass());
		ModuleContainer container = ModuleLoader.getLoadedModule(id);
		if (container == null) {
			return false;
		}
		container.fireDisposed(o);
		boolean returnValue = container.unregisterInstance(o);
		o.dispose();
		return returnValue;
	}
	
	/**
	 * Checks whether the wanted argument list would fit to the declared 
	 * arguments.
	 * @param args The arguments of a declared arguments.
	 * @param wanted The types of the objects that are intended to be arguments.
	 * @return <code>true</code> if objects of the type of wanted satisfy 
	 * the requirements of the argument list args.
	 */
	private static boolean argListMatches(Class[] args, Class[] wanted) {
		if (args == null || wanted == null) {
			return false;
		}
		if (args.length != wanted.length) {
			return false;
		}
		for (int i = 0; i < args.length && i < wanted.length; i++) {
			if (!args[i].isAssignableFrom(wanted[i])) {
				return false;
			}
		}
		return true;
	}
	
}
