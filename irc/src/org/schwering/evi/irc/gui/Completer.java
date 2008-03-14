/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

/**
 * Interface for tab-completer.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface Completer {
	String complete(String str);
}
