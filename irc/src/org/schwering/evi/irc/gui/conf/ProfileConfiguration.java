package org.schwering.evi.irc.gui.conf;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.schwering.evi.irc.conf.Profile;

public class ProfileConfiguration extends JPanel {
	private Profile profile;
	
	public ProfileConfiguration() {
		super(new BorderLayout());
		setBorder(new TitledBorder("Profile"));
		
		JPanel connection = new JPanel(new GridLayout(9, 0));
		addServerAndPort(connection);
		addSSL(connection);
		addPassword(connection);
		addNickname(connection);
		addUsername(connection);
		addRealname(connection);
		addPartMsg(connection);
		addQuitMsg(connection);

		JPanel appearance = new JPanel(new GridLayout(11, 0));
		
		JPanel misc = new JPanel(new GridLayout(7, 0));
		
		JPanel perform = new JPanel(new GridLayout(1, 0));
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.add("Connection", connection);
		tabs.add("Appearance", appearance);
		tabs.add("Miscellauneous", misc);
		tabs.add("Perform", perform);
		JPanel p = new JPanel();
		p.add(tabs);
		
		JPanel buttons = new JPanel();
		final JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (profile != null) {
					
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
		row.add(new JLabel("SSL-Connection:"));
		row.add(sub);
		p.add(row);
	}
	
	private JTextField password = new JTextField(10);
	
	private void addPassword(JPanel p) {
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

	public void setProfile(Profile p) {
		profile = p;
		setBorder(new TitledBorder("Profile "+ p));
	}
}
