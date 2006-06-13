package org.schwering.evi.gui.conf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.core.IModuleLoaderListener;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleLoader;
import org.schwering.evi.core.Requirement;
import org.schwering.evi.util.ExceptionDialog;
import org.schwering.evi.util.RightClickMenu;

/**
 * A GUI to setup the module list.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ModuleConfigurationPanel extends JPanel 
implements IPanel {
	private static final long serialVersionUID = 7345516016302120010L;

	/**
	 * The panel's title.
	 */
	public static final String DEFAULT_TITLE = Messages.getString("ModuleConfigurationPanel.DEFAULT_TITLE"); //$NON-NLS-1$
	
	/**
	 * The default module list URL.
	 */
	public static final String MODULE_LIST_URL = Messages.getString("ModuleConfigurationPanel.EVI_BERLIOS_DE_MODULELIST"); //$NON-NLS-1$
	
	/**
	 * Gives access to the one and only instance of this panel.
	 * This avoids that the user might create a bunch of panels
	 * which would make no sense.
	 */
	private static ModuleConfigurationPanel instance = null;
	
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
	public static ModuleConfigurationPanel getInstance() {
		if (instance == null) {
			instance = new ModuleConfigurationPanel();
		}
		instanceCount++;
		return instance;
	}
		
	private InputPanel inputPanel = new InputPanel(this);
	private TablePanel tablePanel = new TablePanel();
	
	/**
	 * Creates a new instance of the configuration GUI.
	 * @see #getInstance()
	 */
	private ModuleConfigurationPanel() {
		super(new BorderLayout());
		add(inputPanel, BorderLayout.NORTH);
		add(tablePanel, BorderLayout.CENTER);
		add(getButtonPanel(), BorderLayout.SOUTH);
	}
	
	public JPanel getButtonPanel() {
		JPanel p = new JPanel(new GridLayout(0, 3));
		JButton remove = new JButton(Messages.getString("ModuleConfigurationPanel.REMOVE")); //$NON-NLS-1$
		remove.setToolTipText(Messages.getString("ModuleConfigurationPanel.REMOVE_TOOLTIP")); //$NON-NLS-1$
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTable table = tablePanel.getTable();
				int[] selected = table.getSelectedRows();
				for (int i = selected.length - 1; i >= 0; i--) {
					String id = (String)table.getModel().getValueAt(selected[i], 1);
					ModuleLoader.unload(id);
				}
			}
		});
		JButton shiftUp = new JButton(Messages.getString("ModuleConfigurationPanel.UP")); //$NON-NLS-1$
		shiftUp.setToolTipText(Messages.getString("ModuleConfigurationPanel.UP_TOOLTIP")); //$NON-NLS-1$
		shiftUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tablePanel.shiftSelectedUp();
			}
		});
		JButton shiftDown = new JButton(Messages.getString("ModuleConfigurationPanel.DOWN")); //$NON-NLS-1$
		shiftDown.setToolTipText(Messages.getString("ModuleConfigurationPanel.DOWN_TOOLTIP")); //$NON-NLS-1$
		shiftDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tablePanel.shiftSelectedDown();
			}
		});
		p.add(remove);
		p.add(shiftUp);
		p.add(shiftDown);
		return p;
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
			IModuleLoaderListener l = (IModuleLoaderListener)tablePanel.getModel();
			ModuleLoader.removeListener(l);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#requestFocusInWindow()
	 */
	public boolean requestFocusInWindow() {
		boolean b1 = super.requestFocusInWindow();
		boolean b2 = (inputPanel != null) ? inputPanel.requestFocusInWindow() 
				: true;
		return b1 && b2;
	}

	/**
	 * Gives a user interface to put in a URL from where a module can be 
	 * (down)loaded.
	 * @author Christoph Schwering (schwering@gmail.com)
	 */
	class InputPanel extends JPanel {
		private static final long serialVersionUID = -3316773806722992040L;
		private JPanel urlComboBoxContainer;
		private JComboBox urlComboBox;
		private JProgressBar loadingProgressBar;
		private ModuleConfigurationPanel owner;
		
		/**
		 * Creates a new input user interface.
		 * @param o The owning moduleconfigurationpanel.
		 */
		public InputPanel(ModuleConfigurationPanel o) {
			super(new GridLayout(3, 1));
			this.owner = o;
			setBorder(new TitledBorder(Messages.getString("ModuleConfigurationPanel.LOAD_NEW_MODULE") +":")); //$NON-NLS-1$ //$NON-NLS-2$
			add(createLoadByJARPanel());
			add(new JLabel(Messages.getString("ModuleConfigurationPanel.OR"))); //$NON-NLS-1$
			add(createLoadByClassNamePanel());
		}
		
		/**
		 * Creates the panel that allows to load a module from a JAR.
		 * @return The JPanel.
		 */
		private JPanel createLoadByJARPanel() {
			urlComboBox = new JComboBox();
			urlComboBox.setEditable(true);
			try {
				setURLText(MainConfiguration.HOME_DIR.toURL());
			} catch (Exception exc) {
			}
			
			final JFileChooser urlFileChooser = new JFileChooser();
			urlFileChooser.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					return f.isDirectory() 
						|| f.toString().toLowerCase().endsWith(".jar"); //$NON-NLS-1$
				}
				public String getDescription() {
					return Messages.getString("ModuleConfigurationPanel.JAVA_ARCHIVE"); //$NON-NLS-1$
				}
			});
			
			JButton urlChooseButton = new JButton(Messages.getString("ModuleConfigurationPanel.CHOOSE_FILE")); //$NON-NLS-1$
			urlChooseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					File current;
					try {
						current = new File(new URI(getURLText().toString()));
					} catch (Exception exc) {
						current = null;
					}
					if (current != null && current.isDirectory()) {
						urlFileChooser.setCurrentDirectory(current);
					} else if (current != null && current.isFile()) {
						urlFileChooser.setSelectedFile(current);
					}
					int ret = urlFileChooser.showOpenDialog(owner);
					if (ret == JFileChooser.APPROVE_OPTION) {
						try {
							setURLText(urlFileChooser.getSelectedFile().toURL());
						} catch (Exception exc) {
						}
					}
				}
			});
			
			final JButton urlAddButton = new JButton(Messages.getString("ModuleConfigurationPanel.ADD")); //$NON-NLS-1$
			urlAddButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new Thread() {
						public void run() {
							try {
								URL url = getURLText();
								urlAddButton.setEnabled(false);
								enableProgressBar();
								ModuleLoader.load(url);
							} catch (Exception exc) {
								ExceptionDialog.show(Messages.getString("ModuleConfigurationPanel.COULD_NOT_LOAD_MODULE"),  //$NON-NLS-1$
										exc);
							}
							urlAddButton.setEnabled(true);
							disableProgressBar();
						}
					}.start();
				}
			});
			
			JButton getList = new JButton(Messages.getString("ModuleConfigurationPanel.GET_LIST")); //$NON-NLS-1$
			getList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					obtainUrlsFromInternet();
				}
			});
			
			urlComboBoxContainer = new JPanel(new GridLayout(1, 0));
			urlComboBoxContainer.add(urlComboBox);
			
			JPanel urlButtonPanel = new JPanel(new GridLayout(1, 3));
			urlButtonPanel.add(urlChooseButton);
			urlButtonPanel.add(urlAddButton);
			urlButtonPanel.add(getList);
			
			JPanel p1 = new JPanel(new BorderLayout());
			p1.add(new JLabel(Messages.getString("ModuleConfigurationPanel.URL") +": "), BorderLayout.WEST); //$NON-NLS-1$ //$NON-NLS-2$
			p1.add(urlComboBoxContainer, BorderLayout.CENTER);
			p1.add(urlButtonPanel, BorderLayout.EAST);
			return p1;
		}
		
		/**
		 * Starts and adds the indeterminate progressbar.
		 */
		private void enableProgressBar() {
			loadingProgressBar = new JProgressBar();
			loadingProgressBar.setStringPainted(true);
			loadingProgressBar.setString(Messages.getString("ModuleConfigurationPanel.LOADING_MODULE")); //$NON-NLS-1$
			loadingProgressBar.setIndeterminate(true);
			loadingProgressBar.setToolTipText(Messages.getString("ModuleConfigurationPanel.LOADING_MODULE_TOOLTIP")); //$NON-NLS-1$
			urlComboBoxContainer.remove(urlComboBox);
			urlComboBoxContainer.add(loadingProgressBar);
			urlComboBoxContainer.revalidate();
			urlComboBoxContainer.repaint();
		}
		
		/**
		 * Removes the indeterminate progressbar.
		 */
		private void disableProgressBar() {
			urlComboBoxContainer.remove(loadingProgressBar);
			loadingProgressBar = null;
			urlComboBoxContainer.add(urlComboBox);
			urlComboBoxContainer.revalidate();
			urlComboBoxContainer.repaint();
		}
		
		/**
		 * Returns a JPanel that allows to load a module by classname.
		 * @return The JPanel.
		 */
		private JPanel createLoadByClassNamePanel() {
			final JTextField classNameTextField = new JTextField();
			RightClickMenu.addRightClickMenu(classNameTextField);
			
			JButton addClassNameButton = new JButton(Messages.getString("ModuleConfigurationPanel.ADD")); //$NON-NLS-1$
			addClassNameButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new Thread() {
						public void run() {
							try {
								String className = classNameTextField.getText();
								ModuleLoader.load(className);
							} catch (Exception exc) {
								ExceptionDialog.show(Messages.getString("ModuleConfigurationPanel.COULD_NOT_LOAD_MODULE"),  //$NON-NLS-1$
										exc);
							}
						}
					}.start();
				}
			});
			
			JPanel classNameButtonPanel = new JPanel(new GridLayout(1, 1));
			classNameButtonPanel.add(addClassNameButton);
			
			JPanel p2 = new JPanel(new BorderLayout());
			p2.add(new JLabel(Messages.getString("ModuleConfigurationPanel.CLASSNAME") +": "), BorderLayout.WEST); //$NON-NLS-1$ //$NON-NLS-2$
			p2.add(classNameTextField, BorderLayout.CENTER);
			p2.add(classNameButtonPanel, BorderLayout.EAST);
			return p2;
		}
		
		/**
		 * Obtains a list of module URLs from the internet.
		 */
		private void obtainUrlsFromInternet() {
			new Thread() {
				public void run() {
					try {
						String strurl = MainConfiguration.PROPS.getString("app.modulelist",  //$NON-NLS-1$
								MODULE_LIST_URL);
						URL url = new URL(strurl);
						InputStream is = url.openStream();
						InputStreamReader isr = new InputStreamReader(is);
						BufferedReader br = new BufferedReader(isr);
						for (String s; (s = br.readLine()) != null; ) {
							s = s.trim();
							if (s.length() > 0 && s.charAt(0) != '#') {
								urlComboBox.addItem(s);
							}
						}
						br.close();
					} catch (Exception exc) {
						ExceptionDialog.show(Messages.getString("ModuleConfigurationPanel.OBTAINING_MODULE_FAILED"), exc); //$NON-NLS-1$
					}
				}
			}.start();
		}
		
		/**
		 * Sets the content of the textfield with a URL.
		 * @param url The new content of the textfield.
		 */
		private void setURLText(URL url) {
			try {
				urlComboBox.setSelectedItem(url.toString());
			} catch (Exception exc) {
				urlComboBox.setSelectedItem("file://"); //$NON-NLS-1$
			}
		}
		
		/**
		 * Returns the current content of the textfield als URL.
		 * @return The URL which is typed in the textfield.
		 */
		private URL getURLText() {
			try {
				String s = (String)urlComboBox.getSelectedItem();
				return new URL(s);
			} catch (Exception exc) {
				try {
					return MainConfiguration.CONFIG_DIR.toURL();
				} catch (Exception e) {
					ExceptionDialog.show(Messages.getString("ModuleConfigurationPanel.UNEXPECTED_EXCEPTION"), e); //$NON-NLS-1$
					return null;
				}
			}
		}

		/**
		 * Forwards the focus to the textfield.
		 */
		public boolean requestFocusInWindow() {
			boolean b1 = super.requestFocusInWindow();
			boolean b2 = (urlComboBox != null) 
				? urlComboBox.requestFocusInWindow() : true;
			return b1 && b2;
		}
	}
	
	/**
	 * Draws a table with all modules, their version and their requirements.
	 * @author Christoph Schwering (schwering@gmail.com)
	 */
	class TablePanel extends JPanel {
		private static final long serialVersionUID = 6389410631669650830L;
		private TableModel model = new TableModel();
		private JTable table = new JTable(model);

		/**
		 * Draws a table with all modules, their version and their requirements.
		 */
		public TablePanel() {
			super(new GridLayout(1, 0));
			table.getColumnModel().getColumn(0).setPreferredWidth(150);
			table.getColumnModel().getColumn(1).setPreferredWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(10);
			table.getColumnModel().getColumn(3).setPreferredWidth(150);
			table.getColumnModel().getColumn(4).setPreferredWidth(50);
			table.sizeColumnsToFit(-1);

			final JPopupMenu rightClickMenu = new JPopupMenu();
			JMenuItem removeItem = new JMenuItem(Messages.getString("ModuleConfigurationPanel.REMOVE")); //$NON-NLS-1$
			removeItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] selected = table.getSelectedRows();
					for (int i = selected.length - 1; i >= 0; i--) {
						String id = (String)table.getModel().getValueAt(selected[i], 1);
						ModuleLoader.unload(id);
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
						rightClickMenu.show(table, e.getX(), e.getY());
					}
				}
				public void mouseReleased(MouseEvent e) {
				}
			});
			JScrollPane scrollPane = new JScrollPane(table);
			add(scrollPane);
		}
		
		/**
		 * Returns the javax.swing.JTable.
		 * @return The JTable.
		 */
		public JTable getTable() {
			return table;
		}
		
		/**
		 * Returns the table model.
		 * @return The table model.
		 */
		public TableModel getModel() {
			return model;
		}
		
		/**
		 * Shifts all selected elements up by one.
		 */
		public void shiftSelectedUp() {
			JTable table = tablePanel.getTable();
			int[] selected = table.getSelectedRows();
			for (int i = 0; i < selected.length; i++) {
				tablePanel.getModel().swap(selected[i]-1, selected[i]);
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
			JTable table = tablePanel.getTable();
			int[] selected = table.getSelectedRows();
			for (int i = selected.length - 1; i >= 0; i--) {
				tablePanel.getModel().swap(selected[i], selected[i]+1);
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
	class TableModel extends AbstractTableModel 
	implements IModuleLoaderListener {
		private static final long serialVersionUID = 9083965451997722754L;

		/**
		 * Stores all currently loaded modules as 
		 * <code>ModuleContainer</code>s.
		 */
		private Vector modules;
		
		/**
		 * The column names.
		 */
		private String[] colNames = new String[] { 
				Messages.getString("ModuleConfigurationPanel.SOURCE"), //$NON-NLS-1$ 
				Messages.getString("ModuleConfigurationPanel.MODULE"), //$NON-NLS-1$
				Messages.getString("ModuleConfigurationPanel.VERSION"), //$NON-NLS-1$
				Messages.getString("ModuleConfigurationPanel.NAME"), //$NON-NLS-1$
				Messages.getString("ModuleConfigurationPanel.REQUIREMENTS") //$NON-NLS-1$
		};
		
		/**
		 * Creates a new object. The constructor simply fills the vector 
		 * with the currently loaded modules.
		 */
		public TableModel() {
			ModuleContainer[] containers = ModuleLoader.getLoadedModules();
			modules = new Vector(containers.length);
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
		
		
		public void swap(int i, int j) {
			if (i < 0 || j < 0 || i >= modules.size() || j >= modules.size()) {
				return;
			}
			ModuleContainer m1 = (ModuleContainer)modules.get(i);
			ModuleContainer m2 = (ModuleContainer)modules.get(j);
			int priority1 = m1.getPriority();
			int priority2 = m2.getPriority();
			m1.setPriority(priority2);
			m2.setPriority(priority1);
			modules.set(i, m2);
			modules.set(j, m1);
			fireTableRowsUpdated(i, j);
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return 5;
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
				return m.getSource();
			} else if (col == 1) {
				return m.getId();
			} else if (col == 2) {
				return new Float(m.getVersion());
			} else if (col == 3) {
				return m.getName();
			} else {
				Requirement[] reqs = m.getRequirements();
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < reqs.length; i++) {
					buf.append(reqs[i].toString());
					buf.append(" "); //$NON-NLS-1$
				}
				return buf.toString();
			}
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnClass(int)
		 */
		public Class getColumnClass(int col) {
			return getValueAt(0, col).getClass();
		}
	}
}
