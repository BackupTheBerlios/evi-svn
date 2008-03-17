/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.Attributes;

/**
 * Provides methods to load JARs.<br>
 * <br>
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
	private static Hashtable<String, ModuleContainer> table = new Hashtable<String, ModuleContainer>();
	
	/**
	 * Contains all <code>IModuleLoaderListener</code>s.
	 */
	private static List<IModuleLoaderListener> listeners = new LinkedList<IModuleLoaderListener>();
	
	/**
	 * The URL of the JAR file.
	 */
	private URL url;
	
	/**
	 * Creates a new module loader. 
	 * @param url The URL of the JAR.
	 */
	private ModuleLoader(URL url) {
		super(new URL[] { url }, ModuleLoader.class.getClassLoader());
		this.url = url;
	}
	
	/**
	 * Loads the <code>Module-Info-Class</code> implementation of the JAR.
	 * @return The <code>Class</code> representation of the class 
	 * the <code>Module-Info-Class</code> manifest-attribute points to.
	 * @throws ModuleLoaderException If the JAR cannot be loaded, if 
	 * the Module-Info-Class attribute is not defined or if the class 
	 * cannot be found for any reason.
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends IModuleInfo> getModuleInfoClass() throws ModuleLoaderException {
		try {
			URL jarURL = new URL("jar", "", url +"!/");
			JarURLConnection conn = (JarURLConnection)jarURL.openConnection();
			Attributes attr = conn.getMainAttributes();
			if (attr == null) {
				throw new ModuleLoaderException("No Module-Info-Class defined");
			}
			
			String infoClassName = attr.getValue(ATTR_MODULE_INFO_CLASS);
			if (infoClassName == null) {
				throw new ModuleLoaderException("No Module-Info defined");
			}
			infoClassName = infoClassName.trim();
			Class<?> infoClass = findClass(infoClassName);
			if (IModuleInfo.class.isAssignableFrom(infoClass)) {
				return (Class<? extends IModuleInfo>)infoClass;
			} else {
				throw new ModuleLoaderException("Module-Info-Class doens't" +
						" implement IModuleInfo");
			}
		} catch (ModuleLoaderException exc) {
			throw exc;
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
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((IModuleLoaderListener)listeners.get(i)).loaded(module);
		}
	}
	
	/**
	 * Fires the {@link IModuleLoaderListener#unloaded(ModuleContainer)} event.
	 * @param module The removed module.
	 */
	private static void fireUnloaded(ModuleContainer module) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((IModuleLoaderListener)listeners.get(i)).unloaded(module);
		}
	}
	
	/**
	 * Loads a new module from a JAR.
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
	 * Loads a new module from a JAR. 
	 * Fires the {@link IModuleLoaderListener#loaded(ModuleContainer)} event.
	 * @param url The destination of the JAR.
	 * @return A <code>ModuleContainer</code> for the module.
	 * @throws ModuleLoaderException If anything fails.
	 */
	public static ModuleContainer load(URL url) throws ModuleLoaderException {
		try {
			ModuleLoader loader = new ModuleLoader(url);
			Class<? extends IModuleInfo> moduleInfoClass = loader.getModuleInfoClass();
			IModuleInfo moduleInfo = moduleInfoClass.newInstance();
			
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
	 * Loads a module directly from the classpath. It does not have to be 
	 * in a seperate JAR but it must be accessible in the classpath.<br>
	 * The method also fires the 	 
	 * {@link IModuleLoaderListener#loaded(ModuleContainer)} event.
	 * @param moduleInfoClassName The classname modules 
	 * {@link IModuleInfo} class
	 * @return A <code>ModuleContainer</code> for the module.
	 * @throws ModuleLoaderException If anything fails.
	 */
	@SuppressWarnings("unchecked")
	public static ModuleContainer load(String moduleInfoClassName) 
	throws ModuleLoaderException {
		try {
			Class<? extends IModuleInfo> moduleInfoClass = 
				(Class<? extends IModuleInfo>)Class.forName(moduleInfoClassName);
			IModuleInfo moduleInfo = moduleInfoClass.newInstance();
			
			ModuleContainer container = new ModuleContainer(moduleInfo);
			container.setSource(moduleInfoClassName);
			
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
		Vector<String> list = new Vector<String>();
		Enumeration<String> e = table.keys();
		while (e.hasMoreElements()) {
			list.add((String)e.nextElement());
		}
		String[] arr = new String[list.size()];
		list.toArray(arr);
		return arr;
	}
	
	/**
	 * Returns a sorted array of the loaded modules. It is sorted by the 
	 * module's priorities, i.e. the returned array represents the 
	 * order in which the modules were loaded.
	 * @return A sorted array containing all loaded modules.
	 */
	public static ModuleContainer[] getLoadedModules() {
		Vector<ModuleContainer> list = new Vector<ModuleContainer>();
		Enumeration<ModuleContainer> e = table.elements();
		while (e.hasMoreElements()) {
			list.add((ModuleContainer)e.nextElement());
		}
		ModuleContainer[] arr = new ModuleContainer[list.size()];
		list.toArray(arr);
		Comparator<ModuleContainer> comparator = new Comparator<ModuleContainer>() {
			public int compare(ModuleContainer m1, ModuleContainer m2) {
				if (m1.getPriority() < m2.getPriority()) {
					return -1;
				} else if (m1.getPriority() == m2.getPriority()) {
					return 0;
				} else {
					return 1;
				}
			}
		};
		Arrays.sort(arr, comparator);
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