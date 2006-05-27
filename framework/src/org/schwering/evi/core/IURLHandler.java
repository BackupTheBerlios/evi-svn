package org.schwering.evi.core;

import java.net.URL;

/**
 * ModuleInfoClass interface for URL-handling-capable modules.<br>
 * <br>
 * Modules that know how to handle specific URLs should implement this 
 * interface.<br>
 * <br>
 * For example, it makes sense that a webbrowser-module identifies 
 * itself as URL handler for the "http" protocol. This enables EVI to start 
 * the webbrowser-module when it finds http-URLs in the command line 
 * arguments.
 * @see ModuleLoader
 * @see ModuleContainer
 * @see ModuleFactory
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IURLHandler extends IModuleInfo {
	/**
	 * Must create a new instance of the ModuleClass.<br>
	 * <br>
	 * This constructor will be invoked when EVI finds a URL with a protocol 
	 * listed in <code>getProtocols</code> in its command line arguments. EVI 
	 * will pass the respective URL to this module using this 
	 * <code>newInstance</code> method.
	 * <br>
	 * <b>Note:</b> Never instantiate modules directly via their constructor(s) or 
	 * via <code>IModuleInfo.newInstance</code>! <i>Instead</i> it is 
	 * <i>highly</i> recommended to use the {@link ModuleFactory} class to 
	 * <b>create and dispose module instances</b>.<br>
	 * @see ModuleFactory#newInstance(ModuleContainer)
	 * @param url The URL that should be handled.
	 * @return The newly created module instance as <code>IModule</code>.
	 */
	public IModule newInstance(URL url);
	
	/**
	 * Should return an array of protocols. For example, a webbrowser-mdoule 
	 * might return <code>new String[] { "http", https" }</code> to notify 
	 * EVI that it is able to deal with the HTTP and HTTPS protocols.<br>
	 * <br>
	 * The returned protocol-identifiers need to be the same like the 
	 * protocol-part in a URL (the part before the "://").
	 * @return An array containing the URLs this method can handle.
	 */
	public String[] getProtocols();
}
