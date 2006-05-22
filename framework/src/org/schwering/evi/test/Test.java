package org.schwering.evi.test;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.schwering.evi.core.*;

/**
 * Test module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id: Test.java 50 2006-05-11 00:21:14Z schwering $
 */
public class Test extends JPanel implements IModule, IPanel, Runnable {
	private static final long serialVersionUID = 6675184535258501432L;

	private JTable table = new JTable();
	private Thread thread;
	private volatile boolean run = true;
	
	public Test() {
		super(new GridLayout(1, 1));
		setBorder(new TitledBorder("Running Modules"));
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		while (run) {
			try {
				updateTable();
				Thread.sleep(2000);
			} catch (Exception exc) {
			}
		}
	}
	
	private void updateTable() {
		remove(table);
		String[] ids = ModuleLoader.getLoadedIds();
		table = new JTable(ids.length, 2);
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			String count = String.valueOf(ModuleLoader.getLoadedModule(ids[i]).getInstances().length);
			table.setValueAt(id, i, 0);
			table.setValueAt(count, i, 1);
		}
		add(table);
		revalidate();
		repaint();
	}
	
	public Icon getIcon() {
		return null;
	}
	
	public Component getPanelInstance() {
		return this;
	}
	
	public void dispose() {
		run = false;
		System.out.println("IModule.dispose() in org.schwering.evi.test.Test");
	}

	public String getTitle() {
		return "Testmodule";
	}
}
