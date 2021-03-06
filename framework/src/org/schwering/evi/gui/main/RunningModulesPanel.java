/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.gui.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleListener;
import org.schwering.evi.core.IModuleLoaderListener;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleFactory;
import org.schwering.evi.core.ModuleLoader;
import org.schwering.evi.util.ExceptionDialog;

/**
 * Shows all running module instances.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class RunningModulesPanel extends JPanel implements IPanel {
	private static final long serialVersionUID = 7224934024984148710L;
	
	public static final String DEFAULT_TITLE = org.schwering.evi.gui.main.Messages.getString("RunningModulesPanel.DEFAULT_TITLE"); //$NON-NLS-1$
	
	private JTable table = new JTable(new TableModel());
	private JPopupMenu menu = new JPopupMenu();
	
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
	
	/**
	 * Creates a new panel with a table that lists all module instances.
	 * This list is always up to date thanks to the <code>TableModel</code>.
	 */
	private RunningModulesPanel() {
		super(new GridLayout(1, 1));
		setBorder(new TitledBorder(DEFAULT_TITLE));
		
		JScrollPane scrollPane = new JScrollPane(table);
		JPanel panel = new JPanel(new BorderLayout(1, 0));
		panel.add(scrollPane);
		add(panel);
		
		final JMenuItem newInstance = new JMenuItem(Messages.getString("RunningModulesPanel.NEW_INSTANCE")); //$NON-NLS-1$
		newInstance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] rows = table.getSelectedRows();
				instantiate(rows);
			}
		});
		final JMenuItem terminateOne = new JMenuItem(Messages.getString("RunningModulesPanel.TERMINATE")); //$NON-NLS-1$
		terminateOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] rows = table.getSelectedRows();
				terminate(rows);
			}
		});
		final JMenuItem terminateAll = new JMenuItem(Messages.getString("RunningModulesPanel.TERMINATE_ALL")); //$NON-NLS-1$
		terminateAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] rows = table.getSelectedRows();
				terminateAll(rows);
			}
		});
		
		menu.add(newInstance);
		menu.addSeparator();
		menu.add(terminateOne);
		menu.add(terminateAll);
		
		table.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) { 
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 1 
						&& e.getButton() == MouseEvent.BUTTON3) {
					try {
						int selRowCount = table.getSelectedRowCount();
						if (selRowCount > 1) {
							newInstance.setEnabled(true);
							terminateOne.setEnabled(true);
							terminateAll.setEnabled(true);
						} else if (selRowCount == 1) {
							newInstance.setEnabled(true);
							Object object = table.getValueAt(table.getSelectedRow(), 1);
							int count = Integer.parseInt((String)object);
							terminateOne.setEnabled(count >= 1);
							terminateAll.setEnabled(count >= 2);
						} else {
							newInstance.setEnabled(false);
							terminateOne.setEnabled(false);
							terminateAll.setEnabled(false);
						}
					} catch (Exception exc) {
						newInstance.setEnabled(true);
						terminateOne.setEnabled(true);
						terminateAll.setEnabled(true);
					}
					menu.show(table, e.getX(), e.getY());
				}
			}
			public void mouseReleased(MouseEvent e) {
			}
		} );
	}
	
	/**
	 * Asks to terminate one instances of each modules listed in the 
	 * given rows.
	 * @param rows The rows of the table belonging to the modules that are 
	 * to be killed.
	 */
	private void terminate(int[] rows) {
		if (rows == null) {
			return;
		}
		
		for (int i = 0; i < rows.length; i++) {
			String id = (String)table.getValueAt(rows[i], 0);
			terminate(id);
		}
	}
	
	/**
	 * Kills one module instances of the given id. 
	 * @param id The id of the module whose instance is to killed.
	 */
	private void terminate(String id) {
		ModuleContainer container = ModuleLoader.getLoadedModule(id);
		if (container == null) {
			return;
		}
		IModule[] instances = container.getInstances();
		
		if (instances.length > 0) {
			int i = JOptionPane.showConfirmDialog(null, 
					Messages.getString("RunningModulesPanel.TERMINATE_ONE_INSTANCE")+ id,  //$NON-NLS-1$
					Messages.getString("RunningModulesPanel.TERMINATE"),  //$NON-NLS-1$
					JOptionPane.YES_NO_OPTION);
			if (i == JOptionPane.YES_OPTION) {
				ModuleFactory.disposeInstance(instances[instances.length-1]);
			}
		}
	}
	
	/**
	 * Asks to terminate all instances of the modules listed in the 
	 * given rows.
	 * @param rows The rows of the table belonging to the modules that are 
	 * to be killed.
	 */
	private void terminateAll(int[] rows) {
		if (rows == null) {
			return;
		}
		
		for (int i = 0; i < rows.length; i++) {
			String id = (String)table.getValueAt(rows[i], 0);
			terminateAll(id);
		}
	}
	
	/**
	 * Kills all module instances of the given id.
	 * @param id The id of the module whose instances are to killed.
	 */
	private void terminateAll(String id) {
		ModuleContainer container = ModuleLoader.getLoadedModule(id);
		if (container == null) {
			return;
		}
		IModule[] instances = container.getInstances();
		
		if (instances.length > 0) {
			int i = JOptionPane.showConfirmDialog(null, 
					Messages.getString("RunningModulesPanel.TERMINATE_ALL_INSTANCES")+ id,  //$NON-NLS-1$
					Messages.getString("RunningModulesPanel.TERMINATE"),  //$NON-NLS-1$
					JOptionPane.YES_NO_OPTION);
			if (i == JOptionPane.YES_OPTION) {
				for (int j = 0; j < instances.length; j++) {
					ModuleFactory.disposeInstance(instances[j]);
				}
			}
		}
	}
	
	/**
	 * Instantiates a number of modules with.
	 * @param rows The rows of the table belonging to the modules that are 
	 * to be instantiated.
	 */
	private void instantiate(int[] rows) {
		for (int i = 0; i < rows.length; i++) {
			try {
				String id = (String)table.getValueAt(rows[i], 0);
				ModuleContainer container = ModuleLoader.getLoadedModule(id);
				ModuleFactory.newInstance(container);
			} catch (Exception exc) {
				ExceptionDialog.show(Messages.getString("RunningModulesPanel.NEW_INSTANCE_FAILED"), //$NON-NLS-1$
						exc);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getIcon()
	 */
	public Icon getIcon() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getPanelInstance()
	 */
	public Component getPanelInstance() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
		instanceCount--;
		if (instanceCount == 0) {
			instance = null;
			TableModel model = (TableModel)table.getModel();
			ModuleContainer[] containers = ModuleLoader.getLoadedModules();
			for (int i = 0; i < containers.length; i++) {
				containers[i].removeListener((IModuleListener)model);
			}
			ModuleLoader.removeListener((IModuleLoaderListener)model);
		}
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return DEFAULT_TITLE;
	}
	
	/**
	 * A table model that updates the table using listeners.
	 * @author Christoph Schwering (schwering@gmail.com)
	 */
	class TableModel extends AbstractTableModel 
	implements IModuleListener, IModuleLoaderListener {
		private static final long serialVersionUID = 2712132043640778311L;
		
		/**
		 * Stores all currently loaded modules as 
		 * <code>ModuleContainer</code>s.
		 */
		private Vector<ModuleContainer> modules;
		
		/**
		 * The column names.
		 */
		private String[] colNames = new String[] {
				Messages.getString("RunningModulesPanel.MODULE_ID"),  //$NON-NLS-1$
				Messages.getString("RunningModulesPanel.INSTANCE_COUNT"),  //$NON-NLS-1$
		};
		
		/**
		 * Creates a new object. The constructor simply fills the vector 
		 * with the currently loaded modules.
		 */
		public TableModel() {
			ModuleContainer[] containers = ModuleLoader.getLoadedModules();
			modules = new Vector<ModuleContainer>(containers.length);
			for (int i = 0; i < containers.length; i++) {
				modules.add(containers[i]);
				containers[i].addListener(this);
			}
			ModuleLoader.addListener(this);
		}

		/* (non-Javadoc)
		 * @see org.schwering.evi.core.IModuleLoaderListener#loaded(org.schwering.evi.core.ModuleContainer)
		 */
		public void loaded(ModuleContainer loadedModule) {
			modules.add(loadedModule);
			int i = modules.indexOf(loadedModule);
			fireTableRowsInserted(i, i);
		}

		/* (non-Javadoc)
		 * @see org.schwering.evi.core.IModuleLoaderListener#unloaded(org.schwering.evi.core.ModuleContainer)
		 */
		public void unloaded(ModuleContainer unloadedModule) {
			int i = modules.indexOf(unloadedModule);
			modules.remove(unloadedModule);
			fireTableRowsDeleted(i, i);
		}

		/* (non-Javadoc)
		 * @see org.schwering.evi.core.IModuleListener#disposed(org.schwering.evi.core.IModule)
		 */
		public void disposed(IModule disposedInstance) {
			String id = ModuleContainer.getIdByClass(disposedInstance.getClass());
			ModuleContainer m = ModuleLoader.getLoadedModule(id);
			int i = modules.indexOf(m);
			fireTableCellUpdated(i, 1);
		}

		/* (non-Javadoc)
		 * @see org.schwering.evi.core.IModuleListener#instantiated(org.schwering.evi.core.IModule)
		 */
		public void instantiated(IModule newInstance) {
			String id = ModuleContainer.getIdByClass(newInstance.getClass());
			ModuleContainer m = ModuleLoader.getLoadedModule(id);
			int i = modules.indexOf(m);
			fireTableCellUpdated(i, 1);
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return 2;
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			return modules.size();
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnName(int)
		 */
		public String getColumnName(int col) {
			return colNames[col];
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			ModuleContainer m = (ModuleContainer)modules.get(row);
			if (col == 0) {
				return m.getId();
			} else {
				return String.valueOf(m.getInstances().length);
			}
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnClass(int)
		 */
		public Class<?> getColumnClass(int col) {
			return getValueAt(0, col).getClass();
		}
	}
}