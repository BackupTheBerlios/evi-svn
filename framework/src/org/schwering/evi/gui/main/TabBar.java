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
import org.schwering.evi.core.IModuleLoaderListener;
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
 * {@link #addTab(IPanel)} and {@link #remove(IPanel)}.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class TabBar extends JTabbedPane 
implements IModuleListener, IModuleLoaderListener {
	private static final long serialVersionUID = 2781164155079468404L;
	
	private RightClickMenu menu = new RightClickMenu();
	private TabBar pointerToThisForListeners = this;
	
	/**
	 * The keys are java.awt.Components (returned by IPanel.getPanelInstance())
	 * and the values are the owning IModule objects.
	 */
	private Hashtable table = new Hashtable();
	
	/**
	 * Creates a new tabbar.
	 */
	public TabBar() {
		setTabPlacement(MainConfiguration.getInt("gui.tabs.placement", TOP)); //$NON-NLS-1$
		
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
		
		ModuleLoader.addListener(this);
		
		ModuleContainer[] modules = ModuleLoader.getLoadedModules();
		for (int i = 0; i < modules.length; i++) {
			if (modules[i].isPanel()) {
				modules[i].addListener(this);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleLoaderListener#loaded(org.schwering.evi.core.ModuleContainer)
	 */
	public void loaded(ModuleContainer loadedModule) {
		if (loadedModule.isPanel()) {
			loadedModule.addListener(this);
		}
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleLoaderListener#unloaded(org.schwering.evi.core.ModuleContainer)
	 */
	public void unloaded(ModuleContainer unloadedModule) {
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
			ExceptionDialog.show(Messages.getString("TabBar.UNEXPECTED_ERROR"), exc); //$NON-NLS-1$
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
			/*
			 * The following line tries to remove the module from the internal 
			 * table. Otherwise, the subsequent remove() call would resultin a 
			 * later removeTabAt() call where the module, which notice that the
			 * module is still registered and then invoke 
			 * ModuleLoader.disposeInstance() which would fire this disposed()
			 * event once again. This would result in a double invokation of 
			 * the disposed() event of all ModuleListeners.
			 */
			IPanel panel = (IPanel)disposedInstance;
			table.remove(panel.getPanelInstance());
			remove(panel);
		} catch (Exception exc) {
			ExceptionDialog.show(Messages.getString("TabBar.UNEXPECTED_ERROR"), exc); //$NON-NLS-1$
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
			title = Messages.getString("TabBar.UNTITLED"); //$NON-NLS-1$
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
	 * Updates the title of the module instance or any other 
	 * <code>IPanel</code>.
	 * @param panel The module instance or any other <code>IPanel</code>
	 * in the tabbar.
	 * @param title The new title.
	 */
	public void setTitle(IPanel panel, String title) {
		if (panel == null) {
			return;
		}
		int index = indexOfComponent(panel.getPanelInstance());
		if (index != -1) {
			setTitleAt(index, title);
		}
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
		if (index < 0 || index >= getTabCount()) {
			return;
		}
		
		Component c = getComponentAt(index);
		Object owner = table.remove(c);
		if (owner != null && owner instanceof IModule) {
 			ModuleFactory.disposeInstance((IModule)owner);
			// real removing done by listener (disposed())
		} else if (owner != null && owner instanceof IPanel) {
			((IPanel)owner).dispose();
			super.removeTabAt(index);
		} else {
			super.removeTabAt(index);
		}
	}
	
	/**
	 * Sets the color of the tab of the given component.
	 * @param tab The component whose tabs should be changed.
	 * @param color The new color.
	 */
	public void hightlightTab(Component tab, Color color) {
		if (tab == null) {
			return;
		}
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
		return c != null && c.requestFocusInWindow();
	}
	
	/**
	 * A simply menu with just a "close" option.
	 */
	class RightClickMenu extends JPopupMenu {
		private static final long serialVersionUID = -7757128381876335202L;

		/**
		 * Creates a new menu with "close".
		 */
		public RightClickMenu() {
			String title = Messages.getString("TabBar.CLOSE");
			JMenuItem close = new JMenuItem(title); //$NON-NLS-1$
			close.setMnemonic(title.charAt(0));
			close.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeSelectedTab();
				}
			});
			add(close);
		}
	}
}