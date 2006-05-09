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
	public static final String DEFAULT_TITLE = "Hello!";
	
	private static final String HELLO_HTML = "hello.html";
	
	/**
	 * Gives access to the one and only instance of the configuration panel.
	 * This avoids that the user might create a bunch of configuration panels
	 * which would make no sense.
	 */
	private static HelloWorldPanel instance = null;
	
	/**
	 * Creates one initial instance and returns it in future until 
	 * <code>dispose()</code> is invoked.
	 * @return The current instance.
	 */
	public static HelloWorldPanel getInstance() {
		if (instance == null) {
			instance = new HelloWorldPanel();
		}
		return instance;
	}
	
	/**
	 * Creates a new instance of the configuration GUI.
	 * @see #instance
	 */
	private HelloWorldPanel() {
		super(HELLO_HTML);
	}
}
