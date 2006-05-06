package org.schwering.evi.gui.main;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.schwering.evi.core.IPanel;
import org.schwering.evi.util.HTMLBrowser;

/**
 * A GUI to setup the main application.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class HelloWorldPanel extends JPanel implements IPanel {
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
		super(new GridLayout(1, 1));
		add(new HTMLBrowser(HELLO_HTML));
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
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getIcon()
	 */
	public Icon getIcon() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
	}
	
	public boolean requestFocusInWindow() {
		boolean b1 = super.requestFocusInWindow();
		boolean b2 = true;
		return b1 && b2;
	}
}
