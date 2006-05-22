package org.schwering.evi.gui.main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

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
public class ToolBar extends JToolBar implements IModuleLoaderListener {
	private static final long serialVersionUID = 6169662234007355911L;
	
	/**
	 * Contains all modules and their respective buttons. Used to remove 
	 * buttons when a module is unloaded.
	 */
	private Hashtable table = new Hashtable();
	
	/**
	 * The panel which contains the buttons.
	 */
	private JPanel buttonPanel = new JPanel();
	
	/**
	 * The panel which contains the small plugins.
	 */
	private JPanel pluginPanel = new JPanel();
	
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
		add(pluginPanel, BorderLayout.CENTER);
		add(closeButton, BorderLayout.EAST);
		
		ModuleContainer[] modules = ModuleLoader.getLoadedModules();
		for (int i = 0; i < modules.length; i++) {
			addModule(modules[i]);
		}
		ModuleLoader.addListener(this);
	}
	
	/**
	 * Adds a new button to the buttonpanel.
	 * @param b The new button.
	 */
	public void add(JButton b) {
		buttonPanel.add(b);
	}
	
	/**
	 * Removes a button.
	 * @param b The button.
	 */
	public void remove(JButton b) {
		buttonPanel.remove(b);
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
		if (o != null && o instanceof JButton) {
			JButton button = (JButton)o;
			remove(button);
		}
	}

	private void addModule(final ModuleContainer module) {
		if (!module.isPanel()) {
			return;
		}
		JButton button = new JButton(module.getName());
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ModuleFactory.newInstance(module);
				} catch (Exception exc) {
					ExceptionDialog.show("", exc); //$NON-NLS-1$
				}
			}
		});
		add(button);
		table.put(module, button);
	}
}
