/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.core;

/**
 * Module instantiation exception.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ModuleInstantiationException extends ModuleException {
	private static final long serialVersionUID = 6591925348130173100L;

	/**
	 * A plain module instantiation exception.
	 */
	public ModuleInstantiationException() {
	}
	
	/**
	 * A plain module instantiation exception.
	 * @param exc The caught exception.
	 */
	public ModuleInstantiationException(Throwable exc) {
		super(exc);
	}
	
	/**
	 * A module instantiation exception with message.
	 * @param msg The error message.
	 */
	public ModuleInstantiationException(String msg) {
		super(msg);
	}
	
	/**
	 * A module instantiation exception with message.
	 * @param msg The error message.
	 * @param exc The caught exception.
	 */
	public ModuleInstantiationException(String msg, Throwable exc) {
		super(msg, exc);
	}
}
