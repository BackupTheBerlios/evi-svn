package org.schwering.evi.gui.main;

import org.schwering.evi.util.HTMLBrowser;

/**
 * A panel that displays help information.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class HelpPanel extends HTMLBrowser {
	private static final long serialVersionUID = -5630341486998996478L;

	/**
	 * Default title of the panel.
	 */
	public static final String DEFAULT_TITLE = Messages.getString("HelpPanel.DEFAULT_TITLE"); //$NON-NLS-1$
	
	private static final String HELP_HTML = Messages.getString("HelpPanel.HELP_HTML"); //$NON-NLS-1$
	
	/**
	 * Displays a panel with a help file.
	 */
	public HelpPanel() {
		super(HELP_HTML);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return DEFAULT_TITLE;
	}
}
