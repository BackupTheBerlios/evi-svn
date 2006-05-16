package org.schwering.evi.gui.conf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.gui.EVI;
import org.schwering.evi.util.Util;

/**
 * A GUI to setup the main application.<br />
 * <br />
 * <b>Note:</b> This class contains code that is... <i>very</i> ugly.
 * Before someone tries to repair anything, I'd encourage him to rewrite the 
 * whole class.
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
	
	private JButton saveButton;
	private JComboBox language;
	private JComboBox tabBarPosition;
	private JComboBox lookAndFeels;
	private JRadioButton askToExit;
	private JComboBox primaryFontName;
	private JTextField primaryFontSize;
	private JComboBox primaryFontStyle;
	
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
		super(new BorderLayout());
		setBorder(new TitledBorder("EVI configuration:"));
		
		JPanel p = new JPanel(new GridLayout(5, 0));
		addLanguageChooser(p);
		addTabBarPositionChooser(p);
		addLookAndFeelChooser(p);
		addAskToExitCheckBox(p);
		addFontSelector(p);

		JPanel buttons = new JPanel();
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		buttons.add(saveButton);
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(p, BorderLayout.NORTH);
		main.add(buttons, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane(main);
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.WEST);
	}
	
	/**
	 * Adds a combobox with the available language files.
	 * @param p The owning panel.
	 */
	private void addLanguageChooser(JPanel p) {
		String[] langs = Util.getInstalledLanguages();
		language = new JComboBox(langs);
		language.setToolTipText("The application language");
		try {
			int current = MainConfiguration.getInt("app.lang");
			//language.setSelectedIndex();
		} catch (Exception exc) {
		}
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(language, BorderLayout.WEST);		
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Language:"));
		row.add(sub);
		p.add(row);
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
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(tabBarPosition, BorderLayout.WEST);		
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Tabbar position:"));
		row.add(sub);
		p.add(row);
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
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(lookAndFeels, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Look and Feel:"));
		row.add(sub);
		p.add(row);
	}
	
	private void addAskToExitCheckBox(JPanel p) {
		boolean current = MainConfiguration.getBoolean("gui.asktoexit", true);
		JRadioButton yes = new JRadioButton("Yes", current);
		JRadioButton no = new JRadioButton("No", !current);
		ButtonGroup group = new ButtonGroup();
		group.add(yes);
		group.add(no);

		askToExit = yes;
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(yes, BorderLayout.WEST);
		sub.add(no, BorderLayout.CENTER);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Ask to exit when closing:"));
		row.add(sub);
		p.add(row);
	}

	private void addFontSelector(JPanel p) {
		Font current = MainConfiguration.getFont("font.primary");
		String currentFontName = current.getFamily();
		int currentFontSize = current.getSize();
		String currentFontStyle = Util.encodeFontStyle(current.getStyle());
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fonts = ge.getAvailableFontFamilyNames();
		primaryFontName = new JComboBox(fonts);
		primaryFontName.setSelectedItem(currentFontName);
		
		primaryFontSize = new JTextField(2);
		primaryFontSize.setText(String.valueOf(currentFontSize));
		
		Wrapper[] objs = new Wrapper[4];
		objs[0] = new Wrapper("Plain", "PLAIN");
		objs[1] = new Wrapper("Bold", "BOLD");
		objs[2] = new Wrapper("Italic", "Italic");
		objs[3] = new Wrapper("Bold & Italic", "BOLDITALIC");
		primaryFontStyle = new JComboBox(objs);
		primaryFontStyle.setSelectedIndex(find(currentFontStyle, objs));
		
		JPanel sub1 = new JPanel(new BorderLayout());
		sub1.add(primaryFontName);
		JPanel sub2 = new JPanel(new BorderLayout());
		sub2.add(primaryFontSize, BorderLayout.WEST);
		sub2.add(new JLabel("pt  "), BorderLayout.CENTER);
		sub2.add(primaryFontStyle, BorderLayout.EAST);
		
		JPanel sub = new JPanel(new GridLayout(2, 0));
		sub.add(sub1);
		sub.add(sub2);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Primary font:"));
		row.add(sub);
		p.add(row);
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
		
		String lang = (String)language.getSelectedItem();
		MainConfiguration.setString("app.lang", lang);
		
		w = (Wrapper)tabBarPosition.getSelectedItem();
		int tabPos = ((Integer)w.getObject()).intValue();
		MainConfiguration.setInt("gui.tabs.placement", tabPos);
		EVI.getInstance().getMainFrame().getMainTabBar().setTabPlacement(tabPos);
		
		w = (Wrapper)lookAndFeels.getSelectedItem();
		String lookAndFeel = (String)w.getObject();
		MainConfiguration.setString("gui.lookandfeel", lookAndFeel);
		
		MainConfiguration.setBoolean("gui.asktoexit", askToExit.isSelected());
		
		String primaryFontName = (String)this.primaryFontName.getSelectedItem();
		String primaryFontSize = this.primaryFontSize.getText().trim();
		w = (Wrapper)this.primaryFontStyle.getSelectedItem();
		String primaryFontStyle = (String)w.getObject();
		Font primaryFont = Util.decodeFont(primaryFontName, primaryFontSize, 
				primaryFontStyle);
		MainConfiguration.setFont("font.primary", primaryFont);
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
		boolean b2 = saveButton.requestFocusInWindow();
		return b1 && b2;
	}
}
