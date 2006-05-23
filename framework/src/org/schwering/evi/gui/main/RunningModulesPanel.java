package org.schwering.evi.gui.main;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.schwering.evi.core.*;

/**
 * Shows all running module instances.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class RunningModulesPanel extends JPanel implements IPanel, Runnable {
	private static final long serialVersionUID = 7224934024984148710L;
	
	public static final String DEFAULT_TITLE = Messages.getString("RunningModulesPanel.DEFAULT_TITLE"); //$NON-NLS-1$
	
	private JTable table = new JTable();
	private Thread thread;
	private volatile boolean run = true;
	
	/**
	 * Gives access to the one and only instance of this panel.
	 * This avoids that the user might create a bunch of panels
	 * which would make no sense.
	 */
	private static RunningModulesPanel instance = null;
	
	/**
	 * The number of times getInstance() was invoked. 
	 * Exactly this times dispose() will be invoked.
	 */
	private static int instanceCount = 0;
	
	/**
	 * Creates one initial instance and returns it in future until 
	 * <code>dispose()</code> is invoked.
	 * @return The current instance.
	 */
	public static RunningModulesPanel getInstance() {
		if (instance == null) {
			instance = new RunningModulesPanel();
		}
		instanceCount++;
		return instance;
	}
	
	private RunningModulesPanel() {
		super(new GridLayout(1, 1));
		setBorder(new TitledBorder(DEFAULT_TITLE));
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
		instanceCount--;
		if (instanceCount == 0) {
			instance = null;
		}
		run = false;
	}

	public String getTitle() {
		return DEFAULT_TITLE;
	}
}