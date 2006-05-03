package org.schwering.evi.gui.main;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.schwering.evi.core.IPanel;
import org.schwering.evi.util.HTMLBrowser;

/**
 * A panel with a textfield containing the license.
 * @author Christoph Schwering (schwering@gmail.com)
 */
public class AboutPanel extends JPanel implements IPanel {
	/**
	 * Default title of the panel.
	 */
	public static final String DEFAULT_TITLE = "About";
	
	private static final String ABOUT_HTML = "about.html";
	
	/**
	 * Displays a panel with a short description.
	 */
	public AboutPanel() {
		super(new GridLayout(1, 1));
		add(new HTMLBrowser(ABOUT_HTML));
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getIcon()
	 */
	public Icon getIcon() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getPanelInstance()
	 */
	public Component getPanelInstance() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return DEFAULT_TITLE;
	}
}
