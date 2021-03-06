/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.gui.main;

import org.schwering.evi.util.HTMLBrowser;

/**
 * A panel with a textfield containing the some information.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class AboutPanel extends HTMLBrowser {
	private static final long serialVersionUID = -5630341486998996478L;

	/**
	 * Default title of the panel.
	 */
	public static final String DEFAULT_TITLE = Messages.getString("AboutPanel.0"); //$NON-NLS-1$
	
	private static final String ABOUT_HTML = Messages.getString("AboutPanel.1"); //$NON-NLS-1$
	
	/**
	 * Displays a panel with a short description.
	 */
	public AboutPanel() {
		super(ABOUT_HTML);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return DEFAULT_TITLE;
	}
}
