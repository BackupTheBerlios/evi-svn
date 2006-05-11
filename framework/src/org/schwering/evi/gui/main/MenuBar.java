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
import org.schwering.evi.core.ModuleConfigFactory;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleFactory;
import org.schwering.evi.core.ModuleLoader;
import org.schwering.evi.gui.conf.MainConfigurationPanel;
import org.schwering.evi.gui.conf.ModuleAutoStartConfigurationPanel;
import org.schwering.evi.gui.conf.ModuleConfigurationPanel;
import org.schwering.evi.util.EnvironmentPanel;
import org.schwering.evi.util.ExceptionDialog;
import org.schwering.evi.util.Util;

/**
 * The menu bar allows to open configuration dialogs and instantiate modules.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class MenuBar extends JMenuBar implements IModuleLoaderListener {
	private static final long serialVersionUID = -992480608627651585L;
	
	private MainFrame owner;
	private Hashtable table = new Hashtable();
	
	public MenuBar(MainFrame owner) {
		this.owner = owner;
		
		// File menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		addMainConfigurationMenu(fileMenu);
		addModuleConfigurationMenu(fileMenu);
		addModuleAutoStartConfigurationMenu(fileMenu);
		addCloseMenu(fileMenu);
		addExitMenu(fileMenu);
		add(fileMenu);
		
		// About menu
		JMenu aboutMenu = new JMenu("About");
		aboutMenu.setMnemonic('A');
		addLicenseMenu(aboutMenu);
		addEnvironmentMenu(aboutMenu);
		addInformationMenu(aboutMenu);
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
	private void addModule(final ModuleContainer module) {
		if (!module.isPanel() && !module.isConfigurable()) {
			return;
		}
		
		String name = module.getName();
		if (name == null || name.length() == 0) {
			name = "Untitled";
		}
		JMenu m = new JMenu(name);
		m.setMnemonic(name.charAt(0));
		
		if (module.isPanel()) {
			JMenuItem i = new JMenuItem("New instance");
			i.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						ModuleFactory.newInstance(module);
					} catch (Exception exc) {
						ExceptionDialog.show("Module could not be instantiated", 
								exc);
					}
				}
			});
			m.add(i);
		}
		
		if (module.isConfigurable()) {
			JMenuItem i = new JMenuItem("Configure");
			i.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						IPanel config = ModuleConfigFactory.newInstance(module);
						MainFrame.getInstance().getMainTabBar().addTab(config);
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// TODO
// Start module configuration panel
// This requires solving the configurationpanel
// issue!!!
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					} catch (Exception exc) {
						ExceptionDialog.show("Module configuration could not " +
								"be started", exc);
					}
				}
			});
			m.add(i);
		}
		add(m, getMenuCount() - 1);
		table.put(module, m);
	}
	
	private void addMainConfigurationMenu(JMenu m) {
		JMenuItem i = new JMenuItem(MainConfigurationPanel.DEFAULT_TITLE);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TabBar tabBar = owner.getMainTabBar();
				if (tabBar != null) {
					tabBar.addTab(MainConfigurationPanel.getInstance());
				}
			}
		});
		i.setMnemonic('o');
		m.add(i);
	}
	
	private void addModuleConfigurationMenu(JMenu m) {
		JMenuItem i = new JMenuItem(ModuleConfigurationPanel.DEFAULT_TITLE);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TabBar tabBar = owner.getMainTabBar();
				if (tabBar != null) {
					tabBar.addTab(ModuleConfigurationPanel.getInstance());
				}
			}
		});
		i.setMnemonic('M');
		m.add(i);
	}
	
	private void addModuleAutoStartConfigurationMenu(JMenu m) {
		JMenuItem i = new JMenuItem(
				ModuleAutoStartConfigurationPanel.DEFAULT_TITLE);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TabBar tabBar = owner.getMainTabBar();
				if (tabBar != null) {
					tabBar.addTab(
							ModuleAutoStartConfigurationPanel.getInstance());
				}
			}
		});
		i.setMnemonic('A');
		m.add(i);
	}
	
	private void addCloseMenu(JMenu m) {
		JMenuItem i = new JMenuItem("Close Tab");
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TabBar tabBar = owner.getMainTabBar();
				if (tabBar != null) {
					tabBar.removeSelectedTab();
				}
			}
		});
		i.setMnemonic('C');
		i.setAccelerator(KeyStroke.getKeyStroke('W', Event.CTRL_MASK));
		m.add(i);
	}
	
	private void addExitMenu(JMenu m) {
		JMenuItem i = new JMenuItem("Exit");
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Util.askToExit();
			}
		});
		i.setMnemonic('x');
		i.setAccelerator(KeyStroke.getKeyStroke('Q', Event.CTRL_MASK));
		m.add(i);
	}
	
	private void addLicenseMenu(JMenu m) {
		JMenuItem i = new JMenuItem("License");
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TabBar tabBar = owner.getMainTabBar();
				if (tabBar != null) {
					tabBar.addTab(new LicensePanel());
				}
			}
		});
		i.setMnemonic('L');
		m.add(i);
	}
	
	private void addEnvironmentMenu(JMenu m) {
		JMenuItem i = new JMenuItem(EnvironmentPanel.DEFAULT_TITLE);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TabBar tabBar = owner.getMainTabBar();
				if (tabBar != null) {
					tabBar.addTab(new EnvironmentPanel());
				}
			}
		});
		i.setMnemonic('E');
		m.add(i);
	}
	
	private void addInformationMenu(JMenu m) {
		JMenuItem i = new JMenuItem(AboutPanel.DEFAULT_TITLE);
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TabBar tabBar = owner.getMainTabBar();
				if (tabBar != null) {
					tabBar.addTab(new AboutPanel());
				}
			}
		});
		i.setMnemonic('A');
		m.add(i);
	}
}
