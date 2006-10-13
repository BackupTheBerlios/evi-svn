/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.util.EventListener;

/**
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IInputListener extends EventListener {
	public void inputFired(String text);
}
