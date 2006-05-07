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

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.conf.ModuleConfiguration;
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
implements IPanel, IModuleLoaderListener {
	private static final long serialVersionUID = 7345516016302120010L;

	/**
	 * The panel's title.
	 */
	public static final String DEFAULT_TITLE = "Modules";
	
	/**
	 * The default module list URL.
	 */
	private static final String MODULE_LIST_URL = "http://evi.berlios.de/modulelist"
	
	/**
	 * Gives access to the one and only instance of the configuration panel.
	 * This avoids that the user might create a bunch of configuration panels
	 * which would make no sense.
	 */
	private static ModuleConfigurationPanel instance = null;
	
	/**
	 * Creates one initial instance and returns it in future until 
	 * <code>dispose()</code> is invoked.
	 * @return The current instance.
	 */
	public static ModuleConfigurationPanel getInstance() {
		if (instance == null) {
			instance = new ModuleConfigurationPanel();
		}
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
		ModuleLoader.addListener(this);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleLoaderListener#loaded(org.schwering.evi.core.ModuleContainer)
	 */
	public void loaded(ModuleContainer loadedModule) {
		reloadTablePanel();
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IModuleLoaderListener#unloaded(org.schwering.evi.core.ModuleContainer)
	 */
	public void unloaded(ModuleContainer unloadedModule) {
		reloadTablePanel();
	}

	/**
	 * Removes the tablepanel, creates a new one and adds it again. 
	 * This method is a quite brutal way to reload the table :-).
	 */
	private void reloadTablePanel() {
		remove(tablePanel);
		tablePanel = new TablePanel();
		add(tablePanel, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	/**
	 * Gives a user interface to put in a URL from where a module can be 
	 * (down)loaded.
	 */
	class InputPanel extends JPanel {
		private static final long serialVersionUID = -3316773806722992040L;
		private JComboBox urlComboBox;
		private ModuleConfigurationPanel owner;
		
		/**
		 * Creates a new input user interface.
		 * @param o The owning moduleconfigurationpanel.
		 */
		public InputPanel(ModuleConfigurationPanel o) {
			super(new GridLayout(3, 1));
			this.owner = o;
			setBorder(new TitledBorder("Load new module"));
			add(createLoadByJARPanel());
			add(new JLabel("or"));
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
				setURLText(MainConfiguration.CONFIG_DIR.toURL());
			} catch (Exception exc) {
			}
			
			final JFileChooser urlFileChooser = new JFileChooser();
			urlFileChooser.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					return f.isDirectory() 
						|| f.toString().toLowerCase().endsWith(".jar");
				}
				public String getDescription() {
					return "Java Archive (*.jar)";
				}
			});
			
			JButton urlChooseButton = new JButton("Choose");
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
			
			JButton urlAddButton = new JButton("Add");
			urlAddButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new Thread() {
						public void run() {
							try {
								URL url = getURLText();
								ModuleLoader.load(url);
								ModuleConfiguration.addURL(url);
							} catch (Exception exc) {
								ExceptionDialog.show("Could not load module", 
										exc);
							}
						}
					}.start();
				}
			});
			
			JButton getList = new JButton("Get List");
			getList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					obtainUrlsFromInternet();
				}
			});
			
			JPanel urlButtonPanel = new JPanel(new GridLayout(1, 3));
			urlButtonPanel.add(urlChooseButton);
			urlButtonPanel.add(urlAddButton);
			urlButtonPanel.add(getList);
			
			JPanel p1 = new JPanel(new BorderLayout());
			p1.add(new JLabel("URL: "), BorderLayout.WEST);
			p1.add(urlComboBox, BorderLayout.CENTER);
			p1.add(urlButtonPanel, BorderLayout.EAST);
			return p1;
		}
		
		/**
		 * Returns a JPanel that allows to load a module by classname.
		 * @return The JPanel.
		 */
		private JPanel createLoadByClassNamePanel() {
			final JTextField classNameTextField = new JTextField();
			RightClickMenu.addRightClickMenu(classNameTextField);
			
			JButton addClassNameButton = new JButton("Add");
			addClassNameButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new Thread() {
						public void run() {
							try {
								String className = classNameTextField.getText();
								ModuleLoader.load(className);
								ModuleConfiguration.addClassName(className);
							} catch (Exception exc) {
								ExceptionDialog.show("Could not load module", 
										exc);
							}
						}
					}.start();
				}
			});
			
			JPanel classNameButtonPanel = new JPanel(new GridLayout(1, 1));
			classNameButtonPanel.add(addClassNameButton);
			
			JPanel p2 = new JPanel(new BorderLayout());
			p2.add(new JLabel("Classname: "), BorderLayout.WEST);
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
						URL url = new URL(MODULE_LIST_URL);
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
						ExceptionDialog.show("Obtaining module list failed", exc);
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
				urlComboBox.setSelectedItem("file://");
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
					ExceptionDialog.show("Unexpected exception", e);
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
	 */
	class TablePanel extends JPanel {
		private static final long serialVersionUID = 6389410631669650830L;

		/**
		 * Draws a table with all modules, their version and their requirements.
		 * @param o The owning moduleconfigurationpanel.
		 */
		public TablePanel() {
			super(new GridLayout(1, 0));
			ModuleContainer[] containers = ModuleLoader.getLoadedModules();
			Object[][] data = new Object[containers.length][4];
			for (int i = 0; i < data.length; i++) {
				StringBuffer buf = new StringBuffer();
				Requirement[] reqs = containers[i].getRequirements();
				for (int j = 0; j < reqs.length; j++) {
					buf.append(reqs[i].toString());
				}
				data[i][0] = containers[i].getSource();
				data[i][1] = containers[i].getId();
				data[i][2] = String.valueOf(containers[i].getVersion());
				data[i][3] = buf.toString();
			}
			String[] header = new String[] { "Destination", "Module",
					"Version", "Requirements"}; 
						
			final JTable table = new JTable(data, header) {
				private static final long serialVersionUID = -4479060561124641756L;
				public boolean isCellEditable(int x, int y) {
					return false;
				}
			}; 
			table.getColumnModel().getColumn(0).setPreferredWidth(150);
			table.getColumnModel().getColumn(1).setPreferredWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(10);
			table.getColumnModel().getColumn(3).setPreferredWidth(150);
			table.sizeColumnsToFit(-1);

			final JPopupMenu rightClickMenu = new JPopupMenu();
			JMenuItem removeItem = new JMenuItem("Remove");
			removeItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] selected = table.getSelectedRows();
					for (int i = 0; i < selected.length; i++) {
						Object source = table.getModel().getValueAt(selected[i], 0);
						String id = (String)table.getModel().getValueAt(selected[i], 1);
						ModuleConfiguration.remove(source);
						ModuleLoader.unload(id);
					}
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
		boolean b2 = (inputPanel != null) ? inputPanel.requestFocusInWindow() 
				: true;
		return b1 && b2;
	}
}
