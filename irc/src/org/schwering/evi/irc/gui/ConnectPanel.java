package org.schwering.evi.irc.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.schwering.evi.irc.conf.Profile;

/**
 * The connect panel lets the user choose a profile or do a quick-connect.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ConnectPanel extends JPanel {
	private TabBar owner;
	
	public ConnectPanel(TabBar owner) {
		this.owner = owner;
		setLayout(new GridLayout(1, 1));
		
		JPanel tmp = new JPanel();
		tmp.setLayout(new BorderLayout());
		JPanel quickConnectPanel = makeQuickConnectPanel();
		tmp.add(quickConnectPanel, BorderLayout.WEST);
		JPanel profileConnectPanel = makeProfileConnectPanel();
		tmp.add(profileConnectPanel, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane(tmp);
		add(scrollPane);
	}
	
	public JPanel makeQuickConnectPanel() {
		JPanel p = new JPanel();
		p.setBorder(new TitledBorder("Quick Connect"));
		p.setLayout(new GridLayout(5, 0));
		
		final JTextField urlTF = new JTextField(10);
		final JTextField serverTF = new JTextField(10);
		final JTextField portTF = new JTextField(4);
		final JTextField nickTF = new JTextField(10);
		final JTextField channelTF = new JTextField(10);
		final JButton connectButton = new JButton("Connect");
		
		JPanel row, sub;
		
		sub = new JPanel(new BorderLayout());
		sub.add(urlTF, BorderLayout.CENTER);
		row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("URL:"));
		row.add(sub);
		p.add(row);
		
		sub = new JPanel(new BorderLayout());
		sub.add(serverTF, BorderLayout.WEST);
		sub.add(new JLabel(":"), BorderLayout.CENTER);
		sub.add(portTF, BorderLayout.EAST);
		row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Server & Port:"));
		row.add(sub);
		p.add(row);
		
		sub = new JPanel(new BorderLayout());
		sub.add(nickTF, BorderLayout.CENTER);
		row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Nickname:"));
		row.add(sub);
		p.add(row);
		
		sub = new JPanel(new BorderLayout());
		sub.add(channelTF, BorderLayout.CENTER);
		row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Channel:"));
		row.add(sub);
		p.add(row);
		
		row = new JPanel();
		row.add(connectButton);
		p.add(row);

		JPanel tmp1 = new JPanel(new BorderLayout());
		tmp1.add(p, BorderLayout.WEST);
		JPanel tmp2 = new JPanel(new BorderLayout());
		tmp2.add(tmp1, BorderLayout.NORTH);
		return tmp2;
	}
	
	public JPanel makeProfileConnectPanel() {
		JPanel p = new JPanel();
		p.setBorder(new TitledBorder("Connect Profile"));
		p.setLayout(new GridLayout(2, 0));
		
		final JButton connectButton = new JButton("Connect");
		final JComboBox profileBox = new JComboBox(Profile.getProfiles());
		
		JPanel row, sub;
		
		sub = new JPanel(new BorderLayout());
		sub.add(profileBox, BorderLayout.CENTER);
		row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Profile:"));
		row.add(sub);
		p.add(row);
		
		row = new JPanel();
		row.add(connectButton);
		p.add(row);

		JPanel tmp1 = new JPanel(new BorderLayout());
		tmp1.add(p, BorderLayout.WEST);
		JPanel tmp2 = new JPanel(new BorderLayout());
		tmp2.add(tmp1, BorderLayout.NORTH);
		return tmp2;
	}
}
