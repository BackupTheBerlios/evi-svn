package org.schwering.evi.core;

import java.net.URL;
import java.util.HashSet;
import java.util.Vector;

/**
 * Provides access to a concrete module.<br />
 * This class administers the module's main class, its id, its version number,
 * the module's requirements and, more importantly, also all current instances
 * of the module.<br />
 * <br />
 * The most interesting methods for module developers are the following:
 * <ul>
 * <li> {@link ModuleLoader#getLoadedModule(String)}
 * <li> {@link ModuleFactory#newInstance(ModuleContainer)}
 * <li> {@link ModuleFactory#disposeInstance(IModule)}
 * <li> {@link #getInstances()}
 * <li> {@link #addListener(IModuleListener)}
 * </ul>
 * @see ModuleFactory
 * @see IModuleListener
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public final class ModuleContainer {
	private Class cls;
	private float version;
	private String name;
	private String[] protocols;
	private Requirement[] reqs = new Requirement[0];
	private URL infoURL;
	private Object source;
	
	private HashSet instances = new HashSet(5);
	private Vector listeners = new Vector(2);
	
	/**
	 * Creates a new instance of a module container.
	 * @param moduleClass The main class object of the module.
	 * @param moduleVersion The module's version (default: 0.0).
	 * @throws ModuleLoaderException If the class is no module.
	 */
	ModuleContainer(Class moduleClass, float moduleVersion) 
	throws ModuleLoaderException {
		if (!isModule(moduleClass)) {
			throw new ModuleLoaderException(moduleClass +" is no module");
		}
		cls = moduleClass;
		version = moduleVersion;
	}
	
	/**
	 * Registers a new instance of a module. <br />
	 * Invoked by {@link ModuleFactory#newInstance(ModuleContainer)} 
	 * and 
	 * {@link ModuleFactory#newInstance(ModuleContainer, Object[])}.
	 * @param o The new module.
	 * @see #unregisterInstance(IModule)
	 */
	void registerInstance(IModule o) {
		instances.add(o);
	}
	
	/**
	 * Removes an instance.<br />
	 * Invoked by {@link ModuleFactory#disposeInstance(IModule)}.
	 * @param o A reference to the instance.
	 * @see #registerInstance(IModule)
	 */
	boolean unregisterInstance(IModule o) {
		return (o != null) ? instances.remove(o) : false;
	}
	
	/**
	 * Fires the {@link IModuleListener#instantiated(IModule)} event for all 
	 * listeners.
	 * @param m The newly created instance.
	 */
	void fireInstantiated(IModule m) {
		int len = listeners.size();
		for (int i = 0; i < len; i++) {
			((IModuleListener)listeners.get(i)).instantiated(m);
		}
	}
	
	/**
	 * Fires the {@link IModuleListener#disposed(IModule)} event for all 
	 * listeners.
	 * @param m The instance which is disposed.
	 */
	void fireDisposed(IModule m) {
		int len = listeners.size();
		for (int i = 0; i < len; i++) {
			((IModuleListener)listeners.get(i)).disposed(m);
		}
	}

	/**
	 * Returns an array of all instances of this module.
	 * @return An array that contains all instances.
	 */
	public IModule[] getInstances() {
		IModule[] arr = new IModule[instances.size()];
		arr = (IModule[])instances.toArray(arr);
		return arr;
	}
	
	/**
	 * Adds a <code>IModuleListener</code> for this module.
	 * @param listener The new listener.
	 */
	public void addModuleListener(IModuleListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a <code>IModuleListener</code> from this module.
	 * @param listener The listener.
	 * @return <code>true</code> if successfully removed.
	 */
	public boolean removeModuleListener(IModuleListener listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Returns the id.
	 * @return The id.
	 * @see ModuleLoader#ATTR_MODULE_CLASS
	 */
	public String getId() {
		return getIdByClass(cls);
	}
	
	/**
	 * Returns the id of a class. This is typically simply its name (e.g.
	 * org.schwering.evi.core.ModuleContainer).
	 * @return The class's id.
	 */
	public static String getIdByClass(Class cls) {
		return cls.getName();
	}
	
	/**
	 * Returns the <code>Class</code> of the module. 
	 * Do not mix up with <code>getClass</code> which is inherited from 
	 * <code>Object</code>.
	 * @return The main module class.
	 * @see ModuleLoader#ATTR_MODULE_CLASS
	 */
	Class getModuleClass() {
		return cls;
	}
	
	/**
	 * Returns the version.
	 * @return The version.
	 * @see ModuleLoader#ATTR_MODULE_VERSION
	 */
	public float getVersion() {
		return version;
	}
	
	/**
	 * Initializes the module's name.
	 * @param name The module's name.
	 * @see ModuleLoader#ATTR_MODULE_NAME
	 */
	void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the module. If no name was set (a name is set if a 
	 * <code>ModuleName</code> attribute exists in the manifest), the module's
	 * id is returned.
	 * @return The name or, if no name is set, the id.
	 * @see ModuleLoader#ATTR_MODULE_NAME
	 */
	public String getName() {
		return (name != null) ? name : getId();
	}
	
	/**
	 * Sets the protocols handled by this module.
	 */
	void setProtocols(String[] protocols) {
		this.protocols = protocols;
	}
	
	/**
	 * Checks whether the module is able to handle the protocol. 
	 * @param protocol The protocol which is to check.
	 * @return <code>true</code> if <code>protocol</code> is a registered 
	 * protocol for this module.
	 * @see ModuleLoader#ATTR_MODULE_PROTOCOLS
	 */
	public boolean handlesProtocol(String protocol) {
		if (protocol == null || protocols == null) {
			return false;
		}
		for (int i = 0; i < protocols.length; i++) {
			if (protocol.equalsIgnoreCase(protocols[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds an array of requirements to the module. Note that the array is 
	 * copied into this class.
	 * @param r The new requirements.
	 * @see ModuleLoader#ATTR_MODULE_REQUIREMENTS
	 */
	void setRequirements(Requirement[] r) {
		if (r != null) {
			reqs = new Requirement[r.length];
			System.arraycopy(r, 0, reqs, 0, r.length);
		}
	}
	
	/**
	 * Returns the current requirements. If there are no requirements, the 
	 * returned array has length 0.
	 * @return The current requirements. 
	 * @see ModuleLoader#ATTR_MODULE_REQUIREMENTS
	 */
	public Requirement[] getRequirements() {
		return reqs;
	}
	
	/**
	 * Sets the information file. This string should point to a resource 
	 * that's a HTML file.
	 * @param resourceName The HTML resource.
	 */
	void setInfoURL(URL infoURL) {
		this.infoURL = infoURL;
	}
	
	/**
	 * Returns the information resource or <code>null</code>. The information
	 * resource should be a HTML file that contains some describing 
	 * information.
	 * @return A URL that points to the information resource HTML file. 
	 */
	public URL getInfoURL() {
		return infoURL;
	}
	
	/**
	 * Sets the source, which is either a URL or a classname.
	 * @param source The new source.
	 */
	void setSource(Object source) {
		this.source = source;
	}
	
	/**
	 * Returns the source, which is either a URL or a classname.
	 * @return The source.
	 */
	public Object getSource() {
		return source;
	}
	
	/**
	 * Returns the id.
	 * @return The id.
	 * @see ModuleLoader#ATTR_MODULE_CLASS
	 */
	public String toString() {
		return getId();
	}
	
	/**
	 * Returns <code>true</code> if the module implements <code>IModule</code>.
	 * @return <code>true</code> if the module implements <code>IModule</code>.
	 */
	private static boolean isModule(Class c) {
		return classImplements(c, IModule.class);
	}
	
	/**
	 * Returns <code>true</code> if the module implements <code>IPanel</code>.
	 * @return <code>true</code> if the module implements <code>IPanel</code>.
	 */
	public boolean isPanel() {
		return classImplements(cls, IPanel.class);
	}
	
	/**
	 * Returns <code>true</code> if the module implements <code>IApplet</code>.
	 * @return <code>true</code> if the module implements <code>IApplet</code>.
	 */
	public boolean isApplet() {
		return classImplements(cls, IApplet.class);
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>IButtonable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>IButtonable</code>.
	 * @see ModuleConfigurationInvoker
	 */
	public boolean isButtonable() {
		return classImplements(cls, IButtonable.class);
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>IConfigurableable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>IConfigurableable</code>.
	 */
	public boolean isConfigurable() {
		return classImplements(cls, IConfigurable.class);
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>IMenuable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>IMenuable</code>.
	 * @see ModuleMenuInvoker
	 */
	public boolean isMenuable() {
		return classImplements(cls, IMenuable.class);
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>IDefaultMenuable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>IDefaultMenuable</code>.
	 * @see ModuleMenuInvoker
	 */
	public boolean isDefaultMenuable() {
		return classImplements(cls, IDefaultMenuable.class);
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>ICustomMenuable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>ICustomMenuable</code>.
	 * @see ModuleMenuInvoker
	 */
	public boolean isCustomMenuable() {
		return classImplements(cls, ICustomMenuable.class);
	}
	
	/**
	 * Indicates whether a given class implements a given interface.
	 * @param c The class.
	 * @param i The interface.
	 * @return <code>true</code> if c implements i. Otherwise 
	 * <code>false</code>.
	 */
	private static boolean classImplements(Class c, Class i) {
		if (c == null || i == null) {
			return false;
		}
		return i.isAssignableFrom(c);
	}
}
