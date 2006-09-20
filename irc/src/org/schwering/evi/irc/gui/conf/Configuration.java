package org.schwering.evi.irc.gui.conf;

import java.awt.Component;
import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.schwering.evi.core.IPanel;
import org.schwering.evi.irc.conf.Profile;

public class Configuration extends JPanel implements IPanel {
	
	private static Configuration instance = null;
	
	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}
	
	private ProfileConfiguration configPanel = new ProfileConfiguration();
	
	public Configuration() {
		setLayout(new BorderLayout());
		add(new ProfileChooser(this), BorderLayout.NORTH);
		add(configPanel, BorderLayout.CENTER);
	}
	
	public void setProfile(Profile p) {
	}

	public void dispose() {
		instance = null;
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
