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
	private Vector history = new Vector();
	
	/**
	 * Creates a new HTML frame with the content of a resource.
	 * @param resource The resource which should be displayed. This means it must be 
	 * contained in the JAR which also contains the <code>HTMLPane</code> (i.e. not in 
	 * a seperate module).
	 */
	public HTMLPane(String resource) {
		this(HTMLPane.class.getClassLoader().getResource(resource));
	}
	
	/**
	 * Creates a new HTML frame with the content of a resource.
	 * @param cls The class whose classloader is used to access the resource.
	 * @param resource The resource which should be displayed. This means it must be 
	 * contained in the JAR which <code>cls</code>.
	 */
	public HTMLPane(Class cls, String resource) {
		this(cls.getClassLoader().getResource(resource));
	}
	
	/**
	 * Creates a new HTML frame which displays the content behind the given URL.
	 * @param url The URL whose content should be displayed.
	 */
	public HTMLPane(URL url) {
		super();
		setEditable(false);
		setContentType("text/html");
		addHyperlinkListener(this);
		addKeyListener(this);
		RightClickMenu.addRightClickMenu(this);
		setPage(url);
	}
	
	
	/**
	 * Follows links when clicked.
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			JEditorPane pane = (JEditorPane) e.getSource();
			if (e instanceof HTMLFrameHyperlinkEvent) {
				HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
				HTMLDocument doc = (HTMLDocument)pane.getDocument();
				doc.processHTMLFrameHyperlinkEvent(evt);
			} else {
				goTo(e.getURL());
			}
		}
	}
	
	/**
	 * Loads the previous site in the history.
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'b' || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			goBack();
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
	 * Goes the the previous page in history.
	 */
	public void goBack() {
		try {
			int index = history.size() - 1;
			if (index >= 0) {
				URL prev = (URL)history.get(index);
				history.remove(index);
				setPage(prev);
			}
		} catch (Exception exc) {
			showError(new Exception("Could not load previous page!"));
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
			history.add(old);
		}
		setPage(url);
	}
	
	/**
	 * Tries to load a page. The old page is NOT added to history.
	 * @param url The new page's URL.
	 * @see #goTo(URL)
	 */
	public void setPage(URL url) {
		try {
			super.setPage(url);
			setCaretPosition(0);
		} catch (Throwable t) {
			showError(t);
		}
	}

	/**
	 * Displays some HTML code with the error message.
	 * @param t The <code>Throwable</code> which was thrown.
	 */
	public void showError(Throwable t) {
		setText("<html>"+ 
				"<body>"+
				"<h1>An error occured while loading the page</h1>"+
				"<code>"+
				Util.exceptionToString(t)+
				"</code>"+
				"</body>"+
				"</html>");
		setCaretPosition(0);
	}
}