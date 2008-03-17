/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 * A simple HTML frame with hyperlinks and simple history.
 * The previous website can be obtained by pressing 'b'.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class HTMLPane extends JEditorPane 
implements HyperlinkListener, KeyListener {
	private static final long serialVersionUID = 7455924935046624890L;
	private Vector<URL> previousURLs = new Vector<URL>();
	private Vector<URL> nextURLs = new Vector<URL>();
	private Vector<IHTMLPaneListener> listeners = new Vector<IHTMLPaneListener>(2);

	/**
	 * Creates a new empty HTML frame.
	 */
	public HTMLPane() {
		super();
		setEditable(false);
		setContentType("text/html"); //$NON-NLS-1$
		addHyperlinkListener(this);
		addKeyListener(this);
		RightClickMenu.addRightClickMenu(this);
	}
	
	/**
	 * Creates a new HTML frame with the content of a resource.
	 * @param resource The resource which should be displayed. This means it must be 
	 * contained in the JAR which also contains the <code>HTMLPane</code> (i.e. not in 
	 * a seperate module).
	 */
	public HTMLPane(String resource) {
		this(resourceToURL(resource));
	}
	
	/**
	 * Creates a new HTML frame with the content of a resource.
	 * @param cls The class whose classloader is used to access the resource.
	 * @param resource The resource which should be displayed. This means it must be 
	 * contained in the JAR which <code>cls</code>.
	 */
	public HTMLPane(Class<?> cls, String resource) {
		this(cls.getClassLoader().getResource(resource));
	}
	
	/**
	 * Creates a new HTML frame which displays the content behind the given URL.
	 * @param url The URL whose content should be displayed.
	 */
	public HTMLPane(URL url) {
		this();
		setPage(url);
	}
	
	/**
	 * Adds a new listener.
	 * @param listener The new listener.
	 */
	public void addListener(IHTMLPaneListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener.
	 * @param listener The listener.
	 */
	public void removeListener(IHTMLPaneListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Fires the <code>addressChanged</code> event.
	 * @param url The new address.
	 * @see IHTMLPaneListener#addressChanged(URL)
	 */
	private void fireAddressChanged(URL url) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((IHTMLPaneListener)listeners.get(i)).addressChanged(url);
		}
	}
	
	/**
	 * Fires the <code>loading</code> event.
	 * @param url The new address.
	 * @see IHTMLPaneListener#loading(URL)
	 */
	private void fireLoading(URL url) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((IHTMLPaneListener)listeners.get(i)).loading(url);
		}
	}
	
	/**
	 * Fires the <code>loaded</code> event.
	 * @param url The new address.
	 * @see IHTMLPaneListener#loaded(URL)
	 */
	private void fireLoaded(URL url) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((IHTMLPaneListener)listeners.get(i)).loaded(url);
		}
	}
	
	/**
	 * Follows links when clicked.
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			JEditorPane pane = (JEditorPane)e.getSource();
			if (e instanceof HTMLFrameHyperlinkEvent) {
				URL url = e.getURL();
				HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent)e;
				HTMLDocument doc = (HTMLDocument)pane.getDocument();
				fireLoading(url);
				doc.processHTMLFrameHyperlinkEvent(evt);
				fireLoaded(url);
			} else {
				URL url = e.getURL();
				goTo(url);
			}
		}
	}
	
	/**
	 * Loads the previous site in the history.
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'b' || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			goToPrevious();
		}
	}

	/**
	 * Does nothing.
	 * @see #keyPressed(KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Does nothing.
	 * @see #keyPressed(KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}
	
	/**
	 * Returns the previously called URLs.
	 * @return The "previous" history.
	 */
	public URL[] getPreviousURLs() {
		URL[] previous = new URL[previousURLs.size()];
		previousURLs.toArray(previous);
		return previous;
	}
	
	/**
	 * Goes the the previous page in history.
	 */
	public void goToPrevious() {
		try {
			int index = previousURLs.size() - 1;
			if (index >= 0) {
				URL prev = (URL)previousURLs.get(index);
				previousURLs.remove(index);
				URL current = getPage();
				nextURLs.add(current);
				setPage(prev);
			}
		} catch (Exception exc) {
			showError(new Exception(Messages.getString("HTMLPane.COULD_NOT_LOAD_PREVIOUS_PAGE"))); //$NON-NLS-1$
		}
	}
	
	/**
	 * Returns the URLs called after the current one.
	 * @return The "next" history.
	 */
	public URL[] getNextURLs() {
		URL[] next = new URL[nextURLs.size()];
		nextURLs.toArray(next);
		return next;
	}
	
	/**
	 * Goes the the next page in history.
	 */
	public void goToNext() {
		try {
			int index = nextURLs.size() - 1;
			if (index >= 0) {
				URL current = getPage();
				previousURLs.add(current);
				URL next = (URL)nextURLs.get(index);
				nextURLs.remove(index);
				setPage(next);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			showError(new Exception(Messages.getString("HTMLPane.COULD_NOT_LOAD_PREVIOUS_PAGE"))); //$NON-NLS-1$
		}
	}
	
	/**
	 * Goes to the given url and sets the history respectively.
	 * @param url The new page.
	 * @see #setPage(URL)
	 */
	public void goTo(URL url) {
		URL old = getPage();
		if (old != null) {
			previousURLs.add(old);
			nextURLs.removeAllElements();
		}
		setPage(url);
	}
	
	/**
	 * Tries to load a page. The old page is NOT added to history in this method.
	 * @param url The new page's URL.
	 * @see #goTo(URL)
	 */
	public void setPage(final URL url) {
		if (url == null) {
			return;
		}
		Thread t = new Thread() {
			public void run() {
				setPageHelperMethod(url);
			}
		};
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * The helper method for {@link #setPage(URL)}. Needed due to access to 
	 * <code>super.setPage</code>.
	 * @param url The new page's URL.
	 */
	private void setPageHelperMethod(URL url) {
		fireLoading(url);
		try {
			super.setPage(url);
		} catch (Throwable t) {
			showError(t);
		}
		fireLoaded(url);
		fireAddressChanged(url);
	}
	
	/**
	 * Displays some HTML code with the error message.
	 * @param t The <code>Throwable</code> which was thrown.
	 */
	public void showError(Throwable t) {
		try {
			super.setPage(resourceToURL("htmlerror.html"));
			
// does not work -- why??
//			HTMLDocument doc = (HTMLDocument)getDocument();
//			Element msgElem = doc.getElement("message");
//			doc.setInnerHTML(msgElem, Messages.getString("HTMLPane.LOAD_ERROR"));
//			Element stacktraceElem = doc.getElement("stacktrace");
//			doc.setInnerHTML(stacktraceElem, Util.exceptionToString(t));
//			super.setDocument(doc);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		setCaretPosition(0);
	}
	
	/**
	 * Converts a resource that must be in the classpath that contains 
	 * this class (HTMLPane) to a URL.
	 * @param resource The resource name.
	 * @return A URL that points to the URL or <code>null</code>.
	 */
	private static URL resourceToURL(String resource) {
		return HTMLPane.class.getClassLoader().getResource(resource);
	}
}