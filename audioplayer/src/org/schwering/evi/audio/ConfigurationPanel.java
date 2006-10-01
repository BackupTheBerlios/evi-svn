/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.schwering.evi.core.IPanel;

/**
 * The configuration panel.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ConfigurationPanel extends JPanel implements IPanel {
	private static final long serialVersionUID = 6730011417712875823L;
	
	private JCheckBox applet = new JCheckBox(Messages.getString("ConfigurationPanel.APPLET_DESC")); //$NON-NLS-1$
	private JCheckBox searchDirectly = new JCheckBox(Messages.getString("ConfigurationPanel.SEARCHDIRECTLY_DESC")); //$NON-NLS-1$
	
	public ConfigurationPanel() {
		super(new BorderLayout());
		
		final JButton save = new JButton(Messages.getString("ConfigurationPanel.SAVE")); //$NON-NLS-1$
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				store();
			}
		});
		
		JPanel p = new JPanel(new GridLayout(3, 0));
		addLine(p, applet);
		addLine(p, searchDirectly);
		addCenter(p, save);
		JPanel sub = new JPanel();
		sub.add(p);
		add(sub, BorderLayout.WEST);
		
		load();
	}
	
	private void addLine(JPanel p, Component c) {
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(c, BorderLayout.WEST);
		sub.add(new JPanel(), BorderLayout.EAST);
		p.add(sub);
	}
	
	private void addCenter(JPanel p, Component c) {
		JPanel sub = new JPanel(new BorderLayout());
		sub.add(new JPanel(), BorderLayout.WEST);
		sub.add(c, BorderLayout.CENTER);
		sub.add(new JPanel(), BorderLayout.EAST);
		p.add(sub);
	}
	
	private void store() {
		Configuration.setApplet(applet.isSelected());
		Configuration.setSearchDirectly(searchDirectly.isSelected());
	}
	
	private void load() {
		applet.setSelected(Configuration.isApplet());
		searchDirectly.setSelected(Configuration.isSearchDirectly());
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
		store();
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getIcon()
	 */
	public Icon getIcon() {
		return null;
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
		return Messages.getString("ConfigurationPanel.CONF_TITLE"); //$NON-NLS-1$
	}
}
