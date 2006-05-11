package org.schwering.evi.core;

/**
 * Module config panel instantiation exception.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ModuleConfigInstantiationException extends ModuleException {
	private static final long serialVersionUID = 6591925348130173100L;

	/**
	 * A plain module config panel instantiation exception.
	 */
	public ModuleConfigInstantiationException() {
	}
	
	/**
	 * A plain module instantiation exception.
	 * @param exc The caught exception.
	 */
	public ModuleConfigInstantiationException(Exception exc) {
		super(exc);
	}
	
	/**
	 * A module config panel instantiation exception with message.
	 * @param msg The error message.
	 */
	public ModuleConfigInstantiationException(String msg) {
		super(msg);
	}
	
	/**
	 * A module config panel instantiation exception with message.
	 * @param msg The error message.
	 * @param exc The caught exception.
	 */
	public ModuleConfigInstantiationException(String msg, Exception exc) {
		super(msg, exc);
	}
}
