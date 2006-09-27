/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.net.URL;

/**
 * Listens for address events in a HTMLPane.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IHTMLPaneListener {
	/**
	 * Fired when the main address changed.
	 * @param url The new address.
	 */
	public void addressChanged(URL url);
	
	/**
	 * Fired when an address is loaded.
	 * @param url The address that's being loaded.
	 */
	public void loading(URL url);

	/**
	 * Fired when the address in the HTMLPane is changed.
	 * @param url The address that has been loaded.
	 */
	public void loaded(URL url);
}
