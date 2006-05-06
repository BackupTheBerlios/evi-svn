package org.schwering.evi.gui.conf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.gui.EVI;
import org.schwering.evi.util.Util;

/**
 * A GUI to setup the main application.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class MainConfigurationPanel extends JPanel implements IPanel {
	private static final long serialVersionUID = 3298287361548588732L;

	/**
	 * The panel's title.
	 */
	public static final String DEFAULT_TITLE = "Configuration";
	
	/**
	 * Gives access to the one and only instance of the configuration panel.
	 * This avoids that the user might create a bunch of configuration panels
	 * which would make no sense.
	 */
	private static MainConfigurationPanel instance = null;
	
	private JComboBox tabBarPosition;
	private JComboBox lookAndFeels;
	
	/**
	 * Creates one initial instance and returns it in future until 
	 * <code>dispose()</code> is invoked.
	 * @return The current instance.
	 */
	public static MainConfigurationPanel getInstance() {
		if (instance == null) {
			instance = new MainConfigurationPanel();
		}
		return instance;
	}
	
	/**
	 * Creates a new instance of the configuration GUI.
	 * @see #getInstance()
	 */
	private MainConfigurationPanel() {
		super(new GridLayout(1, 0));
		JPanel p = new JPanel(new GridLayout(3, 2));
		addTabBarPositionChooser(p);
		addLookAndFeelChooser(p);
		JScrollPane scrollPane = new JScrollPane(p);

		JPanel buttons = new JPanel();
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		buttons.add(saveButton);
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(scrollPane, BorderLayout.CENTER);
		main.add(buttons, BorderLayout.SOUTH);
		add(main);
	}
	
	/**
	 * Adds a combobox with the positions.
	 * @param p The owning panel.
	 */
	private void addTabBarPositionChooser(JPanel p) {
		Wrapper[] objs = new Wrapper[] {
				new Wrapper("Top", new Integer(JTabbedPane.TOP)),
				new Wrapper("Right", new Integer(JTabbedPane.RIGHT)),
				new Wrapper("Bottom", new Integer(JTabbedPane.BOTTOM)),
				new Wrapper("Left", new Integer(JTabbedPane.LEFT)),
		};
		tabBarPosition = new JComboBox(objs);
		tabBarPosition.setToolTipText("The position of the main tabbar.");
		try {
			int current = MainConfiguration.getInt("gui.tabs.placement");
			tabBarPosition.setSelectedIndex(find(new Integer(current), objs));
		} catch (Exception exc) {
		}
		p.add(new JLabel("Tabbar position:"));
		p.add(new JScrollPane(tabBarPosition));
	}
	
	private void addLookAndFeelChooser(JPanel p) {
		String[] lafs = Util.getLookAndFeels();
		Wrapper[] objs = new Wrapper[lafs.length];
		for (int i = 0; i < lafs.length; i++) {
			int lastDot = lafs[i].lastIndexOf('.');
			String name = lafs[i].substring(lastDot + 1);
			objs[i] = new Wrapper(name, lafs[i]);
		}
		lookAndFeels = new JComboBox(objs);
		lookAndFeels.setToolTipText("The appearance of the application. "+
				"Takes effect after the next start.");
		try {
			String current = MainConfiguration.getString("gui.lookandfeel");
		    EVI.getInstance().getMainFrame().repaint();
			lookAndFeels.setSelectedIndex(find(current, objs));
		} catch (Exception exc) {
		}
		p.add(new JLabel("Look and Feel:"));
		p.add(lookAndFeels);
	}
	
	/**
	 * Looks for a given object in an array of wrappers.
	 * @param searched The object which is searched.
	 * @param objs The objects wrapped by the <code>Wrapper</code>s in this 
	 * array are compared to <code>searched</code>. 
	 * @return The index or -1.
	 */
	private static int find(Object searched, Wrapper[] objs) {
		if (objs == null) {
			return -1;
		}
		for (int i = 0; i < objs.length; i++) {
			if (objs[i].getObject() != null 
					&& objs[i].getObject().equals(searched)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Wraps any object.
	 */
	class Wrapper {
		private Object object;
		private String string;
		
		public Wrapper(String key, Object obj) {
			string = key;
			object = obj;
		}
		
		public Object getObject() {
			return object;
		}
		
		public String toString() {
			return string;
		}
	}
	
	private void save() {
		Wrapper w;
		
		w = (Wrapper)tabBarPosition.getSelectedItem();
		int tabPos = ((Integer)w.getObject()).intValue();
		MainConfiguration.setInt("gui.tabs.placement", tabPos);
		EVI.getInstance().getMainFrame().getMainTabBar().setTabPlacement(tabPos);
		
		w = (Wrapper)lookAndFeels.getSelectedItem();
		String lookAndFeel = (String)w.getObject();
		MainConfiguration.setString("gui.lookandfeel", lookAndFeel);
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
		save();
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.Component#requestFocusInWindow()
	 */
	public boolean requestFocusInWindow() {
		boolean b1 = super.requestFocusInWindow();
		boolean b2 = true;
		return b1 && b2;
	}
}
