package org.schwering.evi.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.Attributes;

/**
 * Provides methods to load JARs.<br />
 * <br />
 * For a module developer, most of these methods are probably unimportant.
 * However, he might be interesting in the following: 
 * <ul>
 * <li> The following three methods can be used to search for loaded modules:
 * 		<ul>
 * 		<li> {@link #getLoadedIds()} 
 * 		<li> {@link #getLoadedModules()}
 * 		<li> {@link #getLoadedModule(String)}
 * 		</ul>
 * <li> The following methods can be used to check whether a module is loaded:
 * 		<ul>
 * 		<li> {@link #isLoaded(ModuleContainer)}
 * 		<li> {@link #isLoaded(String)}
 * 		</ul>
 * </ul>
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @see ModuleContainer
 * @version $Id$
 */
public final class ModuleLoader extends URLClassLoader {
	private static final String ATTR_MODULE_INFO_CLASS = "Module-Info-Class";
	
	/**
	 * Contains the <code>ModuleContainer</code> objects. Each value's key 
	 * is the module's id.
	 */
	private static Hashtable table = new Hashtable();
	
	/**
	 * Contains all <code>IModuleLoaderListener</code>s.
	 */
	private static Vector listeners = new Vector(2);
	
	/**
	 * The URL of the JAR file.
	 */
	private URL url;
	
	/**
	 * Creates a new module loader. 
	 * @param url The URL of the JAR.
	 */
	private ModuleLoader(URL url) {
		super(new URL[] { url });
		this.url = url;
	}
	
	private Class getModuleInfoClass() throws ModuleLoaderException {
		try {
			URL jarURL = new URL("jar", "", url +"!/");
			JarURLConnection conn = (JarURLConnection)jarURL.openConnection();
			Attributes attr = conn.getMainAttributes();
			if (attr == null) {
				throw new ModuleLoaderException("No Module-Class defined");
			}
			
			String infoClassName = attr.getValue(ATTR_MODULE_INFO_CLASS);
			if (infoClassName == null) {
				throw new ModuleLoaderException("No Module-Info defined");
			}
			infoClassName = infoClassName.trim();
			Class infoClass = findClass(infoClassName);
			return infoClass;
		} catch (Exception exc) {
			throw new ModuleLoaderException(exc);
		}
	}
	
