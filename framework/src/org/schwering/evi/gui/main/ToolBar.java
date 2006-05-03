package org.schwering.evi.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleFactory;
import org.schwering.evi.gui.EVI;
import org.schwering.evi.util.ExceptionDialog;

public class ToolBar extends JToolBar {
	private MainFrame owner;
	
	public ToolBar(MainFrame owningFrame) {
		this.owner = owningFrame;
		setFloatable(false);
	}
	
	public void addModule(final ModuleContainer module) {
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
	}
}
