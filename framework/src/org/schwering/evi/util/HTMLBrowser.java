package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.schwering.evi.core.IPanel;
import org.schwering.evi.gui.main.MainFrame;

/**
 * A <code>HTMLPane</code> with a 'Home' and 'Back' button.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class HTMLBrowser extends JPanel implements IPanel, IHTMLPanelListener {
	private static final long serialVersionUID = 4347796608541318947L;
	private URL startURL;
	private JTextField addrField = new JTextField(); 
	private HTMLPane htmlPane = new HTMLPane();
	private String title;
	
	/**
	 * Creates a new empty HTMLBrowser
	 */
	public HTMLBrowser() {
		this((URL)null);
	}
	
	/**
	 * Creates a new HTMLBrowser.
	 * @param resource The resource which should be displayed. This means it must be 
	 * contained in the JAR which also contains the <code>HTMLPane</code> (i.e. not in 
	 * a seperate module).
	 */
	public HTMLBrowser(String resource) {
		this(HTMLBrowser.class.getClassLoader().getResource(resource));
	}
	
	/**
	 * Creates a new HTML frame with the content of a resource.
	 * @param cls The class whose classloader is used to access the resource.
	 * @param resource The resource which should be displayed. This means it must be 
	 * contained in the JAR which <code>cls</code>.
	 */
	public HTMLBrowser(Class cls, String resource) {
		this(cls.getClassLoader().getResource(resource));
	}
	
	/**
	 * Creates a new HTMLBrowser.
	 * @param url The URL whose content should be displayed.
	 */
	public HTMLBrowser(URL url) {
		super(new BorderLayout());
		startURL = url;
		
		RightClickMenu.addRightClickMenu(addrField);
		addrField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = addrField.getText();
				try {
					URL url = new URL(s);
					htmlPane.goTo(url);
				} catch (Exception exc) {
					// try it with a "http://" in front of the address
					try {
						if (s.indexOf("http://") == -1) {
							 s = "http://"+ s;
							 URL url = new URL(s);
							 htmlPane.goTo(url);
						}
					} catch (Exception exc2) {
					}
				}
			}
		});
		
		htmlPane.addListener(this);
		htmlPane.goTo(url);
		
		JButton home = new JButton(Messages.getString("HTMLBrowser.HOME")); //$NON-NLS-1$
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				htmlPane.goTo(startURL);
			}
		});
		JButton prev = new JButton(Messages.getString("HTMLBrowser.PREVIOUS")); //$NON-NLS-1$
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				htmlPane.goToPrevious();
			}
		});
		JButton next = new JButton(Messages.getString("HTMLBrowser.NEXT")); //$NON-NLS-1$
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				htmlPane.goToNext();
			}
		});
		
		JPanel top = new JPanel(new BorderLayout());
		top.add(home, BorderLayout.EAST);
		top.add(addrField, BorderLayout.CENTER);
		JPanel prevnext = new JPanel();
		prevnext.add(prev);
		prevnext.add(next);
		top.add(prevnext, BorderLayout.WEST);
		add(top, BorderLayout.NORTH);
		add(new JScrollPane(htmlPane), BorderLayout.CENTER);
	}

	/**
	 * Updates the address field.
	 * @param url The new displayed URL.
	 */
	public void addressChanged(URL url) {
		addrField.setText(url.toString());
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
	
	/**
	 * Sets a new title.
	 * @param title The new title.
	 */
	public void setTitle(String title) {
		this.title = title;
		MainFrame.getInstance().getMainTabBar().setTitle(this, title);
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return (title != null) ? title : Messages.getString("HTMLBrowser.DEFAULT_TITLE"); //$NON-NLS-1$
	}
}
