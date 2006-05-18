package org.schwering.evi.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.core.*;

/**
 * Test module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id: Test.java 50 2006-05-11 00:21:14Z schwering $
 */
public class Test extends JPanel implements IModule, IPanel {
	private static final long serialVersionUID = 6675184535258501432L;
	
	public Test() {
		super(new BorderLayout());
		String[] ids = ModuleLoader.getLoadedIds();
		Object[][] data = new Object[ids.length][2];
		for (int i = 0; i < data.length; i++) {
			data[i][0] = ids[i];
			data[i][1] = String.valueOf(ModuleLoader.getLoadedModule(ids[i]).getInstances().length);
		}
		JTable table = new JTable(data, new String[] { "ID", "Instances" });
		
		JLabel label = new JLabel("Running Modules");
		label.setForeground(MainConfiguration.getColor("color.primary"));
		label.setBackground(MainConfiguration.getColor("color.secondary"));
		label.setFont(MainConfiguration.getFont("font.primary"));
		
		add(label, BorderLayout.NORTH);
		add(table, BorderLayout.CENTER);
	}
	
	public Icon getIcon() {
		return null;
	}
	
	public Component getPanelInstance() {
		return this;
	}
	
	public void dispose() {
		System.out.println("IModule.dispose() in org.schwering.evi.test.Test");
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return "Testmodule";
	}
}
