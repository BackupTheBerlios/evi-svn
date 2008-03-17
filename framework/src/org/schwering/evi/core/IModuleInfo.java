/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.core;

/**
 * ModuleInfoClass basis interface.<br>
 * <br>
 * The implementing class must define a constructor that takes no arguments 
 * (the implicit default constructor).
 * <br>
 * Each ModuleInfoClass must implement this interface. The ModuleInfoClass 
 * provides the basical information for each module such as its appearance 
 * name or its version.<br>
 * <br> 
 * Every module needs a ModuleInfoClass and a ModuleClass. The latter 
 * must implement {@link IModule}. It provides the real functionality of 
 * the module and is something like the main class of the module.<br>
 * <br>
 * A module developer will get in contact with this module when he writes 
 * his ModuleInfoClass. However, <code>IModuleInfo</code> objects do not 
 * appear publicly in a module's lifecycle later. The <code>IModuleInfo</code>
 * is wrapped by {@link ModuleContainer} completely.
 * @see ModuleLoader
 * @see ModuleContainer
 * @see ModuleFactory
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IModuleInfo {
	/**
	 * Must create a new instance of the ModuleClass.<br>
	 * <br>
	 * <b>Note:</b> Never instantiate modules directly via their constructor(s) or 
	 * via <code>IModuleInfo.newInstance</code>! <i>Instead</i> it is 
	 * <i>highly</i> recommended to use the {@link ModuleFactory} class to 
	 * <b>create and dispose module instances</b>.
	 * @see ModuleFactory#newInstance(ModuleContainer)
	 * @return The newly created module instance as <code>IModule</code>.
	 */
	public IModule newInstance();
	
	/**
	 * Must return the <code>Class</code> representation of the ModuleClass.
	 * @return The <code>Class</code> representation of the ModuleClass.
	 */
	public Class<? extends IModule> getModuleClass();
	
	/**
	 * Should return the appearance name of the module. If this method 
	 * returns <code>null</code>, the module's id is used as appearance 
	 * name instead.
	 * @return The appearance name of the module.
	 */
	public String getName();
	
	/**
	 * Should return the module's version. This is not very important as long 
	 * as no other module depends on this module and requires it in a 
	 * specific version. 
	 * @return The version as <code>float</code>, e.g. <code>1.0f</code>.
	 */
	public float getVersion();
	
	/**
	 * The returned URL should point to a HTML file with information about the 
	 * module. 
	 * <b>Tip:</b> The method might simply return "about.html" if this file 
	 * is added as resource to the module's JAR file!
	 * @return A URL as <code>String</code> that points to a information 
	 * file in HTML format.
	 */
	public String getInfoURL();
}
