package org.schwering.evi.gui.conf;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.schwering.evi.conf.ModuleAutoStartConfiguration;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleLoader;

/**
 * A GUI to configure the module auto start list.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ModuleAutoStartConfigurationPanel extends JPanel 
implements IPanel {
	private static final long serialVersionUID = 420606842791596917L;


	/**
	 * The panel's title.
	 */
	public static final String DEFAULT_TITLE = "Auto Start";
	
	
	/**
	 * Gives access to the one and only instance of the configuration panel.
	 * This avoids that the user might create a bunch of configuration panels
	 * which would make no sense.
	 */
	private static ModuleAutoStartConfigurationPanel instance = null;
	
	/**
	 * Creates one initial instance and returns it in future until 
	 * <code>dispose()</code> is invoked.
	 * @return The current instance.
	 */
	public static ModuleAutoStartConfigurationPanel getInstance() {
		if (instance == null) {
			instance = new ModuleAutoStartConfigurationPanel();
		}
		return instance;
	}
	
	private LoadPanel loadPanel = new LoadPanel(this);
	private ModulePanel modulePanel = new ModulePanel(this);
	
	/**
	 * Creates a new instance of the configuration GUI.
	 * @see #getInstance()
	 */
	private ModuleAutoStartConfigurationPanel() {
		super(new GridLayout(2, 0));
		add(modulePanel);
		add(loadPanel);
	}
	
	/**
	 * Removes the loadpanel, creates a new one and adds it again. 
	 * This method is a quite brutal way to reload the table :-).
	 */
	private void reloadLoadPanel() {
		remove(loadPanel);
		loadPanel = new LoadPanel(this);
		add(loadPanel);
		revalidate();
		repaint();
	}
	
	
	/**
	 * Draws a table with all modules that are automatically loaded.
	 */
	class LoadPanel extends JPanel {
		private static final long serialVersionUID = 7788432414929181492L;
		private ModuleAutoStartConfigurationPanel owner;
		
		/**
		 * Draws a table with all modules, their version and their requirements.
		 * @param o The owning moduleconfigurationpanel.
		 */
		public LoadPanel(ModuleAutoStartConfigurationPanel o) {
			super(new GridLayout(1, 0));
			this.owner = o;
			setBorder(new TitledBorder("Start automatically:"));
			String[] ids = ModuleAutoStartConfiguration.getIds();
			String[] args = ModuleAutoStartConfiguration.getArgs();
			
			String[][] data = new String[ids.length][2];
			for (int i = 0; i < data.length; i++) {
				data[i][0] = ids[i];
				data[i][1] = (args[i] != null) ? args[i] : "";
			}
			String[] header = new String[] { "Module", "Arguments "}; 
			
			final JTable table = new JTable(data, header);
			table.setToolTipText("Double click the row where you want to "+
					"modify the arguments.");
			table.getModel().addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					if (e.getType() == TableModelEvent.UPDATE) {
						ModuleAutoStartConfiguration.removeAll();
						int rowcount = table.getRowCount();
						for (int i = 0; i < rowcount; i++) {
							String id = (String)table.getModel().getValueAt(i, 0);
							String arg = (String)table.getModel().getValueAt(i, 1);
							ModuleAutoStartConfiguration.addModule(id, arg);
						}
					}
				}
			});
			final JPopupMenu rightClickMenu = new JPopupMenu();
			JMenuItem editArgsItem = new JMenuItem("Edit Arguments");
			editArgsItem.setToolTipText("Arguments are transmitted to the "+
					"module when it starts. Put in your arguments in the "+
					"right column.");
			editArgsItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int[] selected = table.getSelectedRows();
					if (selected.length > 0) {
						table.editCellAt(selected[0], 1);
					}
				}
			});
			rightClickMenu.add(editArgsItem);
			JMenuItem removeItem = new JMenuItem("Remove");
			removeItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] selected = table.getSelectedRows();
					for (int i = 0; i < selected.length; i++) {
						String id = (String)table.getModel().getValueAt(selected[i], 0);
						String arg = (String)table.getModel().getValueAt(selected[i], 1);
						ModuleAutoStartConfiguration.remove(id, arg);
					}
					owner.reloadLoadPanel();
				}
			});
			rightClickMenu.add(removeItem);
			
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
							table.editCellAt(selected, 1);
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
	 * Draws a table with all available module ids.
	 */
	class ModulePanel extends JPanel {
		private static final long serialVersionUID = 1991518906418792176L;
		private ModuleAutoStartConfigurationPanel owner;
		
		/**
		 * Draws a table with all module ids.
		 * @param o The owning moduleautostartconfigurationpanel.
		 */
		public ModulePanel(ModuleAutoStartConfigurationPanel o) {
			super(new GridLayout(1, 0));
			this.owner = o;
			setBorder(new TitledBorder("Available modules (select -> "+
					"right click -> add):"));
			ModuleContainer[] containers = ModuleLoader.getLoadedModules();
			String[][] data = new String[containers.length][1];
			for (int i = 0; i < data.length; i++) {
				data[i][0] = containers[i].getId();
			}
			String[] header = new String[] { "Module" }; 
			
			final JTable table = new JTable(data, header) {
				private static final long serialVersionUID = 370084356696900374L;
				public boolean isCellEditable(int x, int y) {
					return false;
				}
			};
			table.setToolTipText("All modules that are currently available "+
					"and thus could be started automatically are listed in "+
					"this table.");
			final JPopupMenu rightClickMenu = new JPopupMenu();
			JMenuItem addItem = new JMenuItem("Add to Autostart");
			addItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] selected = table.getSelectedRows();
					for (int i = 0; i < selected.length; i++) {
						String id = (String)table.getModel().getValueAt(selected[i], 0);
						ModuleAutoStartConfiguration.addModule(id, "");
					}
					owner.reloadLoadPanel();
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
							String id = (String)table.getModel().getValueAt(selected, 0);
							ModuleAutoStartConfiguration.addModule(id, "");
							owner.reloadLoadPanel();
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
}