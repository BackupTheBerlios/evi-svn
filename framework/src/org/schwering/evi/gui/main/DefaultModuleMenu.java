package org.schwering.evi.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleFactory;
import org.schwering.evi.util.ExceptionDialog;
import org.schwering.evi.util.HTMLBrowser;

/**
 * The (default) menu for modules that implement 
 * {@link org.schwering.evi.core.IMenuable}.<br>
 * This default has up to three points:
 * <ul>
 * <li> "New instance" (if {@link org.schwering.evi.core.ModuleContainer#isApplet()} || {@link org.schwering.evi.core.ModuleContainer#isPanel()}
 * <li> "Configure" (if {@link org.schwering.evi.core.ModuleContainer#isConfigurable()})
 * <li> "Info" (if {@link org.schwering.evi.core.ModuleContainer#getInfoURL()} != null})
 * </ul>
 * You may want to override this menu to modify your module's menu and use
 * it as custom module menu. (See {@link org.schwering.evi.core.ICustomMenuable}.)
 * @see org.schwering.evi.core.IMenuable
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class DefaultModuleMenu extends JMenu {
	private static final long serialVersionUID = 8086518440159292776L;
	
	protected ModuleContainer module;
	
	/**
	 * Creates a new module menu with the title 
	 * <code>module.getName()</code>.
	 * @param module The module for which a menu is generated.
	 */
	public DefaultModuleMenu(ModuleContainer module) {
		this(module, module.getName());
	}
	
	/**
	 * Creates a new module menu with a given title.
	 * @param module The module for which a menu is generated.
	 * @param title The menu's title.
	 */
	public DefaultModuleMenu(ModuleContainer module, String title) {
		super(module.getName());
		this.module = module;
		
		setMnemonic(module.getName().charAt(0));
		addNewInstanceItem();
		addConfigurationItem();
		addInfoItem();
	}
	
	/**
	 * Simply returns the title. Can be overridden to just change 
	 * the title of the "New instance" item.
	 * @return The "New instance" item title.
	 */
	protected String getNewInstanceTitle() {
		return Messages.getString("DefaultModuleMenu.NEW_INSTANCE"); //$NON-NLS-1$
	}
	
	/**
	 * Simply returns the title. Can be overridden to just change 
	 * the title of the "Configure" item.
	 * @return The "Configure" item title.
	 */
	protected String getConfigureTitle() {
		return Messages.getString("DefaultModuleMenu.CONFIGURE"); //$NON-NLS-1$
	}
	
	/**
	 * Simply returns the title. Can be overridden to just change 
	 * the title of the "Info" item.
	 * @return The "Info" item title.
	 */
	protected String getInfoTitle() {
		return Messages.getString("DefaultModuleMenu.INFO"); //$NON-NLS-1$
	}
	
	/**
	 * Adds the "New instance" item to the menu.
	 */
	protected void addNewInstanceItem() {
		if (module.isPanel() || module.isApplet()) {
			JMenuItem i = new JMenuItem(getNewInstanceTitle());
			i.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						ModuleFactory.newInstance(module);
					} catch (Exception exc) {
						ExceptionDialog.show(
								Messages.getString("DefaultModuleMenu.MODULE_INSTANTIATION_EXCEPTION_NOTICE"),  //$NON-NLS-1$
								exc);
					}
				}
			});
			add(i);
		}
	}
	
	/**
	 * Adds the "Configure" item to the menu.
	 */
	protected void addConfigurationItem() {
		if (module.isConfigurable()) {
			JMenuItem i = new JMenuItem(getConfigureTitle());
			i.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						IPanel config = module.getConfigPanel();
						addTab(config);
					} catch (Exception exc) {
						ExceptionDialog.show(Messages.getString("DefaultModuleMenu.MODULE_CONFIGURATION_EXCEPTION_NOTICE"), //$NON-NLS-1$
								exc);
					}
				}
			});
			add(i);
		}
	}
	
	/**
	 * Adds the "Info" item to the menu.
	 */
	protected void addInfoItem() {
		if (module.getInfoURL() != null) {
			JMenuItem i = new JMenuItem(getInfoTitle());
			i.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						URL url = module.getInfoURL();
						HTMLBrowser browser = new HTMLBrowser(url) {
							private static final long serialVersionUID = 1;
							public String getTitle() {
								return module.getName() +" "+ getInfoTitle();
							}
						};
						addTab(browser);
					} catch (Exception exc) {
						ExceptionDialog.show(Messages.getString("DefaultModuleMenu.MODULE_INFORMATION_EXCEPTION_NOTICE"), //$NON-NLS-1$
								exc); //$NON-NLS-1$
					}
				}
			});
			add(i);
		}
	}
	
	/**
	 * Just a shorthand for MainFrame.getInstance().getMainTabBar().addTab().
	 * @param panel The panel which should be added.
	 */
	protected void addTab(IPanel panel) {
		MainFrame.getInstance().getMainTabBar().addTab(panel);
	}
}
