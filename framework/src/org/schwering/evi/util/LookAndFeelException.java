package org.schwering.evi.util;

/**
 * Base module exception.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class LookAndFeelException extends Exception {
	/**
	 * A plain module exception.
	 */
	public LookAndFeelException() {
	}
	
	/**
	 * A plain module exception.
	 * @param exc The caught exception.
	 */
	public LookAndFeelException(Exception exc) {
		initCause(exc);
	}
	
	/**
	 * A module exception with message.
	 * @param msg The error message.
	 */
	public LookAndFeelException(String msg) {
		super(msg);
	}

	/**
	 * A module exception with message.
	 * @param msg The error message.
	 * @param exc The caught exception.
	 */
	public LookAndFeelException(String msg, Exception exc) {
		super(msg);
		initCause(exc);
	}
}
