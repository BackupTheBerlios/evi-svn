package org.schwering.evi.gui.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleListener;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleFactory;
import org.schwering.evi.core.ModuleLoader;
import org.schwering.evi.util.ExceptionDialog;

/**
 * The main tabbar of the application.<br />
 * <br />
 * Though it is possible to add all kinds of <code>java.awt.Component</code>s
 * to the tabbar, one should add only <code>IPanel</code>s. There are two 
 * kinds of <code>IPanel</code>s: those that are instances of 
 * <code>IModule</code>s and those that are not. <b>Module instances are added 
 * and removed automaticall</b> by a <code>IModuleListener</code>. All other 
 * kind <code>IPanel</code>s should be added and removed via the methods 
 * {@link #addTab(IPanel)} and {@link #removeTab(IPanel)}.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class TabBar extends JTabbedPane implements IModuleListener {
	private RightClickMenu menu = new RightClickMenu();
	private MainFrame owner;
	private TabBar pointerToThisForListeners = this;
	private Hashtable table = new Hashtable();
	
	/**
	 * Creates a new tabbar.
	 * @param owner The owning main-frame.
	 */
	public TabBar(MainFrame owner) {
		this.owner = owner;
		setTabPlacement(MainConfiguration.getInt("gui.tabs.placement", TOP));
		
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 1 
						&& e.getButton() == MouseEvent.BUTTON3) {
					menu.show(pointerToThisForListeners, e.getX(), e.getY());
				}
			}
			public void mouseReleased(MouseEvent e) {
			}
		});
		
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				forwardFocusInWindow();
			}
		});
		
		ModuleContainer[] modules = ModuleLoader.getLoadedModules();
		for (int i = 0; i < modules.length; i++) {
			if (modules[i].isPanel()) {
				modules[i].addListener(this);
			}
		}
	}
	
	/**
	 * The event method fired when a module is instantiated. 
	 * If the module which is instantiated is an an instance of 
	 * <code>IPanel</code>, a respective tab is added.
	 * @param newInstance The newly created instance object.
	 */
	public void instantiated(IModule newInstance) {
		try {
			addTab((IPanel)newInstance);
		} catch (Exception exc) {
			ExceptionDialog.show("Unexpected error occured", exc);
		}
	}
	
	/**
	 * The event method fired when a module is disposed.
	 * If the disposing module is an instance of <code>IPanel</code>, 
	 * the respective tab is removed.
	 * @param disposedInstance The instance object which disposing.
	 */
	public void disposed(IModule disposedInstance) {
		try {
			remove((IPanel)disposedInstance);
		} catch (Exception exc) {
			ExceptionDialog.show("Unexpected error occured", exc);
		}
	}

	/**
	 * Adds a panel.<br />
	 * Do not use this method to add module instances! New module instances 
	 * are added automatically with a listener 
	 * ({@link #instantiated(IModule)}).
	 * @param panel The new panel.
	 */
	public void addTab(IPanel panel) {
		int index = getTabCount();
		String title = panel.getTitle();
		Icon icon = panel.getIcon();
		Component component = panel.getPanelInstance();
		
		table.put(component, panel);
		
		if (title == null || title.length() == 0) {
			title = "Untitled";
		}
		
		super.insertTab(title, icon, component, title, index);
		setSelectedComponent(component);
		forwardFocusInWindow();
	}
	
	/**
	 * Removes a panel.<br />
	 * Do not use this method to remove module instances! Module instances 
	 * are removed automatically with a listener 
	 * ({@link #instantiated(IModule)}).
	 * @param panel The panel that should be removed.
	 */
	public void remove(IPanel panel) {
		super.remove(panel.getPanelInstance());
	}
	
	/**
	 * Removes the selected panel from the tabbar.
	 * @see #removeTabAt(int)
	 */
	public void removeSelectedTab() {
		int index = getSelectedIndex();
		if (index < 0 || index >= getTabCount()) {
			return;
		}
		removeTabAt(index);
	}
	
	/**
	 * Removes a panel with this procedure:
	 * <ul>
	 * <li> If the component at the index is the panel of a module (e.g. if the
	 * corresponding object in the internal hashtable is instance of 
	 * <code>IModule</code>), the module is disposed with 
	 * <code>ModuleFactory.disposeInstance</code>. Then, the event
	 * <code>disposed</code> is thrown and this event method will remove the 
	 * concrete component from the tabbar. </li>
	 * <li> If the component at the index is the panel of an instance of 
	 * <code>IPanel</code>, the instance is disposed via
	 * <code>IPanel.dispose</code> and the tab is removed immediately. </li>
	 * <li> Otherwise, the component is simply removed immediately. </li>
	 * @param index The index of the panel on the tabbar.
	 */
	public void removeTabAt(int index) {
		System.out.println("removeTabAt()");
		if (index < 0 || index >= getTabCount()) {
			return;
		}
		
		Component c = getComponentAt(index);
		Object owner = table.remove(c);
		if (owner != null && owner instanceof IModule) {
 			System.out.println("ModuleFactory.disposeInstance(IModule)");
			ModuleFactory.disposeInstance((IModule)owner);
			// real removing done by listener (disposed())
		} else if (owner != null && owner instanceof IPanel) {
			System.out.println("IPanel.dispose()");
			((IPanel)owner).dispose();
			super.removeTabAt(index);
		} else {
			System.out.println("normal window");
			super.removeTabAt(index);
		}
	}
	
	/**
	 * Sets the color of the tab of the given component.
	 * @param tab The component whose tabs should be changed.
	 * @param color The new color.
	 */
	public void hightlightTab(Component tab, Color color) {
		int index = indexOfComponent(tab);
		if (index < 0 || index >= getTabCount()) {
			return;
		}
		setBackgroundAt(index, color);
	}
	
	/**
	 * Tries to catch the focus and passes it on to the selected panel.
	 * @return <code>false</code> if it definitely failed. <code>true</code> 
	 * does not guarantee anything.<br />
	 * @see Component#requestFocusInWindow()
	 */
	private boolean forwardFocusInWindow() {
		requestFocusInWindow();
		Component c = getSelectedComponent();
		if (c != null) {
			return c.requestFocusInWindow();
		} else {
			return false;
		}
	}
	
	/**
	 * A simply menu with just a "close" option.
	 */
	class RightClickMenu extends JPopupMenu {
		/**
		 * Creates a new menu with "close".
		 */
		public RightClickMenu() {
			JMenuItem close = new JMenuItem("Close");
			close.setMnemonic('C');
			close.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeSelectedTab();
				}
			});
			add(close);
		}
	}
}