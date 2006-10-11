/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.core;

/**
 * Thrown when a format is not supported.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 * @see PlayerFactory#createPlayer(URL url)
 */
public class FormatNotSupportedException extends PlayerException {
	private static final long serialVersionUID = 1L;

	public FormatNotSupportedException() {
	}
	
	public FormatNotSupportedException(String msg) {
		super(msg);
	}
	
	public FormatNotSupportedException(Exception exc) {
		super(exc);
	}
	
	public FormatNotSupportedException(String msg, Exception exc) {
		super(msg, exc);
	}
}