	/**
	 * Adds a new <code>ModuleLoaderListener</code>.
	 * @param listener The <code>IModuleLoaderListener</code>.
	 */
	public static void addListener(IModuleLoaderListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a <code>ModuleLoaderListener</code>.
	 * @param listener The <code>IModuleLoaderListener</code>.
	 */
	public static void removeListener(IModuleLoaderListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Fires the {@link IModuleLoaderListener#loaded(ModuleContainer)} event.
	 * @param module The new module.
	 */
	private static void fireLoaded(ModuleContainer module) {
		if (listeners == null) {
			return;
		}
		for (int i = 0; i < listeners.size(); i++) {
			((IModuleLoaderListener)listeners.get(i)).loaded(module);
		}
	}
	
	/**
	 * Fires the {@link IModuleLoaderListener#unloaded(ModuleContainer)} event.
	 * @param module The removed module.
	 */
	private static void fireUnloaded(ModuleContainer module) {
		if (listeners == null) {
			return;
		}
		for (int i = 0; i < listeners.size(); i++) {
			((IModuleLoaderListener)listeners.get(i)).unloaded(module);
		}
	}
	
	/**
	 * Loads a new module.
	 * Fires the {@link IModuleLoaderListener#loaded(ModuleContainer)} event.
	 * @param f The destination of the JAR.
	 * @return A <code>ModuleContainer</code> for the module.
	 * @throws ModuleLoaderException If anything fails.
	 */
	public static ModuleContainer load(File f) throws ModuleLoaderException {
		try {
			return load(f.toURL());
		} catch (Exception exc) {
			throw new ModuleLoaderException(exc);
		}
	}

	/**
	 * Loads a new module. 
	 * Fires the {@link IModuleLoaderListener#loaded(ModuleContainer)} event.
	 * @param url The destination of the JAR.
	 * @return A <code>ModuleContainer</code> for the module.
	 * @throws ModuleLoaderException If anything fails.
	 */
	public static ModuleContainer load(URL url) throws ModuleLoaderException {
		try {
			ModuleLoader loader = new ModuleLoader(url);
			Class moduleInfoClass = loader.getModuleInfoClass();
			Object object = moduleInfoClass.newInstance();
			IModuleInfo moduleInfo = (IModuleInfo)object;
			
			ModuleContainer container = new ModuleContainer(moduleInfo);
			container.setSource(url);
			
			String id = container.getId();
			if (!isLoaded(id)) {
				table.put(id, container);
				fireLoaded(container);
				return container;
			} else {
				return getLoadedModule(id);
			}
		} catch (ModuleLoaderException exc) {
			throw exc;
		} catch (Throwable exc) {
			throw new ModuleLoaderException(exc);
		}
	}
	
	/**
	 * Loads a one-class-module. The module has plain settings, this means 
	 * it cannot be buttonable nor menuable nor configurable. However, 
	 * of course it can implement <code>IApplet</code> and/or 
	 * <code>IPanel</code>.<br />
	 * The method also fires the 	 
	 * {@link IModuleLoaderListener#loaded(ModuleContainer)} event.
	 * @param moduleClassName The classname: <code>my.package.Class</code>
	 * @return A <code>ModuleContainer</code> for the module.
	 * @throws ModuleLoaderException If anything fails.
	 */
	public static ModuleContainer load(String moduleClassName) 
	throws ModuleLoaderException {
		try {
			Class moduleClass = Class.forName(moduleClassName);
			ModuleContainer container = new ModuleContainer(moduleClass);
			container.setSource(moduleClassName);
			String id = container.getId();
			if (!isLoaded(id)) {
				table.put(id, container);
				fireLoaded(container);
				return container;
			} else {
				return getLoadedModule(id);
			}
		} catch (ModuleLoaderException exc) {
			throw exc;
		} catch (Exception exc) {
			throw new ModuleLoaderException(exc);
		}
	}
	
	/**
	 * Unloads a module. Each instance of module is shut down softly 
	 * via <code>ModuleFactory.disposeInstance()</code>. After that, the 
	 * {@link IModuleLoaderListener#unloaded(ModuleContainer)} event is fired.
	 * @param id The id of the module that is intended to be unloaded.
	 */
	public static void unload(String id) {
		ModuleContainer container = getLoadedModule(id);
		if (container == null) {
			return;
		}
		IModule[] instances = container.getInstances();
		for (int i = 0; i < instances.length; i++) {
			ModuleFactory.disposeInstance(instances[i]);
		}
		table.remove(id);
		fireUnloaded(container);
	}
	
	/**
	 * Returns an array of the loaded modules' ids.
	 * @return An array containing the ids of all loaded modules.
	 */
	public static String[] getLoadedIds() {
		Vector list = new Vector();
		Enumeration e = table.keys();
		while (e.hasMoreElements()) {
			list.add((String)e.nextElement());
		}
		String[] arr = new String[list.size()];
		list.toArray(arr);
		return arr;
	}
	
	/**
	 * Returns an array of the loaded modules.
	 * @return An array containing all loaded modules.
	 */
	public static ModuleContainer[] getLoadedModules() {
		Vector list = new Vector();
		Enumeration e = table.elements();
		while (e.hasMoreElements()) {
			list.add((ModuleContainer)e.nextElement());
		}
		ModuleContainer[] arr = new ModuleContainer[list.size()];
		list.toArray(arr);
		return arr;
	}
	
	/**
	 * Looks for a loaded module.
	 * @param moduleId The id of the module that is requested.
	 * @return A loaded module.
	 */
	public static ModuleContainer getLoadedModule(String moduleId) {
		return (ModuleContainer)table.get(moduleId);
	}

	/**
	 * Indicates whether a module is loaded or not.
	 * @param id The id of the module that might have been loaded.
	 * @return <code>true</code> if the module is loaded.
	 */
	public static boolean isLoaded(String id) {
		return table.containsKey(id);
	}

	/**
	 * Indicates whether a module is loaded or not.
	 * @param module The container of the module that might have been loaded.
	 * @return <code>true</code> if the module is loaded.
	 */
	public static boolean isLoaded(ModuleContainer module) {
		return table.containsValue(module);
	}
}