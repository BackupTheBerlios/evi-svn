/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio;

/**
 * Indicates problems with the player (mostly decoding errors).
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class PlayerException extends Exception {
	public PlayerException() {
	}
	
	public PlayerException(Exception exc) {
		super(exc);
	}
}
