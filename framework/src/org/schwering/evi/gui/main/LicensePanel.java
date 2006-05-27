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
import org.schwering.evi.gui.EVI;
import org.schwering.evi.util.HTMLBrowser;
import org.schwering.evi.util.RightClickMenu;

/**
 * A panel with a textfield containing the license.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class LicensePanel extends JPanel implements IPanel {
	private static final long serialVersionUID = -261152592794287431L;

	/**
	 * Default title of the panel.
	 */
	public static final String DEFAULT_TITLE = Messages.getString("LicensePanel.LICENSE"); //$NON-NLS-1$
	
	private static final String LICENSE_HTML = "license.html"; //$NON-NLS-1$
	
	/**
	 * Gives access to the one and only instance of the panel.
	 * This avoids that the user might create a bunch of panels
	 * which would make no sense.
	 */
	private static LicensePanel instance = null;
	
	/**
	 * The number of times getInstance() was invoked. 
	 * Exactly this times dispose() will be invoked.
	 */
	private static int instanceCount = 0;
	
	/**
	 * Creates one initial instance and returns it in future until 
	 * <code>dispose()</code> is invoked.
	 * @return The current instance.
	 */
	public static LicensePanel getInstance() {
		if (instance == null) {
			instance = new LicensePanel();
		}
		instanceCount++;
		return instance;
	}
	
	/**
	 * Displays a panel with a textfield and some license/copyright information.
	 */
	private LicensePanel() {
		super(new GridLayout(1, 1));
		final JLabel label = new JLabel(Messages.getString("LicensePanel.LOADING")); //$NON-NLS-1$
		add(label);
		new Thread() {
			public void run() {
				JTextComponent copyright = createCopyrightPanel();
				HTMLBrowser license = new HTMLBrowser(LICENSE_HTML);
				remove(label);
				
				JSplitPane splitPane = new JSplitPane(
						JSplitPane.VERTICAL_SPLIT);
				splitPane.add(new JScrollPane(copyright));
				splitPane.add(license);
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
		String s = EVI.TITLE +" Framework "+ EVI.VERSION + "\n"+ //$NON-NLS-1$ //$NON-NLS-2$
		"\n"+ //$NON-NLS-1$
		"Copyright (C) 2006 Christoph Schwering\n"+ //$NON-NLS-1$
		"\n"+ //$NON-NLS-1$
		"This copyright notice and the license only apply to the EVI "+ //$NON-NLS-1$
		"framework. All EVI modules are subject to the copyright of "+ //$NON-NLS-1$
		"their respective owners!\n"+ //$NON-NLS-1$
		"\n"+ //$NON-NLS-1$
		"This program is free software; you can redistribute it and/or "+ //$NON-NLS-1$
		"modify it under the terms of the GNU General Public License as "+ //$NON-NLS-1$
		"published by the Free Software Foundation; either version 2 of the "+ //$NON-NLS-1$
		"License.\n"+ //$NON-NLS-1$
		"\n"+ //$NON-NLS-1$
		"This program is distributed in the hope that it will be useful, but "+ //$NON-NLS-1$
		"WITHOUT ANY WARRANTY; without even the implied warranty of "+ //$NON-NLS-1$
		"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU "+ //$NON-NLS-1$
		"General Public License for more details.\n"+ //$NON-NLS-1$
		"\n"+ //$NON-NLS-1$
		"You should have received a copy of the GNU General Public License " + //$NON-NLS-1$
		"along with this program; if not, write to the Free Software" + //$NON-NLS-1$
		"Foundation, Inc., 59 Temple Place, Suite 330, Boston, " + //$NON-NLS-1$
		"MA 02111-1307, USA."; //$NON-NLS-1$
		JTextArea textArea = new JTextArea(s);
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		RightClickMenu.addRightClickMenu(textArea);
		return textArea;
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

	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
		instanceCount--;
		if (instanceCount == 0) {
			instance = null;
		}
	}
}
