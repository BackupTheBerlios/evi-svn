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
	/**
	 * The module's main class (which is also the id) attribute in the 
	 * manifest.<br />
	 * This field is public because you should know about this attribute.<br />
	 * This field's value is: "<code>Module-Class</code>".<br />
	 * An example for a module-class in a manifest-file is:<br />
	 * <code>Module-Class: bla.package.comes.here.MyModule</code>
	 */
	public static final String ATTR_MODULE_CLASS = "Module-Class";
	
	/**
	 * The module's version attribute in the manifest. It is optional to 
	 * define a module version.<br />
	 * This field is public because you should know about this attribute.<br />
	 * This field's value is: "<code>Module-Version</code>".<br />
	 * An example for a module-version-attribute in a manifest-file is:<br />
	 * <code>Module-Version: 1.0</code>
	 */
	public static final String ATTR_MODULE_VERSION = "Module-Version";
	
	/**
	 * The module's name attribute in the manifest. It is optional to 
	 * define a module name. If no name is set, the id is also used as name.
	 * <br />
	 * This field is public because you should know about this attribute.<br />
	 * This field's value is: "<code>Module-Version</code>".<br />
	 * An example for a module-version-attribute in a manifest-file is:<br />
	 * <code>Module-Version: 1.0</code>
	 */
	public static final String ATTR_MODULE_NAME = "Module-Name";
	
	/**
	 * The module's config classname if the module is configurable with a GUI.
	 * Providing a configuration GUI for a module is optionally; just leave 
	 * this attribute out if your module doesn't have a configuration panel.
	 * <br />
	 * This field is public because you should know about this attribute.<br />
	 * This field's value is: "<code>Module-Config-Class</code>".<br />
	 * An example for a module-protocol-attribute in a manifest-file is:
	 * <br />
	 * "<code>Module-Config-Class: my.package.module.ConfigPanel</code>".
	 * @see ModuleConfigFactory
	 */
	public static final String ATTR_MODULE_CONFIG_CLASS = "Module-Config-Class";
	
	/**
	 * The module's protocols attribute in the manifest. It is optional to
	 * define protocols. For example, a browser module should register "http".
	 * <br />
	 * This field is public because you should know about this attribute.<br />
	 * This field's value is: "<code>Module-Protocols</code>".<br />
	 * An example for a module-protocol-attribute in a manifest-file is:
	 * <br />
	 * "<code>Module-Protocols: http https irc</code>".
	 */
	public static final String ATTR_MODULE_PROTOCOLS = "Module-Protocols";
	
	/**
	 * The module's requirements attribute in the manifest. It is optional to
	 * define requirements.<br />
	 * This field is public because you should know about this attribute.<br />
	 * This field's value is: "<code>Requirements</code>".<br />
	 * An example for a module-requirement-attribute in a manifest-file is:
	 * <br />
	 * "<code>Requires: foo.bar.BlaModule foo.bar.BlupModule:1.0 
	 * foo.bar.BumModule foo.bar.PiffModule foo.bar.PaffModule:2.1</code>".
	 */
	public static final String ATTR_MODULE_REQUIREMENTS = "Requires";
	
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
	
	/**
	 * Searches for the "Main-Class" as specified in the JAR's manifest.
	 * @return The name of the Main-Class, if specified, or 
	 * <code>DEFAULT_MAIN_CLASS</code>.
	 * @throws ModuleLoaderException E.g. if the JAR cannot be opened.
	 */
	private ModuleLoader.ModuleInfoContainer getModuleInfo() 
	throws ModuleLoaderException {
		try {
			URL jarURL = new URL("jar", "", url +"!/");
			JarURLConnection conn = (JarURLConnection)jarURL.openConnection();
			Attributes attr = conn.getMainAttributes();
			if (attr == null) {
				throw new ModuleLoaderException("No Module-Class defined");
			}
			
			String moduleClassName = attr.getValue(ATTR_MODULE_CLASS);
			if (moduleClassName == null) {
				throw new ModuleLoaderException("No Module-Class defined");
			} else {
				ModuleInfoContainer info = new ModuleInfoContainer();
				info.moduleClassName = moduleClassName;
				info.version = parseFloat(attr.getValue(ATTR_MODULE_VERSION));
				info.name = attr.getValue(ATTR_MODULE_NAME);
				info.configClassName = attr.getValue(ATTR_MODULE_CONFIG_CLASS);
				info.protocols = getProtocols(attr);
				info.requirements = getRequirements(attr);
				return info;
			}
		} catch (Exception exc) {
			throw new ModuleLoaderException(exc);
		}
	}
	
	/**
	 * Grabs the protocols the module can handle.
	 * @param attr The manifest's attributes.
	 * @return The protocols.
	 */
	private String[] getProtocols(Attributes attr) {
		String str = attr.getValue(ATTR_MODULE_PROTOCOLS);
		if (str == null) {
			return null;
		}
		return str.trim().split(" ");
	}
	
	/**
	 * Grabs the requirements out of a JAR.
	 * @param attr The manifest's attributes.
	 * @return The requirements.
	 */
	private Requirement[] getRequirements(Attributes attr) {
		String str = attr.getValue(ATTR_MODULE_REQUIREMENTS);
		if (str == null) {
			return null;
		}
		String[] strArr = str.trim().split(" ");
		Requirement[] reqs = new Requirement[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			String[] strIdAndVersion = strArr[i].split(":");
			String id = strIdAndVersion[0].trim();
			float version;
			if (strIdAndVersion.length > 1) {
				version = parseFloat(strIdAndVersion[1].trim());
			} else {
				version = 0.0f;
			}
			reqs[i] = new Requirement(id, version);
		}
		return reqs;
	}
	
	/**
	 * A small helper class that just contains information grabbed out of a
	 * JAR's manifest.
	 * @author chs
	 */
	class ModuleInfoContainer {
		String moduleClassName;
		float version;
		String name;
		String configClassName;
		String[] protocols;
		Requirement[] requirements;
	}
	
	/**
	 * Parses a float or returns 0.
	 * @param s The string which represents a float.
	 * @return The the float or 0.0 if parsing fails.
	 */
	private static float parseFloat(String s) {
		try {
			return Float.parseFloat(s);
		} catch (Exception exc) {
			return 0.0f;
		}
	}
	
	/**
	 * Adds a new listener.
	 * @param listener The <code>IModuleLoaderListener</code>.
	 */
	public static void addListener(IModuleLoaderListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener.
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
			ModuleLoader.ModuleInfoContainer info = loader.getModuleInfo();
			
			String moduleClassName = info.moduleClassName;
			Class cls = loader.findClass(moduleClassName);
			float version = info.version;
			ModuleContainer module = new ModuleContainer(cls, version);
			
			String name = info.name;
			module.setName(name);
			
			String configClassName = info.configClassName;
			Class configCls;
			try {
				configCls = loader.findClass(configClassName);
			} catch (Exception exc) {
				configCls = null;
			}
			module.setConfigClass(configCls);
			
			String[] protocols = info.protocols;
			module.setProtocols(protocols);
			
			Requirement[] requirements = info.requirements;
			module.setRequirements(requirements);
			
			module.setSource(url);
			
			String id = module.getId();
			if (!isLoaded(id)) {
				table.put(id, module);
				fireLoaded(module);
				return module;
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
	 * Loads a one-class-module. The version is simply 0.0 and there are no 
	 * requirements. The method also fires the 	 
	 * {@link IModuleLoaderListener#loaded(ModuleContainer)} event.
	 * @param moduleClassName The classname: <code>my.package.Class</code>
	 * @return A <code>ModuleContainer</code> for the module.
	 * @throws ModuleLoaderException If anything fails.
	 */
	public static ModuleContainer load(String moduleClassName) 
	throws ModuleLoaderException {
		try {
			float version = 0.0f;
			Requirement[] requirements = new Requirement[0];
			
			Class cls = Class.forName(moduleClassName);
			ModuleContainer module = new ModuleContainer(cls, version);
			module.setRequirements(requirements);
			module.setSource(moduleClassName);
			String id = module.getId();
			if (!isLoaded(id)) {
				table.put(id, module);
				fireLoaded(module);
				return module;
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