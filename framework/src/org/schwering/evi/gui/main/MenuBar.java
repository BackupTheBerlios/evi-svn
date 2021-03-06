/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.gui.main;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.schwering.evi.core.IModuleLoaderListener;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleLoader;
import org.schwering.evi.gui.conf.MainConfigurationPanel;
import org.schwering.evi.gui.conf.ModuleAutoStartConfigurationPanel;
import org.schwering.evi.gui.conf.ModuleConfigurationPanel;
import org.schwering.evi.util.EnvironmentPanel;
import org.schwering.evi.util.Util;

/**
 * The menu bar allows to open configuration dialogs and instantiate modules.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class MenuBar extends JMenuBar implements IModuleLoaderListener {
	private static final long serialVersionUID = -992480608627651585L;
	
	private Hashtable<ModuleContainer, JMenu> table = new Hashtable<ModuleContainer, JMenu>();
	
	public MenuBar() {
		String title;
		// File menu
		title = Messages.getString("MenuBar.FILE"); //$NON-NLS-1$
		JMenu fileMenu = new JMenu(title);
		if (title != null && title.length() > 0) {
			fileMenu.setMnemonic(title.charAt(0));
		}
		addMainConfigurationMenu(fileMenu);
		addModuleConfigurationMenu(fileMenu);
		addModuleAutoStartConfigurationMenu(fileMenu);
		addRunningModulesMenu(fileMenu);
		fileMenu.addSeparator();
		addCloseMenu(fileMenu);
		addExitMenu(fileMenu);
		add(fileMenu);
		
		// About menu
		title = Messages.getString("MenuBar.ABOUT"); //$NON-NLS-1$
		JMenu aboutMenu = new JMenu(title); //$NON-NLS-1$
		if (title != null && title.length() > 0) {
			aboutMenu.setMnemonic(title.charAt(0));
		}
		aboutMenu.setMnemonic('A');
		addEnvironmentMenu(aboutMenu);
		addLicenseMenu(aboutMenu);
		addInformationMenu(aboutMenu);
		addHelpMenu(aboutMenu);
		add(aboutMenu);
		
		
		ModuleContainer[] modules = ModuleLoader.getLoadedModules();
		for (int i = 0; i < modules.length; i++) {
			addModule(modules[i]);
		}
		ModuleLoader.addListener(this);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleLoaderListener#loaded(org.schwering.evi.core.ModuleContainer)
	 */
	public void loaded(ModuleContainer loadedModule) {
		addModule(loadedModule);
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleLoaderListener#unloaded(org.schwering.evi.core.ModuleContainer)
	 */
	public void unloaded(ModuleContainer unloadedModule) {
		if (!unloadedModule.isPanel()) {
			return;
		}
		Object o = table.remove(unloadedModule);
		if (o != null && o instanceof JMenu) {
			JMenu menu = (JMenu)o;
			remove(menu);
		}
	}

	/**
	 * Adds menu entries to initialize and/or configure the module.
	 * @param module The module for which menu entries are to be added.
	 */
	private void addModule(ModuleContainer module) {
		JMenu menu = null;
		if (module.isMenuable()) {
			if (module.isCustomMenuable()) {
				menu = module.getCustomMenu();
			} else {
				menu = new DefaultModuleMenu(module);
			}
		}
		if (menu != null) {
			add(menu, getMenuCount() - 1);
			table.put(module, menu);
		}
	}
	
	private void addMainConfigurationMenu(JMenu m) {
		String title = MainConfigurationPanel.DEFAULT_TITLE;
		JMenuItem i = new JMenuItem(title);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTab(MainConfigurationPanel.getInstance());
			}
		});
		if (title != null && title.length() > 0) {
			i.setMnemonic(title.charAt(0));
		}
		m.add(i);
	}
	
	private void addModuleConfigurationMenu(JMenu m) {
		String title = ModuleConfigurationPanel.DEFAULT_TITLE;
		JMenuItem i = new JMenuItem(title);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTab(ModuleConfigurationPanel.getInstance());
			}
		});
		if (title != null && title.length() > 0) {
			i.setMnemonic(title.charAt(0));
		}
		m.add(i);
	}
	
	private void addModuleAutoStartConfigurationMenu(JMenu m) {
		String title = ModuleAutoStartConfigurationPanel.DEFAULT_TITLE;
		JMenuItem i = new JMenuItem(title);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTab(ModuleAutoStartConfigurationPanel.getInstance());
			}
		});
		if (title != null && title.length() > 0) {
			i.setMnemonic(title.charAt(0));
		}
		m.add(i);
	}
	
	private void addRunningModulesMenu(JMenu m) {
		String title = RunningModulesPanel.DEFAULT_TITLE;
		JMenuItem i = new JMenuItem(title);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTab(RunningModulesPanel.getInstance());
			}
		});
		if (title != null && title.length() > 0) {
			i.setMnemonic(title.charAt(0));
		}
		m.add(i);
	}
	
	private void addCloseMenu(JMenu m) {
		String title = Messages.getString("MenuBar.CLOSE_TAB"); //$NON-NLS-1$
		JMenuItem i = new JMenuItem(title);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.getInstance().getMainTabBar().removeSelectedTab();
			}
		});
		if (title != null && title.length() > 0) {
			i.setMnemonic(title.charAt(0));
		}
		i.setAccelerator(KeyStroke.getKeyStroke('W', Event.CTRL_MASK));
		m.add(i);
	}
	
	private void addExitMenu(JMenu m) {
		String title = Messages.getString("MenuBar.EXIT"); //$NON-NLS-1$
		JMenuItem i = new JMenuItem(title);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Util.askToExit();
			}
		});
		if (title != null && title.length() > 1) {
			i.setMnemonic(title.charAt(1));
		}
		i.setAccelerator(KeyStroke.getKeyStroke('Q', Event.CTRL_MASK));
		m.add(i);
	}
	
	private void addLicenseMenu(JMenu m) {
		String title = Messages.getString("MenuBar.LICENSE"); //$NON-NLS-1$
		JMenuItem i = new JMenuItem(title);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTab(LicensePanel.getInstance());
			}
		});
		if (title != null && title.length() > 0) {
			i.setMnemonic(title.charAt(0));
		}
		m.add(i);
	}
	
	private void addEnvironmentMenu(JMenu m) {
		String title = EnvironmentPanel.DEFAULT_TITLE;
		JMenuItem i = new JMenuItem(title);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTab(new EnvironmentPanel());
			}
		});
		if (title != null && title.length() > 0) {
			i.setMnemonic(title.charAt(0));
		}
		m.add(i);
	}
	
	private void addInformationMenu(JMenu m) {
		String title = AboutPanel.DEFAULT_TITLE;
		JMenuItem i = new JMenuItem(title);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTab(new AboutPanel());
			}
		});
		if (title != null && title.length() > 0) {
			i.setMnemonic(title.charAt(0));
		}
		m.add(i);
	}
	
	private void addHelpMenu(JMenu m) {
		String title = HelpPanel.DEFAULT_TITLE;
		JMenuItem i = new JMenuItem(title);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTab(new HelpPanel());
			}
		});
		if (title != null && title.length() > 0) {
			i.setMnemonic(title.charAt(0));
		}
		m.add(i);
	}
	
	/**
	 * Just a shorthand for MainFrame.getInstance().getMainTabBar().addTab().
	 * @param panel The panel which should be added.
	 */
	private void addTab(IPanel panel) {
		MainFrame.getInstance().getMainTabBar().addTab(panel);
	}
}
