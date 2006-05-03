package org.schwering.evi.gui.main;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import org.schwering.evi.core.IPanel;
import org.schwering.evi.util.HTMLBrowser;
import org.schwering.evi.util.RightClickMenu;

/**
 * A panel with a textfield containing the license.
 * @author Christoph Schwering (schwering@gmail.com)
 */
public class LicensePanel extends JPanel implements IPanel {
	/**
	 * Default title of the panel.
	 */
	public static final String DEFAULT_TITLE = "License";
	
	private static final String LICENSE_HTML = "license.html";
	
	/**
	 * Displays a panel with a textfield and some license/copyright information.
	 */
	public LicensePanel() {
		super(new GridLayout(1, 1));
		final JLabel label = new JLabel("Loading...");
		add(label);
		new Thread() {
			public void run() {
				JTextComponent copyright = createCopyrightPanel();
				HTMLBrowser license = new HTMLBrowser(LICENSE_HTML);
				remove(label);
				
				JSplitPane splitPane = new JSplitPane(
						JSplitPane.VERTICAL_SPLIT);
				splitPane.add(new JScrollPane(copyright));
				splitPane.add(new JScrollPane(license));
				int location = (int)((double)getHeight() * 0.4);
			    splitPane.setDividerLocation((location > 0) ? location : 100);

				add(splitPane);
				repaint();
				revalidate();
			}
		}.start();
	}
	
	/**
	 * Returns the GPL copyright disclaimer.
	 * @return The GPL disclaimer.
	 */
	private JTextComponent createCopyrightPanel() {
		String s = "Copyright (C) 2006 Christoph Schwering\n"+
		"\n"+
		"This program is free software; you can redistribute it and/or\n"+
		"modify it under the terms of the GNU General Public License as\n"+
		"published by the Free Software Foundation; either version 2 of the\n"+
		"License.\n"+
		"\n"+
		"This program is distributed in the hope that it will be useful, but\n"+
		"WITHOUT ANY WARRANTY; without even the implied warranty of\n"+
		"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU\n"+
		"General Public License for more details.\n"+
		"\n"+
		"You should have received a copy of the GNU General Public License\n" +
		"along with this program; if not, write to the Free Software\n" +
		"Foundation, Inc., 59 Temple Place, Suite 330, Boston, \n" +
		"MA 02111-1307, USA.";
		JTextArea textArea = new JTextArea(s);
		textArea.setEditable(false);
		RightClickMenu.addRightClickMenu(textArea);
		return textArea;
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
		return this;
	}

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return DEFAULT_TITLE;
	}
}
