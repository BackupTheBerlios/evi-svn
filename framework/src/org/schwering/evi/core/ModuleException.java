package org.schwering.evi.core;

/**
 * Base module exception.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ModuleException extends Exception {
	private static final long serialVersionUID = -3441748133268186209L;

	/**
	 * A plain module exception.
	 */
	public ModuleException() {
	}
	
	/**
	 * A plain module exception.
	 * @param exc The caught exception.
	 */
	public ModuleException(Throwable exc) {
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
	public ModuleException(String msg, Throwable exc) {
		super(msg);
		initCause(exc);
	}
}
