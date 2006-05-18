package org.schwering.evi.core;

/**
 * Module invocation exception indicates that invoking a static method failed.
 * @see ModuleConfigurationInvoker
 * @see ModuleMenuInvoker
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id: ModuleInvocationException.java 77 2006-05-15 14:17:40Z schwering $
 */
public class ModuleInvocationException extends ModuleException {
	private static final long serialVersionUID = 2430360888617215944L;

	/**
	 * A plain module config panel instantiation exception.
	 */
	public ModuleInvocationException() {
	}
	
	/**
	 * A plain module instantiation exception.
	 * @param exc The caught exception.
	 */
	public ModuleInvocationException(Throwable exc) {
		super(exc);
	}
	
	/**
	 * A module config panel instantiation exception with message.
	 * @param msg The error message.
	 */
	public ModuleInvocationException(String msg) {
		super(msg);
	}
	
	/**
	 * A module config panel instantiation exception with message.
	 * @param msg The error message.
	 * @param exc The caught exception.
	 */
	public ModuleInvocationException(String msg, Throwable exc) {
		super(msg, exc);
	}
}
