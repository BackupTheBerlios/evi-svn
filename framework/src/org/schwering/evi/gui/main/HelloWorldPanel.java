package org.schwering.evi.gui.main;

import org.schwering.evi.util.HTMLBrowser;

/**
 * A GUI to setup the main application.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class HelloWorldPanel extends HTMLBrowser {
	private static final long serialVersionUID = -1869059776657313783L;

	/**
	 * Default title of the panel.
	 */
	public static final String DEFAULT_TITLE = Messages.getString("HelloWorldPanel.0"); //$NON-NLS-1$
	
	private static final String HELLO_HTML = Messages.getString("HelloWorldPanel.1"); //$NON-NLS-1$
	
	/**
	 * Gives access to the one and only instance of the panel.
	 * This avoids that the user might create a bunch of panels
	 * which would make no sense.
	 */
	private static HelloWorldPanel instance = null;
	
	/**
	 * The number of times getInstance() was invoked. 
	 * Exactly this times dispose() will be invoked.
	 */
	private static int instanceCount = 0;
	
	/**
	 * Creates one initial instance and returns it in future until 
	 * <code>dispose()</code> is invoked.
	 * @return The current instance.
	 */
	public static HelloWorldPanel getInstance() {
		if (instance == null) {
			instance = new HelloWorldPanel();
		}
		instanceCount++;
		return instance;
	}
	
	/**
	 * Creates a new instance of the configuration GUI.
	 * @see #instance
	 */
	private HelloWorldPanel() {
		super(HELLO_HTML);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return DEFAULT_TITLE;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
		instanceCount--;
		if (instanceCount == 0) {
			instance = null;
			super.dispose();
		}
	}
}
