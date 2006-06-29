package org.schwering.evi.irc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.net.URI;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.schwering.evi.conf.Properties;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.gui.main.MainFrame;

/**
 * The IModule class of the IRC module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class IRC implements IModule, IPanel {
	private Properties props;
	private JPanel panel;
	private JTabbedPane tabs;

	public IRC() {
		initPanel();
		
		JPanel test = new JPanel();
		final javax.swing.JTextField textfield = new javax.swing.JTextField("Der neue Titel");
		test.add(textfield);
		
		javax.swing.JButton button = new javax.swing.JButton("Highlight");
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				highlight();
				updateTitle(textfield.getText());
			}
		});
		test.add(button);
		
		javax.swing.JButton reset = new javax.swing.JButton("Reset");
		reset.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				reset();
			}
		});
		test.add(reset);
		
		tabs.add(test, "Begrüßung");
	}
	
	public IRC(Object[] args) {
		initPanel();
	}
	
	public IRC(URI uri) {
		initPanel();
	}
	
	private void initPanel() {
		try {
			props = new Properties(ModuleContainer.getIdByClass(IRC.class));
			props.setShutdownHook(true);
		} catch (Exception exc) {
			throw new RuntimeException("Could not create or open config for IRC module", exc);
		}
		
		panel = new JPanel(new BorderLayout());
		tabs = new JTabbedPane();
		panel.add(tabs);
		int placement = props.getInt("gui.tabs.placement", JTabbedPane.LEFT);
		tabs.setTabPlacement(placement);
	}
		
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
		
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
		return panel;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return "IRC";
	}

	public JTabbedPane getTabbedPane() {
		return tabs;
	}
	
	public void updateTitle(String newTitle) {
		MainFrame.getInstance().getMainTabBar().setTitle(this, newTitle);
	}
	
	public void reset() {
		MainFrame.getInstance().getMainTabBar().setDefaultBackground(this);
		MainFrame.getInstance().getMainTabBar().setDefaultForeground(this);
	}
	
	public void highlight() {
		MainFrame.getInstance().getMainTabBar().setHighlightForeground(this);
		MainFrame.getInstance().getMainTabBar().setHighlightBackground(this);
	}
}
