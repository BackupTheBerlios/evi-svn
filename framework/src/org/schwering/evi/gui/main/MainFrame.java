package org.schwering.evi.gui.main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.gui.EVI;
import org.schwering.evi.util.Util;

/**
 * The main frame of the GUI.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 7791599223725729008L;
	
	/**
	 * Frame icon's resource name/path.
	 */
	public static final String ICON_ATTRIBUTE = "evi.ico";

	/**
	 * Gives access to the one and only instance of the mainframe.
	 * With {@link #getInstance()} this provides simple access to the owning 
	 * mainframe.
	 */
	private static MainFrame instance = null;
	
	private Container pane = getContentPane();
	private ToolBar toolBar;
	private MenuBar menuBar;
	private TabBar tabBar;
	
	/**
	 * Creates one initial instance and returns a reference to it in all 
	 * subsequent calls.
	 * @return The one and only instance.
	 */
	public static MainFrame getInstance() {
		if (instance == null) {
			instance = new MainFrame();
		}
		return instance;
	}
	
	/**
	 * Creates a new main frame with the specified components. 
	 * The toolbar, menubar and tabbar are initialized with default values 
	 * (which should be ok for everybody).
	 */
	private MainFrame() {
		super(EVI.TITLE);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {
			}
			public void windowClosed(WindowEvent arg0) {
			}
			public void windowClosing(WindowEvent arg0) {
				Util.askToExit();
			}
			public void windowDeactivated(WindowEvent arg0) {
			}
			public void windowDeiconified(WindowEvent arg0) {
			}
			public void windowIconified(WindowEvent arg0) {
			}
			public void windowOpened(WindowEvent arg0) {
			}
		});
		pane.setLayout(new BorderLayout());
		addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent arg0) {
			}
			public void componentMoved(ComponentEvent arg0) {
				Point p = new Point(getX(), getY()); 
				MainConfiguration.setPoint("gui.topleft", p);
			}
			public void componentResized(ComponentEvent arg0) {
				Dimension d = getSize();
				MainConfiguration.setDimension("gui.size", d);
			}
			public void componentShown(ComponentEvent arg0) {
			}
		});
		setMainMenuBar(new MenuBar(this));
		setMainToolBar(new ToolBar());
		setMainTabBar(new TabBar());
		loadIcon();
	}
	
	/**
	 * Sets a new menubar.
	 * @param menuBar The menubar.
	 */
	public void setMainMenuBar(MenuBar menuBar) {
		this.menuBar = menuBar;
		super.setJMenuBar(menuBar);
	}
	
	/**
	 * Returns the menubar.
	 * @return The menubar.
	 */
	public MenuBar getMainMenuBar() {
		return menuBar;
	}
	
	/**
	 * Sets a new toolbar.
	 * @param toolBar The new toolbar.
	 */
	public void setMainToolBar(ToolBar toolBar) {
		if (this.toolBar != null) {
			remove(toolBar);
		}
		this.toolBar = toolBar;
		getContentPane().add(toolBar, BorderLayout.NORTH);
	}
	
	/**
	 * Returns the toolbar.
	 * @return The toolbar.
	 */
	public ToolBar getMainToolBar() {
		return toolBar;
	}
	
	/**
	 * Sets a new tabbar.
	 * @param tabBar The tabbar.
	 */
	public void setMainTabBar(TabBar tabBar) {
		if (this.tabBar != null) {
			remove(tabBar);
		}
		this.tabBar = tabBar;
		getContentPane().add(tabBar, BorderLayout.CENTER);
	}
	
	/**
	 * Returns the tabbar.
	 * @return The tabbar.
	 */
	public TabBar getMainTabBar() {
		return tabBar;
	}
	
	/**
	 * Loads and sets the image icon. The icon is searched in the *.JAR file 
	 * as <code>/ros.ico</code>.
	 */
	private void loadIcon() {
		try {
			ClassLoader cl = getClass().getClassLoader();
			InputStream is = cl.getResourceAsStream(ICON_ATTRIBUTE);
			byte[] iconBytes = new byte[is.available()];
			is.read(iconBytes);
			is.close();
			setIconImage(new ImageIcon(iconBytes).getImage());
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
