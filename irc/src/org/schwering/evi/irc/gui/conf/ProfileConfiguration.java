package org.schwering.evi.irc.gui.conf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.irc.conf.Profile;
import org.schwering.evi.irc.conf.DefaultValues;
import org.schwering.evi.util.ExceptionDialog;
import org.schwering.evi.util.RightClickMenu;
import org.schwering.evi.util.FontSelector;

public class ProfileConfiguration extends JPanel {
	private Profile profile;
	
	public ProfileConfiguration() {
		super(new BorderLayout());
		setBorder(new TitledBorder("Profile"));
		
		JPanel connectionPanel = new JPanel(new GridLayout(8, 1));
		addServerAndPort(connectionPanel);
		addSSL(connectionPanel);
		addPassword(connectionPanel);
		addNickname(connectionPanel);
		addUsername(connectionPanel);
		addRealname(connectionPanel);
		addPartMsg(connectionPanel);
		addQuitMsg(connectionPanel);

		JPanel appearancePanel = new JPanel(new GridLayout(7, 1));
		addOwnColorChooser(appearancePanel);
		addOtherColorChooser(appearancePanel);
		addNeutralColorChooser(appearancePanel);
		addPaletteColorChooser(appearancePanel);
		addConsoleFontSelector(appearancePanel);
		addChannelFontSelector(appearancePanel);
		addQueryFontSelector(appearancePanel);
		
		JPanel miscPanel = new JPanel(new GridLayout(7, 1));
		
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
					profile.setSSL(ssl.isEnabled());
					profile.setPassword(password.getText());
					profile.setNickname(nickname.getText());
					profile.setUsername(username.getText());
					profile.setRealname(realname.getText());
					profile.setPartMessage(partMsg.getText());
					profile.setQuitMessage(quitMsg.getText());
					profile.setOwnColor(ownColor.getBackground());
					profile.setOtherColor(otherColor.getBackground());
					profile.setNeutralColor(neutralColor.getBackground());
					Color[] palette = new Color[DefaultValues.PALETTE_SIZE];
					for (int i = 0; i < DefaultValues.PALETTE_SIZE; i++) {
						palette[i] = colorPalette[i].getBackground();
					}
					profile.setColorPalette(palette);
					profile.setConsoleFont(consoleFont.getSelectedFont());
					profile.setChannelFont(channelFont.getSelectedFont());
					profile.setQueryFont(queryFont.getSelectedFont());
					profile.setPerform(perform.getText());
				}
			}
		});
		buttons.add(saveButton);
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(p, BorderLayout.WEST);
		main.add(buttons, BorderLayout.SOUTH);
		JScrollPane scrollPane = new JScrollPane(main);
		scrollPane.setBorder(null);
		add(scrollPane);
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
		row.add(new JLabel("Server & Port:"));
		row.add(sub);
		p.add(row);
	}

	private JRadioButton ssl;
	
	private void addSSL(JPanel p) {
		JRadioButton yes = new JRadioButton("yes", false);
		JRadioButton no = new JRadioButton("no", true);
		ButtonGroup group = new ButtonGroup();
		group.add(no);
		
		ssl = no;

		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(yes, BorderLayout.WEST);
		sub.add(no, BorderLayout.CENTER);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("SSL-Connection:"));
		row.add(sub);
		p.add(row);
	}
	
	private JTextField password = new JTextField(10);
	
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
		row.add(new JLabel("Quit-Message:"));
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
		row.add(new JLabel("Part-Message:"));
		row.add(sub);
		p.add(row);
	}
	
	private JButton ownColor = new JButton("     ");
	
	private void addOwnColorChooser(JPanel p) {
		ownColor.setBorderPainted(false);
		ownColor.setEnabled(false);
		
		final JButton choose = new JButton("Choose");
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color c = JColorChooser.showDialog(choose, "Own Color", ownColor.getBackground());
				if (c != null) {
					ownColor.setBackground(c);
				}
			}
		});
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(choose, BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(ownColor, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Own Color:"));
		row.add(sub);
		p.add(row);
	}
	
	private JButton otherColor = new JButton("     ");
	
	private void addOtherColorChooser(JPanel p) {
		otherColor.setBorderPainted(false);
		otherColor.setEnabled(false);
		
		final JButton choose = new JButton("Choose");
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color c = JColorChooser.showDialog(choose, "Other's Color", otherColor.getBackground());
				if (c != null) {
					otherColor.setBackground(c);
				}
			}
		});
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(choose, BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(otherColor, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Other's Color:"));
		row.add(sub);
		p.add(row);
	}
	
	private JButton neutralColor = new JButton("     ");
	
	private void addNeutralColorChooser(JPanel p) {
		neutralColor.setBorderPainted(false);
		neutralColor.setEnabled(false);
		
		final JButton choose = new JButton("Choose");
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color c = JColorChooser.showDialog(choose, "Neutral Color", neutralColor.getBackground());
				if (c != null) {
					neutralColor.setBackground(c);
				}
			}
		});
		
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(choose, BorderLayout.EAST);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(neutralColor, BorderLayout.WEST);
		
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Neutral Color:"));
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
					Color newColor = JColorChooser.showDialog(b, "Color", oldColor);
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
		row.add(new JLabel("Color Palette:"));
		row.add(sub);
		p.add(row);
	}
	
	private FontSelector consoleFont = new FontSelector();
	
	private void addConsoleFontSelector(JPanel p) {
		consoleFont.setSelectedFont(MainConfiguration.PROPS.getFont("font.secondary"));
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Console Font:"));
		row.add(consoleFont);
		p.add(row);
	}
	
	private FontSelector channelFont = new FontSelector();
	
	private void addChannelFontSelector(JPanel p) {
		channelFont.setSelectedFont(MainConfiguration.PROPS.getFont("font.primary"));
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Channel Font:"));
		row.add(channelFont);
		p.add(row);
	}
	
	private FontSelector queryFont = new FontSelector();
	
	private void addQueryFontSelector(JPanel p) {
		queryFont.setSelectedFont(MainConfiguration.PROPS.getFont("font.primary"));
		JPanel row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Query Font:"));
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
	
	public void setProfile(Profile p) {
		profile = p;
		setBorder(new TitledBorder("Profile "+ p));
		server.setText(p.getServer());
		port.setText(new Integer(p.getPort()).toString());
		ssl.setEnabled(p.getSSL());
		password.setText(p.getPassword());
		nickname.setText(p.getNickname());
		username.setText(p.getUsername());
		realname.setText(p.getRealname());
		partMsg.setText(p.getPartMessage());
		quitMsg.setText(p.getQuitMessage());
		ownColor.setBackground(p.getOwnColor());
		otherColor.setBackground(p.getOtherColor());
		neutralColor.setBackground(p.getNeutralColor());
		Color[] palette = p.getColorPalette();
		for (int i = 0; i < DefaultValues.PALETTE_SIZE; i++) {
			colorPalette[i].setBackground(palette[i]);
		}
		consoleFont.setSelectedFont(p.getConsoleFont());
		channelFont.setSelectedFont(p.getChannelFont());
		queryFont.setSelectedFont(p.getQueryFont());
		perform.setText(p.getPerform());
	}
}
