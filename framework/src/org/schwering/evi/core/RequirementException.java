package org.schwering.evi.core;

/**
 * Module requirement exception.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class RequirementException extends Exception {
	/**
	 * A plain module requirement exception.
	 */
	public RequirementException() {
	}
	
	/**
	 * A plain module requirement exception.
	 * @param exc The caught exception.
	 */
	public RequirementException(Exception exc) {
		super(exc);
	}
	
	/**
	 * A module requirement exception with message.
	 * @param msg The error message.
	 */
	public RequirementException(String msg) {
		super(msg);
	}
	
	/**
	 * A module requirement exception with message.
	 * @param msg The error message.
	 * @param exc The caught exception.
	 */
	public RequirementException(String msg, Exception exc) {
		super(msg, exc);
	}
}
