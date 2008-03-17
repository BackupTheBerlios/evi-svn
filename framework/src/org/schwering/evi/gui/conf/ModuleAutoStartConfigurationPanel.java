/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.gui.conf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;

import org.schwering.evi.conf.ModuleAutoStartConfiguration;
import org.schwering.evi.core.IModuleLoaderListener;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleLoader;

/**
 * A GUI to configure the module auto start list.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ModuleAutoStartConfigurationPanel extends JPanel 
implements IPanel {
	private static final long serialVersionUID = 420606842791596917L;

	/**
	 * The panel's title.
	 */
	public static final String DEFAULT_TITLE = Messages.getString("ModuleAutoStartConfigurationPanel.DEFAULT_TITLE"); //$NON-NLS-1$
		
	/**
	 * Gives access to the one and only instance of this panel.
	 * This avoids that the user might create a bunch of panels
	 * which would make no sense.
	 */
	private static ModuleAutoStartConfigurationPanel instance = null;
	
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
	public static ModuleAutoStartConfigurationPanel getInstance() {
		if (instance == null) {
			instance = new ModuleAutoStartConfigurationPanel();
		}
		instanceCount++;
		return instance;
	}
	
	private LoadPanel loadPanel = new LoadPanel();
	private ModulePanel modulePanel = new ModulePanel(this);
	private JPanel loadWrapper = new JPanel(new GridLayout(1, 0));
	private JPanel moduleWrapper = new JPanel(new GridLayout(1, 0));
	
	/**
	 * Creates a new instance of the configuration GUI.
	 * @see #getInstance()
	 */
	private ModuleAutoStartConfigurationPanel() {
		super(new GridLayout(2, 0));
		moduleWrapper.add(modulePanel);
		loadWrapper.add(loadPanel);
		add(moduleWrapper);
		add(loadWrapper);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getPanelInstance()
	 */
	public Component getPanelInstance() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return DEFAULT_TITLE;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getIcon()
	 */
	public Icon getIcon() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
		instanceCount--;
		if (instanceCount == 0) {
			instance = null;
			IModuleLoaderListener l = (IModuleLoaderListener)modulePanel.model;
			ModuleLoader.removeListener(l);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#requestFocusInWindow()
	 */
	public boolean requestFocusInWindow() {
		boolean b1 = super.requestFocusInWindow();
		boolean b2 = (loadPanel != null) ? loadPanel.requestFocusInWindow() 
				: true;
		return b1 && b2;
	}

	/**
	 * Draws a table with all modules that are automatically loaded.
	 */
	class LoadPanel extends JPanel {
		private static final long serialVersionUID = 7788432414929181492L;

		private LoadTableModel model = new LoadTableModel();
		private JTable table = new JTable(model);
		
		/**
		 * Draws a table with all modules, their version and their requirements.
		 * @param o The owning moduleconfigurationpanel.
		 */
		public LoadPanel() {
			super(new GridLayout(1, 0));
			setBorder(new TitledBorder(Messages.getString("ModuleAutoStartConfigurationPanel.START_AUTOMATICALLY") +":")); //$NON-NLS-1$ //$NON-NLS-2$
			
			DefaultCellEditor dce = new DefaultCellEditor(new JTextField());
			dce.addCellEditorListener(new CellEditorListener() {
				public void editingCanceled(ChangeEvent arg0) {
				}

				public void editingStopped(ChangeEvent arg0) {
					int row = table.getSelectedRow();
					String arg = (String)table.getCellEditor(row, 1).getCellEditorValue();
					model.updateArg(row, arg);
				}
			});
			table.setDefaultEditor(String.class, dce);
			table.setCellEditor(dce);
			table.setToolTipText(Messages.getString("ModuleAutoStartConfigurationPanel.DOUBLE_CLICK_TO_MODIFY_ARGUMENTS")); //$NON-NLS-1$
			final JPopupMenu rightClickMenu = new JPopupMenu();
			final JMenuItem editArgsItem = new JMenuItem(Messages.getString("ModuleAutoStartConfigurationPanel.EDIT_ARGUMENTS")); //$NON-NLS-1$
			editArgsItem.setToolTipText(Messages.getString("ModuleAutoStartConfigurationPanel.ARGUMENTS_ARE_GIVEN_TO_MODULE")); //$NON-NLS-1$
			editArgsItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int[] selected = table.getSelectedRows();
					if (selected.length > 0) {
						table.editCellAt(selected[0], 1);
					}
				}
			});
			JMenuItem removeItem = new JMenuItem(Messages.getString("ModuleAutoStartConfigurationPanel.REMOVE")); //$NON-NLS-1$
			removeItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] selected = table.getSelectedRows();
					for (int i = selected.length - 1; i >= 0; i--) {
						model.remove(selected[i]);
					}
				}
			});
			JMenuItem shiftUp = new JMenuItem(Messages.getString("ModuleConfigurationPanel.UP")); //$NON-NLS-1$
			shiftUp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					shiftSelectedUp();
				}
			});
			JMenuItem shiftDown = new JMenuItem(Messages.getString("ModuleConfigurationPanel.DOWN")); //$NON-NLS-1$
			shiftDown.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					shiftSelectedDown();
				}
			});
			rightClickMenu.add(editArgsItem);
			rightClickMenu.add(removeItem);
			rightClickMenu.addSeparator();
			rightClickMenu.add(shiftUp);
			rightClickMenu.add(shiftDown);
			
			table.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
				}
				public void mouseEntered(MouseEvent e) {
				}
				public void mouseExited(MouseEvent e) {
				}
				public void mousePressed(MouseEvent e) {
					if (e.getClickCount() == 1 
							&& e.getButton() == MouseEvent.BUTTON3
							&& table.getSelectedRow() != -1) {
						int row = table.getSelectedRow();
						String id = (String)table.getValueAt(row, 0);
						boolean editArgsEnabled = false;
						try {
							ModuleContainer container = ModuleLoader.getLoadedModule(id);
							editArgsEnabled = container.isParameterizable();
						} catch (Exception exc) {
							editArgsEnabled = true;
						}
						editArgsItem.setEnabled(editArgsEnabled);
						rightClickMenu.show(table, e.getX(), e.getY());
					} else if (e.getClickCount() == 2
							&& e.getButton() == MouseEvent.BUTTON1) {
						int selected = table.getSelectedRow();
						if (selected != -1) {
							table.editCellAt(selected, 1);
						}
					}
				}
				public void mouseReleased(MouseEvent e) {
				}
			});
			JScrollPane scrollPane = new JScrollPane(table);
			
			JPanel p = new JPanel(new BorderLayout());
			p.add(scrollPane, BorderLayout.CENTER);
			p.add(getButtonPanel(), BorderLayout.SOUTH);
			add(p);
		}
		
		public JPanel getButtonPanel() {
			JPanel p = new JPanel(new GridLayout(0, 3));
			JButton remove = new JButton(Messages.getString("ModuleConfigurationPanel.REMOVE")); //$NON-NLS-1$
			remove.setToolTipText(Messages.getString("ModuleConfigurationPanel.REMOVE_TOOLTIP")); //$NON-NLS-1$
			remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] selected = table.getSelectedRows();
					for (int i = selected.length - 1; i >= 0; i--) {
						model.remove(selected[i]);
					}
				}
			});
			JButton shiftUp = new JButton(Messages.getString("ModuleConfigurationPanel.UP")); //$NON-NLS-1$
			shiftUp.setToolTipText(Messages.getString("ModuleConfigurationPanel.UP_TOOLTIP")); //$NON-NLS-1$
			shiftUp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					shiftSelectedUp();
				}
			});
			JButton shiftDown = new JButton(Messages.getString("ModuleConfigurationPanel.DOWN")); //$NON-NLS-1$
			shiftDown.setToolTipText(Messages.getString("ModuleConfigurationPanel.DOWN_TOOLTIP")); //$NON-NLS-1$
			shiftDown.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					shiftSelectedDown();
				}
			});
			p.add(remove);
			p.add(shiftUp);
			p.add(shiftDown);
			return p;
		}
		
		/**
		 * Shifts all selected elements up by one.
		 */
		public void shiftSelectedUp() {
			int[] selected = table.getSelectedRows();
			if (selected == null || selected.length == 0) {
				return;
			}
			for (int i = 0; i < selected.length; i++) {
				model.swap(selected[i]-1, selected[i]);
			}
			int first = selected[0] - 1;
			int last = selected[selected.length - 1] - 1;
			try {
				table.setRowSelectionInterval(first, last);
			} catch (Exception exc) {
			}
		}
		
		/**
		 * Shifts all selected elements down by one.
		 */
		public void shiftSelectedDown() {
			int[] selected = table.getSelectedRows();
			if (selected == null || selected.length == 0) {
				return;
			}
			for (int i = selected.length - 1; i >= 0; i--) {
				model.swap(selected[i], selected[i]+1);
			}
			int first = selected[0] + 1;
			int last = selected[selected.length - 1] + 1;
			try {
				table.setRowSelectionInterval(first, last);
			} catch (Exception exc) {
			}
		}
	}
	
	/**
	 * A table model that updates the table using listeners.
	 * @author Christoph Schwering (schwering@gmail.com)
	 */
	class LoadTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 4482500674100897664L;

		/**
		 * Stores all ids.
		 */
		private Vector<String> ids;
		
		/**
		 * Stores all arguments.
		 */
		private Vector<String> args;
		
		/**
		 * The column names.
		 */
		private String[] colNames = new String[] { 
				Messages.getString("ModuleAutoStartConfigurationPanel.MODULE"),   //$NON-NLS-1$ 
				Messages.getString("ModuleAutoStartConfigurationPanel.ARGUMENTS") //$NON-NLS-1$
		};
		
		/**
		 * Creates a new object. The constructor simply fills the vector 
		 * with the currently loaded modules.
		 */
		public LoadTableModel() {
			String[] idsArr = ModuleAutoStartConfiguration.getIds();
			String[] argsArr = ModuleAutoStartConfiguration.getArgs();
			ids = new Vector<String>(idsArr.length);
			args = new Vector<String>(idsArr.length);
			
			for (int i = 0; i < idsArr.length; i++) {
				ids.add(i, idsArr[i]);
				args.add(i, (argsArr[i] != null) ? argsArr[i] : ""); //$NON-NLS-1$
			}
		}
		
		public void add(String id, String arg) {
			ids.add(id);
			args.add(arg);
			int i = ids.size() - 1;
			fireTableRowsInserted(i, i);
			ModuleAutoStartConfiguration.addModule(id, arg);
		}
		
		public void updateArg(int row, String arg) {
			args.set(row, arg);
			fireTableCellUpdated(row, 1);
			ModuleAutoStartConfiguration.updateArg(row, arg);
		}
		
		public void remove(int i) {
			String id = (String)ids.remove(i);
			String arg = (String)args.remove(i);
			ModuleAutoStartConfiguration.remove(id, arg);
			fireTableRowsDeleted(i, i);
		}

		public void swap(int i, int j) {
			if (i < 0 || j < 0 || i >= ids.size() || j >= args.size()) {
				return;
			}
			String id1 = (String)ids.get(i);
			String arg1 = (String)args.get(i);
			String id2 = (String)ids.get(j);
			String arg2 = (String)args.get(j);
			ids.set(i, id2);
			ids.set(j, id1);
			args.set(i, arg2);
			args.set(j, arg1);
			fireTableRowsUpdated(i, j);
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
			return ids.size();
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
			if (col == 0) {
				return (String)ids.get(row);
			} else {
				return (String)args.get(row);
			}
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int row, int col) {
			if (col != 1) {
				return false;
			}
			try {
				String id = (String)ids.get(row);
				ModuleContainer container = ModuleLoader.getLoadedModule(id);
				return container.isParameterizable();
			} catch (Exception exc) {
				return false;
			}
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnClass(int)
		 */
		public Class<String> getColumnClass(int col) {
			return String.class;
		}
	}
	
	/**
	 * Draws a table with all available module ids.
	 */
	class ModulePanel extends JPanel {
		private static final long serialVersionUID = 1991518906418792176L;
		private ModuleAutoStartConfigurationPanel owner;
		private ModuleTableModel model = new ModuleTableModel();
		
		/**
		 * Draws a table with all module ids.
		 * @param o The owning moduleautostartconfigurationpanel.
		 */
		public ModulePanel(ModuleAutoStartConfigurationPanel o) {
			super(new GridLayout(1, 0));
			this.owner = o;
			setBorder(new TitledBorder(Messages.getString("ModuleAutoStartConfigurationPanel.AVAILABLE_MODULES"))); //$NON-NLS-1$
			
			final JTable table = new JTable(model);
			table.setToolTipText(Messages.getString("ModuleAutoStartConfigurationPanel.ALL_MODULES_THAT_ARE_AVAILBLE")); //$NON-NLS-1$
			final JPopupMenu rightClickMenu = new JPopupMenu();
			JMenuItem addItem = new JMenuItem(Messages.getString("ModuleAutoStartConfigurationPanel.ADD_TO_AUTOSTART")); //$NON-NLS-1$
			addItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] selected = table.getSelectedRows();
					for (int i = 0; i < selected.length; i++) {
						ModuleContainer mc = (ModuleContainer)table.getValueAt(selected[i], 0);
						owner.loadPanel.model.add(mc.getId(), "");
					}
				}
			});
			rightClickMenu.add(addItem);
			
			table.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
				}
				public void mouseEntered(MouseEvent e) {
				}
				public void mouseExited(MouseEvent e) {
				}
				public void mousePressed(MouseEvent e) {
					if (e.getClickCount() == 1 
							&& e.getButton() == MouseEvent.BUTTON3
							&& table.getSelectedRow() != -1) {
						rightClickMenu.show(table, e.getX(), e.getY());
					} else if (e.getClickCount() == 2
							&& e.getButton() == MouseEvent.BUTTON1) {
						int selected = table.getSelectedRow();
						if (selected != -1) {
							ModuleContainer mc = (ModuleContainer)table.getValueAt(selected, 0);
							owner.loadPanel.model.add(mc.getId(), "");
						}
					}
				}
				public void mouseReleased(MouseEvent e) {
				}
			});
			JScrollPane scrollPane = new JScrollPane(table);
			add(scrollPane);
		}
	}
	
	/**
	 * A table model that updates the table using listeners.
	 * @author Christoph Schwering (schwering@gmail.com)
	 */
	class ModuleTableModel extends AbstractTableModel 
	implements IModuleLoaderListener {
		private static final long serialVersionUID = 6212004722975790077L;

		/**
		 * Stores all currently loaded modules as 
		 * <code>ModuleContainer</code>s.
		 */
		private Vector<ModuleContainer> modules;
		
		/**
		 * The column names.
		 */
		private String[] colNames = new String[] { 
				Messages.getString("ModuleAutoStartConfigurationPanel.MODULE") //$NON-NLS-1$ 
		};
		
		/**
		 * Creates a new object. The constructor simply fills the vector 
		 * with the currently loaded modules.
		 */
		public ModuleTableModel() {
			ModuleContainer[] containers = ModuleLoader.getLoadedModules();
			modules = new Vector<ModuleContainer>(containers.length);
			for (int i = 0; i < containers.length; i++) {
				modules.add(containers[i]);
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
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return 1;
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
			return m;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnClass(int)
		 */
		public Class<?> getColumnClass(int col) {
			return getValueAt(0, col).getClass();
		}
	}
}