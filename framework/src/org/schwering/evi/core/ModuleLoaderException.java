package org.schwering.evi.core;

/**
 * Module loader exception.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ModuleLoaderException extends ModuleException {
	private static final long serialVersionUID = -455343856994123351L;

	/**
	 * A plain module loader exception.
	 */
	public ModuleLoaderException() {
	}
	
	/**
	 * A plain module loader exception.
	 * @param exc The caught exception.
	 */
	public ModuleLoaderException(Exception exc) {
		super(exc);
	}
	
	/**
	 * A module loader exception with message.
	 * @param msg The error message.
	 */
	public ModuleLoaderException(String msg) {
		super(msg);
	}
	
	/**
	 * A module loader exception with message.
	 * @param msg The error message.
	 * @param exc The caught exception.
	 */
	public ModuleLoaderException(String msg, Exception exc) {
		super(msg, exc);
	}
}
