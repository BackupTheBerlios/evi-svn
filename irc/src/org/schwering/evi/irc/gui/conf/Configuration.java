/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui.conf;

import java.awt.Component;
import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.schwering.evi.core.IPanel;
import org.schwering.evi.irc.conf.FullProfile;

public class Configuration extends JPanel implements IPanel {
	private static final long serialVersionUID = -812108760596619774L;
	
	private static Configuration instance = null;
	private static int instanceCount = 0;
	
	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		instanceCount++;
		return instance;
	}
	
	private ProfileConfiguration configPanel = new ProfileConfiguration();
	
	public Configuration() {
		setLayout(new BorderLayout());
		add(new ProfileChooser(this), BorderLayout.NORTH);
		add(configPanel, BorderLayout.CENTER);
	}
	
	public void setProfile(FullProfile p) {
		if (p != null) {
			if (!configPanel.isVisible()) {
				configPanel.setVisible(true);
			}
			configPanel.setProfile(p);
		} else {
			configPanel.setVisible(false);
		}
	}

	public void dispose() {
		instanceCount--;
		if (instanceCount == 0) {
			instance = null;
		}
	}

	public Icon getIcon() {
		return null;
	}

	public Component getPanelInstance() {
		return this;
	}

	public String getTitle() {
		return "IRC Configuration";
	}

}
