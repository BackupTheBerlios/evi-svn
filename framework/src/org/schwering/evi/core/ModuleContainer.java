package org.schwering.evi.core;

import java.net.URL;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JMenu;

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
	private IModuleInfo info;
	private Object source;
	
	private HashSet instances = new HashSet(5);
	private Vector listeners = new Vector(2);
	
	ModuleContainer(Class moduleClass) throws ModuleLoaderException {
		if (!isModule(moduleClass)) {
			throw new ModuleLoaderException(moduleClass +" is no module");
		}
		cls = moduleClass;
	}
	
	ModuleContainer(IModuleInfo moduleInfo) throws ModuleLoaderException {
		this(moduleInfo.getModuleClass());
		info = moduleInfo;
	}
	
	IModuleInfo getModuleInfo() {
		return info;
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
	public void addListener(IModuleListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a <code>IModuleListener</code> from this module.
	 * @param listener The listener.
	 * @return <code>true</code> if successfully removed.
	 */
	public boolean removeListener(IModuleListener listener) {
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
	 * @see IModuleInfo#getModuleClass()
	 */
	Class getModuleClass() {
		return cls;
	}
	
	/**
	 * Returns the version.
	 * @return The version.
	 * @see IModuleInfo#getVersion()
	 */
	public float getVersion() {
		return (info != null) ? info.getVersion() : 0.0f;
	}
	
	/**
	 * Returns the name of the module. If no name was set (a name is set if a 
	 * <code>ModuleName</code> attribute exists in the manifest), the module's
	 * id is returned.
	 * @return The name or, if no name is set, the id.
	 */
	public String getName() {
		if (info != null && info.getName() != null) {
			return info.getName();
		} else {
			return getId();
		}
	}
	
	/**
	 * Returns the information resource or <code>null</code>. The information
	 * resource should be a HTML file that contains some describing 
	 * information.
	 * @return A URL that points to the information resource HTML file. 
	 */
	public URL getInfoURL() {
		if (info != null && info.getInfoURL() != null 
				&& source instanceof URL) {
			try {
				return new URL((URL)source, info.getInfoURL());
			} catch (Exception exc) {
				return null;
			}
		} else {
			return null;
		}
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
	 * Returns <code>true</code> if the module implements <code>IApplet</code>.
	 * @return <code>true</code> if the module implements <code>IApplet</code>.
	 */
	public boolean isApplet() {
		return classImplements(cls, IApplet.class);
	}
	
	/**
	 * Returns <code>true</code> if the module implements <code>IPanel</code>.
	 * @return <code>true</code> if the module implements <code>IPanel</code>.
	 */
	public boolean isPanel() {
		return classImplements(cls, IPanel.class);
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>IButtonable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>IButtonable</code>.
	 * @see IButtonable
	 */
	public boolean isButtonable() {
		return info != null && info instanceof IButtonable
			&& ((IButtonable)info).isButtonable();
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>ICustomMenuable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>ICustomMenuable</code>.
	 * @see ICustomButtonable
	 */
	public boolean isCustomButtonable() {
		return isButtonable() && info instanceof ICustomButtonable;
	}
	
	/**
	 * Returns the custom button of the module or <code>null</code>.
	 * This method firstly checks whether this module is 
	 * custom-buttonable via <code>isCustomButtonable</code>. 
	 * If this returns <code>true</code>, the 
	 * <code>ICustomButtonable.getCustomButton</code> method is 
	 * invoked.
	 * @return The custom button or <code>null</code>.
	 */
	public JButton getCustomButton() {
		return (isCustomButtonable())
				? ((ICustomButtonable)info).getCustomButton()
				: null;
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>IConfigurable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>IConfigurable</code>.
	 * @see IConfigurable
	 */
	public boolean isConfigurable() {
		return info != null && info instanceof IConfigurable;
	}
	
	/**
	 * Returns the config panel of the module or <code>null</code>.
	 * This method firstly checks whether this module is 
	 * configurable via <code>isConfigurable</code>. 
	 * If this returns <code>true</code>, the 
	 * <code>IConfigurable.getConfigPanel</code> method is 
	 * invoked.
	 * @return The config panel or <code>null</code>.
	 */
	public IPanel getConfigPanel() {
		return (isConfigurable()) 
				? ((IConfigurable)info).getConfigPanel() 
				: null;
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>IDemanding</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>IDemanding</code>.
	 * @see IDemanding
	 */
	public boolean isDemanding() {
		return info != null && info instanceof IDemanding;
	}
	
	/**
	 * Returns the current requirements. If there are no requirements, the 
	 * returned array has length 0.
	 * @return The current requirements. 
	 * @see IModuleInfo#getRequirements()
	 */
	public Requirement[] getRequirements() {
		if (isDemanding()) {
			Requirement[] reqs = ((IDemanding)info).getRequirements();
			return (reqs != null) ? reqs : new Requirement[0];
		} else {
			return new Requirement[0];
		}
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>IMenuable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>IMenuable</code>.
	 * @see IMenuable
	 */
	public boolean isMenuable() {
		return info != null && info instanceof IMenuable
			&& ((IMenuable)info).isMenuable();
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>ICustomMenuable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>ICustomMenuable</code>.
	 * @see ICustomMenuable
	 */
	public boolean isCustomMenuable() {
		return isMenuable() && info instanceof ICustomMenuable;
	}
	
	/**
	 * Returns the custom menu of the module or <code>null</code>.
	 * This method firstly checks whether this module is 
	 * configurable via <code>isCustomMenuable</code>. 
	 * If this returns <code>true</code>, the 
	 * <code>ICustomMenuable.getCustomMenu</code> method is 
	 * invoked.
	 * @return The custom menu or <code>null</code>.
	 */
	public JMenu getCustomMenu() {
		return (isCustomMenuable())
				? ((ICustomMenuable)info).getCustomMenu()
				: null;
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>IParameterizable</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>IParameterizable</code>.
	 * @see IParameterizable
	 */
	public boolean isParameterizable() {
		return info != null && info instanceof IParameterizable;
	}
	
	/**
	 * Returns <code>true</code> if the module implements 
	 * <code>IURLHandler</code>.
	 * @return <code>true</code> if the module implements 
	 * <code>IURLHandler</code>.
	 * @see IURLHandler
	 */
	public boolean isURLHandler() {
		return info != null && info instanceof IURLHandler;
	}
	
	/**
	 * Checks whether the module is able to handle the protocol. 
	 * @param protocol The protocol which is to check.
	 * @return <code>true</code> if <code>protocol</code> is a registered 
	 * protocol for this module.
	 * @see IModuleInfo#getProtocols()
	 */
	public boolean handlesURL(String protocol) {
		if (!isURLHandler()) {
			return false;
		}
		String[] protocols = ((IURLHandler)info).getProtocols();
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
