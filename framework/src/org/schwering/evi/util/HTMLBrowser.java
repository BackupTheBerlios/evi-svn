/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.schwering.evi.core.IPanel;
import org.schwering.evi.gui.main.MainFrame;

/**
 * A <code>HTMLPane</code> with a 'Home' and 'Back' button.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class HTMLBrowser extends JPanel implements IPanel, IHTMLPaneListener {
	private static final long serialVersionUID = 4347796608541318947L;
	private URL startURL;
	private JTextField addrField = new JTextField(); 
	private HTMLPane htmlPane = new HTMLPane();
	private JPanel statusPanel = new JPanel(new BorderLayout());
	private JProgressBar progressBar = new JProgressBar();
	private String title;
	JButton prevButton = new JButton(Messages.getString("HTMLBrowser.PREVIOUS")); //$NON-NLS-1$
	JButton nextButton = new JButton(Messages.getString("HTMLBrowser.NEXT")); //$NON-NLS-1$
	
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
		
		statusPanel.add(progressBar);
		
		JButton home = new JButton(Messages.getString("HTMLBrowser.HOME")); //$NON-NLS-1$
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				htmlPane.goTo(startURL);
			}
		});
		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				htmlPane.goToPrevious();
			}
		});
		prevButton.setEnabled(false);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				htmlPane.goToNext();
			}
		});
		nextButton.setEnabled(false);
		
		JPanel prevnextPanel = new JPanel();
		prevnextPanel.add(prevButton);
		prevnextPanel.add(nextButton);
		JPanel homePanel = new JPanel();
		homePanel.add(home);
		JPanel top = new JPanel(new BorderLayout());
		top.add(prevnextPanel, BorderLayout.WEST);
		top.add(addrField, BorderLayout.CENTER);
		top.add(homePanel, BorderLayout.EAST);
		add(top, BorderLayout.NORTH);
		add(new JScrollPane(htmlPane), BorderLayout.CENTER);
		add(statusPanel, BorderLayout.SOUTH);
	}

	/**
	 * Draws a progressbar.
	 * @param url The new displayed URL.
	 * @see #loaded(URL)
	 */
	public void loading(URL url) {
		progressBar.setIndeterminate(true);
		progressBar.setValue(0);
		progressBar.setString("Loading "+ url);
		progressBar.setStringPainted(true);
	}

	/**
	 * Hides the progessar.
	 * @param url The new displayed URL.
	 * @see #loading(URL)
	 */
	public void loaded(URL url) {
		progressBar.setIndeterminate(false);
		progressBar.setValue(0);
		progressBar.setStringPainted(false);
	}
	
	/**
	 * Updates the address field.
	 * @param url The new displayed URL.
	 */
	public void addressChanged(URL url) {
		addrField.setText(url.toString());
		prevButton.setEnabled(htmlPane.getPreviousURLs().length > 0);
		nextButton.setEnabled(htmlPane.getNextURLs().length > 0);
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
