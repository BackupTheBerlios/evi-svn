package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.schwering.evi.core.IPanel;

/**
 * A panel which contains a scrollable table with the environment information.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class EnvironmentPanel extends JPanel implements IPanel {
	private static final long serialVersionUID = -4204646793461931426L;

	/**
	 * Default title of the panel.
	 */
	public static final String DEFAULT_TITLE = Messages.getString("EnvironmentPanel.DEFAULT_TITLE"); //$NON-NLS-1$
	
	private MemoryPanel memoryPanel = new MemoryPanel();
	
	/**
	 * Draws a panel which contains a scrollable table with the environment 
	 * information.
	 */
	public EnvironmentPanel() {
		super(new BorderLayout());
		add(createMemoryPanel(), BorderLayout.CENTER);
		add(memoryPanel, BorderLayout.NORTH);
	}
	
	private JPanel createMemoryPanel() {
		Properties sysProps = System.getProperties();
		Enumeration sysKeys = sysProps.keys();
		int sysLen = sysProps.size();
		String[][] vars = new String[sysLen][2];
		
		String[] keys = new String[sysLen];
		for (int i = 0; i < sysLen && sysKeys.hasMoreElements(); i++) {
			keys[i] = (String)sysKeys.nextElement();
		}
		Arrays.sort(keys);
		
		for (int i = 0; i < sysLen; i++) {
			vars[i][0] = keys[i];
			vars[i][1] = sysProps.getProperty(keys[i]);
		}
		
		JTable table = new JTable(vars, new String[] { Messages.getString("EnvironmentPanel.VARIABLE"), Messages.getString("EnvironmentPanel.VALUE") }) { //$NON-NLS-1$ //$NON-NLS-2$
			private static final long serialVersionUID = 2298937007677219773L;

			public boolean isCellEditable(int x, int y) {
				return false;
			}
		};
		
		JScrollPane scrollPane = new JScrollPane(table, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel panel = new JPanel(new GridLayout(1, 0));
		panel.add(scrollPane);
		return panel;
	}
	
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
		memoryPanel.stop();
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
		return DEFAULT_TITLE;
	}
}