package org.schwering.evi.irc.gui.conf;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.schwering.evi.core.IPanel;

public class Configuration extends JPanel implements IPanel {
	
	

	public void dispose() {
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
