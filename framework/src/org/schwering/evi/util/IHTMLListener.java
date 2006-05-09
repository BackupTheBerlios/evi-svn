package org.schwering.evi.util;

import java.net.URL;

/**
 * Listens for HTML events.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public interface IHTMLListener {
	/**
	 * Fired when a new website is loaded. Currently, this event is fired 
	 * when either a frame's or the whole window's content changes. 
	 * @param newSite The new site.
	 */
	public void siteChanged(URL newSite);
}
