/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import org.schwering.evi.gui.main.MainFrame;
import org.schwering.evi.util.HTMLBrowser;
import org.schwering.evi.util.TextPane;

/**
 * A PinButton that opens a org.schwering.evi.util.HTMLBrowser.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class URLPinButton extends PinButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6239775499107091525L;

	public URLPinButton(TextPane tp, String urlString) throws MalformedURLException {
		super(tp);
		if (!urlString.startsWith("http"))
			urlString = "http://"+ urlString;
		final URL url = new URL(urlString);
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HTMLBrowser browser = new HTMLBrowser(url) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 4578996987470393590L;

					public String getTitle() {
						return url.toString();
					}
				};
				MainFrame.getInstance().getMainTabBar().addTab(browser);
			}
		});
	}
}
