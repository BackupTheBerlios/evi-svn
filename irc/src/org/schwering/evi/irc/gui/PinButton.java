/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;
import javax.swing.text.StyleConstants;

import org.schwering.evi.util.TextPane;

/**
 * A small square button.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class PinButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4598432184897992096L;

	public PinButton(TextPane tp) {
		final int size = StyleConstants.getFontSize(tp.getAttributes()) - 1;
		setPreferredSize(new Dimension(size, size));
		setMaximumSize(new Dimension(size, size));
		setBackground(StyleConstants.getBackground(tp.getAttributes()));
		setForeground(StyleConstants.getForeground(tp.getAttributes()));
		setBorder(new LineBorder(StyleConstants.getForeground(tp.getAttributes())));
		setAlignmentY(1.0f);
	}
	
	public PinButton(TextPane tp, final JPopupMenu menu) {
		this(tp);
		final PinButton owner = this;
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menu.show(owner, getWidth()/2, getHeight()/2);
			}
		});
	}
}
