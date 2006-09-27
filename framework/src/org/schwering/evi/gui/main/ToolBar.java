/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
 * middle (CENTER) and the closebutton is at the right (EAST).
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
		setLayout(new BorderLayout());
		
		final JButton closeButton = new JButton("X"); //$NON-NLS-1$
		Font f = closeButton.getFont();
		f = new Font(f.getName(), Font.BOLD, f.getSize());
		closeButton.setFont(f);
		closeButton.setForeground(Color.black);
		closeButton.setToolTipText(Messages.getString("ToolBar.CLOSE_TAB")); //$NON-NLS-1$
		closeButton.setFocusPainted(false);
		closeButton.setBorderPainted(false);
		closeButton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				closeButton.setForeground(Color.red);
			}

			public void mouseExited(MouseEvent e) {
				closeButton.setForeground(Color.black);
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
		});
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
		
		JPanel alignAppletPanel = new JPanel(new BorderLayout());
		alignAppletPanel.add(appletPanel, BorderLayout.EAST);
		add(buttonPanel, BorderLayout.WEST);
		add(alignAppletPanel, BorderLayout.CENTER);
		add(closeButton, BorderLayout.EAST);
		
		ModuleLoader.addListener(this);
		ModuleContainer[] modules = ModuleLoader.getLoadedModules();
		for (int i = 0; i < modules.length; i++) {
			addButton(modules[i]);
			
			if (modules[i].isApplet()) {
				modules[i].addListener(this);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleLoaderListener#loaded(org.schwering.evi.core.ModuleContainer)
	 */
	public void loaded(ModuleContainer loadedModule) {
		addButton(loadedModule);
		
		if (loadedModule.isApplet()) {
			loadedModule.addListener(this);
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
			ExceptionDialog.show(Messages.getString("ToolBar.UNEXPECTED_ERROR"), exc); //$NON-NLS-1$
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
				appletPanel.revalidate();
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
				appletPanel.revalidate();
			}
		}
	}
	
	private void addButton(ModuleContainer module) {
		if (!module.isButtonable()) {
			return;
		}
		JButton button = (module.isCustomButtonable()) 
							? module.getCustomButton()
							: getDefaultButton(module);
		buttonPanel.add(button);
		buttonPanel.revalidate();
		buttonTable.put(module, button);
	}
	
	private JButton getDefaultButton(final ModuleContainer module) {
		JButton button = new JButton(module.getName());
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ModuleFactory.newInstance(module);
				} catch (Exception exc) {
					ExceptionDialog.show(
							Messages.getString("ToolBar.MODULE_INSTANTIATION_EXCEPTION_NOTICE") //$NON-NLS-1$ 
							+":\n"+  //$NON-NLS-1$ 
							module.getId(),
							exc);
				}
			}
		});
		return button;
	}
	
	private void removeButton(ModuleContainer module) {
		if (!module.isButtonable()) {
			return;
		}
		Object o = buttonTable.remove(module);
		if (o != null && o instanceof JButton) {
			JButton button = (JButton)o;
			buttonPanel.remove(button);
			buttonPanel.revalidate();
		}
	}
}
