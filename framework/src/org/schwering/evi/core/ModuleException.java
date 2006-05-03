package org.schwering.evi.core;

/**
 * Base module exception.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ModuleException extends Exception {
	/**
	 * A plain module exception.
	 */
	public ModuleException() {
	}
	
	/**
	 * A plain module exception.
	 * @param exc The caught exception.
	 */
	public ModuleException(Exception exc) {
		initCause(exc);
	}
	
	/**
	 * A module exception with message.
	 * @param msg The error message.
	 */
	public ModuleException(String msg) {
		super(msg);
	}

	/**
	 * A module exception with message.
	 * @param msg The error message.
	 * @param exc The caught exception.
	 */
	public ModuleException(String msg, Exception exc) {
		super(msg);
		initCause(exc);
	}
}
