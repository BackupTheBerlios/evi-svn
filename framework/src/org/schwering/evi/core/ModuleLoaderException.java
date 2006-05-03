package org.schwering.evi.core;

/**
 * Module loader exception.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ModuleLoaderException extends ModuleException {
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
