/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui.conf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import org.schwering.evi.gui.EVI;
import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.irc.conf.FullProfile;
import org.schwering.evi.irc.conf.DefaultValues;
import org.schwering.evi.util.ColorSelector;
import org.schwering.evi.util.ExceptionDialog;
import org.schwering.evi.util.RightClickMenu;
import org.schwering.evi.util.FontSelector;

public class ProfileConfiguration extends JPanel {
	private static final long serialVersionUID = -2004581161964697319L;

	private FullProfile profile;
	
	public ProfileConfiguration() {
		JPanel connectionPanel = new JPanel(new GridLayout(8, 1));
		addServerAndPort(connectionPanel);
		addSSL(connectionPanel);
		addPassword(connectionPanel);
		addNickname(connectionPanel);
		addUsername(connectionPanel);
		addRealname(connectionPanel);
		addPartMsg(connectionPanel);
		addQuitMsg(connectionPanel);

		JPanel appearancePanel = new JPanel(new GridLayout(8, 1));
		addOwnColorChooser(appearancePanel);
		addOtherColorChooser(appearancePanel);
		addNeutralColorChooser(appearancePanel);
		addEnableColors(appearancePanel);
		addPaletteColorChooser(appearancePanel);
		addConsoleFontSelector(appearancePanel);
		addChannelFontSelector(appearancePanel);
		addQueryFontSelector(appearancePanel);
		
		JPanel miscPanel = new JPanel(new GridLayout(8, 1));
		addReconnect(miscPanel);
		addRejoinOnKick(miscPanel);
		addBeepOnMention(miscPanel);
		addBeepOnQuery(miscPanel);
		addEnableLogging(miscPanel);
		addLoggingDir(miscPanel);
		addBrowser(miscPanel);
		addAcceptCerts(miscPanel);
		
		JPanel performPanel = new JPanel(new GridLayout(1, 1));
		addPerform(performPanel);

		JTabbedPane tabs = new JTabbedPane();
		JPanel tmp;
		
		tmp = new JPanel(new BorderLayout()); 
		tmp.add(connectionPanel, BorderLayout.WEST); 
		tabs.add("Connection", tmp);
		
		tmp = new JPanel(new BorderLayout()); 
		tmp.add(appearancePanel, BorderLayout.WEST); 
		tabs.add("Appearance", tmp);
		
		tmp = new JPanel(new BorderLayout()); 
		tmp.add(miscPanel, BorderLayout.WEST); 
		tabs.add("Miscellauneous", tmp);
		
		tabs.add("Perform", performPanel);
		
		JPanel p = new JPanel();
		p.add(tabs);
		
		JPanel buttons = new JPanel();
		final JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (profile != null) {
					profile.setServer(server.getText());
					try {
						int p = Integer.parseInt(port.getText());
						profile.setPort(p);
					} catch (Exception exc) {
						ExceptionDialog.show(exc);
					}
					profile.setSSL(ssl.isSelected());
					profile.setPassword(new String(password.getPassword()));
					profile.setNickname(nickname.getText());
					profile.setUsername(username.getText());
					profile.setRealname(realname.getText());
					profile.setPartMessage(partMsg.getText());
					profile.setQuitMessage(quitMsg.getText());
					profile.setOwnColor(ownColor.getColor());
					profile.setOtherColor(otherColor.getColor());
					profile.setNeutralColor(neutralColor.getColor());
					profile.setEnableColors(enableColors.isSelected());
					Color[] palette = new Color[DefaultValues.PALETTE_SIZE];
					for (int i = 0; i < DefaultValues.PALETTE_SIZE; i++) {
						palette[i] = colorPalette[i].getBackground();
					}
					profile.setColorPalette(palette);
					profile.setConsoleFont(consoleFont.getSelectedFont());
					profile.setChannelFont(channelFont.getSelectedFont());
					profile.setQueryFont(queryFont.getSelectedFont());
					profile.setReconnect(reconnect.isSelected());
					profile.setRejoinOnKick(rejoinOnKick.isSelected());
					profile.setBeepOnMention(beepOnMention.isSelected());
					profile.setBeepOnQuery(beepOnQuery.isSelected());
					profile.setEnableLogging(enableLogging.isSelected());
					profile.setLoggingDir(loggingDir.getText().toString());
					profile.setAcceptCerts(acceptCerts.isSelected());
					profile.setBrowser(browser.getText());
					profile.setPerform(perform.getText());
				}
			}
		});
		buttons.add(saveButton);
		
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		main.add(p, BorderLayout.CENTER);
		main.add(buttons, BorderLayout.SOUTH);
		
		setLayout(new BorderLayout());
		add(new JScrollPane(main), BorderLayout.WEST);
	}
	
	private JTextField server = new JTextField(10);
	private JTextField port = new JTextField(4);
	
	private void addServerAndPort(JPanel p) {
		RightClickMenu.addRightClickMenu(server);
		RightClickMenu.addRightClickMenu(port);
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(server, BorderLayout.WEST);
		sub.add(new JLabel(":"), BorderLayout.CENTER);
		sub.add(port, BorderLayout.EAST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Server / port:"));
		row.add(sub);
		p.add(row);
	}

	private JRadioButton ssl;
	
	private void addSSL(JPanel p) {
		JRadioButton yes = new JRadioButton("yes", false);
		JRadioButton no = new JRadioButton("no", true);
		ButtonGroup group = new ButtonGroup();
		group.add(yes);
		group.add(no);
		
		ssl = yes;

		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(yes, BorderLayout.WEST);
		sub.add(no, BorderLayout.CENTER);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("SSL connection:"));
		row.add(sub);
		p.add(row);
	}
	
	private JPasswordField password = new JPasswordField(10);
	
	private void addPassword(JPanel p) {
		RightClickMenu.addRightClickMenu(password);
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(password, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Password:"));
		row.add(sub);
		p.add(row);
	}
	
	private JTextField nickname = new JTextField(10);
	
	private void addNickname(JPanel p) {
		RightClickMenu.addRightClickMenu(nickname);
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(nickname, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Nickname:"));
		row.add(sub);
		p.add(row);
	}

	private JTextField username = new JTextField(10);

	private void addUsername(JPanel p) {
		RightClickMenu.addRightClickMenu(username);
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(username, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Username:"));
		row.add(sub);
		p.add(row);
	}

	private JTextField realname = new JTextField(10);
	
	private void addRealname(JPanel p) {
		RightClickMenu.addRightClickMenu(realname);
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(realname, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Realname:"));
		row.add(sub);
		p.add(row);
	}

	private JTextField quitMsg = new JTextField(10);
	
	private void addQuitMsg(JPanel p) {
		RightClickMenu.addRightClickMenu(quitMsg);
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(quitMsg, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Quit message:"));
		row.add(sub);
		p.add(row);
	}

	private JTextField partMsg = new JTextField(10);
	
	private void addPartMsg(JPanel p) {
		RightClickMenu.addRightClickMenu(partMsg);
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(partMsg, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Part message:"));
		row.add(sub);
		p.add(row);
	}
	
	private ColorSelector ownColor = new ColorSelector();
	
	private void addOwnColorChooser(JPanel p) {
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Own color:"));
		row.add(ownColor);
		p.add(row);
	}
	
	private ColorSelector otherColor = new ColorSelector();
	
	private void addOtherColorChooser(JPanel p) {
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Other's color:"));
		row.add(otherColor);
		p.add(row);
	}
	
	private ColorSelector neutralColor = new ColorSelector();
	
	private void addNeutralColorChooser(JPanel p) {
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Neutral color:"));
		row.add(neutralColor);
		p.add(row);
	}
	
	private JRadioButton enableColors;
	
	private void addEnableColors(JPanel p) {
		JRadioButton yes = new JRadioButton("yes", false);
		JRadioButton no = new JRadioButton("no", true);
		ButtonGroup group = new ButtonGroup();
		group.add(yes);
		group.add(no);
		
		enableColors = yes;

		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(yes, BorderLayout.WEST);
		sub.add(no, BorderLayout.CENTER);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Enable colors:"));
		row.add(sub);
		p.add(row);
	}
	
	private JButton[] colorPalette = new JButton[DefaultValues.PALETTE_SIZE];
	
	private void addPaletteColorChooser(JPanel p) {
		for (int i = 0; i < DefaultValues.PALETTE_SIZE; i++) {
			colorPalette[i] = new JButton("");
			colorPalette[i].setBorderPainted(false);
			colorPalette[i].setPreferredSize(new Dimension(25, 5));
			colorPalette[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton b = (JButton)e.getSource();
					Color oldColor = b.getBackground();
					Color newColor = JColorChooser.showDialog(EVI.getInstance().getMainFrame(), 
							"Color", oldColor);
					if (newColor != null) {
						b.setBackground(newColor);
					}
				}
			});
		}
		
		
		JPanel sub = new JPanel(new GridLayout(2, 8));
		for (int i = 0; i < DefaultValues.PALETTE_SIZE; i++) {
			sub.add(colorPalette[i]);
		}
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Color palette:"));
		row.add(sub);
		p.add(row);
	}
	
	private FontSelector consoleFont = new FontSelector();
	
	private void addConsoleFontSelector(JPanel p) {
		consoleFont.setSelectedFont(MainConfiguration.PROPS.getFont("font.secondary"));
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Console font:"));
		row.add(consoleFont);
		p.add(row);
	}
	
	private FontSelector channelFont = new FontSelector();
	
	private void addChannelFontSelector(JPanel p) {
		channelFont.setSelectedFont(MainConfiguration.PROPS.getFont("font.primary"));
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Channel font:"));
		row.add(channelFont);
		p.add(row);
	}
	
	private FontSelector queryFont = new FontSelector();
	
	private void addQueryFontSelector(JPanel p) {
		queryFont.setSelectedFont(MainConfiguration.PROPS.getFont("font.primary"));
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Query font:"));
		row.add(queryFont);
		p.add(row);
	}
	
	private JTextArea perform = new JTextArea();
	
	private void addPerform(JPanel p) {
		Font font = Font.decode("Monospaced");
		perform.setFont(font);
		RightClickMenu.addRightClickMenu(perform);
		JScrollPane scrollPane = new JScrollPane(perform);
		p.add(scrollPane);
	}
	
	private JRadioButton reconnect;
	
	private void addReconnect(JPanel p) {
		JRadioButton yes = new JRadioButton("yes", false);
		JRadioButton no = new JRadioButton("no", true);
		ButtonGroup group = new ButtonGroup();
		group.add(yes);
		group.add(no);
		
		reconnect = yes;

		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(yes, BorderLayout.WEST);
		sub.add(no, BorderLayout.CENTER);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Automatically reconnect:"));
		row.add(sub);
		p.add(row);
	}

	private JRadioButton rejoinOnKick;
	
	private void addRejoinOnKick(JPanel p) {
		JRadioButton yes = new JRadioButton("yes", false);
		JRadioButton no = new JRadioButton("no", true);
		ButtonGroup group = new ButtonGroup();
		group.add(yes);
		group.add(no);
		
		rejoinOnKick = yes;

		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(yes, BorderLayout.WEST);
		sub.add(no, BorderLayout.CENTER);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Rejoin after kick:"));
		row.add(sub);
		p.add(row);
	}
	
	private JRadioButton beepOnMention;
	
	private void addBeepOnMention(JPanel p) {
		JRadioButton yes = new JRadioButton("yes", false);
		JRadioButton no = new JRadioButton("no", true);
		ButtonGroup group = new ButtonGroup();
		group.add(yes);
		group.add(no);
		
		beepOnMention = yes;

		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(yes, BorderLayout.WEST);
		sub.add(no, BorderLayout.CENTER);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Beep when nick mentioned:"));
		row.add(sub);
		p.add(row);
	}
	
	private JRadioButton beepOnQuery;
	
	private void addBeepOnQuery(JPanel p) {
		JRadioButton yes = new JRadioButton("yes", false);
		JRadioButton no = new JRadioButton("no", true);
		ButtonGroup group = new ButtonGroup();
		group.add(yes);
		group.add(no);
		
		beepOnQuery = yes;

		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(yes, BorderLayout.WEST);
		sub.add(no, BorderLayout.CENTER);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Beep on incoming query:"));
		row.add(sub);
		p.add(row);
	}
	
	private JRadioButton enableLogging;
	
	private void addEnableLogging(JPanel p) {
		JRadioButton yes = new JRadioButton("yes", false);
		JRadioButton no = new JRadioButton("no", true);
		ButtonGroup group = new ButtonGroup();
		group.add(yes);
		group.add(no);
		
		enableLogging = yes;

		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(yes, BorderLayout.WEST);
		sub.add(no, BorderLayout.CENTER);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Enable logging:"));
		row.add(sub);
		p.add(row);
	}
	
	private JTextField loggingDir = new JTextField(10);
	
	private void addLoggingDir(JPanel p) {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final JButton button = new JButton("Choose");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = loggingDir.getText();
				if (s != null) {
					fileChooser.setSelectedFile(new File(s));
				}
				int ret = fileChooser.showOpenDialog(EVI.getInstance().getMainFrame());
				if (ret == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					if (f != null) {
						loggingDir.setText(f.toString());
					}
				}
			}
		});
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(loggingDir, BorderLayout.WEST);
		sub.add(button, BorderLayout.EAST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Logging directory:"));
		row.add(sub);
		p.add(row);
	}
	
	private JRadioButton acceptCerts;
	
	private void addAcceptCerts(JPanel p) {
		JRadioButton yes = new JRadioButton("yes", false);
		JRadioButton no = new JRadioButton("no", true);
		ButtonGroup group = new ButtonGroup();
		group.add(yes);
		group.add(no);
		
		acceptCerts = yes;

		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(yes, BorderLayout.WEST);
		sub.add(no, BorderLayout.CENTER);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Accept X509 certificates:"));
		row.add(sub);
		p.add(row);
	}
	
	private JTextField browser = new JTextField(10);
	
	private void addBrowser(JPanel p) {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		final JButton button = new JButton("Choose");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = browser.getText();
				if (s != null) {
					fileChooser.setSelectedFile(new File(s));
				}
				int ret = fileChooser.showOpenDialog(EVI.getInstance().getMainFrame());
				if (ret == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					if (f != null) {
						browser.setText(f.toString());
					}
				}
			}
		});
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(browser, BorderLayout.WEST);
		sub.add(button, BorderLayout.EAST);

		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Browser:"));
		row.add(sub);
		p.add(row);
	}
	
	public void setProfile(FullProfile p) {
		profile = p;
		setBorder(new TitledBorder("FullProfile "+ p));
		server.setText(p.getServer());
		port.setText(new Integer(p.getPort()).toString());
		ssl.setSelected(p.getSSL());
		password.setText(p.getPassword());
		nickname.setText(p.getNickname());
		username.setText(p.getUsername());
		realname.setText(p.getRealname());
		partMsg.setText(p.getPartMessage());
		quitMsg.setText(p.getQuitMessage());
		ownColor.setColor(p.getOwnColor());
		otherColor.setColor(p.getOtherColor());
		neutralColor.setColor(p.getNeutralColor());
		enableColors.setSelected(p.getEnableColors());
		Color[] palette = p.getColorPalette();
		for (int i = 0; i < DefaultValues.PALETTE_SIZE; i++) {
			colorPalette[i].setBackground(palette[i]);
		}
		consoleFont.setSelectedFont(p.getConsoleFont());
		channelFont.setSelectedFont(p.getChannelFont());
		queryFont.setSelectedFont(p.getQueryFont());
		reconnect.setSelected(p.getReconnect());
		rejoinOnKick.setSelected(p.getRejoinOnKick());
		beepOnMention.setSelected(p.getBeepOnMention());
		beepOnQuery.setSelected(p.getBeepOnQuery());
		enableLogging.setSelected(p.getEnableLogging());
		loggingDir.setText(p.getLoggingDir());
		acceptCerts.setSelected(p.getAcceptCerts());
		browser.setText(p.getBrowser());
		perform.setText(p.getPerform());
	}
}
