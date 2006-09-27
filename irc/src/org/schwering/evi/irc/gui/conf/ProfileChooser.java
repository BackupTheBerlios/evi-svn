/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui.conf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import org.schwering.evi.util.ExceptionDialog;
import org.schwering.evi.irc.conf.Profile;

public class ProfileChooser extends JPanel {
	private static final long serialVersionUID = 8018487724029132222L;
	
	private JButton delProfile = new JButton("Delete Profile");
	private JComboBox box = new JComboBox();
	private Configuration owner;
	
	public ProfileChooser(final Configuration owner) {
		this.owner = owner;
		final JButton newProfile = new JButton("New Profile");
		newProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String name = JOptionPane.showInputDialog("Enter the name of the new profile:");
					if (name != null) {
						addProfile(new Profile(name));
					}
				} catch (Exception exc) {
					ExceptionDialog.show("Could not create new profile", exc);
				}
			}
		});
		
		delProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Profile p = (Profile)box.getSelectedItem();
					removeProfile(p);
					p.delete();
				} catch (Exception exc) {
					ExceptionDialog.show("Could not create new profile", exc);
				}
			}
		});
		
		Profile[] p = Profile.getProfiles();
		Arrays.sort(p, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				return arg0.toString().compareTo(arg1.toString());
			}
		});
		for (int i = 0; i < p.length; i++) {
			addProfile(p[i]);
		}
		box.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					owner.setProfile((Profile)e.getItem());
				}
			}
		});
		
		delProfile.setEnabled(box.getItemCount() != 0);
		owner.setProfile((box.getItemCount() > 0) ? (Profile)box.getSelectedItem() : null);
		
		add(newProfile);
		add(box);
		add(delProfile);
	}
	
	private void addProfile(Profile p) {
		box.addItem(p);
		delProfile.setEnabled(box.getItemCount() > 0);
	}
	
	private void removeProfile(Profile p) {
		box.removeItem(p);
		delProfile.setEnabled(box.getItemCount() > 0);
		if (box.getItemCount() == 0) {
			owner.setProfile(null);
		}
	}
}
