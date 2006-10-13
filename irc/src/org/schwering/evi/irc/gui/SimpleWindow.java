/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Component;

/**
 * A simple window with chat field and input field.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class SimpleWindow extends AbstractWindow {

	public SimpleWindow() {
		super();
	}
	
	protected Component getHeaderComponent() {
		return null;
	}
	
	protected Component getTextComponent() {
		return text;
	}
}
