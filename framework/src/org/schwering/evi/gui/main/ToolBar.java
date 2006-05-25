package org.schwering.evi.gui.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.schwering.evi.core.IApplet;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleListener;
import org.schwering.evi.core.IModuleLoaderListener;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleFactory;
import org.schwering.evi.core.ModuleLoader;
import org.schwering.evi.util.ExceptionDialog;

/**
 * The toolbar. The toolbar contains three parts:
 * <ul>
 * <li> The buttonpanel </li>
 * <li> The pluginpanel </li>
 * <li> The closebutton </li>
 * </ul>
 * These elements are added with a <code>BorderLayout</code>.
 * The buttonpanel is at the left (WEST), the pluginpanel at the 
 * middle (CENTER) and the closebutton is at the right (EAST).<br />
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ToolBar extends JToolBar 
implements IModuleListener, IModuleLoaderListener {
	private static final long serialVersionUID = 6169662234007355911L;
	
	/**
	 * Contains all modules and their respective buttons. Used to remove 
	 * buttons when a module is unloaded.
	 */
	private Hashtable buttonTable = new Hashtable();
	
	/**
	 * The panel which contains the buttons.
	 */
	private JPanel buttonPanel = new JPanel();
	
	/**
	 * The panel which contains the small plugins.
	 */
	private JPanel appletPanel = new JPanel();
	
	/**
	 * Creates a new ToolBar.
	 */
	public ToolBar() {
		setFloatable(false);
		setRollover(true);
		setLayout(new java.awt.BorderLayout());
		
		JButton closeButton = new JButton("X"); //$NON-NLS-1$
		Font f = closeButton.getFont();
		f = new Font(f.getName(), Font.BOLD, f.getSize());
		closeButton.setFont(f);
		closeButton.setToolTipText(Messages.getString("ToolBar.CLOSE_TAB")); //$NON-NLS-1$
		closeButton.setFocusPainted(false);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainFrame mf = MainFrame.getInstance();
				if (mf == null) {
					return;
				}
				TabBar tb = mf.getMainTabBar();
				if (tb == null) {
					return;
				}
				tb.removeSelectedTab();
			}
		});
		
		add(buttonPanel, BorderLayout.WEST);
		add(appletPanel, BorderLayout.CENTER);
		add(closeButton, BorderLayout.EAST);
		
		ModuleContainer[] modules = ModuleLoader.getLoadedModules();
		for (int i = 0; i < modules.length; i++) {
			addButton(modules[i]);
			
			if (modules[i].isApplet()) {
				modules[i].addModuleListener(this);
			}
		}
		ModuleLoader.addModuleLoaderListener(this);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleLoaderListener#loaded(org.schwering.evi.core.ModuleContainer)
	 */
	public void loaded(ModuleContainer loadedModule) {
		addButton(loadedModule);
		
		if (loadedModule.isApplet()) {
			loadedModule.addModuleListener(this);
		}
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleLoaderListener#unloaded(org.schwering.evi.core.ModuleContainer)
	 */
	public void unloaded(ModuleContainer unloadedModule) {
		removeButton(unloadedModule);
	}

	/**
	 * The event method fired when a module is instantiated. 
	 * If the module which is instantiated is an an instance of 
	 * <code>IApplet</code>, the applet-panel is added.
	 * @param newInstance The newly created instance object.
	 */
	public void instantiated(IModule newInstance) {
		try {
			addApplet((IApplet)newInstance);
		} catch (Exception exc) {
			ExceptionDialog.show(Messages.getString("ToolBar.UNEXPECTED_ERROR"), exc); //$NON-NLS-1$
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
			IApplet applet = (IApplet)disposedInstance;
			removeApplet(applet);
		} catch (Exception exc) {
			ExceptionDialog.show(Messages.getString("TabBar.UNEXPECTED_ERROR"), exc); //$NON-NLS-1$
		}
	}

	/**
	 * Adds a new component to the appletpanel.
	 * @param b The new component.
	 */
	private void addApplet(IApplet a) {
		if (a != null) {
			Component c = a.getAppletInstance();
			if (c != null) {
				appletPanel.add(c);
			}
		}
	}
	
	/**
	 * Removes a component.
	 * @param c The component.
	 */
	private void removeApplet(IApplet a) {
		if (a != null) {
			Component c = a.getAppletInstance();
			if (c != null) {
				appletPanel.remove(c);
			}
		}
	}
	
	private void addButton(final ModuleContainer module) {
		if (!module.isButtonable()) {
			return;
		}
		JButton button = new JButton(module.getName());
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ModuleFactory.newInstance(module);
				} catch (Exception exc) {
					ExceptionDialog.show(Messages.getString("MODULE_INSTANTIATION_EXCEPTION_NOTICE"), //$NON-NLS-1$ 
							exc);
				}
			}
		});
		buttonPanel.add(button);
		buttonTable.put(module, button);
	}
	
	private void removeButton(ModuleContainer module) {
		if (!module.isButtonable()) {
			return;
		}
		Object o = buttonTable.remove(module);
		if (o != null && o instanceof JButton) {
			JButton button = (JButton)o;
			buttonPanel.remove(button);
		}
	}
}
