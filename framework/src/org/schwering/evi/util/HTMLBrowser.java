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

import org.schwering.evi.core.IPanel;
import org.schwering.evi.gui.EVI;
import org.schwering.evi.gui.main.MainFrame;
import org.schwering.evi.gui.main.TabBar;

/**
 * A <code>HTMLPane</code> with a 'Home' and 'Back' button.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class HTMLBrowser extends JPanel implements IPanel, IHTMLListener {
	private static final long serialVersionUID = 4347796608541318947L;
	private URL startURL;
	private HTMLPane htmlPane;
	protected String default_html_title = "HTML-Browser";
	
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

		htmlPane = new HTMLPane(url);
		htmlPane.addListener(this);
		
		JButton home = new JButton("Home");
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				htmlPane.setPage(startURL);
			}
		});
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				htmlPane.goBack();
			}
		});
		
		JPanel top = new JPanel(new BorderLayout());
		top.add(home, BorderLayout.EAST);	
		top.add(back, BorderLayout.WEST);
		add(top, BorderLayout.NORTH);
		add(new JScrollPane(htmlPane), BorderLayout.CENTER);
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.util.IHTMLListener#siteChanged(java.net.URL)
	 */
	public void siteChanged(URL url) {
		EVI evi = EVI.getInstance();
		if (evi == null) {
			return;
		}
		MainFrame mf = evi.getMainFrame();
		if (mf == null) {
			return;
		}
		TabBar tabBar = mf.getMainTabBar();
		if (tabBar == null) {
			return;
		}
		tabBar.setTitle(this, getTitle());
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
		String doctitle = htmlPane.getTitle();
		if (doctitle != null) {
			return doctitle;
		} else if (default_html_title != null) {
			return default_html_title;
		} else {
			return "HTML-Browser";
		}
	}
}
