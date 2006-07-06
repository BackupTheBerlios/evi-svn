package org.schwering.evi.gui.conf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.schwering.evi.conf.LanguageAdministrator;
import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.gui.EVI;
import org.schwering.evi.util.RightClickMenu;
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
	public static final String DEFAULT_TITLE = Messages.getString("MainConfigurationPanel.DEFAULT_TITLE"); //$NON-NLS-1$
	
	private JButton saveButton;
	
	/**
	 * Gives access to the one and only instance of this panel.
	 * This avoids that the user might create a bunch of panels
	 * which would make no sense.
	 */
	private static MainConfigurationPanel instance = null;
	
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
	public static MainConfigurationPanel getInstance() {
		if (instance == null) {
			instance = new MainConfigurationPanel();
		}
		instanceCount++;
		return instance;
	}
	
	/**
	 * Creates a new instance of the configuration GUI.
	 * @see #getInstance()
	 */
	private MainConfigurationPanel() {
		super(new BorderLayout());
		setBorder(new TitledBorder(Messages.getString("MainConfigurationPanel.EVI_CONFIGURATION"))); //$NON-NLS-1$
		
		JPanel p = new JPanel(new GridLayout(9, 0));
		addLanguageChooser(p);
		addTabBarPositionChooser(p);
		addLookAndFeelChooser(p);
		addAskToExitCheckBox(p);
		addPrimaryFontSelector(p);
		addSecondaryFontSelector(p);
		addPrimaryColorChooser(p);
		addSecondaryColorChooser(p);
		addModuleListField(p);

		JPanel buttons = new JPanel();
		saveButton = new JButton(Messages.getString("MainConfigurationPanel.SAVE")); //$NON-NLS-1$
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
	
	private JComboBox language;

	private void addLanguageChooser(JPanel p) {
		Locale[] available = LanguageAdministrator.AVAILABLE_LOCALES;
		Wrapper[] objs = new Wrapper[available.length];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = new Wrapper(available[i].getDisplayLanguage(), available[i]);
		}
		language = new JComboBox(objs);
		language.setToolTipText(Messages.getString("MainConfigurationPanel.MAIN_APPLICATION_LANGUAGE")); //$NON-NLS-1$
		try {
			Locale current = MainConfiguration.PROPS.getLocale("app.lang"); //$NON-NLS-1$
			language.setSelectedIndex(find(current, objs));
		} catch (Exception exc) {
		}
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(language, BorderLayout.WEST);		
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel(Messages.getString("MainConfigurationPanel.LANGUAGE"))); //$NON-NLS-1$
		row.add(sub);
		p.add(row);
	}
	
	private JComboBox tabBarPosition;

	private void addTabBarPositionChooser(JPanel p) {
		Wrapper[] objs = new Wrapper[] {
				new Wrapper(Messages.getString("MainConfigurationPanel.TOP"), new Integer(JTabbedPane.TOP)), //$NON-NLS-1$
				new Wrapper(Messages.getString("MainConfigurationPanel.RIGHT"), new Integer(JTabbedPane.RIGHT)), //$NON-NLS-1$
				new Wrapper(Messages.getString("MainConfigurationPanel.BOTTOM"), new Integer(JTabbedPane.BOTTOM)), //$NON-NLS-1$
				new Wrapper(Messages.getString("MainConfigurationPanel.LEFT"), new Integer(JTabbedPane.LEFT)), //$NON-NLS-1$
		};
		tabBarPosition = new JComboBox(objs);
		tabBarPosition.setToolTipText(Messages.getString("MainConfigurationPanel.POSITION_OF_TABBAR")); //$NON-NLS-1$
		try {
			int current = MainConfiguration.PROPS.getInt("gui.tabs.placement"); //$NON-NLS-1$
			tabBarPosition.setSelectedIndex(find(new Integer(current), objs));
		} catch (Exception exc) {
		}
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(tabBarPosition, BorderLayout.WEST);		
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel(Messages.getString("MainConfigurationPanel.TABBAR_POSITION"))); //$NON-NLS-1$
		row.add(sub);
		p.add(row);
	}
	
	private JComboBox lookAndFeels;

	private void addLookAndFeelChooser(JPanel p) {
		String[] lafs = Util.getLookAndFeels();
		Wrapper[] objs = new Wrapper[lafs.length];
		for (int i = 0; i < lafs.length; i++) {
			int lastDot = lafs[i].lastIndexOf('.');
			String name = lafs[i].substring(lastDot + 1);
			objs[i] = new Wrapper(name, lafs[i]);
		}
		lookAndFeels = new JComboBox(objs);
		lookAndFeels.setToolTipText(Messages.getString("MainConfigurationPanel.APPEARANCE_OF_APPLICATION")+ //$NON-NLS-1$
				Messages.getString("MainConfigurationPanel.TAKES_EFFECT_AFTER_NEXT_START")); //$NON-NLS-1$
		try {
			String current = MainConfiguration.PROPS.getString("gui.lookandfeel"); //$NON-NLS-1$
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
		row.add(new JLabel(Messages.getString("MainConfigurationPanel.LOOK_AND_FEEL"))); //$NON-NLS-1$
		row.add(sub);
		p.add(row);
	}
	
	private JRadioButton askToExit;
	
	private void addAskToExitCheckBox(JPanel p) {
		boolean current = MainConfiguration.PROPS.getBoolean("gui.asktoexit", true); //$NON-NLS-1$
		JRadioButton yes = new JRadioButton(Messages.getString("MainConfigurationPanel.YES"), current); //$NON-NLS-1$
		JRadioButton no = new JRadioButton(Messages.getString("MainConfigurationPanel.NO"), !current); //$NON-NLS-1$
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
		row.add(new JLabel(Messages.getString("MainConfigurationPanel.ASK_TO_EXIT_WHEN_CLOSING"))); //$NON-NLS-1$
		row.add(sub);
		p.add(row);
	}

	private JComboBox primFontName;
	private JTextField primFontSize;
	private JComboBox primFontStyle;
	
	private void addPrimaryFontSelector(JPanel p) {
		Font defaultFont = new Font("SansSerif", Font.PLAIN, 12);
		Font current = MainConfiguration.PROPS.getFont("font.primary", defaultFont); //$NON-NLS-1$
		String currentFontName = current.getFamily();
		int currentFontSize = current.getSize();
		String currentFontStyle = Util.encodeFontStyle(current.getStyle());
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fonts = ge.getAvailableFontFamilyNames();
		primFontName = new JComboBox(fonts);
		primFontName.setSelectedItem(currentFontName);
		
		primFontSize = new JTextField(3);
		primFontSize.setText(String.valueOf(currentFontSize));
		RightClickMenu.addRightClickMenu(primFontSize);
		
		Wrapper[] objs = new Wrapper[4];
		objs[0] = new Wrapper(Messages.getString("MainConfigurationPanel.PLAIN"), "PLAIN"); //$NON-NLS-1$ //$NON-NLS-2$
		objs[1] = new Wrapper(Messages.getString("MainConfigurationPanel.BOLD"), "BOLD"); //$NON-NLS-1$ //$NON-NLS-2$
		objs[2] = new Wrapper(Messages.getString("MainConfigurationPanel.ITALIC"), "ITALIC"); //$NON-NLS-1$ //$NON-NLS-2$
		objs[3] = new Wrapper(Messages.getString("MainConfigurationPanel.BOLD_AND_ITALIC"), "BOLDITALIC"); //$NON-NLS-1$ //$NON-NLS-2$
		primFontStyle = new JComboBox(objs);
		primFontStyle.setSelectedIndex(find(currentFontStyle, objs));
		
		JPanel sub1 = new JPanel(new BorderLayout());
		sub1.add(primFontName);
		JPanel sub2 = new JPanel(new BorderLayout());
		sub2.add(primFontSize, BorderLayout.WEST);
		sub2.add(new JLabel("pt  "), BorderLayout.CENTER); //$NON-NLS-1$
		sub2.add(primFontStyle, BorderLayout.EAST);
		
		JPanel sub = new JPanel(new GridLayout(2, 0));
		sub.add(sub1);
		sub.add(sub2);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel(Messages.getString("MainConfigurationPanel.PRIMARY_FONT"))); //$NON-NLS-1$
		row.add(sub);
		p.add(row);
	}
	
	private JComboBox secFontName;
	private JTextField secFontSize;
	private JComboBox secFontStyle;
	
	private void addSecondaryFontSelector(JPanel p) {
		Font defaultFont = new Font("Monospaced", Font.PLAIN, 12);
		Font current = MainConfiguration.PROPS.getFont("font.secondary", defaultFont); //$NON-NLS-1$
		String currentFontName = current.getFamily();
		int currentFontSize = current.getSize();
		String currentFontStyle = Util.encodeFontStyle(current.getStyle());
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fonts = ge.getAvailableFontFamilyNames();
		secFontName = new JComboBox(fonts);
		secFontName.setSelectedItem(currentFontName);
		
		secFontSize = new JTextField(3);
		secFontSize.setText(String.valueOf(currentFontSize));
		RightClickMenu.addRightClickMenu(secFontSize);
		
		Wrapper[] objs = new Wrapper[4];
		objs[0] = new Wrapper(Messages.getString("MainConfigurationPanel.PLAIN"), "PLAIN"); //$NON-NLS-1$ //$NON-NLS-2$
		objs[1] = new Wrapper(Messages.getString("MainConfigurationPanel.BOLD"), "BOLD"); //$NON-NLS-1$ //$NON-NLS-2$
		objs[2] = new Wrapper(Messages.getString("MainConfigurationPanel.ITALIC"), "ITALIC"); //$NON-NLS-1$ //$NON-NLS-2$
		objs[3] = new Wrapper(Messages.getString("MainConfigurationPanel.BOLD_AND_ITALIC"), "BOLDITALIC"); //$NON-NLS-1$ //$NON-NLS-2$
		secFontStyle = new JComboBox(objs);
		secFontStyle.setSelectedIndex(find(currentFontStyle, objs));
		
		JPanel sub1 = new JPanel(new BorderLayout());
		sub1.add(secFontName);
		JPanel sub2 = new JPanel(new BorderLayout());
		sub2.add(secFontSize, BorderLayout.WEST);
		sub2.add(new JLabel("pt  "), BorderLayout.CENTER); //$NON-NLS-1$
		sub2.add(secFontStyle, BorderLayout.EAST);
		
		JPanel sub = new JPanel(new GridLayout(2, 0));
		sub.add(sub1);
		sub.add(sub2);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel(Messages.getString("MainConfigurationPanel.SECONDARY_FONT"))); //$NON-NLS-1$
		row.add(sub);
		p.add(row);
	}
	
	private Color primaryColor;
	
	private void addPrimaryColorChooser(JPanel p) {
		primaryColor = MainConfiguration.PROPS.getColor("color.primary", Color.RED); //$NON-NLS-1$
		
		final JButton label = new JButton("    "); //$NON-NLS-1$
		label.setBorderPainted(false);
		label.setEnabled(false);
		label.setBackground(primaryColor);
		
		final JButton choose = new JButton(Messages.getString("MainConfigurationPanel.CHOOSE_COLOR")); //$NON-NLS-1$
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color c = JColorChooser.showDialog(choose, 
						Messages.getString("MainConfigurationPanel.PRIMARY_COLOR"), primaryColor); //$NON-NLS-1$
				if (c != null) {
					primaryColor = c;
					label.setBackground(primaryColor);
				}
			}
		});
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(choose, BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(label, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel(Messages.getString("MainConfigurationPanel.PRIMARY_COLOR"))); //$NON-NLS-1$
		row.add(sub);
		p.add(row);
	}
	
	private Color secondaryColor;
	
	private void addSecondaryColorChooser(JPanel p) {
		secondaryColor = MainConfiguration.PROPS.getColor("color.secondary",  //$NON-NLS-1$
				Color.BLUE);
		
		final JButton label = new JButton("    "); //$NON-NLS-1$
		label.setBorderPainted(false);
		label.setEnabled(false);
		label.setBackground(secondaryColor);
		
		final JButton choose = new JButton(Messages.getString("MainConfigurationPanel.CHOOSE_COLOR")); //$NON-NLS-1$
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color c = JColorChooser.showDialog(choose, 
						Messages.getString("MainConfigurationPanel.SECONDARY_COLOR"), secondaryColor); //$NON-NLS-1$
				if (c != null) {
					secondaryColor = c;
					label.setBackground(secondaryColor);
				}
			}
		});
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(choose, BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(label, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel(Messages.getString("MainConfigurationPanel.SECONDARY_COLOR"))); //$NON-NLS-1$
		row.add(sub);
		p.add(row);
	}
	
	private JTextField moduleList;
	
	private void addModuleListField(JPanel p) {
		String current = MainConfiguration.PROPS.getString("app.modulelist",  //$NON-NLS-1$
				ModuleConfigurationPanel.MODULE_LIST_URL);
		moduleList = new JTextField(current, 10);
		RightClickMenu.addRightClickMenu(moduleList);
		
		JButton reset = new JButton(Messages.getString("MainConfigurationPanel.RESET")); //$NON-NLS-1$
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				moduleList.setText(ModuleConfigurationPanel.MODULE_LIST_URL);
			}
		});
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(reset, BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(moduleList, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel(Messages.getString("MainConfigurationPanel.MODULE_LIST_URL"))); //$NON-NLS-1$
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
		
		w = (Wrapper)language.getSelectedItem();
		Locale lang = (Locale)w.getObject();
		MainConfiguration.PROPS.setLocale("app.lang", lang); //$NON-NLS-1$
		LanguageAdministrator.load();
		
		w = (Wrapper)tabBarPosition.getSelectedItem();
		int tabPos = ((Integer)w.getObject()).intValue();
		MainConfiguration.PROPS.setInt("gui.tabs.placement", tabPos); //$NON-NLS-1$
		EVI.getInstance().getMainFrame().getMainTabBar().setTabPlacement(tabPos);
		
		w = (Wrapper)lookAndFeels.getSelectedItem();
		String lookAndFeel = (String)w.getObject();
		MainConfiguration.PROPS.setString("gui.lookandfeel", lookAndFeel); //$NON-NLS-1$
		
		MainConfiguration.PROPS.setBoolean("gui.asktoexit", askToExit.isSelected()); //$NON-NLS-1$
		
		String primaryFontName = (String)primFontName.getSelectedItem();
		String primaryFontSize = primFontSize.getText().trim();
		w = (Wrapper)primFontStyle.getSelectedItem();
		String primaryFontStyle = (String)w.getObject();
		Font primaryFont = Util.decodeFont(primaryFontName, primaryFontSize, 
				primaryFontStyle);
		MainConfiguration.PROPS.setFont("font.primary", primaryFont); //$NON-NLS-1$
		
		String secondaryFontName = (String)secFontName.getSelectedItem();
		String secondaryFontSize = secFontSize.getText().trim();
		w = (Wrapper)secFontStyle.getSelectedItem();
		String secondaryFontStyle = (String)w.getObject();
		Font secondaryFont = Util.decodeFont(secondaryFontName, secondaryFontSize, 
				secondaryFontStyle);
		MainConfiguration.PROPS.setFont("font.secondary", secondaryFont); //$NON-NLS-1$
		
		MainConfiguration.PROPS.setColor("color.primary", primaryColor); //$NON-NLS-1$

		MainConfiguration.PROPS.setColor("color.secondary", secondaryColor); //$NON-NLS-1$
	
		String moduleListURL = moduleList.getText();
		MainConfiguration.PROPS.setString("app.modulelist", moduleListURL); //$NON-NLS-1$
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
			save();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#requestFocusInWindow()
	 */
	public boolean requestFocusInWindow() {
		return super.requestFocusInWindow();
	}
}
