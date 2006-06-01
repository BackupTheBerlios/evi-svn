package org.schwering.evi.util;

import java.net.URL;

/**
 * Listens for address events in a HTMLPanel.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IHTMLPanelListener {
	/**
	 * Fired when the address in the HTMLPane is changed.
	 * @param url The new address.
	 */
	public void addressChanged(URL url);
}
