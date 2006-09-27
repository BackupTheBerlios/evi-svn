/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.gui.main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import org.schwering.evi.gui.EVI;
import org.schwering.evi.util.Util;

/**
 * Shows a small frame with a progressbar. The progressbar can be manipulated by
 * the methods <code>update</code> which set a string or the percent.
 * @author Christoph Schwering &lt;ch@schwering.org&gt;
 * @version $Id$
 */
public class ProgressFrame extends JFrame {
	private static final long serialVersionUID = -8694844069528547935L;
	
	/**
	 * The progressbar.
	 */
	private JProgressBar pb;
	
	/**
	 * Creates a new small undecorated frame with a progressbar which is set to
	 * <code>0 %</code>.
	 */
	public ProgressFrame() {
		setTitle(EVI.TITLE);
		setSize(250, 50);
		Util.centerComponent(this);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setUndecorated(true);
		getContentPane().add(initPanel());
		setVisible(true);
	}
	
	/**
	 * Returns the panel with the progressbar.
	 * @return The new Panel.
	 */
	private JPanel initPanel() {
		JPanel p = new JPanel(new BorderLayout(1,1));
		pb = new JProgressBar();
		pb.setStringPainted(true);
		pb.setIndeterminate(false);
		update(0, EVI.TITLE);
		p.add(pb);
		return p;
	}
	
	/**
	 * Sets the new percentage <code>i</code> and progressbar-string.
	 * @param i The new percentage.
	 * @param s The new string of the progressbar.
	 */
	public void update(int i, String s) {
		update(i);
		update(s);
	}
	
	/**
	 * Sets the new percentage <code>i</code> of the progressbar.
	 * @param i The new percentage.
	 */
	public void update(int i) {
		pb.setValue(i);
		if (i >= 100) {
			dispose();
		}
	}
	
	/**
	 * Sets the new progressbar-string.
	 * @param s The new string of the progessbar.
	 */
	public void update(String s) {
		pb.setString(s);
	}
	
	/**
	 * Changes the indeterminate-style of the progressbar. 
	 * Simply invokes <code>JProgressBar.setIndeterminate(b)</code>.
	 * @param b Enable/disable indetermination.
	 */
	public void setIndeterminate(boolean b) {
		pb.setIndeterminate(b);
	}
}