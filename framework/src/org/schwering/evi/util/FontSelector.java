/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.schwering.evi.gui.main.MainFrame;

/**
 * GUI component that allows to select a font. The JPanel has a GridLayout with two
 * rows. The first row contains a JComboBox with all fontnames, the second row 
 * contains a textfield for the fontsize and a JComboBox for the style.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class FontSelector extends JPanel {
	private static final long serialVersionUID = 7921314592883676271L;
	
	private Font font;
	private TextField tf;
	
	/**
	 * Creates a new font selector panel.
	 */
	public FontSelector() {
		super(new BorderLayout());
		
		final FontSelector owner = this;
		
		JButton button = new JButton("Choose");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FontDialog fd = new FontDialog(MainFrame.getInstance(), font);
				setSelectedFont(fd.getSelectedFont());
			}
		});
		
		tf = new TextField();
		
		add(tf, BorderLayout.CENTER);
		add(button, BorderLayout.EAST);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#doLayout()
	 */
	public void doLayout() {
		super.doLayout();
		tf.setBackground(getBackground());
		tf.setForeground(getForeground());
		tf.setBorder(null);
		tf.setEditable(false);
	}
	
	/**
	 * Sets the newly selected font.
	 * @param f The new font.
	 */
	public void setSelectedFont(Font f) {
		font = f;
		tf.setFont(f);
		tf.setText(f.getName());
		System.out.println("set font to "+ font);
	}
	
	/**
	 * Returns the currently selected font.
	 * @return The font.
	 */
	public Font getSelectedFont() {
		return font;
	}
}
