/* Copyright (C) 2006 Christoph Schwering */
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.schwering.evi.core.IApplet;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.ModuleFactory;
import org.schwering.evi.util.MemoryPanel;
import org.schwering.evi.util.RightClickMenu;

public class MemoryApplet implements IModule, IApplet {

	MemoryPanel m;
	
	public MemoryApplet() {
		m = new MemoryPanel(MemoryPanel.BAR);
		
		JPopupMenu menu = new JPopupMenu();
		JMenuItem item = new JMenuItem("Close");
		final IModule instance = this;
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ModuleFactory.disposeInstance(instance);
			}
		});
		menu.add(item);
		RightClickMenu.addRightClickMenu(m, menu);
	}
	
	public Component getAppletInstance() {
		return m;
	}

	public void dispose() {
	}
}
