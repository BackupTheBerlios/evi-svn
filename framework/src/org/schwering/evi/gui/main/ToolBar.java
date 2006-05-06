package org.schwering.evi.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JToolBar;

import org.schwering.evi.core.IModuleLoaderListener;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleFactory;
import org.schwering.evi.core.ModuleLoader;
import org.schwering.evi.gui.EVI;
import org.schwering.evi.util.ExceptionDialog;

public class ToolBar extends JToolBar implements IModuleLoaderListener {
	private static final long serialVersionUID = 6169662234007355911L;
	
	/**
	 * Contains all modules and their respective buttons. Used to remove 
	 * buttons when a module is unloaded.
	 */
	private Hashtable table = new Hashtable();
	
	public ToolBar() {
		setFloatable(false);
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
					ModuleFactory.newInstance(module, EVI.getInstance());
				} catch (Exception exc) {
					ExceptionDialog.show("Module could not be instantiated", exc);
				}
			}
		});
		add(button);
		table.put(module, button);
	}
}
