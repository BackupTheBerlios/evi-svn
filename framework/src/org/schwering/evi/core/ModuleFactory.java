package org.schwering.evi.core;

import java.lang.reflect.Constructor;
import java.net.URL;

/**
 * Provides methods to instantiate and dispose a module.<br>
 * When a module is instantiated, this new instance is registered in the 
 * module's <code>ModuleContainer</code>. And the <code>newInstance</code> / 
 * <code>disposeInstance</code> methods cause <code>IModuleListener</code> 
 * events to be fired. Hence, it is necessary to <b>use this class's methods 
 * to instantiate and dispose a module</b> to keep the internal mechanisms 
 * faultless!<br>
 * <br>
 * Zhis class adds a shutdownhook that invokes <code>disposeInstance</code> 
 * for each module instance when the client is shut down. (This shutdownhook 
 * is added in a <code>static</code> block; this means it is added 
 * automatically when this class is loaded.)
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
	 * Creates a new instance of the given module.
	 * The method also registers the instance internally and fires the 
	 * {@link IModuleListener#instantiated(IModule)} event. 
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
	 * Creates a new instance of the given module, optionally with arguments.
	 * The method also registers the instance internally and fires the 
	 * {@link IModuleListener#instantiated(IModule)} event. <br>
	 * <br>
	 * <h3>If the module was loaded from a JAR with a <code>IModuleInfo</code> 
	 * information class:</h3>
	 * <ul>
	 * <li> If <code>args</code> is <code>null</code>, the module is tried to 
	 * be instantiated without arguments using 
	 * {@link IModuleInfo#newInstance()}. </li>
	 * <li> If <code>args</code> has just one element and if this element is an 
	 * instance of <code>java.net.URL</code> and if the module is a URL 
	 * handler, the instance is created using the method 
	 * {@link IURLHandler#newInstance(URL)}. </li>
	 * <li> Otherwise, if the module is parameterizable, the <code>args</code> 
	 * are passed to the module using the 
	 * {@link IParameterizable#newInstance(Object[])} method. </li>
	 * <li> Otherwise, an exception is thrown. </li>
	 * </ul>
	 * <br>
	 * <h3>If the module was loaded as single class using 
	 * <code>ModuleLoader.load(Class)</code></h3>
	 * <ul>
	 * <li> If <code>args</code> is <code>null</code>, a constructor is 
	 * searched that takes no arguments. If it is found, the constructor 
	 * is used to create a new instance. </li>
	 * <li> Otherwise, a constructor is searched that matches the 
	 * types of the objects in <code>args</code>. The constructor is invoked 
	 * and the new instance is returned. </li>
	 * <li> Otherwise, an exception is thrown. </li>
	 * </ul>
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
		System.out.println("newInstance("+ module +", "+ args.length +")");
		try {
			IModuleInfo info = module.getModuleInfo();
			IModule instance = null;
			if (info != null) {
				Object object = null;
				if (args == null) {
					System.out.println("args == null");
					object = info.newInstance();
				} else if (args.length == 1 
						&& args[0] instanceof URL
						&& module.isURLHandler()) {
					System.out.println("args is URL");
					URL url = (URL)args[0];
					object = ((IURLHandler)info).newInstance(url);
				} else if (module.isParameterizable()) {
					System.out.println("args != null");
					object = ((IParameterizable)info).newInstance(args);
				} else {
					StringBuffer msg = new StringBuffer();
					msg.append("The method failed:\n");
					msg.append("\tnewInstance("+ module +", args)\n");
					msg.append("where args ");
					if (args == null) {
						msg.append("null.\n");
					} else {
						msg.append("has "+args.length+" elements:\n");
						for (int i = 0; i < args.length; i++) {
							msg.append("\targs["+i+"] is instance of ");
							msg.append((args[i] == null) ? null : args[i].getClass());
							msg.append("\n");
						}
					}
					msg.append("Module information:\n");
					msg.append("\tURLHandler: ");
					msg.append((module.isURLHandler()) ? "yes" : "no");
					msg.append("\n");
					msg.append("\tParameterizable: ");
					msg.append((module.isParameterizable()) ? "yes" : "no");
					msg.append("\n");
					throw new ModuleInstantiationException(msg.toString());
				}
				instance = (IModule)object;
			} else {
				Constructor c = searchConstructor(module, args);
				Object object = c.newInstance(args);
				instance = (IModule)object;
			}
			module.registerInstance(instance);
			module.fireInstantiated(instance);
			return instance;
		} catch (ModuleInstantiationException exc) {
			throw exc;
		} catch (Throwable exc) {
			throw new ModuleInstantiationException("Creating instance failed.",
					exc);
		}
	}
	
	/**
	 * Searches for the first constructor in the module's ModuleClass
	 * that matches the types of the objects in <code>args</code>.
	 * @param module The module that should be instantiated.
	 * @param args The arguments that its constructor should take.
	 * @return A matching constructor.
	 * @throws ModuleInstantiationException If no constructor is found.
	 */
	private static Constructor searchConstructor(ModuleContainer module, 
			Object[] args) throws ModuleInstantiationException {
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
				return cons[i];
			}
		}
		throw new ModuleInstantiationException("No matching constructor.");
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
	
	/**
	 * Fires the {@link IModuleListener#disposed(IModule)} event, unregisters
	 * the module internally and invokes {@link IModule#dispose()}.
	 * <br>
	 * Why should you call <code>ModuleFactorydisposeInstance(module)</code> 
	 * instead of calling <code>module.dispose()</code> directly?<br>
	 * Because (as told two lines above) this method does more than just 
	 * calling <code>dispose()</code>: it also unregisters the instance 
	 * internally and fires the respective event.
	 * @param instance The module.
	 * @return <code>true</code> if the module is unregistered successfully.
	 */
	public synchronized static boolean disposeInstance(IModule instance) {
		if (instance == null) {
			return false;
		}
		String id = ModuleContainer.getIdByClass(instance.getClass());
		ModuleContainer container = ModuleLoader.getLoadedModule(id);
		if (container == null) {
			return false;
		}
		container.fireDisposed(instance);
		boolean returnValue = container.unregisterInstance(instance);
		instance.dispose();
		return returnValue;
	}
}
