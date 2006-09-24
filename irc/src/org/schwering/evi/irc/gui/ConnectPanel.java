package org.schwering.evi.irc.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.schwering.evi.irc.conf.Configuration;
import org.schwering.evi.irc.conf.DefaultValues;
import org.schwering.evi.irc.conf.Profile;
import org.schwering.evi.util.RightClickMenu;

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
		p.setLayout(new GridLayout(6, 0));
		
		final JTextField uriTF = new JTextField(10);
		final JTextField serverTF = new JTextField(10);
		final JTextField portTF = new JTextField(4);
		final JCheckBox sslCB = new JCheckBox();
		final JTextField nickTF = new JTextField(10);
		final JTextField channelTF = new JTextField(10);
		final JButton connectButton = new JButton("Connect");
		
		uriTF.setText(Configuration.getLastURI());
		synchronizeTextFields(uriTF, serverTF, portTF, sslCB, nickTF, channelTF);
		uriTF.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
			}
			
			public void focusLost(FocusEvent e) {
				synchronizeTextFields(uriTF, serverTF, portTF, sslCB, nickTF, channelTF);
			}
		});
		
		FocusListener synchronizeURIListener = new FocusListener() {
			public void focusGained(FocusEvent e) {
			}
			
			public void focusLost(FocusEvent e) {
				synchronizeURI(uriTF, serverTF, portTF, sslCB, nickTF, channelTF);
			}
		};
		
		serverTF.addFocusListener(synchronizeURIListener);
		portTF.addFocusListener(synchronizeURIListener);
		sslCB.addFocusListener(synchronizeURIListener);
		nickTF.addFocusListener(synchronizeURIListener);
		channelTF.addFocusListener(synchronizeURIListener);
		
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Configuration.setLastURI(uriTF.getText());
			}
		});
		
		JPanel row, sub;
		
		RightClickMenu.addRightClickMenu(uriTF);
		sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(uriTF, BorderLayout.CENTER);
		row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("URI:"));
		row.add(sub);
		p.add(row);
		
		RightClickMenu.addRightClickMenu(serverTF);
		RightClickMenu.addRightClickMenu(portTF);
		sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(serverTF, BorderLayout.WEST);
		sub.add(new JLabel(":"), BorderLayout.CENTER);
		sub.add(portTF, BorderLayout.EAST);
		row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Server & Port:"));
		row.add(sub);
		p.add(row);
		
		sub = new JPanel(new BorderLayout());
		sub.add(sslCB, BorderLayout.WEST);
		sslCB.setText("Enable SSL");
		row = new JPanel(new GridLayout(0, 1));
		row.add(sslCB);
		p.add(row);
		
		RightClickMenu.addRightClickMenu(nickTF);
		sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.SOUTH);
		sub.add(nickTF, BorderLayout.CENTER);
		row = new JPanel(new GridLayout(0, 2));
		row.add(new JLabel("Nickname:"));
		row.add(sub);
		p.add(row);
		
		RightClickMenu.addRightClickMenu(channelTF);
		sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.NORTH);
		sub.add(new JPanel(), BorderLayout.SOUTH);
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
	
	private void synchronizeURI(JTextField uriTF, JTextField serverTF, 
			JTextField portTF, JCheckBox sslCB, JTextField nickTF, JTextField channelTF) {
		String uriText = uriTF.getText();
		if (uriText == null) {
			return;
		}
		try {
			boolean ssl = sslCB.isSelected();
			String protocol = ssl ? "ircs" : "irc";
			String nickname = nickTF.getText();
			String server = serverTF.getText();
			int port = Integer.parseInt(portTF.getText());
			String channel = channelTF.getText();
			if (channel != null && channel.length() > 1) {
				channel = "/"+ channel.substring(1);
			}
			URI uri = new URI(protocol, nickname, server, port, channel, null, null);
			uriTF.setText(uri.toString());
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	private void synchronizeTextFields(JTextField uriTF, JTextField serverTF, 
			JTextField portTF, JCheckBox sslCB, JTextField nickTF, JTextField channelTF) {
		try {
			String uriText = uriTF.getText();
			if (uriText == null) {
				return;
			}
			URI uri = new URI(uriText);
			boolean ssl = !uri.getScheme().equals("irc");
			String server = uri.getHost();
			int defaultPort = ssl ? DefaultValues.DEFAULT_SSL_PORT : DefaultValues.DEFAULT_PORT;
			int port = uri.getPort() != -1 ? uri.getPort() : defaultPort;
			String nick = uri.getUserInfo();
			String chan = "#"+ uri.getPath().substring(1);
			serverTF.setText(server);
			portTF.setText(String.valueOf(port));
			sslCB.setSelected(ssl);
			nickTF.setText(nick);
			channelTF.setText(chan);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	private JPanel makeProfileConnectPanel() {
		JPanel p = new JPanel();
		p.setBorder(new TitledBorder("Connect Profile"));
		p.setLayout(new GridLayout(2, 0));
		
		Profile[] profiles = Profile.getProfiles();
		String lastProfile = Configuration.getLastProfile();
		int selected = -1;
		for (int i = 0; i < profiles.length; i++) {
			if (profiles[i].toString().equals(lastProfile)) {
				selected = i;
				break;
			}
		}
		
		final JComboBox profileBox = new JComboBox(profiles);
		if (selected != -1) {
			profileBox.setSelectedIndex(selected);
		}
		final JButton connectButton = new JButton("Connect");
		
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Configuration.setLastProfile(profileBox.getSelectedItem().toString());
			}
		});
		
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
